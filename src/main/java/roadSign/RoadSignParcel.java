package roadSign;

import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.core.model.pdp.ParcelDTO;

import javax.annotation.Nullable;

public class RoadSignParcel extends Parcel {

    private static int ID_COUNTER = 0;

    public RoadSignParcel(ParcelDTO parcelDto) {
        super(parcelDto);

        if (parcelDto.getPickupLocation() == null || parcelDto.getDeliveryLocation() == null)
            throw new NullPointerException("Pickup- and DeliveryLocation have to be specified in the ParcelDTO");

        ID = ID_COUNTER;
        if (ID_COUNTER < Integer.MAX_VALUE)
            ID_COUNTER += 1;
        else
            ID_COUNTER = 0;

        pickupRSPoint = new RoadSignPoint(this, parcelDto.getPickupLocation());
        deliveryRSPoint = new RoadSignPoint(this, parcelDto.getDeliveryLocation());
    }

    private final int ID;

    public int getID() {
        return ID;
    }

    private final RoadSignPoint pickupRSPoint;
    private final RoadSignPoint deliveryRSPoint;

    public RoadSignPoint getPickupLocationRoadSignPoint() {
        return pickupRSPoint;
    }
    public RoadSignPoint getDeliveryLocationRoadSignPoint() {
        return deliveryRSPoint;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return other != null && other instanceof RoadSignParcel && this.getID() == ((RoadSignParcel) other).getID();
    }

    public int hashCode() { return getID(); }
}
