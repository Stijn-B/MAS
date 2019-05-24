package heuristic;

import model.roadSignPoint.PlannedPath;

import java.util.Collections;
import java.util.List;

public abstract class Heuristic {

    /* CALCULATING HEURISTIC */

    /**
     *  Calculates the heuristic of the given PlannedPath.
     * @param path the path for which to calculate the heuristic
     * @return the heuristic of the given PlannedPath
     */
    public abstract double calculate(PlannedPath path);

    /**
     * Assigns heuristics to the PlannedPaths in the given list.
     * @param list list of PlannedPaths
     */
    public void assignHeuristics(List<PlannedPath> list) {
        for (PlannedPath ap : list) {
            ap.setHeuristicScore(calculate(ap));
        }
    }


    /* SELECTING PATH */

    /**
     * Assigns heuristics to the PlannedPaths in the given list and sorts the list based on these heuristics.
     * @param list
     */
    public void sortPlannedPathList(List<PlannedPath> list) {
        assignHeuristics(list);
        Collections.sort(list);
    }

    public PlannedPath getBest(List<PlannedPath> paths) {
        sortPlannedPathList(paths);
        if (paths.isEmpty()) return null;
        else return paths.get(0);
    }
}
