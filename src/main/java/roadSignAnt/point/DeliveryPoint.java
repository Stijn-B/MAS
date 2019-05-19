package roadSignAnt.point;

public class DeliveryPoint extends ParcelPoint {

	@Override
	public setParcel(RoadSignAntParcel parcel) {
		if (! parcel.getDeliveryPoint().equals(this)) {
			//TODO: throw suitable exception
			throw Exception("Inconsistent state: trying to set parcel with different delivery point");
		}
		super.setParcel();
	}

}

// vim: set noexpandtab:
