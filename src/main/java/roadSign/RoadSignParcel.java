import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.core.model.pdp.ParcelDTO;

public class RoadSignParcel extends Parcel {

    public RoadSignParcel(ParcelDTO parcelDto) {
        super(parcelDto);

        if (parcelDto.getPickupLocation() == null || parcelDto.getDeliveryLocation() == null)
            throw new NullPointerException("Pickup- and DeliveryLocation have to be specified in the ParcelDTO");

        pickupRSPoint = new RoadSignPoint(parcelDto.getPickupLocation());
        deliveryRSPoint = new RoadSignPoint(parcelDto.getDeliveryLocation());
    }

    private final RoadSignPoint pickupRSPoint;
    private final RoadSignPoint deliveryRSPoint;

    public RoadSignPoint getPickupLocationRoadSignPoint() {
        return pickupRSPoint;
    }

    public RoadSignPoint getDeliveryLocationRoadSignPoint() {
        return deliveryRSPoint;
    }
}
