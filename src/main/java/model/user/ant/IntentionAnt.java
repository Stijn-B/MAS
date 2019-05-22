package model.user.ant;

import model.pheromones.roadSign.PlannedPath;
import model.pheromones.roadSign.RoadSign;
import model.user.owner.AGV;

import java.util.Iterator;

/**
 * Transporting AVG that sends out scouting AVGs and transports packages based on their intel
 */
public class IntentionAnt extends Ant {

    /**
     * Check whether the model is still valid and if it is, refresh it
     */
    public static boolean refresh(RoadSign rs) {
        return false; // TODO
    }

    /**
     * Declares the intention of the given AGV over the given path. Returns whether the path is still valid. A path is
     * invalid if it contains 1 or more RoadSigns that are invalid (see RoadSign.isValid())
     */
    public boolean declareIntention(AGV agv, PlannedPath path, long now) {

        double totalDist = 0;

        // iterate over the complete PlannedPath
        Iterator<RoadSign> iter = path.getIterator();
        while (iter.hasNext()) {
            RoadSign curr = iter.next();

            // if the current RoadSign is not valid, return false
            if (!curr.isValid()) return false;

            totalDist += curr.getDistance();

            // revitalize roadSign and register intention at its destination
            curr.revitalize(5000);
            long ETA = now + agv.distanceToDuration(totalDist);
            curr.getDestination().registerIntention(agv, ETA);
        }
        return true;
    }

	//TODO: deprecated code, though there might be something useful down here
//	private static final double DEFAULT_SPEED = 1000d;
//	private static final int DEFAULT_CAPACITY = 5;
//	private static final int EXPLORATION_ANT_AMOUNT = 5;
//
//	public IntentionAnt(Point startPosition) {
//		this(startPosition, DEFAULT_SPEED, DEFAULT_CAPACITY);
//	}
//
//	public IntentionAnt(Point startPosition, double speed, int capacity) {
//		super(VehicleDTO.builder().capacity(capacity).startPosition(startPosition).speed(speed).build());
//	}
//
//	public RoadModel getRoadModel() {
//		return getRoadModel();
//	}
//
//
//	/* ROADSINGPOINT */
//
//	/**
//	 * Get the closest RoadSignPoint
//	 * @param amount the amount of RoadSignPoints to return
//	 * @return a list of size 'amount' of the closest RoadSignPoints
//	 */
//	public Map<Double, RoadSignPoint> getClosestParcels(int amount) {
//		Map<Double, RoadSignPoint> map = new HashMap<>();
//
//		// get position of this IntentionAnt
//		Point from = getRoadModel().getPosition(this);
//
//		// iterate over parcel list
//		ArrayList<model.user.owner.RoadSignParcel> list = getRSModel().getOwnerList();
//		for (int i = 0; i < list.size(); i++) {
//
//			// get RoadSignPoint of parcel pickuplocation and distance to it
//			RoadSignPoint dest = list.get(i).getPickupLocationRoadSignPoint();
//			double distance = getDistanceToPoint(dest);
//
//			// if map didn't reach limit, add point to it, else consider replacing one
//			if (map.size() < amount) {
//				map.put(distance, dest);
//			} else {
//				double max = Collections.max(map.keySet()); // highest distance
//				if (distance < max) {
//					map.remove(max);
//					map.put(distance, dest);
//				}
//			}
//		}
//		return map;
//	}
//
//	/**
//	 * Get the distance from this IntentionAnt to the given point
//	 * @param p the Point to distance measure to
//	 * @return the distance from this IntentionAnt to the given point
//	 */
//	public double getDistanceToPoint(Point p) {
//		Point location = getRoadModel().getPosition(this);
//		return getRoadModel().getDistanceOfPath(new ArrayList<>(Arrays.asList(location, p))).getValue();
//	}
//
//
//	/* EXPLORATION ANT USAGE */
//
//	/**
//	 * Send out ExplorationAnts from the given RoadSignPoint
//	 * @param startingPoint the starting point of the ExplorationAnts
//	 */
//	public void sendOutAnts(RoadSignPoint startingPoint) {
//		Map<Double, RoadSignPoint> map = getClosestParcels(EXPLORATION_ANT_AMOUNT);
//
//		// TODO
//
//	}
//
//
//	/* IMPLEMENTED INTERFACE METHODS */
//
//	@Override
//	protected void tickImpl(TimeLapse time) {
//		// TODO
//		// getRoadModel().followPath();
//	}

}
