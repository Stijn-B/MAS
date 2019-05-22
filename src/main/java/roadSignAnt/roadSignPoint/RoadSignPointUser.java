package roadSignAnt.roadSignPoint;

import roadSignAnt.RoadSignPointModel;

public interface RoadSignPointUser {

    // dependency injection
    void injectRoadSignModel(RoadSignPointModel model);

    // unregister this Owner
    void unregisterFromRoadSignModel();
    boolean isRegisteredToRoadSignModel();
}
