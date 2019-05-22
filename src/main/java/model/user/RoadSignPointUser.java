package model.user;

import model.RoadSignPointModel;

public interface RoadSignPointUser {

    // dependency injection
    void injectRoadSignPointModel(RoadSignPointModel model);

    // unregister this Owner
    void removeRoadSignPointModel();

    // whether registered with RSPModel
    boolean hasRoadSignPointModel();
}
