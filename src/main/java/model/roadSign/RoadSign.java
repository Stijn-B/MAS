package model.roadSign;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RoadSign implements Comparable<RoadSign> {

	/* STATIC VAR */

	public static long DEFAULT_LIFETIME_MS = 20000; // default life time of a RoadSign in milliseconds


	/* CONSTRUCTORS */

	/**
	 * Creates a model.roadSign.RoadSign object
	 * @param destination the destination point of the model.roadSign.RoadSign
	 * @param distance the distance between the start- and endpoints
	 * @param lifeTime how long the model.roadSign.RoadSign should stay alive in milliseconds
	 */
	public RoadSign(RoadSignPoint location, RoadSignPoint destination, double distance, long lifeTime) {
		this.location = location;
		this.destination = destination;
		this.distance = distance;
		this.life = lifeTime;
	}

	/**
	 * Creates a model.roadSign.RoadSign object. The model.roadSign.RoadSign.DEFAULT_LIFETIME_MS is taken as the lifeTime
	 * @param destination the destination point of the model.roadSign.RoadSign
	 * @param distance the distance between the start- and endpoints
	 */
	public RoadSign(RoadSignPoint location, RoadSignPoint destination, double distance) {
		this(location, destination, distance, DEFAULT_LIFETIME_MS);
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
	 * Get the distance between the start and endpoint of the model.roadSign.RoadSign
	 * @return the distance between the start and endpoint of the model.roadSign.RoadSign
	 */
	public double getDistance() {
		return distance;
	}

	/* AGE */

	private double life;

	/**
	 * Get the remaining life time of the model.roadSign.RoadSign
	 * @return the remaining life time of the model.roadSign.RoadSign
	 */
	public double getRemainingLifeTime() {
		return life;
	}

	/**
	 * Returns whether the given RoadSign is still alive.
	 * @return whether the given RoadSign is still alive
	 */
	public boolean alive() {
		return 0 < getRemainingLifeTime();
	}

	/**
	 * Ages the model.roadSign.RoadSign by the given amount of milliseconds and returns whether the model.roadSign.RoadSign has life time left
	 * @param ms how much milliseconds the model.roadSign.RoadSign should be aged
	 */
	public boolean age(long ms) {
		life -= ms;
		return alive();
	}


	/* OVERRIDDEN METHODS */

	@Override
	public String toString() {
		return "model.roadSign.RoadSign to " + getDestination().toString() + ", distance: " + String.valueOf(getDistance()) + ", remaining lifetime: " + String.valueOf(getRemainingLifeTime());
	}

	@Override
	public int compareTo(@NotNull RoadSign o) throws NullPointerException {
		if (o == null) throw new NullPointerException("compareTo argument can't be null");

		// misschien niet de efficientste methode
		if (this.getDistance() > o.getDistance()) {
			return 1;
		} else if (this.getDistance() == o.getDistance()) {
			return 0;
		} else {
			return -1;
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
			RoadSign p = (RoadSign) other;
			return this.getDestination() == p.getDestination() && this.getDistance() == p.getDistance() && this.getLocation() == p.getLocation();
		}
	}
}
