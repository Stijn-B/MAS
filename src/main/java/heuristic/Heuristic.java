package heuristic;

import roadSignAnt.RoadSignAntPath;
import roadSignAnt.ant.PlannedPath;

import java.util.Collections;
import java.util.List;

//TODO: not refactored yet
public abstract class Heuristic {

    /**
     *  Calculates the heuristic of the given roadSignAnt.ant.PlannedPath.
     * @param path the path for which to calculate the heuristic
     * @return the heuristic of the given roadSignAnt.ant.PlannedPath
     */
    public abstract double calculate(RoadSignAntPath path);

    /**
     * Assigns heuristics to the AntPaths in the given list.
     * @param list list of AntPaths
     */
    public void assignHeuristics(List<RoadSignAntPath> list) {
        for (RoadSignAntPath ap : list) {
            ap.setHeuristic(calculate(ap));
        }
    }

    /**
     * Assigns heuristics to the AntPaths in the given list and sorts the list based on these heuristics.
     * @param list
     */
    public void sortAntPathList(List<RoadSignAntPath> list) {
        assignHeuristics(list);
        Collections.sort(list);
    }
}
