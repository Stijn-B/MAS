package model.user.owner;

import com.github.rinde.rinsim.core.model.road.MovingRoadUser;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import heuristic.Heuristic;
import model.roadSignPoint.PlannedPath;
import model.user.ant.*;
import model.roadSignPoint.RoadSignPoint;

import java.util.LinkedList;
import java.util.List;

public class AGV extends AbstractRoadSignPointOwner implements TickListener, MovingRoadUser, RoadSignPointOwner {

    static final double AGV_SPEED = 20400;

    // default time between reconsidering the committed path
    static final long RECONSIDER_DELAY = 5000;

    static long NOW = 0; // current time, updated every tick()

    /* CONSTRUCTOR */

    public AGV(Point startPosition, Heuristic heuristic) {
        super(OwnerType.AGV, startPosition, RoadSignPoint.PointType.AGV);

        setHeuristic(heuristic);
    }


    /* PROPERTIES */

    private double speed = AGV_SPEED;

    public long distanceToDuration(double distance) {
        return Math.round(distance/speed);
    }

    public long distanceToETA(double distance) {
        return NOW + distanceToDuration(distance);
    }

    /**
     * Returns whether this AGV is at the given point (on its RoadModel)
     */
    public boolean isAtPosition(Point point) {
        return getRoadModel() != null
                && getRoadModel().getPosition(this) == point;
    }

    public Point getPosition() {
        return getRoadSignPoints()[0].getPosition();
    }

    public boolean planPassingBase() {
        return false;
    }


    /* PARCELS */

    private LinkedList<RoadSignParcel> parcels = new LinkedList<>();

    public LinkedList<RoadSignParcel> getParcels() {
        return parcels;
    }

    public void addParcel(RoadSignParcel parcel) {
        parcels.add(parcel);
    }

    public void removeParcel(RoadSignParcel parcel) {
        parcels.remove(parcel);
    }

    public boolean carries(RoadSignParcel parcel) {
        return parcels.contains(parcel);
    }

    public void deliverParcel(RoadSignParcel parcel) {
        if (carries(parcel)) {
            removeParcel(parcel);
            // TODO: registreren dat deze parcel is afgeleverd.
        }
    }


    /* HEURISTIC */

    private Heuristic heuristic;

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
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
        return new ExplorationAnt(this).explore(getRoadSignPoints()[0], new PlannedPath(this));
    }


    /* INTENDED PATH */

    private PlannedPath intendedPath = new PlannedPath();

    private PlannedPath getIntendedPath() {
        if (intendedPath == null) return new PlannedPath();
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
        return new IntentionAnt().declareIntention(this, path, timeNow);
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
        getRoadModel().moveTo(this, getIntendedPath().getFirstDest().getPosition(), tm);
        //System.out.println("[AGV " + getID() + "] moved to " + getRoadModel().getPosition(this));

        // keep RoadSignPoint position up to date
        roadSignPoints[0].setPosition(getRoadModel().getPosition(this));
    }

    /**
     * Acts on the given RoadSignPoint. Does nothing if not at the given RoadSignPoints location.
     */
    public void tryToAct(TimeLapse tm) {

        // if there is no PlannedPath, do nothing
        if (getIntendedPath() == null) return;

        RoadSignPoint dest = getIntendedPath().getFirstDest();

        // if the AGV has no destination to act on, do nothing
        if (dest == null) return;

        // if the AGV is not at the position of the destination, do nothing
        if (!isAtPosition(dest.getPosition())) return;

        // otherwise, act on the destination and remove it from the path
        System.out.println("[AGV " + getID() + "] At position, try to act");
        System.out.println(getIntendedPath());

        if (dest.act(this)) getIntendedPath().popFirst();
        System.out.println(getIntendedPath());
    }


    /* INTERFACES */

    // TickListener

    @Override
    public void tick(TimeLapse timeLapse) {
        long now = timeLapse.getTime();
        NOW = now;


        System.out.println("[AGV " + String.valueOf(getID()) + "]");  // PRINT

        // CONSIDER EXPLORING NEW PATH

        if (reconsiderCondition(now)) {
            System.out.println("[AGV " + String.valueOf(getID()) + "] Explore new path");  // PRINT
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

    // MovingRoadUser

    RoadModel roadModel;

    RoadModel getRoadModel() {
        return roadModel;
    }

    @Override
    public void initRoadUser(RoadModel model) {
        // dit geeft een error 'RoadUser does not exist' : model.getPosition(this);
        roadModel = model;

        if (getPosition() != null) {
            model.addObjectAt(this, getPosition());
        }
    }

    @Override
    public double getSpeed() {
        return speed;
    }
}
