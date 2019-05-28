package model.roadSignPoint.parcel;

import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.geom.Point;
import model.roadSignPoint.AGV;
import model.roadSignPoint.AbstractRoadSignPoint;
import model.pheromones.IntentionData;

import java.util.Iterator;

public abstract class AbstractParcelPoint extends AbstractRoadSignPoint {

    AbstractParcelPoint(Point position, int ID) {
        super(position);
        this.ID = ID;
    }

    public final int ID;

    public boolean belongToSameParcel(AbstractParcelPoint point) {
        return point.ID == this.ID;
    }


    @Override
    public boolean wouldAgvArriveInTime(AGV agv, long ETA) {
        // check whether agv would be first
        Iterator<IntentionData> iter = getIntentionIterator();
        while (iter.hasNext()) {
            IntentionData curr = iter.next();
            if (curr.getETA() < ETA && curr.getAgv() != agv) return false;
        }
        return true;
    }




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
            int id = getNewParcelID();
            sim.register(new ParcelPickup(getRandomPosition(sim), id));
            sim.register(new ParcelDelivery(getRandomPosition(sim), id));
        }

        public static Point getRandomPosition(Simulator sim) {
            return sim.getModelProvider().getModel(RoadModel.class).getRandomPosition(sim.getRandomGenerator());
        }
    }
}
