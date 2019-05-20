package roadSignAnt.point;

import roadSignAnt.RoadSign;

import java.util.HashMap;

import com.github.rinde.rinsim.geom.Point;
import com.github.rinde.rinsim.core.model.time.TickListener;

public abstract class RoadSignPoint extends Point implements RoadSignAntObject, TickListener {

	//TODO: pick a suitable number
	public static final int NB_FEASABILITY_ANTS = 20;

	private Map<RoadSignPoint, RoadSign> signs = new HashMap<>();

	public double getDistance(RoadSignPoint target) {
		RoadSign sign = this.signs.get(target);
		if (sign == null) {
			//TODO: throw suitable exception
			throw new Exception("No sign for specified target");
		}
		return sign.getDistance();
	}

	//TODO: enforce two-way signs within this class
	public void addSign(RoadSignPoint target) {
		List<Point> path = getModel().getShortestPathTo(getOwner(), target);
		Measure<Double, Length> distance = getModel().getDistanceOfPath(path);
		this.signs.put(target, new RoadSign(target, distance.getValue()));
	}

	public void destroySign(RoadSignPoint target) {
		this.signs.remove(target);
	}

	/**
	 * Prepare RoadSignPoint for termination. Removes all known RoadSigns to this point.
	 */
	public void close() {
		Iterator iterator = this.signs.keySet().iterator();
		while (iterator.hasNext()) {
			iterator.next().destroySign(this);
		}
	}

	/**
	 * Returns true if this point will be available at some future time.
	 * @param ETA The estimated time of arrival, the time to check availability for.
	 */
	public boolean isAvailable(int ETA);

	/**
	 * Declare an intention of visiting this point at some future time.
	 * @param ETA The estimated time of arrival.
	 */
	public boolean declareIntention(int ETA) {
		//TODO: implement: use declared intentions to decide availibility. Possible just return true and let subclasses implement this.
		throw java.lang.UnsupportedOperationException("not implemented");
	}

	@Override
	public void tick(TimeLapse timeLapse) {
		//TODO: implement: send out NB_FEASABILITY_ANTS (call crawl() on each)
		throw java.lang.UnsupportedOperationException("not implemented");
	}

	@Override
	public void afterTick(TimeLapse timeLapse) {
	}

}

// vim: set noexpandtab:
