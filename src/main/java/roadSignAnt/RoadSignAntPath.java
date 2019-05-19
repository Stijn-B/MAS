package roadSignAnt;

//TODO: this class still needs to be refactored, requires RoadSignPoint to be refactored first
public class RoadSignAntPath implements Comparable<RoadSignAntPath> {

	private List<Pair<RoadSignPoint,Double>> path;


	/* HEURISTIC */

	/*
	 * TODO: I believe it might be better to dynamically calculate heuristics in Vehicle using a getHeuristic(RoadSignAntPath path) method
	 *	Alternative names: getWorth, ...
	 */
	
	// initialised to -1 -> getHeuristic() == -1 means no heuristic was assigned yet
	private double heuristic = -1;

	/**
	 * Returns the heuristic assigned to the given roadSignAnt.RoadSignAntPath.
	 * heuristic == -1 means no value was assigned yet.
	 * @return he heuristic assigned to the given roadSignAnt.RoadSignAntPath
	 */
	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double h) {
		heuristic = h;
	}


	/* EDITING */

	//TODO: better name might be followRoadSign
	public void append(RoadSignPoint rs) {
		append(new Pair(rs.getDestination(), rs.getDistance()));
	}

	public void append(Pair<RoadSignPoint,Double> a) {
		this.path.add(a);
	}


	/* CONTENT */

	public int getLength() {
		return this.path.size();
	}

	public Iterator<Pair<RoadSignPoint,Double>> getIterator() {
		return this.path.iterator();
	}

	public RoadSignAntPath copy() {
		RoadSignAntPath ap = new RoadSignAntPath();
		for (int i = 0; i < getLength(); i++) {
			ap.append(this.path.get(i));
		}
		return ap;
	}


	/* PATH CHECKING */

	/**
	 * Returns whether the given destination is acceptable considering the given path.
	 */
	public boolean acceptableNextHop(RoadSignPoint dest) {

		// if dest is an roadUser.AGV -> not ok
		if (dest.getType() == RoadSignPoint.Type.AGV) return false;

		// if dest was already passed -> not ok
		if (pathContains(dest)) return false;

		// if dest is a delivery point AND the dest owner has never been passed (so pickup was never passed) -> not ok
		if (dest.getType() == RoadSignPoint.Type.PARCEL_DELIVERY
				&& !pathContainsOwnerID(dest.getRoadSignPointOwner().getID())) return false;

		// otherwise the destination is ok
		return true;
	}

	/**
	 * Returns whether the given path contains the given RoadSignPoint.
	 */
	public boolean pathContains(RoadSignPoint rsPoint) {
		// iterate over all list pairs
		Iterator<Pair<RoadSignPoint,Double>> iterator = getIterator();
		while (iterator.hasNext()) {
			if (iterator.next().getKey() == rsPoint) return true;
		}
		return false;
	}

	/**
	 * Returns whether the given path contains a RoadSignPoint of which the RoadSignPointOwners ID equals the given ID
	 * (used for checking whether a given path contains the pickup location)
	 */
	public boolean pathContainsOwnerID(int ID) {
		// iterate over all list pairs
		Iterator<Pair<RoadSignPoint,Double>> iterator = getIterator();
		while (iterator.hasNext()) {
			if (iterator.next().getKey().getRoadSignPointOwner().getID() == ID) return true;
		}
		return false;
	}


	/* INTERFACE Comparable<roadSignAnt.RoadSignAntPath> */

	@Override
	public int compareTo(@NotNull RoadSignAntPath o) {
		if (this.getHeuristic() > o.getHeuristic()) { // Highest heuristic should be first in list -> other object is seen as bigger number and thus placed later in list
			return 1;
		} else if (this.getHeuristic() == o.getHeuristic()) {
			return 0;
		} else {
			return -1;
		}
	}
}

// vim: set noexpandtab:
