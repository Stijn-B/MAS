package model.roadSignPoint.parcel;

import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.geom.Point;
import model.roadSignPoint.AGV;
import model.roadSignPoint.AbstractRoadSignPoint;
import model.pheromones.IntentionData;

import java.util.Iterator;

public abstract class AbstractParcelPoint extends AbstractRoadSignPoint {

    AbstractParcelPoint(Point position, int parcelID) {
        super(position);
        this.parcelID = parcelID;
    }



    public final int parcelID;

    public int getParcelID() {
        return parcelID;
    }

    public boolean belongToSameParcel(AbstractParcelPoint point) {
        return point.getParcelID() == this.getParcelID();
    }


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
