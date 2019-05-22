package model.user.ant;

import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import model.roadSign.RoadSignPoint;
import model.RoadSignPointModel;
import model.user.owner.RoadSignPointOwner;

import javax.measure.Measure;
import javax.measure.quantity.Length;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeasibilityAnt extends Ant implements TickListener, RoadUser {

	/* STATIC VAR */

	public static final double ROADSIGNS_PER_SEC = 20; // amount of RoadSigns the ant creates per seconds
	public static final long MS_PER_ROADSIGN = Math.round(Math.ceil(1000/ROADSIGNS_PER_SEC)); // amount of ms the ant uses to create a RoadSign


	/* CONSTRUCTOR */

	/**
	 * Create a FeasibilityAnt at the given model.user.owner.RoadSignParcel
	 * @param curr the model.user.owner.RoadSignParcel where to create the new FeasibilityAnt
	 */
	FeasibilityAnt(RoadSignPointOwner curr) {
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
		// if one of the models is not registered yet or the argument is null, do nothing
		if (getRoadModel() == null || !hasRoadSignPointModel() || nextOwner == null) return;

		// create list of all RoadSignPoints (of both the current and next RoadSignPointOwner)
		List<RoadSignPoint> points = new ArrayList<>();
		points.addAll(Arrays.asList(getCurrOwner().getRoadSignPoints()));
		points.addAll(Arrays.asList(nextOwner.getRoadSignPoints()));
		int size = points.size();

		// create RoadSigns between all the objects in the list, in both directions (e.g. iterates over (1,0) and (0,1))
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i != j) createRoadSign(points.get(i), points.get(j));
			}
		}

		// 'move' to next owner
		currOwner = nextOwner;
	}

	/**
	 * Create a RoadSign from one RoadSignPoint to another RoadSignPoint
	 * @param from the source RoadSignPoint
	 * @param to the destination RoadSignPoint
	 */
	private void createRoadSign(RoadSignPoint from, RoadSignPoint to) {

		if (from == to) return; // don't create a RoadSign from a point to itself

		List<Point> shortestPath = getRoadModel().getShortestPathTo(from, to); // shortest path
		double distance = getRoadModel().getDistanceOfPath(shortestPath).getValue(); // distance of shortest path

		from.addRoadSign(to, distance);
	}


	/* IMPLEMENTED INTERFACE METHODS */

	@Override
	public void tick(TimeLapse timeLapse) {
		// add the time of this TimeLapse to the available time
		time += timeLapse.getTimeLeft();
		timeLapse.consumeAll();

		// create RoadSigns untill there is no time left
		while (MS_PER_ROADSIGN <= time) {
			exploreNextOwner();
			time -= MS_PER_ROADSIGN;
		}
	}

	@Override
	public void afterTick(TimeLapse timeLapse) {
		// TODO: hier evt de ant laten 'sterven' indien nodig
		// TODO: Sander: I do not think feasibility ants should die
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
