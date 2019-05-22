package roadSignAnt.roadSignPoint;

import roadSignAnt.RoadSignPointModel;

public interface RoadSignPointUser {

    // dependency injection
    void injectRoadSignPointModel(RoadSignPointModel model);

    // unregister this Owner
    void removeRoadSignPointModel();
}
