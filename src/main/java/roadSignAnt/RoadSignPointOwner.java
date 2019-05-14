package roadSignAnt;

public interface RoadSignPointOwner {

	// ID of the RoadSignPointOwner
	public int getID();

	// Type of the RoadSignPointOwner
	enum Type { PARCEL, AGV, BASE }
	Type getRoadSignPointOwnerType();

	// Array of the RoadSignPoints owned by the RoadSignPointOwner
	RoadSignPoint[] getRoadSignPoints();

}

