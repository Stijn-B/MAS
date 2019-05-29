package model.roadSignPoint;

import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.geom.Point;
import model.RoadSignPointUser;
import model.pheromones.IntentionData;
import model.pheromones.RoadSign;
import model.RoadSignPointModel;

import java.util.Iterator;

public interface RoadSignPoint extends RoadSignPointUser, RoadUser {

    // Act

    boolean act(AGV agv);

    // Registration

    boolean isRegistered();

    void unregister();

    // RoadUser

    void initRoadUser(RoadModel model);

    boolean hasRoadModel();

    RoadModel getRoadModel();


    // RoadSignPointUser

    void injectRoadSignPointModel(RoadSignPointModel model);

    boolean hasRoadSignPointModel();

    RoadSignPointModel getRoadSignPointModel();

    // Pheromones

    /**
     * Ages the RoadSigns at this point.
     */
    void age(long ms);

    // RoadSigns

    /**
     * Adds a new RoadSing pointing to the given destination with given distance.
     * @param destination destination of the RoadSign
     * @param dist distance to the given destination
     */
    void addRoadSign(RoadSignPoint destination, double dist);

    Iterator<RoadSign> getRoadSignsIterator();

    // Intentions

    void addIntention(AGV agv, long ETA);

    Iterator<IntentionData> getIntentionIterator();

    boolean wouldAgvArriveInTime(AGV agv, long ETA);

    // Position

    default Point getPosition() {
        if (!hasRoadSignPointModel()) return null;
        else return getRoadModel().getPosition(this);
    }
}
