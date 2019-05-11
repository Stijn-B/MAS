import com.github.rinde.rinsim.geom.Point;
import org.jetbrains.annotations.NotNull;

public class RoadSign implements Comparable<RoadSign> {

    // default life time of a RoadSign
    public static long DEFAULT_LIFETIME_MS = 20000;

    /**
     * Creates a RoadSign object
     * @param destination the destination point of the RoadSign
     * @param distance the distance between the start- and endpoints
     * @param lifeTime how long the RoadSign should stay alive in milliseconds
     */
    RoadSign(Point destination, double distance, long lifeTime) {
        this.dest = destination;
        this.distance = distance;
        this.life = lifeTime;
    }

    /**
     * Creates a RoadSign object. The RoadSign.DEFAULT_LIFETIME_MS is taken as the lifeTime
     * @param destination the destination point of the RoadSign
     * @param distance the distance between the start- and endpoints
     */
    RoadSign(Point destination, double distance) {
        this(destination, distance, DEFAULT_LIFETIME_MS);
    }

    private final Point dest;
    private final double distance;
    private double life;

    /**
     * Get the endpoint of the Roadsign
     * @return the endpoint of the Roadsign
     */
    public Point getDestination() {
        return dest;
    }

    /**
     * Get the distance between the start and endpoint of the RoadSign
     * @return the distance between the start and endpoint of the RoadSign
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Get the remaining life time of the RoadSign
     * @return the remaining life time of the RoadSign
     */
    public double getRemainingLifeTime() {
        return life;
    }

    /**
     * Ages the RoadSign by the given amount of milliseconds and returns whether the RoadSign has life time left
     * @param ms how much milliseconds the RoadSign should be aged
     */
    public boolean age(long ms) {
        life -= ms;
        return 0 < life;
    }

    @Override
    public String toString() {
        return "RoadSign to " + getDestination().toString() + ", distance: " + String.valueOf(getDistance()) + ", remaining lifetime: " + String.valueOf(getRemainingLifeTime());
    }

    @Override
    public int compareTo(@NotNull RoadSign o) throws NullPointerException {
        if (o == null) throw new NullPointerException("compareTo argument can't be null");

        // misschien niet de efficientste methode
        if (this.getDistance() > o.getDistance()) {
            return 1;
        } else if (this.getDistance() == o.getDistance()) {
            return 0;
        } else {
            return -1;
        }

    }
}
