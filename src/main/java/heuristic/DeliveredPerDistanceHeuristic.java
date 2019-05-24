package heuristic;

import heuristic.Heuristic;
import model.roadSignPoint.PlannedPath;

public class DeliveredPerDistanceHeuristic extends Heuristic {

	@Override
	public double calculate(PlannedPath path) {
		return ((double) path.getNbDeliveries()) / path.getTotalPathLength();
	}

}

// vim: set noexpandtab:
