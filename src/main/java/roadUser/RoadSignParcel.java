package roadUser;

import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.core.model.pdp.ParcelDTO;
import roadSignAnt.RoadSignPointModel;
import roadSignAnt.roadSignPoint.RoadSignPoint;
import roadSignAnt.roadSignPoint.RoadSignPointOwner;
import roadSignAnt.roadSignPoint.RoadSignPointOwnerID;

import javax.annotation.Nullable;

public class RoadSignParcel extends Parcel implements RoadSignPointOwner {

	public RoadSignParcel(ParcelDTO parcelDto) {
		super(parcelDto);

		if (parcelDto.getPickupLocation() == null || parcelDto.getDeliveryLocation() == null)
			throw new NullPointerException("Pickup- and DeliveryLocation have to be specified in the ParcelDTO");

		ID = RoadSignPointOwnerID.getNewID();

		pickupRSPoint = new RoadSignPoint(this, RoadSignPoint.Type.PARCEL_PICKUP, parcelDto.getPickupLocation());
		deliveryRSPoint = new RoadSignPoint(this, RoadSignPoint.Type.PARCEL_DELIVERY, parcelDto.getDeliveryLocation());
		points = new RoadSignPoint[]{ pickupRSPoint, deliveryRSPoint };
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
		return Type.PARCEL;
	}


	/* OTHER */

	@Override
	public boolean equals(@Nullable Object other) {
		return other != null && other instanceof RoadSignParcel && this.getID() == ((RoadSignParcel) other).getID();
	}

	public int hashCode() { return getID(); }

}

// vim: noexpandtab:
