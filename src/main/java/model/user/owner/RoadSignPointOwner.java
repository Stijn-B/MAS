package model.user.owner;

import model.pheromones.roadSign.RoadSignPoint;
import model.user.RoadSignPointUser;

public interface RoadSignPointOwner extends RoadSignPointUser {

	// ID of the RoadSignPointOwner
	int getID();

	// PointType of the RoadSignPointOwner
	enum OwnerType { PARCEL, AGV, BASE, UNDEFINED }
	OwnerType getRoadSignPointOwnerType();

	// Array of the RoadSignPoints owned by the RoadSignPointOwner
	RoadSignPoint[] getRoadSignPoints();

	@Override
	int hashCode();

	@Override
	boolean equals(Object other);

}

