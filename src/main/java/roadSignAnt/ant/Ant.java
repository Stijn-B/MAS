package roadSignAnt.ant;

import roadSignAnt.RoadSignModel;
import roadSignAnt.roadSignPoint.RoadSignPointUser;

public abstract class Ant implements RoadSignPointUser {

    private RoadSignModel model;

    public RoadSignModel getModel() {
        return this.model;
    }

    public void injectRoadSignModel(RoadSignModel model) {
        this.model = model;
    }

}
