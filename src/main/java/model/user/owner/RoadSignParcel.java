package model.user.owner;

import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.core.model.pdp.ParcelDTO;
import model.RoadSignPointModel;
import model.pheromones.roadSign.RoadSignPoint;

import javax.annotation.Nullable;

public class RoadSignParcel extends Parcel implements RoadSignPointOwner {

	public RoadSignParcel(ParcelDTO parcelDto) {
		super(parcelDto);

		if (parcelDto.getPickupLocation() == null || parcelDto.getDeliveryLocation() == null)
			throw new NullPointerException("Pickup- and DeliveryLocation have to be specified in the ParcelDTO");

		ID = AbstractRoadSignPointOwner.getNewID();

		pickupRSPoint = new RoadSignPoint(this, RoadSignPoint.PointType.PARCEL_PICKUP, parcelDto.getPickupLocation());
		deliveryRSPoint = new RoadSignPoint(this, RoadSignPoint.PointType.PARCEL_DELIVERY, parcelDto.getDeliveryLocation());
	}


	/* ROADSINGPOINTS */


	private final RoadSignPoint pickupRSPoint;
	private final RoadSignPoint deliveryRSPoint;

	public RoadSignPoint getPickupLocationRoadSignPoint() {
		return pickupRSPoint;
	}
	public RoadSignPoint getDeliveryLocationRoadSignPoint() {
		return deliveryRSPoint;
	}


	/* INTERFACE RoadSignPointOwner (extends RoadSignPointUser) */

	// RoadSignPointUser

	private RoadSignPointModel roadSignPointModel;

	@Override
	public void injectRoadSignPointModel(RoadSignPointModel model) {
		roadSignPointModel = model;
	}

	@Override
	public void removeRoadSignPointModel() {
		roadSignPointModel = null;
	}

	@Override
	public boolean hasRoadSignPointModel() {
		return roadSignPointModel != null;
	}

	// RoadSignPointOwner

	private int ID;

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public OwnerType getRoadSignPointOwnerType() {
		return OwnerType.PARCEL;
	}

	@Override
	public RoadSignPoint[] getRoadSignPoints() {
		return new RoadSignPoint[]{getPickupLocationRoadSignPoint(), getDeliveryLocationRoadSignPoint()};
	}

	@Override
	public int hashCode() { return getID(); }

	@Override
	public boolean equals(@Nullable Object other) {
		return other != null
				&& other instanceof RoadSignPointOwner
				&& ((RoadSignPointOwner) other).getID() == getID();
	}

}

// vim: noexpandtab:
