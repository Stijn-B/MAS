package heuristic;

import com.github.rinde.rinsim.geom.Point;

import java.lang.Iterable;

import heuristic.Heuristic;
import model.roadSignPoint.PlannedPath;

public abstract class LocalizedDeliveryHeuristic extends Heuristic {

	@Override
	public double calculate(PlannedPath path) {
		double area = circumscribedArea(path.getPoints());
		if (area == 0) {
			// don't stay in one place
			return 0;
		}
		return ((double) path.getNbDeliveries()) / area;
	}

	public abstract double circumscribedArea(Iterable<Point> points);

}

// vim: set noexpandtab:
