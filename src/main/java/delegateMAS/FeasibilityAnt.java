package delegateMAS;

import com.github.rinde.rinsim.core.model.pdp.PDPModel;
import com.github.rinde.rinsim.core.model.pdp.PDPObjectImpl;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import roadSign.RoadSign;
import roadSign.RoadSignModel;
import roadSign.RoadSignParcel;
import roadSign.RoadSignPoint;

import javax.measure.Measure;
import javax.measure.quantity.Length;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeasibilityAnt implements TickListener {


	/* STATIC VAR */

	public static final double ROADSIGNS_PER_SEC = 20; // amount of RoadSigns the ant creates per seconds
	public static final long MS_PER_ROADSIGN = Math.round(Math.ceil(1000/ROADSIGNS_PER_SEC)); // amount of ms the ant uses to create a RoadSign


	//TODO: this should be done by BDI I think
	// de kans dat als de ant bij een pickup location is, de bijbehorende delivery location als volgende punt gekozen wordt (ipv een random punt)
	public static final double FROM_PICKUP_TO_DEL_CHANCE = 0.25;


	/* CONSTRUCTOR AND OBJECT VAR */

	/**
	 * Create a FeasibilityAnt at the given RoadSignParcel
	 * @param curr the RoadSignParcel where to create the new FeasibilityAnt
	 */
	FeasibilityAnt(RoadSignParcel curr, RoadModel rm) {
		currentRSPoint = curr.getPickupLocationRoadSignPoint();
		roadModel = rm;
	}

	private long time = 0;

	private RoadSignPoint currentRSPoint;
	private RoadModel roadModel;

	private RoadModel getRoadModel() {
		return roadModel;
	}


	/* ROADSIGN METHODS */

	/**
	 * Choose a next RoadSignPoint, create a RoadSign that points to it, and move to this chosen RoadSignPoint
	 */
	private void nextRoadSign() {
		RoadSignPoint next;

		// if at pickup point, might go straight to delivery point (otherwise to a random point
		if (currentRSPoint == currentRSPoint.getParcel().getPickupLocationRoadSignPoint() && roadSignModel.getRandomDouble() <= FROM_PICKUP_TO_DEL_CHANCE) {
			next = currentRSPoint.getParcel().getDeliveryLocationRoadSignPoint();
		} else {
			// get a random roadSignParcel
			RoadSignParcel parcel = roadSignModel.getRandomRoadSignParcel();
			// 60/40 chance of going to the pickup/delivery location
			next = roadSignModel.getRandomDouble() < 0.6 ? parcel.getPickupLocationRoadSignPoint() : parcel.getDeliveryLocationRoadSignPoint();
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
	}


	/* DEPENDENCY INJECTION */
	// TODO: dependency injection van deze Model fixen

	void injectRoadSignModel(RoadSignModel m) {
		roadSignModel = m;
	}

	private RoadSignModel roadSignModel;


}

// vim: noexpandtab:
