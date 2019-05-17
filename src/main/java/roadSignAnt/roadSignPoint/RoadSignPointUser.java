package roadSignAnt.roadSignPoint;

import roadSignAnt.RoadSignModel;

public interface RoadSignPointUser {

    // dependency injection
    void injectRoadSignModel(RoadSignModel model);
}
