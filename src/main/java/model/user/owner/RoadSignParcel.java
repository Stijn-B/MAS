package model.user.owner;

import com.github.rinde.rinsim.core.model.pdp.Parcel;
import com.github.rinde.rinsim.core.model.pdp.ParcelDTO;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import model.RoadSignPointModel;
import model.roadSignPoint.RoadSignPoint;

import javax.annotation.Nullable;

public class RoadSignParcel extends Parcel implements RoadSignPointOwner, RoadUser {

	public RoadSignParcel(ParcelDTO parcelDto) {
		super(parcelDto);

		if (parcelDto.getPickupLocation() == null || parcelDto.getDeliveryLocation() == null)
			throw new NullPointerException("Pickup- and DeliveryLocation have to be specified in the ParcelDTO");

		ID = AbstractRoadSignPointOwner.getNewID();

		pickupRSPoint = new RoadSignPoint(this, RoadSignPoint.PointType.PARCEL_PICKUP, parcelDto.getPickupLocation());
		deliveryRSPoint = new RoadSignPoint(this, RoadSignPoint.PointType.PARCEL_DELIVERY, parcelDto.getDeliveryLocation());
	}

	/* PICKED UP */

	private boolean isPickedUp = false;
	private boolean isDelivered = false;

	private AGV carrier;

	// pickup

	public boolean isPickedUp() {
		return isPickedUp;
	}

	/**
	 * Checks whether this Parcel can be picked up by the given AGV and does so if possible.
	 */
	public boolean pickUp(AGV agv) {
		if (!isPickedUp() && agv.isAtPosition(getPickupLocation())) {
			isPickedUp = true;
			carrier = agv;
			agv.addParcel(this);
			return true;
		} else {
			return false;
		}
	}

	// carry

	public boolean isCarriedBy(AGV agv) {
		return isPickedUp() && carrier == agv;
	}

	// deliver

	public boolean isDelivered() {
		return isDelivered;
	}

	/**
	 * Checks whether this Parcel can be delivered up by the given AGV and does so if possible.
	 */
	public boolean deliver(AGV agv) {
		if (isCarriedBy(agv) && agv.isAtPosition(getDeliveryLocation())) {
			isDelivered = true;
			agv.deliverParcel(this);
			return true;
		} else {
			return false;
		}
	}

	// act

	/**
	 * Responds to the given AGV acting on the given RoadSignPoint owned by this Parcel
	 */
	@Override
	public boolean act(AGV agv, RoadSignPoint rsPoint) {

		// pickup attempt
		if (rsPoint == getPickupLocationRoadSignPoint()) return pickUp(agv);

		// delivery attempt
		if (rsPoint == getDeliveryLocationRoadSignPoint()) return deliver(agv);

		return false;
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

	/**
	 * Depending on whether this parcel is picked up and or delivered, returns the pickup and delivery points.
	 * (see implementation)
	 */
	@Override
	public RoadSignPoint[] getRoadSignPoints() {
		if (!isDelivered()) {
			if (!isPickedUp()) {
				return new RoadSignPoint[]{getPickupLocationRoadSignPoint(), getDeliveryLocationRoadSignPoint()};
			} else {
				return new RoadSignPoint[]{getDeliveryLocationRoadSignPoint()};
			}
		} else {
			return new RoadSignPoint[]{};
		}
	}

	@Override
	public int hashCode() { return getID(); }

	@Override
	public boolean equals(@Nullable Object other) {
		return other instanceof RoadSignPointOwner
				&& equals((RoadSignPointOwner) other);
	}

	public boolean equals(@Nullable RoadSignPointOwner other) {
		return other.getID() == this.getID();
	}

}

// vim: noexpandtab:
