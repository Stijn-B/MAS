package model.roadSignPoint;

import com.github.rinde.rinsim.core.model.road.MovingRoadUser;
import com.github.rinde.rinsim.core.model.road.MoveProgress;
import com.github.rinde.rinsim.core.model.road.RoadUnits;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import javax.measure.unit.SI;
import heuristic.Heuristic;
import model.ant.ExplorationAnt;
import model.ant.IntentionAnt;
import model.ant.PlannedPath;
import model.pheromones.RoadSign;
import model.roadSignPoint.parcel.AbstractParcelPoint;

import java.util.HashSet;
import java.util.List;

public class AGV extends AbstractRoadSignPoint implements TickListener, MovingRoadUser {

    // speed in m/s
    static final double AGV_SPEED = 20400;

    // default time between reconsidering the committed path
    static final long RECONSIDER_DELAY = 5000;

    static long NOW = 0; // current time, updated every tick()


    /* CONSTRUCTOR */

    public AGV(Point position, Heuristic heuristic) {
        super(position);
        setHeuristic(heuristic);
    }

    public boolean plansPassingBase() {
        return false; // TODO: mogelijks gebruiken
    }


    /* SPEED */

    private double speed = AGV_SPEED;

    @Override
    public double getSpeed() {
        return speed;
    }

    /**
     * Returns how long it would take this AGV to travel the given distance
     * @param distance Distance in the models distance unit.
     * @return The travel time in ms.
     */
    public long calculateTravelTime(double distance) {
        RoadUnits roadUnits = new RoadUnits(getRoadModel().getDistanceUnit(), getRoadModel().getSpeedUnit());
        double inTime = roadUnits.toInDist(distance) / getSpeed();
        double ms = roadUnits.toExTime(inTime, SI.MILLI(SI.SECOND));
        return Math.round(ms);
    }

    public long distanceToETA(double distance) {
        return NOW + calculateTravelTime(distance);
    }


    /* HEURISTIC */

