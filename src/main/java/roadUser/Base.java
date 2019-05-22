package roadUser;

import com.github.rinde.rinsim.geom.Point;
import roadSignAnt.RoadSignPointModel;
import roadSignAnt.roadSignPoint.RoadSignPoint;
import roadSignAnt.roadSignPoint.RoadSignPointOwner;
import roadSignAnt.roadSignPoint.RoadSignPointOwnerID;

public class Base implements RoadSignPointOwner {

    public Base(Point position) {
        roadSignPoint = new RoadSignPoint(this, RoadSignPoint.Type.BASE, position);
        ID = RoadSignPointOwnerID.getNewID();
    }


    /* INTERFACE RoadSignPointOwner */

    private RoadSignPointModel roadSignPointModel;
    private boolean isRegisteredToRoadSignModel = false;

    @Override
    public void injectRoadSignPointModel(RoadSignPointModel model) {
        roadSignPointModel = model;
        isRegisteredToRoadSignModel = true;
    }

    @Override
    public void removeRoadSignPointModel() {
        isRegisteredToRoadSignModel = false;
    }

    @Override
    public boolean hasRoadSignModel() {
        return isRegisteredToRoadSignModel;
    }

    private int ID;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public Type getRoadSignPointOwnerType() {
        return Type.AGV;
    }

    private RoadSignPoint roadSignPoint;

    @Override
    public RoadSignPoint[] getRoadSignPoints() {
        return new RoadSignPoint[]{ roadSignPoint };
    }
}
