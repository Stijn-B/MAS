package roadSignAnt.ant;

import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import roadSignAnt.*;
import roadSignAnt.point.RoadSignPoint;

import javax.measure.Measure;
import javax.measure.quantity.Length;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeasibilityAnt extends Ant implements TickListener, RoadUser {

	/* STATIC VAR */

	public static final double ROADSIGNS_PER_SEC = 20; // amount of RoadSigns the ant creates per seconds
	public static final long MS_PER_ROADSIGN = Math.round(Math.ceil(1000/ROADSIGNS_PER_SEC)); // amount of ms the ant uses to create a RoadSign

	// de kans dat als de ant bij een pickup location is, de bijbehorende delivery location als volgende punt gekozen wordt (ipv een random punt)
	public static final double FROM_PICKUP_TO_DEL_CHANCE = 0.70;


	/* CONSTRUCTOR AND OBJECT VAR */

	/**
	 * Create a FeasibilityAnt at the given roadUser.RoadSignParcel
	 * @param curr the roadUser.RoadSignParcel where to create the new FeasibilityAnt
	 */
	FeasibilityAnt(RoadSignPoint curr, RoadModel rm) {
		currentRSPoint = curr;
		roadModel = rm;
	}

	private long time = 0;

	private RoadSignPoint currentRSPoint;

	public RoadSignPoint getCurrentRSPoint() {
		return currentRSPoint;
	}

	private RoadSignPointOwner currOwner;

	public RoadSignPointOwner getCurrOwner() {
		return currOwner;
	}

	/* ROADSIGN METHODS */

	private void exploreNextOwner() {
		exploreNextOwner(getModel().getRandomOwner());
	}

	private void exploreNextOwner(RoadSignPointOwner nextOwner) {

		// create list of all RoadSignPoints (of both the current and next RoadSignPointOwner)
		List<RoadSignPoint> points = new ArrayList<>();
		points.addAll(Arrays.asList(currOwner.getRoadSignPoints()));
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

		List<Point> path = getRoadModel().getShortestPathTo(from, to); // shortest path
		Measure<Double, Length> distance = roadModel.getDistanceOfPath(path); // distance of shortest path

		// TODO: controleren of distance.getValue() effectief het juiste getal is (Measure<Double, Length> is niet helemaal duidelijk)
		from.addRoadSign(new RoadSign(to, distance.getValue()));
	}


	/* IMPLEMENTED INTERFACE METHODS */

	@Override
	public void tick(TimeLapse timeLapse) {
		// add the time of this TimeLapse to the available time
		time += timeLapse.getTimeLeft();

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


	/* DEPENDENCY INJECTIONS */

	private RoadModel roadModel;

	public void initRoadUser(RoadModel model) {
		roadModel = model;
	}

	public RoadModel getRoadModel() {
		return roadModel;
	}




}

// vim: noexpandtab:
