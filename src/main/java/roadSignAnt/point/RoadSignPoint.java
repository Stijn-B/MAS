package roadSignAnt.point;

import com.github.rinde.rinsim.core.model.time.TimeLapse;
import roadSignAnt.RoadSign;

import java.util.*;

import com.github.rinde.rinsim.geom.Point;
import com.github.rinde.rinsim.core.model.time.TickListener;
import roadSignAnt.RoadSignAntObject;
import sun.security.util.Length;

import javax.measure.Measure;

//TODO: write equals method
public abstract class RoadSignPoint extends Point implements RoadSignAntObject, TickListener {

	//TODO: pick a suitable number
	public static final int NB_FEASABILITY_ANTS = 20;

	private Map<RoadSignPoint, RoadSign> signs = new HashMap<>();

	/**
	 * Returns the distance to the given RoadSignPoint (using the RoadSigns)
	 * Throws an Exception if no RoadSign contains the target
	 * @param target
	 * @return the distance to the given RoadSignPoint
	 * @throws Exception if no RoadSign contains the target
	 */
	public double getDistance(RoadSignPoint target) throws Exception {
		RoadSign sign = this.signs.get(target);
		if (sign == null) {
			//TODO: throw suitable exception
			throw new Exception("No sign for specified target");
		}
		return sign.getDistance();
	}

	public void addSign(RoadSignPoint target) {
		List<Point> path = getModel().getShortestPathTo(this, target);
		Measure<Double, Length> distance = getModel().getDistanceOfPath(path);
		this.signs.put(target, new RoadSign(this, target, distance.getValue()));
		try {
			target.getDistance(this);
			//TODO: catch specific exception
		} catch (Exception e) {
			target.addSign(this);
		}
	}

	public void destroySign(RoadSignPoint target) {
		this.signs.remove(target);
		try {
			target.getDistance(this);
			target.destroySign(this);
			//TODO: catch specific exception
		} catch (Exception e) {
			return;
		}
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


	public Map<Integer, Long> etaMap; // <Identifier , ETA (in ms)>

	/**
	 * Returns true if this point will be available at some future time.
	 * @param ETA The estimated time of arrival, the time to check availability for.
	 */
	public boolean isAvailable(int ID, long ETA) {
		Set<Integer> IDset = etaMap.keySet();
		Iterator<Integer> iter = IDset.iterator();

		while (iter.hasNext()) {
			int currID = iter.next();
			if (ID != currID && ETA > etaMap.get(currID)) return false; // if another ID has a lower ETA, return false
		}

		return true;
	}



	/**
	 * Declare an intention of visiting this point at some future time.
	 * @param ETA The estimated time of arrival.
	 */
	public boolean declareIntention(int ID, long ETA) { // TODO; iets van ID toevoegen zodat we weten WIE de ETA plaatste (anders conflict AGV mss met zichzelf)
		//TODO: implement: use declared intentions to decide availibility. Possible just return true and let subclasses implement this.
	}

	@Override
	public void tick(TimeLapse timeLapse) {
		//TODO: implement: send out NB_FEASABILITY_ANTS (call crawl() on each)
	}

	@Override
	public void afterTick(TimeLapse timeLapse) {
	}

}

// vim: set noexpandtab:
