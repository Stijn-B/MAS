package model.roadSignPoint.parcel;

import com.github.rinde.rinsim.geom.Point;
import model.roadSignPoint.AGV;

public class ParcelPickup extends AbstractParcelPoint {

    ParcelPickup(Point position, int ID) {
        super(position, ID);
    }

    public boolean canAct(AGV agv) {
        return true;
    }

    @Override
    public boolean act(AGV agv) {
        if (super.act(agv)) {
            agv.addParcelID(getParcelID());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return "PICKUP-" + getParcelID();
    }
}
