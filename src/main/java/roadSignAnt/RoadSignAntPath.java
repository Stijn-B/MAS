package roadSignAnt;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import roadSignAnt.point.RoadSignPoint;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class RoadSignAntPath implements Comparable<RoadSignAntPath> {

	private List<RoadSign> path = new LinkedList<>();

	private void append(RoadSign point) {
		this.path.add(point);
	}


	/**
	 * Appends the given RoadSign to the path. Throws an InvalidArgumentException if the given RoadSign
	 * can't be added
	 * @param sign the RoadSign to be added.
	 * @throws InvalidArgumentException if the given RoadSign can't be added
	 */
	public void appendRoadSign(RoadSign sign) throws InvalidArgumentException {
		if (! isAllowedHop(sign)) throw new InvalidArgumentException(
				new String[]{"Given RoadSign can't be added, see 'isAllowedHop' method."});
		else append(sign);
	}

	/* HEURISTIC */

	// initialised to -1 -> getHeuristic() == -1 means no heuristic was assigned yet
	private double heuristic = -1;

	/**
	 * Returns the heuristic assigned to the given roadSignAnt.ant.PlannedPath.
	 * heuristic == -1 means no value was assigned yet.
	 * @return he heuristic assigned to the given roadSignAnt.ant.PlannedPath
	 */
	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double h) {
		heuristic = h;
	}


	/* CONTENT */

	public int getNbHops() {
		return this.path.size();
	}

	/**
	 * Returns the total length of the path. Calculated by summing the RoadSign distances.
	 * @returnthe total length of the path
	 */
	public double getPathLength() {

		double sum = 0;

		// iterate over all list pairs
		Iterator<RoadSign> iterator = getIterator();
		while (iterator.hasNext()) {
			sum += iterator.next().getDistance();
		}

		return sum;
	}

	public RoadSign getLast() {
		return path.get(path.size());
	}

	public Iterator<RoadSign> getIterator() {
		return path.iterator();
	}

	/**
	 * Returns a copy of this RoadSignAntPath. References the same RoadSignPoints
	 */
	public RoadSignAntPath copy() {
		RoadSignAntPath ap = new RoadSignAntPath();
		for (int i = 0; i < getNbHops(); i++) {
			ap.append(path.get(i));
		}
		return ap;
	}


	/* PATH CHECKING */

	/**
	 * Returns whether the given hop is allowed considering the current path.
	 */
	public boolean isAllowedHop(RoadSign sign) {

		// must continue current path
		if (! sign.getLocation().equals(getLast().getDestination())) {
			return false;
		}

		// must not have passed destination yet
		if (contains(sign.getDestination())) {
			return false;
		}

		// TODO: oude versie heeft checks die mss best ook geïmplementeerd worden
		/*
		// if dest is an roadUser.AGV -> not ok
		if (dest.getType() == RoadSignPoint.Type.AGV) return false;

		// if dest is a delivery point AND the dest owner has never been passed (so pickup was never passed) -> not ok
		if (dest.getType() == RoadSignPoint.Type.PARCEL_DELIVERY
				&& !pathContainsOwnerID(dest.getRoadSignPointOwner().getID())) return false;
		 */

		return true;
	}

	/**
	 * Returns whether the given path contains the given RoadSignPoint.
	 */
	public boolean contains(RoadSignPoint rsPoint) {
		// iterate over all list pairs
		Iterator<RoadSign> iterator = getIterator();
		while (iterator.hasNext()) {
			if (iterator.next().getLocation() == rsPoint) return true;
		}
		return false;
	}


	/* INTERFACE Comparable<RoadSignAntPath> */

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
