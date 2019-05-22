package model.user.owner;

import com.github.rinde.rinsim.core.model.road.MovingRoadUser;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import heuristic.Heuristic;
import model.pheromones.roadSign.PlannedPath;
import model.user.ant.*;
import model.pheromones.roadSign.RoadSignPoint;

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


    /* HEURISTIC */

    private Heuristic heuristic;

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }


    /* EXPLORATION */

    // TODO: exploration een bepaald duur geven.
    /**
     * Explores viable paths using ExplorationAnts
     * @return a List<model.pheromones.roadSign.PlannedPath> containing viable paths (can be empty)
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
        // TODO
        // EXPLORE: List<model.pheromones.roadSign.PlannedPath> paths = explorePaths();
        // HEURISTICS: getHeuristicScore().sortAntPathList(paths); // works on the given list so returns no value

        // TODO: roadSignPoint aanpassen
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
