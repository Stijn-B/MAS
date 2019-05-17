package heuristic;

import roadSignAnt.ant.PlannedPath;

import java.util.Collections;
import java.util.List;

public abstract class Heuristic {

    /**
     *  Calculates the heuristic of the given roadSignAnt.ant.PlannedPath.
     * @param path the path for which to calculate the heuristic
     * @return the heuristic of the given roadSignAnt.ant.PlannedPath
     */
    public abstract double calculate(PlannedPath path);

    /**
     * Assigns heuristics to the AntPaths in the given list.
     * @param list list of AntPaths
     */
    public void assignHeuristics(List<PlannedPath> list) {
        for (PlannedPath ap : list) {
            ap.setHeuristic(calculate(ap));
        }
    }

    /**
     * Assigns heuristics to the AntPaths in the given list and sorts the list based on these heuristics.
     * @param list
     */
    public void sortAntPathList(List<PlannedPath> list) {
        assignHeuristics(list);
        Collections.sort(list);
    }
}
