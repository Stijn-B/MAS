package roadSignAnt;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class RoadSign implements Comparable<RoadSign> {

	/**
	 * Creates a roadSignAnt.RoadSign object
	 * @param destination the destination point of the roadSignAnt.RoadSign
	 * @param distance the distance to the destination
	 */
	public RoadSign(RoadSignPoint destination, double distance) {
		this.destination = destination;
		this.distance = distance;
	}

	private final RoadSignPoint destination;
	private final double distance;

	/**
	 * Get the endpoint of the Roadsign
	 * @return the endpoint of the Roadsign
	 */
	public RoadSignPoint getDestination() {
		return this.destination;
	}

	/**
	 * Get the distance between the start and endpoint of the roadSignAnt.RoadSign
	 * @return the distance between the start and endpoint of the roadSignAnt.RoadSign
	 */
	public double getDistance() {
		return this.distance;
	}

	@Override
	public String toString() {
		return "roadSignAnt.RoadSign to " + getDestination().toString() + ", distance: " + String.valueOf(getDistance());
	}

	@Override
	public int compareTo(@NotNull RoadSign o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException("compareTo argument can't be null");
		}
		return Double.compare(getDistance(), o.getDistance());
	}

	/**
	 * 2 RoadSigns are equal if they indicate the same distance to the same destination
	 * @param other the other object
	 * @return whether the given Object is equal to this RoadSign
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null || !(other instanceof RoadSign)) {
			return false;
		} else {
			RoadSign p = (RoadSign) other;
			return this.getDestination() == p.getDestination() && this.getDistance() == p.getDistance();
		}
	}

}

// vim: set noexpandtab:
