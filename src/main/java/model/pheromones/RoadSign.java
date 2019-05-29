package model.pheromones;

import model.roadSignPoint.RoadSignPoint;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RoadSign extends AgingPheromone implements Comparable<RoadSign> {

	/* STATIC CONST */

	public static final int DEFAULT_LIFETIME = 10000;

	/* CONSTRUCTORS */

	/**
	 * Creates a RoadSign object
	 * @param destination the destination point of the RoadSign
	 * @param distance the distance between the start- and endpoints
	 * @param lifeTime how long the RoadSign should stay alive in milliseconds
	 */
	public RoadSign(RoadSignPoint location, RoadSignPoint destination, double distance, long lifeTime) {
		super(lifeTime);
		this.location = location;
		this.destination = destination;
		this.distance = distance;
	}

	/**
	 * Creates a RoadSign object. The RoadSign.DEFAULT_LIFETIME_MS is taken as the lifeTime
	 * @param destination the destination point of the RoadSign
	 * @param distance the distance between the start- and endpoints
	 */
	public RoadSign(RoadSignPoint location, RoadSignPoint destination, double distance) {
		this(location, destination, distance, DEFAULT_LIFETIME);
	}


	/* INFORMATION */

	private final RoadSignPoint location;
	private final RoadSignPoint destination;
	private final double distance;

	public RoadSignPoint getLocation() {
		return location;
	}

	/**
	 * Get the endpoint of the Roadsign
	 * @return the endpoint of the Roadsign
	 */
	public RoadSignPoint getDestination() {
		return destination;
	}

	/**
	 * Get the distance between the start and endpoint of the RoadSign
	 * @return the distance between the start and endpoint of the RoadSign
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Returns whether this RoadSign is valid. A RoadSign is seen as valid if its location and destination have access
	 * to the RoadSignPointModel (and thus are registered to it)
	 * @return
	 */
	public boolean isValid() {
		return getLocation().hasRoadSignPointModel() && getDestination().hasRoadSignPointModel();
	}


	/* OVERRIDDEN METHODS */

	@Override
	public String toString() {
		return "RS[" + getLocation() + " to " + getDestination().toString() + " | dist("
				+ Math.round(getDistance()) + " )";
	}

	@Override
	public int compareTo(@NotNull RoadSign o) throws NullPointerException {
		if (o == null) throw new NullPointerException("compareTo argument can't be null");

		if (this.equals(o) || this.getDistance() == o.getDistance()) {
			return 0;
		} else if (this.getDistance() > o.getDistance()) {
			return 1;
		} else {
			return -1;
		}
	}


	public boolean equals(@Nullable RoadSign other) {
		if (other == null) {
			return false;
		} else {
			return this.getDestination() == other.getDestination() && this.getLocation() == other.getLocation();
		}
	}

	/**
	 * 2 RoadSigns are equal if they indicate the same distance to the same destination (lifetime is not relevant
	 * @param other the other object
	 * @return whether the given Object is equal to this RoadSign
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null || !(other instanceof RoadSign)) {
			return false;
		} else {
			return this.equals((RoadSign) other);
		}
	}
}
