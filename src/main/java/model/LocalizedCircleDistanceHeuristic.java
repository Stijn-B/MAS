package model;

import model.roadSignPoint.PlannedPath;

public class LocalizedCircleDistanceHeuristic extends LocalizedCircleHeuristic {

	@Override
	public double calculate(PlannedPath path) {
		return super.calculate(path) / path.getTotalPathLength();
	}

}

// vim: set noexpandtab:
