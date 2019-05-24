package model;

import com.github.rinde.rinsim.geom.Point;

import java.lang.Iterable;
import java.util.Iterator;

public class LocalizedRectangleHeuristic extends LocalizedDeliveryHeuristic {

	@Override
	public double circumscribedArea(Iterable<Point> points) {
		double north = 0;
		double east = 0;
		double south = 0;
		double west = 0;
		Iterator<Point> iterator = points.iterator();
		while (iterator.hasNext()) {
			Point curr = iterator.next();
			if (curr.x < west) {
				west = curr.x;
			} else if (curr.x > east) {
				east = curr.x;
			}
			if (curr.y < south) {
				south = curr.y;
			} else if (curr.y > north) {
				north = curr.y;
			}
		}
		return (north - south) * (east - west);
	}

}

// vim: set noexpandtab:
