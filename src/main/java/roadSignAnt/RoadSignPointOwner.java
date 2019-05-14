package roadSignAnt;

public abstract class RoadSignPointOwner {

	private static long ID_COUNTER = 0;

	public static long loopAroundIncrement(long id) {
		if (id < Long.MAX_VALUE)
			return id + 1;
		else
			return 0;
	}

	public static int loopAroundIncrement(int id) {
		if (id < Integer.MAX_VALUE)
			return id + 1;
		else
			return 0;
	}

	/**
	 * Abstract class that implements the ownership of RoadSignPointss
	 * @param points one or an array of RoadSignPoints to be held by the object
	 */
	public RoadSignPointOwner(RoadSignPointOwnerType type, RoadSignPoint... points) {
		this.type = type;
		this.points = points;
		ID = ID_COUNTER;
		loopAroundIncrement(ID_COUNTER);
	}

	/* TYPE */
	public enum RoadSignPointOwnerType { PARCEL, AGV, BASE }
	private final RoadSignPointOwnerType type;
	public RoadSignPointOwnerType getType() {
		return type;
	}

	/* ROADSINGPOINTS */
	private final RoadSignPoint[] points;
	public RoadSignPoint[] getRoadSignPoints() {
		return points.clone();
	}

	/* ID */
	private final long ID;
	public long getID() {
		return ID;
	}





}

