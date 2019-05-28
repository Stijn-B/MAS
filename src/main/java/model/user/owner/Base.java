package model.user.owner;

import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.geom.Point;
import model.roadSignPoint.RoadSignPoint;

public class Base extends AbstractRoadSignPointOwner implements RoadUser {

    public Base(Point position) {
        super(OwnerType.BASE, position, RoadSignPoint.PointType.BASE);
    }

    private RoadModel roadModel;

    public Point getPosition() {
        return getRoadSignPoints()[0].getPosition();
    }

    @Override
    public void initRoadUser(RoadModel model) {
        roadModel = model;
        if (getPosition() != null) {
            model.addObjectAt(this, getPosition());
        }
    }
}
