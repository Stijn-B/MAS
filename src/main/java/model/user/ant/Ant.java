package model.user.ant;

import model.RoadSignPointModel;
import model.user.RoadSignPointUser;

public abstract class Ant implements RoadSignPointUser {

    private RoadSignPointModel model;

    public RoadSignPointModel getRoadSignPointModel() {
        return this.model;
    }

    // dependency injection
    public void injectRoadSignPointModel(RoadSignPointModel model) {
        this.model = model;
    }

    // unregister this Owner
    public void removeRoadSignPointModel() {
        this.model = null;
    }

    public boolean hasRoadSignPointModel() {
        return model != null;
    }

}
