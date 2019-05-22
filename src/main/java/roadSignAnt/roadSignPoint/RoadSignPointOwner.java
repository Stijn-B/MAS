package roadSignAnt.roadSignPoint;

import javax.annotation.Nullable;

public interface RoadSignPointOwner extends RoadSignPointUser {

	// ID of the RoadSignPointOwner
	int getID();

	// PointType of the RoadSignPointOwner
	enum Type { PARCEL, AGV, BASE }
	Type getRoadSignPointOwnerType();

	// Array of the RoadSignPoints owned by the RoadSignPointOwner
	RoadSignPoint[] getRoadSignPoints();

	boolean equals(RoadSignPointOwner other);

}

