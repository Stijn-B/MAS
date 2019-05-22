package model.user.owner;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import heuristic.Heuristic;
import model.RoadSignPointModel;
import model.roadSign.PlannedPath;
import model.user.ant.*;
import model.roadSign.RoadSignPoint;

import java.util.List;

public class AGV extends AbstractRoadSignPointOwner implements TickListener {

    /* CONSTRUCTOR */

    public AGV(Point startPosition, Heuristic heuristic) {
        super(OwnerType.AGV, startPosition, RoadSignPoint.PointType.AGV);
        setHeuristic(heuristic);
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

    // TODO: exploration een bepaald duur geven.
    /**
     * Explores viable paths using ExplorationAnts
     * @return a List<model.roadSign.PlannedPath> containing viable paths (can be empty)
     */
    public List<PlannedPath> explorePaths() {
        return new ExplorationAnt().explore(getRoadSignPoints()[0]);
    }


    /* INTENTIONS */

    private IntentionAnt currIntention;

    public void commit(PlannedPath path) {
        // TODO
    }


    /* INTERFACE TickListener */

    @Override
    public void tick(TimeLapse timeLapse) {
        // TODO
        // EXPLORE: List<model.roadSign.PlannedPath> paths = explorePaths();
        // HEURISTICS: getHeuristicScore().sortAntPathList(paths); // works on the given list so returns no value

        // TODO: roadSignPoint aanpassen
    }

    @Override
    public void afterTick(TimeLapse timeLapse) {
        // TODO
    }


}
