package roadSignAnt;

import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.core.model.pdp.ParcelDTO;

import javax.annotation.Nullable;

public class RoadSignParcel extends Parcel implements RoadSignPointOwner {

	public RoadSignParcel(ParcelDTO parcelDto) {
		super(parcelDto);

		if (parcelDto.getPickupLocation() == null || parcelDto.getDeliveryLocation() == null)
			throw new NullPointerException("Pickup- and DeliveryLocation have to be specified in the ParcelDTO");

		ID = RoadSignPointOwnerID.getID();

		pickupRSPoint = new RoadSignPoint(this, RoadSignPoint.Type.PARCEL_PICKUP, parcelDto.getPickupLocation());
		deliveryRSPoint = new RoadSignPoint(this, RoadSignPoint.Type.PARCEL_DELIVERY, parcelDto.getDeliveryLocation());
		points = new RoadSignPoint[]{ pickupRSPoint, deliveryRSPoint };
	}

	/* ID */

	private final int ID;

	public int getID() {
		return ID;
	}


	/* TYPE */

	public RoadSignPointOwner.Type getRoadSignPointOwnerType() {
		return Type.PARCEL;
	}


	/* ROADSINGPOINTS */

	private final RoadSignPoint[] points;

	public RoadSignPoint[] getRoadSignPoints() {
		return points.clone();
	}

	private final RoadSignPoint pickupRSPoint;
	private final RoadSignPoint deliveryRSPoint;

	public RoadSignPoint getPickupLocationRoadSignPoint() {
		return pickupRSPoint;
	}
	public RoadSignPoint getDeliveryLocationRoadSignPoint() {
		return deliveryRSPoint;
	}

	/* DEPENDENCY INJECTION*/

	private RoadSignModel model;

	public void injectRoadSignModel(RoadSignModel model) {
		this.model = model;
	}


	/* OTHER */

	@Override
	public boolean equals(@Nullable Object other) {
		return other != null && other instanceof RoadSignParcel && this.getID() == ((RoadSignParcel) other).getID();
	}

	public int hashCode() { return getID(); }

}

// vim: noexpandtab:
