package roadSignAnt.ant;

import roadSignAnt.RoadSignPointModel;
import roadSignAnt.roadSignPoint.RoadSignPointUser;

public abstract class Ant implements RoadSignPointUser {

    private RoadSignPointModel model;

    public RoadSignPointModel getRSModel() {
        return this.model;
    }

    public void injectRoadSignPointModel(RoadSignPointModel model) {
        this.model = model;
    }

}
