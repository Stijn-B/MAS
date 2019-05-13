package roadSign;

public abstract class RoadSignPointHolder {

    private static long ID_COUNTER = 0;

    public RoadSignPointHolder(RoadSignPoint... points) {
        this.points = points;

        ID = ID_COUNTER;
        increment(ID_COUNTER);
    }

    private final RoadSignPoint[] points;
    private final long ID;

    public RoadSignPoint[] getRoadSignPoints() {
        return points.clone();
    }

    public long getID() {
        return ID;
    }

    private long increment(long id) {
        if (id < Long.MAX_VALUE)
            return id + 1;
        else
            return 0;
    }
}

