package model.user.ant;

import com.github.rinde.rinsim.core.model.road.GraphRoadModelImpl;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import model.roadSignPoint.RoadSignPoint;
import model.user.owner.RoadSignPointOwner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FeasibilityAnt extends Ant implements TickListener, RoadUser {

	/* STATIC VAR */

	public static final double HOPS_PER_SEC = 5; // amount of RoadSigns the ant creates per seconds
	public static final long MS_PER_HOP = Math.round(Math.ceil(1000/ HOPS_PER_SEC)); // amount of ms the ant uses to create a RoadSign


	/* CONSTRUCTOR */

	/**
	 * Create a FeasibilityAnt at the given model.user.owner.RoadSignParcel
	 * @param curr the model.user.owner.RoadSignParcel where to create the new FeasibilityAnt
	 */
	public FeasibilityAnt(RoadSignPointOwner curr) {
		currOwner = curr;
	}

	private long time = 0;


	/* CURRENT LOCATION */

	private RoadSignPointOwner currOwner;

	public RoadSignPointOwner getCurrOwner() {
		return currOwner;
	}


	/* ROADSIGN METHODS */

	/**
	 * Explores to another random RoadSignPointOwner
	 */
	private void exploreNextOwner() {
		exploreNextOwner(getRoadSignPointModel().getRandomOwnerOtherThan(getCurrOwner()));
	}

	/**
	 * Takes the RoadSignPoints of the current and next RoadSignPointOwner and creates RoadSigns between all of them.
	 */
	private void exploreNextOwner(RoadSignPointOwner nextOwner) {

		// create RoadSigns between the current and the next RoadSignPointOwner.
		createRoadSigns(currOwner, nextOwner);

		// 'move' to next owner
		currOwner = nextOwner;
	}

	/**
	 * Creates RoadSigns in both directions between all points owned by the given owners.
	 */
	private void createRoadSigns(RoadSignPointOwner ownerA, RoadSignPointOwner ownerB) {
		// get all points
		List<RoadSignPoint> points = new ArrayList<>();
		points.addAll(Arrays.asList(ownerA.getRoadSignPoints()));
		points.addAll(Arrays.asList(ownerB.getRoadSignPoints()));

		// create roadsigns
		createRoadSigns(points);
	}

	/**
	 * Creates RoadSigns in both directions between all the given points.
	 */
	private void createRoadSigns(List<RoadSignPoint> points) {

		int size = points.size();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i != j) {
					boolean success = createRoadSign(points.get(i), points.get(j));
					if (!success) System.out.println("Failed to create RoadSign from " + points.get(i) + " to " + points.get(j));
				}
			}
		}
	}

	/**
	 * Create a RoadSign from one RoadSignPoint to another RoadSignPoint. Returns whether the creating succeeded
	 */
	private boolean createRoadSign(RoadSignPoint from, RoadSignPoint to) {
		try {
			List<Point> shortestPath = getRoadModel().getShortestPathTo(from.getPosition(), to.getPosition()); // shortest path
			// double distance = getRoadModel().getDistanceOfPath(shortestPath).getValue(); WERKTE NIET ALTIJD, NIEUWE METHODE +- GOED GENOEG
			double distance = getPathLength(shortestPath);
			from.addRoadSign(to, distance);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	/* PATH LENGTH */

	public double getPathLength(List<Point> path) {
		// if the path contains less than 2 points, there is no distance
		if (path.size() < 2) return 0;

		Iterator<Point> iter = path.iterator();
		Point prev = iter.next();
		Point cur = null;
		double distance = 0d;
		while (iter.hasNext()) {
			cur = iter.next();
			distance += distance(prev, cur);
			prev = cur;
		}

		return distance;
	}

	public double distance(Point a, Point b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}


	/* IMPLEMENTED INTERFACE METHODS */

	@Override
	public void tick(TimeLapse timeLapse) {
		// add the time of this TimeLapse to the available time
		time += timeLapse.getTimeLeft();
		timeLapse.consumeAll();

		// create RoadSigns untill there is no time left
		while (MS_PER_HOP <= time) {
			exploreNextOwner();
			time -= MS_PER_HOP;
		}
	}

	@Override
	public void afterTick(TimeLapse timeLapse) {
		// do nothing
	}


	/* INJECTION RoadModel */

	private RoadModel roadModel;

	public void initRoadUser(RoadModel model) {
		roadModel = model;
	}

	public RoadModel getRoadModel() {
		return roadModel;
	}


}

// vim: noexpandtab:
