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

    /* CONSTRUCTOR */

    public AGV(Point startPosition, Heuristic heuristic) {
        super(OwnerType.AGV, startPosition, RoadSignPoint.PointType.AGV);

        setHeuristic(heuristic);
    }


    /* SPEED */

    public long distanceToDuration(double distance) {
        return Math.round(distance/speed);
    }

    private double speed = 5.5;


    /* ROADSIGN POINTS */

    /**
     * Acts on the given RoadSignPoint. Does nothing if not at the given RoadSignPoints location.
     */
    public void act(RoadSignPoint rsPoint) {
        rsPoint.act(this);
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
        removeParcel(parcel);
        // TODO: registreren dat deze parcel is afgeleverd.
    }

    /* HEURISTIC */

    private Heuristic heuristic;

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }


    /* EXPLORATION */

    private long lastExploreTime = 0;

    private long deltaTimeExplore = 5000;

    /**
     * Returns whether the AGV should explore
     * @param now
     * @return
     */
    private boolean exploreCondition(long now) {
        if (now - lastExploreTime >= deltaTimeExplore) {
            lastExploreTime = now;
            return true;
        } else {
            return false;
        }
    }

    // TODO: exploration een bepaald duur geven.
    /**
     * Explores viable paths using ExplorationAnts
     * @return a List<PlannedPath> containing viable paths (can be empty)
     */
    public List<PlannedPath> explorePaths() {
        return new ExplorationAnt().explore(getRoadSignPoints()[0]);
    }


    /* INTENDED PATH */

    private PlannedPath intendedPath;

    private PlannedPath getIntendedPath() {
        return intendedPath;
    }

    private void setIntendedPath(PlannedPath intendedPath) {
        this.intendedPath = intendedPath;
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


    /* INTERFACES */

    // TickListener

    @Override
    public void tick(TimeLapse timeLapse) {
        long now = timeLapse.getTime();

        // EXPLORING NEW PATH

        if (exploreCondition(now)) {

            // explore possible paths
            List<PlannedPath> paths = explorePaths();

            // sort them by heuristic
            getHeuristic().sortPlannedPathList(paths); // works on the given list so returns no value

            // commit to best path
            commit(paths.get(0), now);
        }

        // MOVING OVER COMITTED PATH

        // TODO

        // keep RoadSignPoint position up to date
        roadSignPoints[0].setPosition(getRoadModel().getPosition(this));
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
