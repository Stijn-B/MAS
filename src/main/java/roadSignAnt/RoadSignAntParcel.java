package roadSignAnt;

import roadSignAnt.point.DeliveryPoint;
import roadSignAnt.point.PickupPoint;

public class RoadSignAntParcel {

	public RoadSignAntParcel(PickupPoint pickup, DeliveryPoint delivery) {
		this.pickupPoint = pickup;
		this.deliveryPoint = delivery;
		getPickupPoint().setParcel(this);
		getDeliveryPoint().setParcel(this);
	}

	private PickupPoint pickupPoint;

	public PickupPoint getPickupPoint() {
		return this.pickupPoint;
	}

	private DeliveryPoint deliveryPoint;

	public DeliveryPoint getDeliveryPoint() {
		return this.deliveryPoint;
	}

}

// vim: set noexpandtab:
