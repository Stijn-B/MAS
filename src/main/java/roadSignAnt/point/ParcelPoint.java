package roadSignAnt.point;

import roadSignAnt.RoadSignAntParcel;

public abstract class ParcelPoint extends RoadSignPoint {

	private RoadSignAntParcel parcel;

	public void setParcel(RoadSignAntParcel parcel) {
		this.parcel = parcel;
	}

	public RoadSignAntParcel getParcel() {
		return this.parcel;
	}

	@Override
	public boolean isAvailable(int ETA) {
		//TODO: implement: use declared intentions to decide availibility.
		throw java.lang.UnsupportedOperationException("not implemented");
	}

}

// vim: set noexpandtab:
