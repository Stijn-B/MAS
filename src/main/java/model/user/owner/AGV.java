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

    static final double AGV_SPEED = 5.5;

    // default time between reconsidering the committed path
    static final long RECONSIDER_DELAY = 5000;

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

    /**
     * Returns whether this AGV is at the given point (on its RoadModel)
     */
    public boolean isAtPosition(Point point) {
        return getRoadModel() != null
                && getRoadModel().getPosition(this) == point;
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
        System.out.println("[AGV " + String.valueOf(getID()) + "] chooseNewPath()"); // PRINT

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
        return new ExplorationAnt().explore(getRoadSignPoints()[0], new PlannedPath(this));
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
        dest.act(this);
        getIntendedPath().popFirst(dest);
    }


    /* INTERFACES */

    // TickListener

    @Override
    public void tick(TimeLapse timeLapse) {
        long now = timeLapse.getTime();


        System.out.println("[AGV " + String.valueOf(getID()) + "] tick()");  // PRINT

        // CONSIDER EXPLORING NEW PATH

        if (reconsiderCondition(now)) chooseNewPath(now);

        // MOVING AND HANDLE

        while (hasDestination() && timeLapse.hasTimeLeft()) {

            System.out.println("[AGV " + String.valueOf(getID()) + "] timeLapse.hasTimeLeft() = " + String.valueOf(timeLapse.hasTimeLeft()));

            // move towards destination
            move(timeLapse);

            // if arrived at destination, act on it
            tryToAct(timeLapse);
        }

    }

    @Override
    public void afterTick(TimeLapse timeLapse) {
        // TODO
    }

    // MovingRoadUser

    RoadModel roadModel;

    RoadModel getRoadModel() {
        return roadModel;
    }

    @Override
    public void initRoadUser(RoadModel model) {
        roadModel = model;
    }

    @Override
    public double getSpeed() {
        return speed;
    }
}
