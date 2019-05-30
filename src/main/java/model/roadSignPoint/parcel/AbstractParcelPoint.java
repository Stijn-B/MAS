package model.roadSignPoint.parcel;

import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.geom.Point;
import model.ant.FeasibilityAnt;
import model.roadSignPoint.AGV;
import model.roadSignPoint.AbstractRoadSignPoint;
import model.pheromones.IntentionData;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractParcelPoint extends AbstractRoadSignPoint {

    AbstractParcelPoint(Point position, int parcelID, long creationTime) {
        super(position);
        this.parcelID = parcelID;
        this.creationTime = creationTime;
    }

    /* DATA */

    public final int parcelID;

    public int getParcelID() {
        return parcelID;
    }

    public final long creationTime;

    public long getCreationTime() {
        return creationTime;
    }

    public double distanceToPartner = 0;

    public double getDistanceToPartner() {
        return distanceToPartner;
    }

    /* PROPERTIES */

    /**
     * Returns a value between 0 and 1 indicating the urgency of this parcel given the current time.
     * Urgency is maximum 5 minutes after creation time.
     */
    public double getUrgency(long now) {
        long waitingTime = now - getCreationTime();
        int fiveMinutes = 5 * 60 * 1000; // 5 minutes * 60 seconds * 1000 ms
        return Math.min((double) waitingTime / fiveMinutes, 1);
    }

    @Override
    public boolean wouldAgvArriveInTime(AGV agv, long ETA) {
        return wouldAgvArriveFirst(agv, ETA);
    }

    public boolean wouldAgvArriveFirst(AGV agv, long ETA) {
        // if there are no intentions yet, this agv would certainly be first
        if (getIntentions().isEmpty()) return true;

        // compare to the first intention (they are sorted by ascending ETA)
        IntentionData first = getIntentions().first();
        return ETA < first.getETA() || agv == first.getAgv();
    }

    public String toString() {
        return getName() + "-" + getParcelID() + "";
    }


    /* ACTING */

    public abstract boolean canAct(AGV agv);

    @Override
    public boolean act(AGV agv) {
        if (canAct(agv)) {
            unregister();
            return true;
        } else {
            return false;
        }
    }


    /* CREATOR */

    public static class ParcelCreator {

        public static int ID_COUNTER = 0;

        public static int getNewParcelID() {
            int d = ID_COUNTER;
            ID_COUNTER = loopAroundIncrement(ID_COUNTER);
            return d;
        }

        public static int loopAroundIncrement(int id) {
            if (id < Integer.MAX_VALUE)
                return id + 1;
            else
                return 0;
        }

        public static void registerNewParcel(Simulator sim) {

            // create pickup and delivery points
            long now = sim.getCurrentTime();
            int id = getNewParcelID();
            ParcelPickup pickup = new ParcelPickup(getRandomPosition(sim), id, now);
            ParcelDelivery delivery = new ParcelDelivery(getRandomPosition(sim), id, now);

            // register points
            sim.register(pickup);
            sim.register(delivery);

            // calculate and set distance between points
            RoadModel roadModel = sim.getModelProvider().getModel(RoadModel.class);
            double p = FeasibilityAnt.getPathLength(roadModel.getShortestPathTo(pickup.getPosition(), delivery.getPosition()));
            double d = FeasibilityAnt.getPathLength(roadModel.getShortestPathTo(delivery.getPosition(), pickup.getPosition()));

            pickup.distanceToPartner = p;
            delivery.distanceToPartner = d;
        }

        public static Point getRandomPosition(Simulator sim) {
            return sim.getModelProvider().getModel(RoadModel.class).getRandomPosition(sim.getRandomGenerator());
        }
    }
}
