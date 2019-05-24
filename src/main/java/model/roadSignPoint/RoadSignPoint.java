package model.roadSignPoint;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import model.roadSignPoint.pheromones.AgingPheromone;
import model.roadSignPoint.pheromones.IntentionData;
import model.roadSignPoint.pheromones.RoadSign;
import model.user.owner.AGV;
import model.user.owner.RoadSignPointOwner;

import javax.annotation.Nullable;
import java.util.*;

public class RoadSignPoint implements TickListener {

	/* CONSTRUCTOR */

	public RoadSignPoint(RoadSignPointOwner owner, PointType pointType, Point point) {
		this.roadSignPointOwner = owner;
		this.pointType = pointType;
		setPosition(point);
	}

	public RoadSignPoint(RoadSignPointOwner owner, PointType pointType, double pX, double pY) {
		this(owner, pointType, new Point(pX, pY));
	}


	/* ROADSIGNPOINT OWNER */

	private final RoadSignPointOwner roadSignPointOwner;

	public RoadSignPointOwner getRoadSignPointOwner() {
		return roadSignPointOwner;
	}

	public boolean act(AGV agv) {
		return getRoadSignPointOwner().act(agv, this);
	}


	/* ROADSIGNPOINT POSITION */

	private Point pos;

	public Point getPosition() {
		return pos;
	}

	public void setPosition(Point pos) {
		this.pos = pos;
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
	 * Returns whether this RoadSignPoint holds the given RoadSign.
	 */
	public boolean holds(RoadSign rs) {
		return roadSigns.contains(rs);
	}

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


	/* INTENTIONS */

	SortedSet<IntentionData> intentions = new TreeSet<>();

	public Iterator<IntentionData> getIntentionIterator() {
		return intentions.iterator();
	}

	/**
	 * Registers an intention for the given AGV who will arrive at the given time.
	 */
	public void registerIntention(IntentionData intention) {
		intentions.add(intention);
	}

	public void registerIntention(AGV agv, long ETA) {
		registerIntention(new IntentionData(agv, ETA));
	}

	/**
	 * Returns whether the given AGV would arrive first at this RoadSignPoint if it would arrive at the given ETA.
	 */
	public boolean isFirst(AGV agv, long ETA) {
		Iterator<IntentionData> iter = getIntentionIterator();
		while (iter.hasNext()) {
			IntentionData curr = iter.next();
			if (curr.getETA() < ETA && curr.getAgv() != agv) return false;
		}
		return true;
	}


	/* AGING */

	/**
	 * Ages all the RoadSigns that this RoadSignPoint holds
	 * @param ms
	 */
	public void age(long ms) {
		age(roadSigns, ms);
		age(intentions, ms);
	}

	/**
	 * Ages all AgingPheromone in the given agingSet, removes AgingPheromone from the set if they are expired.
	 */
	public void age(Set<? extends AgingPheromone> agingSet, long ms) {
		// Iterate over all AgingPheromones and age them
		Iterator<? extends AgingPheromone> iter = agingSet.iterator();
		while(iter.hasNext()) {
			// if the next RoadSign doesn't survive the aging, remove it
			if (!iter.next().age(ms)) {
				iter.remove(); // removes the last item given by iter.next() from the underlying collection
			}
		}
	}


	/* INTERFACE TickListener */

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
				&& this.getPosition() == ((RoadSignPoint) other).getPosition();
	}

}

// vim: noexpandtab:
