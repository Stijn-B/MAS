package roadSignAnt.ant;

import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import roadSignAnt.*;

import javax.measure.Measure;
import javax.measure.quantity.Length;
import java.util.List;

public class FeasibilityAnt extends Ant implements TickListener, RoadUser {

	/* STATIC VAR */

	public static final double ROADSIGNS_PER_SEC = 20; // amount of RoadSigns the ant creates per seconds
	public static final long MS_PER_ROADSIGN = Math.round(Math.ceil(1000/ROADSIGNS_PER_SEC)); // amount of ms the ant uses to create a RoadSign

	//TODO: Sander: pick suitable chance here
	// de kans dat als de ant bij een pickup location is, de bijbehorende delivery location als volgende punt gekozen wordt (ipv een random punt)
	public static final double FROM_PICKUP_TO_DEL_CHANCE = 0.25;


	/* CONSTRUCTOR AND OBJECT VAR */

	/**
	 * Create a FeasibilityAnt at the given RoadSignParcel
	 * @param curr the RoadSignParcel where to create the new FeasibilityAnt
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


	/* ROADSIGN METHODS */

	/**
	 * Choose a next RoadSignPoint, create a RoadSigns, and move to this chosen RoadSignPoint
	 */
	private void nextRoadSign() {
		RoadSignPoint next;

		// if at pickup point, might go straight to delivery point (otherwise to a random point)
		if (getCurrentRSPoint().getRoadSignPointOwner().getType() == RoadSignPointOwner.Type.PARCEL)
		if (currentRSPoint == currentRSPoint.getParcel().getPickupLocationRoadSignPoint() && roadSignModel.getRandomDouble() <= FROM_PICKUP_TO_DEL_CHANCE) {
			next = currentRSPoint.getParcel().getDeliveryLocationRoadSignPoint();
		} else {
			// get a random roadSignParcel
			RoadSignParcel parcel = getModel().getRandomRoadSignParcel();
			// 60/40 chance of going to the pickup/delivery location
			next = getModel().getRandomDouble() < 0.6 ? parcel.getPickupLocationRoadSignPoint() : parcel.getDeliveryLocationRoadSignPoint();
		}

		if (next != null) {
			// create RoadSign
			createRoadSign(currentRSPoint, next);
			createRoadSign(next, currentRSPoint);

			// move to new RoadSign
			currentRSPoint = next;
		}
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
			nextRoadSign();
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


	public void injectRoadSignModel(RoadSignModel m) {
		roadSignModel = m;
	}

	private RoadSignModel roadSignModel;

	public RoadSignModel getRoadSignModel() {
		return roadSignModel;
	}


}

// vim: noexpandtab:
