package model.roadSignPoint.pheromones;

import model.user.owner.AGV;
import org.jetbrains.annotations.NotNull;

public class IntentionData extends AgingPheromone {

    /* CONSTRUCTOR */

    public IntentionData(AGV agv, long ETA, long lifetime) {
        super(lifetime);
        this.agv = agv;
        this.ETA = ETA;
    }

    public IntentionData(AGV agv, long ETA) {
        this(agv, ETA, 5000);
    }

    private final AGV agv;
    private final long ETA;

    public AGV getAgv() {
        return agv;
    }

    public long getETA() {
        return ETA;
    }

}
