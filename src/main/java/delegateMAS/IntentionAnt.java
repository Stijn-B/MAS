package delegateMAS;

import com.github.rinde.rinsim.core.model.pdp.Vehicle;
import com.github.rinde.rinsim.core.model.pdp.VehicleDTO;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import roadSign.RoadSignModel;
import roadSign.RoadSignParcel;
import roadSign.RoadSignPoint;

import javax.measure.Measure;
import javax.measure.quantity.Length;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Transporting AVG that sends out scouting AVGs and transports packages based on their intel
 */
public class IntentionAnt extends Vehicle implements TickListener {

    private static final double DEFAULT_SPEED = 1000d;
    private static final int DEFAULT_CAPACITY = 5;
    private static final int EXPLORATION_ANT_AMOUNT = 5;

    public IntentionAnt(Point startPosition) {
        this(startPosition, DEFAULT_SPEED, DEFAULT_CAPACITY);
    }

    public IntentionAnt(Point startPosition, double speed, int capacity) {
        super(VehicleDTO.builder().capacity(capacity).startPosition(startPosition).speed(speed).build());
    }

    public RoadModel getRoadModel() {
        return getRoadModel();
    }


    /* ROADSINGPOINT */

    /**
     * Get the closest RoadSignPoint
     * @param amount the amount of RoadSignPoints to return
     * @return a list of size 'amount' of the closest RoadSignPoints
     */
    public Map<Double, RoadSignPoint> getClosestParcels(int amount) {
        Map<Double, RoadSignPoint> map = new HashMap<>();

        // get position of this IntentionAnt
        Point from = getRoadModel().getPosition(this);

        // iterate over parcel list
        ArrayList<RoadSignParcel> list = roadSignModel.getParcelList();
        for (int i = 0; i < list.size(); i++) {

            // get RoadSignPoint of parcel pickuplocation and distance to it
            RoadSignPoint dest = list.get(i).getPickupLocationRoadSignPoint();
            double distance = getDistanceToPoint(dest);

            // if map didn't reach limit, add point to it, else consider replacing one
            if (map.size() < amount) {
                map.put(distance, dest);
            } else {
                double max = Collections.max(map.keySet()); // highest distance
                if (distance < max) {
                    map.remove(max);
                    map.put(distance, dest);
                }
            }
        }
        return map;
    }

    /**
     * Get the distance from this IntentionAnt to the given point
     * @param p the Point to distance measure to
     * @return the distance from this IntentionAnt to the given point
     */
    public double getDistanceToPoint(Point p) {
        Point location = getRoadModel().getPosition(this);
        return getRoadModel().getDistanceOfPath(new ArrayList<>(Arrays.asList(location, p))).getValue();
    }


    /* EXPLORATION ANT USAGE */

    /**
     * Send out ExplorationAnts from the given RoadSignPoint
     * @param startingPoint the starting point of the ExplorationAnts
     */
    public void sendOutAnts(RoadSignPoint startingPoint) {
        Map<Double, RoadSignPoint> map = getClosestParcels(EXPLORATION_ANT_AMOUNT);

        // TODO

    }


    /* IMPLEMENTED INTERFACE METHODS */

    @Override
    protected void tickImpl(TimeLapse time) {

    }

    /* DEPENDENCY INJECTION */
    // TODO: dependency injection van deze Model fixen

    void injectRoadSignModel(RoadSignModel m) {
        roadSignModel = m;
    }

    private RoadSignModel roadSignModel;

}
