package heuristic;

import model.ant.PlannedPath;

public class OtherHeuristic extends Heuristic {

    @Override
    public double calculate(PlannedPath path) {
        double plannedPathLen = path.getTotalPathLength();

        return 0;
    }

}
