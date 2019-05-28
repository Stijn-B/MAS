package model.user.owner;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import model.roadSignPoint.RoadSignPoint;
import model.user.RoadSignPointUser;

public interface RoadSignPointOwner extends RoadSignPointUser, TickListener {

	// ID of the RoadSignPointOwner
	int getID();

	// PointType of the RoadSignPointOwner
	enum OwnerType { PARCEL, AGV, BASE, UNDEFINED }
	OwnerType getRoadSignPointOwnerType();

	// Array of the RoadSignPoints owned by the RoadSignPointOwner
	RoadSignPoint[] getRoadSignPoints();

	/**
	 * The given AGV is at one if this owners points and acts on it. Returns whether the act was successful.
	 */
	boolean act(AGV agv, RoadSignPoint rsPoint);

	@Override
	int hashCode();

	@Override
	boolean equals(Object other);

	@Override
	default void tick(TimeLapse timeLapse) {
		// do nothing
	}

	@Override
	default void afterTick(TimeLapse timeLapse) {
		for (RoadSignPoint rsp : getRoadSignPoints()) {
			rsp.age(timeLapse.getTickLength());
		}
	}

}