    private Heuristic heuristic;

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }


    /* PARCELS */

    private HashSet<Integer> parcelIDs = new HashSet<>();

    public HashSet<Integer> getParcelIDs() {
        return parcelIDs;
    }

    public void addParcel(AbstractParcelPoint parcel) {
        addParcelID(parcel.getParcelID());
    }

    public void addParcelID(int id) {
        parcelIDs.add(id);
    }

    public void removeParcelID(int id) {
        parcelIDs.remove(id);
    }

    public boolean carriesParcel(int id) {
        return parcelIDs.contains(id);
    }

    public void deliveredParcel(int parcelID) {
        getRoadSignPointModel().parcelIsDelivered(parcelID);
    }

    public int getMaxParcelCount() {
        return 5;
    }


    /* PATH EXPLORATION */

    private long lastReconsiderTime = 0;

    /**
     * Returns whether the AGV should explore
     */
    private boolean reconsiderCondition(long now) {
        // reconsider if: RECONSIDER_DELAY ms have passed since last reconsider OR agv has no destination
        if (now - lastReconsiderTime >= RECONSIDER_DELAY || !hasDestination()) {
            lastReconsiderTime = now;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Chooses a new PlannedPath and registers it as the intended path
     */
    private void chooseNewPath(long now) {
        // explore possible paths
        List<PlannedPath> paths = explorePaths();

        // commit to best path
        commit(getHeuristic().getBest(paths), now);
    }

    /**
     * Explores viable paths using ExplorationAnts
     * @return a List<PlannedPath> containing viable paths (can be empty)
     */
    public List<PlannedPath> explorePaths() {
        return new ExplorationAnt(this).explore();
    }

    /* INTENDED PATH */

    private PlannedPath intendedPath = new PlannedPath(this);

    private PlannedPath getIntendedPath() {
        if (intendedPath == null)
            setIntendedPath(new PlannedPath(this));
        return intendedPath;
    }

    private void setIntendedPath(PlannedPath intendedPath) {
        if (intendedPath == null)
            intendedPath = new PlannedPath(this);
        this.intendedPath = intendedPath;
    }

    public boolean hasDestination() {
        return !getIntendedPath().isEmpty();
    }

    public RoadSignPoint getDestination() {
        if (hasDestination())
            return getIntendedPath().getFirstDest();
        else
            return null;
    }

    public boolean isAtDestination() {
        return hasDestination() && isAtPosition(getIntendedPath().getFirstDest().getPosition());
    }

    public boolean isAtPosition(Point point) {
        return getRoadModel().getPosition(this).equals(point);
    }

    /* COMMITTING */

    /**
     * Commits the AGV to the given PlannedPath
     */
    public void commit(PlannedPath path, long timeNow) {
        // if path is empty, do nothing
        if (path.isEmpty()) return;

        // set and signal intention
        setIntendedPath(path);
        signalIntention(path, timeNow);
    }

    /**
     * Assures the intention of the AGV and returns whether the PlannedPath is still viable.
     * @return whether this AGVs intended PlannedPath is still viable
     */
    public boolean signalIntention(PlannedPath path, long timeNow) {
        return new IntentionAnt(this).declareIntention(path, timeNow);
    }

    public void resetIntention() {
        setIntendedPath(new PlannedPath(this));
    }


    /* ENVIRONMENT INTERACTION */

    /**
     * Moves the AGV towards the first destination in its PlannedPath
     * @param tm
     */
    private void move(TimeLapse tm) {

        // if no RoadModel, do nothing
        if (getRoadModel() == null) return;

        // if no destination, do nothing
        if (getIntendedPath().getFirstDest() == null) return;

        // move
        try {
            MoveProgress progress = getRoadModel().moveTo(this, getIntendedPath().getFirstDest().getPosition(), tm);
            System.out.println("[" + this + "] Moved " + progress.distance().toString() + " in " + tm.getTimeConsumed() + " " + tm.getTimeUnit().toString());  // PRINT
        } catch (Exception E) {
            System.out.println("[" + this + "] Couldn't move");  // PRINT
            tm.consumeAll();
        }
    }

    /**
     * Acts on the given DeprecatedRoadSignPoint. Does nothing if not at the given RoadSignPoints location.
     */
    public void tryToAct(TimeLapse tm) {
        if (isAtDestination() && getDestination().act(this)) {
            System.out.println("[" + this + "] Acted on " + getDestination());  // PRINT
            getIntendedPath().popFirst();
        }
    }


    /* INTERFACES */

    // TickListener

    @Override
    public void tick(TimeLapse timeLapse) {
        long now = timeLapse.getTime();
        NOW = now;


        System.out.println("_ _ _ _" + this + " tick()_ _ _ _");  // PRINT

        /*
        System.out.print("RoadSigns: ");
        for(RoadSign rs : getRoadSigns()) {
            System.out.print(rs + "  -  ");
        }
        System.out.println();
        */

        if (!parcelIDs.isEmpty()) {
            System.out.print("[" + this + "] Carries parcels with ID " );
            for (int ID : parcelIDs) {
                System.out.print(ID + " ");
            }
            System.out.println();
        }

        // INTENTION

        if (hasDestination()) {

            System.out.println("[" + this + "] Signal Intended Path");  // PRINT

            // signal intention
            boolean viable = signalIntention(getIntendedPath(), now);

            // if intention not viable, reset
            if (! viable) {
                System.out.println("NOT OK -> reset");  // PRINT
                resetIntention();
            } else {
                System.out.println("OK");  // PRINT
            }
        }

        // CONSIDER EXPLORING NEW PATH

        if (reconsiderCondition(now)) {
            System.out.println("[" + this + "] Explore new path");  // PRINT
            chooseNewPath(now);
        }


        // MOVING AND HANDLE

        while (hasDestination() && timeLapse.hasTimeLeft()) {

            // move towards destination
            move(timeLapse);

            // if arrived at destination, act on it
            tryToAct(timeLapse);
        }

    }

    /* NAME */

    @Override
    public String getName() {
        return "AGV";
    }
}
