package model.roadSign;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import model.user.owner.RoadSignPointOwner;

import javax.annotation.Nullable;
import java.util.*;

public class RoadSignPoint extends Point implements TickListener {

	/* CONSTRUCTOR */

	public RoadSignPoint(RoadSignPointOwner owner, PointType pointType, double pX, double pY) {
		super(pX, pY);
		this.roadSignPointOwner = owner;
		this.pointType = pointType;
	}

	public RoadSignPoint(RoadSignPointOwner owner, PointType pointType, Point point) {
		this(owner, pointType, point.x, point.y);
	}


	/* ROADSIGNPOINT OWNER */

	private final RoadSignPointOwner roadSignPointOwner;

	public RoadSignPointOwner getRoadSignPointOwner() {
		return roadSignPointOwner;
	}


	/* TYPES */

	public enum PointType { PARCEL_PICKUP, PARCEL_DELIVERY, AGV, BASE }
	private final PointType pointType;
	public PointType getPointType() {
		return pointType;
	}


	/* ROADSIGNS */

	/**
	 * The RoadSigns are saved in a SortedSet keeping them sorted by distance to destination (ascending order)
	 */
	SortedSet<RoadSign> roadSigns = new TreeSet<>();

	/**
	 * Adds a new RoadSing pointing to the given destination with given distance.
	 * @param destination destination of the RoadSign
	 * @param dist distance to the given destination
	 */
	public void addRoadSign(RoadSignPoint destination, double dist) {
		addRoadSign(new RoadSign(this, destination, dist));
	}

	/**
	 * Adds the given RoadSign to this RoadSignPoint
	 * @param newSign the RoadSign to add
	 */
	public void addRoadSign(RoadSign newSign) {
		roadSigns.add(newSign);
	}

	/**
	 * Get an Iterator of all the RoadSigns this RoadSignPoint contains.
	 * @return an Iterator of all the RoadSigns this RoadSignPoint contains
	 */
	public Iterator<RoadSign> getRoadSignsIterator() {
		return roadSigns.iterator();
	}

	/**
	 * Returns the first n RoadSigns (pointing the n closest destinations)
	 * If there are less than n RoadSigns, less RoadSigns will be returned.
	 * @param n the amount of RoadSigns to return
	 * @return the first n RoadSigns, if there are less than n RoadSigns, less RoadSigns will be returned
	 */
	public List<RoadSign> getNRoadSigns(int n) {
		Iterator<RoadSign> iter = roadSigns.iterator();
		List<RoadSign> result = new ArrayList<>();

		// add the first n elements to the result
		for(int i = 0; i < n; i ++) {
			try {
				result.add(iter.next());
			} catch (NoSuchElementException e) { // no elements left
				return result;
			}
		}
		return result;
	}

	/**
	 * Get the amount of RoadSign objects this RoadSignPoint has
	 * @return the amount of RoadSign objects this RoadSignPoint has
	 */
	public int getRoadSignCount() {
		return roadSigns.size();
	}


	/* AGING */

	/**
	 * Ages all the RoadSigns that this model.roadSign.RoadSignPoint holds
	 * @param ms
	 */
	public void age(long ms) {
		Iterator<RoadSign> iter = roadSigns.iterator();

		while(iter.hasNext()) {
			// if the next model.roadSign.RoadSign doesn't survive the aging, remove it
			if (!iter.next().age(ms)) {
				iter.remove(); // removes the last item given by iter.next() from the underlying collection
			}
		}
	}

	@Override
	public void tick(TimeLapse timeLapse) {
		age(timeLapse.getTime());
		timeLapse.consumeAll();
	}

	@Override
	public void afterTick(TimeLapse timeLapse) {
		// do nothing
	}


	/* OTHER */

	@Override
	public boolean equals(@Nullable Object other) {
		return other != null && other instanceof RoadSignPoint
				&& this.getRoadSignPointOwner() == ((RoadSignPoint) other).getRoadSignPointOwner()
				&& this.getPointType() == ((RoadSignPoint) other).getPointType()
				&& this.x == ((RoadSignPoint) other).x
				&& this.y == ((RoadSignPoint) other).y;
	}

}

// vim: noexpandtab:
