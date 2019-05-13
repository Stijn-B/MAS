package roadSign;

import com.github.rinde.rinsim.geom.Point;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RoadSign implements Comparable<RoadSign> {

	// default life time of a roadSign.RoadSign
	public static long DEFAULT_LIFETIME_MS = 20000;

	/**
	 * Creates a roadSign.RoadSign object
	 * @param destination the destination point of the roadSign.RoadSign
	 * @param distance the distance between the start- and endpoints
	 * @param lifeTime how long the roadSign.RoadSign should stay alive in milliseconds
	 */
	public RoadSign(Point destination, double distance, long lifeTime) {
		this.dest = destination;
		this.distance = distance;
		this.life = lifeTime;
	}

	/**
	 * Creates a roadSign.RoadSign object. The roadSign.RoadSign.DEFAULT_LIFETIME_MS is taken as the lifeTime
	 * @param destination the destination point of the roadSign.RoadSign
	 * @param distance the distance between the start- and endpoints
	 */
	public RoadSign(Point destination, double distance) {
		this(destination, distance, DEFAULT_LIFETIME_MS);
	}

	private final Point dest;
	private final double distance;
	private double life;

	/**
	 * Get the endpoint of the Roadsign
	 * @return the endpoint of the Roadsign
	 */
	public Point getDestination() {
		return dest;
	}

	/**
	 * Get the distance between the start and endpoint of the roadSign.RoadSign
	 * @return the distance between the start and endpoint of the roadSign.RoadSign
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Get the remaining life time of the roadSign.RoadSign
	 * @return the remaining life time of the roadSign.RoadSign
	 */
	public double getRemainingLifeTime() {
		return life;
	}

	/**
	 * Ages the roadSign.RoadSign by the given amount of milliseconds and returns whether the roadSign.RoadSign has life time left
	 * @param ms how much milliseconds the roadSign.RoadSign should be aged
	 */
	public boolean age(long ms) {
		life -= ms;
		return 0 < life;
	}

	@Override
	public String toString() {
		return "roadSign.RoadSign to " + getDestination().toString() + ", distance: " + String.valueOf(getDistance()) + ", remaining lifetime: " + String.valueOf(getRemainingLifeTime());
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
			return this.getDestination() == p.getDestination() && this.getDistance() == p.getDistance();
		}
	}
}
