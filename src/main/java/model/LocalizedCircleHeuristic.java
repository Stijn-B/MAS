package model;

import com.github.rinde.rinsim.geom.Point;

import java.lang.Iterable;
import java.util.Iterator;
import java.lang.Math;

import model.roadSignPoint.PlannedPath;

public class LocalizedCircleHeuristic extends LocalizedDeliveryHeuristic {

	@Override
	public double circumscribedArea(Iterable<Point> points) {
		Point centroid = Point.centroid(points);
		double radius = 0;
		Iterator<Point> = points.iterator();
		while (iterator.hasNext()) {
			double diff = Point.distance(iterator.next(), centroid);
			if (diff > radius) {
				radius = diff;
			}
		}
		return Math.PI * Math.pow(radius, 2);
	}

}

// vim: set noexpandtab:
