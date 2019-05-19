package roadSignAnt.point;

public class PickupPoint extends ParcelPoint {

	@Override
	public setParcel(RoadSignAntParcel parcel) {
		if (! parcel.getPickupPoint().equals(this)) {
			//TODO: throw suitable exception
			throw Exception("Inconsistent state: trying to set parcel with different pickup point);
		}
		super.setParcel();
	}

}

// vim: set noexpandtab:
