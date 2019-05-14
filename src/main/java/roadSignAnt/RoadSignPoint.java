package roadSignAnt;

import com.github.rinde.rinsim.geom.Point;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class RoadSignPoint extends Point {

	private static int ID_COUNTER = 0;

	public RoadSignPoint(RoadSignPointOwner holder, double pX, double pY) {
		super(pX, pY);
		this.roadSignPointHolder = holder;

		ID = ID_COUNTER;
		ID_COUNTER = RoadSignPointOwner.loopAroundIncrement(ID_COUNTER);
	}

	public RoadSignPoint(RoadSignPointOwner roadSignPointHolder, Point point) {
		this(roadSignPointHolder, point.x, point.y);
	}

	private final int ID;

	public int getID() {
		return ID;
	}

	private final RoadSignPointOwner roadSignPointHolder;

	public RoadSignPointOwner getRoadSignPointHolder() {
		return roadSignPointHolder;
	}

	/**
	 * RoadSigns are stored sorted by ascending distance in a LinkedList
	 */
	//TODO: Sander: consider some kind of hash-based sorted list for faster sorting
	// -> is een linked list die altijd gesorteerd blijft (zie addRoadSign), dus er moet nooit een 'sort' operatie uitgevoerd worden
	List<RoadSign> roadSigns = new LinkedList<>();

	/**
	 * Adds the given roadSign.RoadSign to the list while keeping the list sorted by ascending distance
	 * @param newSign the roadSign.RoadSign to add
	 */
	public void addRoadSign(RoadSign newSign) {
		ListIterator<RoadSign> iter = roadSigns.listIterator();

		while(iter.hasNext()) {
			RoadSign nextSign = iter.next();
			if (newSign.compareTo(nextSign) <= 0) { // distance to newSign is equal or smaller than distance to nextSign
				iter.previous(); // go back 1 so iter.add() adds in the right place
				break; // break loop so newSign is added in right position
			}
		}

		iter.add(newSign); // if the list is empty, the new sign is added at the start of the list
	}

	/**
	 * Ages all the RoadSigns that this roadSign.RoadSignPoint holds
	 * @param ms
	 */
	public void age(long ms) {
		ListIterator<RoadSign> iter = roadSigns.listIterator();

		while(iter.hasNext()) {
			// if the next roadSign.RoadSign doesn't survive the aging, remove it
			if (!iter.next().age(ms)) {
				iter.remove();
			}
		}
	}

	/**
	 * Get the size of the roadSign.RoadSign list
	 * @return the size of the roadSign.RoadSign list
	 */
	public int getRoadSignCount() {
		return roadSigns.size();
	}

	/**
	 * Returns the roadSign.RoadSign at the specified position in the roadSign.RoadSign list.
	 * @param index index of the roadSign.RoadSign to return
	 * @return the roadSign.RoadSign at the specified position in the roadSign.RoadSign list
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= getRoadSignCount())
	 */
	public RoadSign getRoadSign(int index) {
		return roadSigns.get(index);
	}


	@Override
	public boolean equals(@Nullable Object other) {
		return other != null && other instanceof RoadSignPoint && this.getID() == ((RoadSignPoint) other).getID();
	}

	public int hashCode() { return getID(); }

}

// vim: noexpandtab:
