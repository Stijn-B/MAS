package model.roadSignPoint;

import com.github.rinde.rinsim.core.model.road.MovingRoadUser;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import heuristic.Heuristic;
import model.ant.ExplorationAnt;
import model.ant.IntentionAnt;
import model.ant.PlannedPath;
import model.roadSignPoint.parcel.AbstractParcelPoint;

import java.util.HashSet;
import java.util.List;

public class AGV extends AbstractRoadSignPoint implements TickListener, MovingRoadUser {

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

    public long distanceToDuration(double distance) {
        return Math.round(distance/speed);
    }

    public long distanceToETA(double distance) {
        return NOW + distanceToDuration(distance);
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
        addParcelID(parcel.ID);
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


    /* PATH EXPLORATION */

    private long lastReconsiderTime = 0;

    /**
     * Returns whether the AGV should explore
     */
    private boolean reconsiderCondition(long now) {
        if (now - lastReconsiderTime >= RECONSIDER_DELAY
                || getIntendedPath() == null
                || getIntendedPath().isEmpty()) {
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
        // System.out.println("[AGV " + getID() + "] chooseNewPath() EXPLORE");

        // explore possible paths
        List<PlannedPath> paths = explorePaths();

        // commit to best path
        commit(getHeuristic().getBest(paths), now);

        //System.out.println("[AGV " + getID() + "] chooseNewPath() - explored " + paths.size() +
        //        " paths, chosen path hop count: " + getIntendedPath().getNbRoadSigns()); // PRINT
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
        if (intendedPath == null) return new PlannedPath(this);
        else return intendedPath;
    }

    private void setIntendedPath(PlannedPath intendedPath) {
        this.intendedPath = intendedPath;
    }

    public boolean hasDestination() {
        return !getIntendedPath().isEmpty();
    }

    /* COMMITTING */

    /**
     * Commits the AGV to the given PlannedPath
     */
    public void commit(PlannedPath path, long timeNow) {
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
            getRoadModel().moveTo(this, getIntendedPath().getFirstDest().getPosition(), tm);
        } catch (Exception E) {
            System.out.println("[" + this + "] couldn't move (" + E.getMessage());
            tm.consumeAll();
        }
    }

    /**
     * Acts on the given DeprecatedRoadSignPoint. Does nothing if not at the given RoadSignPoints location.
     */
    public void tryToAct(TimeLapse tm) {
    // TODO
    }


    /* INTERFACES */

    // TickListener

    @Override
    public void tick(TimeLapse timeLapse) {
        long now = timeLapse.getTime();
        NOW = now;


        //System.out.println("[AGV " + String.valueOf(getID()) + "]");  // PRINT

        // CONSIDER EXPLORING NEW PATH

        if (reconsiderCondition(now)) {
            //System.out.println("[AGV " + String.valueOf(getID()) + "] Explore new path");  // PRINT
            chooseNewPath(now);
        }

        // MOVING AND HANDLE

        while (hasDestination() && timeLapse.hasTimeLeft()) {

            //System.out.println("[AGV " + String.valueOf(getID()) + "] timeLapse.hasTimeLeft() = " + String.valueOf(timeLapse.hasTimeLeft()));

            // move towards destination
            move(timeLapse);

            // if arrived at destination, act on it
            tryToAct(timeLapse);
        }

    }

    @Override
    public void afterTick(TimeLapse timeLapse) {
        // do nothing
    }

    public String toString() {
        return "AGV(" + ID + ")";
    }
}
