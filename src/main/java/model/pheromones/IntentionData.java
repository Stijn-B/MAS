package model.pheromones;

import model.roadSignPoint.AGV;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class IntentionData extends AgingPheromone implements Comparable<IntentionData> {

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
    private long ETA;

    public AGV getAgv() {
        return agv;
    }

    public long getETA() {
        return ETA;
    }

    public void updateETA(long ETA) {
        this.ETA = ETA;
        revitalize();
    }


    @Override
    public int compareTo(@NotNull IntentionData o) throws NullPointerException {
        if (o == null) throw new NullPointerException("compareTo argument can't be null");

        if (this.equals(o) || this.getETA() == o.getETA()) {
            return 0;
        } else if (this.getETA() > o.getETA()) {
            return 1;
        } else {
            return -1;
        }
    }


    public boolean equals(@Nullable IntentionData other) {
        if (other == null) {
            return false;
        } else {
            return this.getAgv() == other.getAgv();
        }
    }

    /**
     * 2 RoadSigns are equal if they indicate the same distance to the same destination (lifetime is not relevant
     * @param other the other object
     * @return whether the given Object is equal to this RoadSign
     */
    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null || !(other instanceof RoadSign)) {
            return false;
        } else {
            return this.equals((RoadSign) other);
        }
    }
}
