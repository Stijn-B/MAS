package model.user.owner;

import com.github.rinde.rinsim.geom.Point;
import model.roadSign.RoadSignPoint;

public class Base extends AbstractRoadSignPointOwner {

    public Base(Point position) {
        super(OwnerType.BASE, position, RoadSignPoint.PointType.BASE);
    }

}
