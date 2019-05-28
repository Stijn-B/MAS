package heuristic;

import com.github.rinde.rinsim.geom.Point;

import java.lang.Iterable;
import java.util.stream.Collectors;

import model.ant.PlannedPath;
import model.roadSignPoint.RoadSignPoint;

public abstract class LocalizedDeliveryHeuristic extends Heuristic {

	@Override
	public double calculate(PlannedPath path) {
		double area = circumscribedArea(
			path.getRoadSignPoint().stream().map(RoadSignPoint::getPosition).collect(Collectors.toList())
		);
		if (area == 0) {
			// don't stay in one place
			return 0;
		}
		return ((double) path.getNbDeliveries()) / area;
	}

	public abstract double circumscribedArea(Iterable<Point> points);

}

// vim: set noexpandtab:
