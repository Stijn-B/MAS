package roadSignAnt.roadSignPoint;

public interface RoadSignPointOwner extends RoadSignPointUser {

	// ID of the RoadSignPointOwner
	int getID();

	// Type of the RoadSignPointOwner
	enum Type { PARCEL, AGV, BASE }
	Type getRoadSignPointOwnerType();

	// Array of the RoadSignPoints owned by the RoadSignPointOwner
	RoadSignPoint[] getRoadSignPoints();

}

