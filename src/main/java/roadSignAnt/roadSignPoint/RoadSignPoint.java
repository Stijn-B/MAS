package roadSignAnt.roadSignPoint;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;

import javax.annotation.Nullable;
import java.util.*;

public class RoadSignPoint extends Point implements TickListener {

	/* CONSTRUCTOR */

	public RoadSignPoint(RoadSignPointOwner owner, Type type, double pX, double pY) {
		super(pX, pY);
		this.roadSignPointOwner = owner;
		this.type = type;
	}

	public RoadSignPoint(RoadSignPointOwner owner, Type type, Point point) {
		this(owner, type, point.x, point.y);
	}


	/* ROADSIGNPOINT OWNER */

	private final RoadSignPointOwner roadSignPointOwner;

	public RoadSignPointOwner getRoadSignPointOwner() {
		return roadSignPointOwner;
	}


	/* TYPES */

	public enum Type { PARCEL_PICKUP, PARCEL_DELIVERY, AGV, BASE }
	private final Type type;
	public Type getType() {
		return type;
	}


	/* ROADSIGNS */

	SortedSet<RoadSign> roadSigns = new TreeSet<>();

	/**
	 * Adds the given RoadSign
	 * @param newSign the RoadSign to add
	 */
	public void addRoadSign(RoadSign newSign) {
		roadSigns.add(newSign);
	}

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
	 * Ages all the RoadSigns that this roadSign.RoadSignPoint holds
	 * @param ms
	 */
	public void age(long ms) {
		Iterator<RoadSign> iter = roadSigns.iterator();

		while(iter.hasNext()) {
			// if the next roadSign.RoadSign doesn't survive the aging, remove it
			if (!iter.next().age(ms)) {
				iter.remove();
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

	}




	/* OTHER */

	@Override
	public boolean equals(@Nullable Object other) {
		return other != null && other instanceof RoadSignPoint
				&& this.getRoadSignPointOwner() == ((RoadSignPoint) other).getRoadSignPointOwner()
				&& this.getType() == ((RoadSignPoint) other).getType()
				&& this.x == ((RoadSignPoint) other).x
				&& this.y == ((RoadSignPoint) other).y;
	}

}

// vim: noexpandtab:
