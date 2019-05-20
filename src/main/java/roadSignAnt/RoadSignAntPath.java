package roadSignAnt;

import org.jetbrains.annotations.NotNull;
import roadSignAnt.point.RoadSignPoint;

import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class RoadSignAntPath implements Comparable<RoadSignAntPath> {

	private List<RoadSign> path = new LinkedList<>();

	private void append(RoadSignPoint point) {
		this.path.add(point);
	}

	/**
	 * Returns whether the given hop is allowed considering the current path.
	 */
	public boolean isAllowedHop(RoadSign sign) {
		if (! sign.getLocation().equals(this.path.getLast().getDestination())) {
			return false;
		}
		if (contains(destination)) {
			return false;
		}
		return true;
	}

	public void followRoadSign(RoadSign sign) {
		if (! isAllowedHop(sign)) {
			//TODO: throw suitable exception
			throw new Exception("Hop not allowed.");
		}
		append(sign);
	}

	public double getPathLength() {
		//TODO: try RoadSign::getDistance instead of lambda
		this.path.stream().mapToDouble(s -> s.getDistance()).sum();
	}

	public int getNbHops() {
		return this.path.size();
	}

	/**
	 * Returns whether the given path contains the given RoadSignPoint.
	 */
	public boolean pathContains(RoadSignPoint rsPoint) {
		try {
			if (this.path.getFirst().getLocation().equals(rsPoint)) {
				return true;
			}
			Iterator<RoadSign> iterator = this.path.iterator();
			while (iterator.hasNext()) {
				if (rsPoint.equals(iterator.next().getDestination())) {
					return true;
				}
			}
			return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	@Override
	public int compareTo(@NotNull RoadSignAntPath o) {
		return 0;
	}
}

// vim: set noexpandtab:
