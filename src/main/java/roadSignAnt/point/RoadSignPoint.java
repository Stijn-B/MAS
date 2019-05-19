package roadSignAnt.point;

import java.util.HashMap

import com.github.rinde.rinsim.geom.Point;
import com.github.rinde.rinsim.core.model.time.TickListener;

//TODO: mostly refactored, but hashMap should still be replaced by a collection of RoadSign instances from commit cc36f15
public abstract class RoadSignPoint extends Point implements RoadSignAntObject, TickListener {

	//TODO: pick a suitable number
	public static final int NB_FEASABILITY_ANTS = 20;

	//TODO: might be better to use RoadSign instances from commit cc36f15, think about it
	private HashMap<RoadSignPoint, Double> signs = new HashMap<>();

	public double getDistance(RoadSignPoint target) {
		Double distance = this.signs.get(target);
		if (distance == null) {
			//TODO: throw suitable exception
			throw new Exception("No sign for specified target");
		}
		return distance.doubleValue();
	}

	public void addSign(RoadSignPoint target) {
		List<Point> path = getModel().getShortestPathTo(getOwner(), target);
		Measure<Double, Length> distance = getModel().getDistanceOfPath(path);
		this.signs.put(target, distance.getValue());
	}

	public void destroySign(RoadSignPoint target) {
		this.signs.remove(target);
	}

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
