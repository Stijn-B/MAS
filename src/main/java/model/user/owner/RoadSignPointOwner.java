package model.user.owner;

import model.roadSignPoint.RoadSignPoint;
import model.user.RoadSignPointUser;

public interface RoadSignPointOwner extends RoadSignPointUser {

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

}

