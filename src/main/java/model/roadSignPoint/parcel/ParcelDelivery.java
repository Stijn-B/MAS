package model.roadSignPoint.parcel;

import com.github.rinde.rinsim.geom.Point;
import model.roadSignPoint.AGV;

public class ParcelDelivery extends AbstractParcelPoint {

    ParcelDelivery(Point position, int ID, long now) {
        super(position, ID, now);
    }

    public boolean canAct(AGV agv) {
        return agv.carriesParcel(getParcelID());
    }

    @Override
    public boolean act(AGV agv) {
        if (super.act(agv)) {
            agv.removeParcelID(getParcelID());
            agv.deliveredParcel(getParcelID());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return "DELIVER";
    }

}
