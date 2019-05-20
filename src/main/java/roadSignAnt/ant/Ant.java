package roadSignAnt.ant;

import roadSignAnt.RoadSignAntModel;
import roadSignAnt.RoadSignAntObject;
import roadSignAnt.point.RoadSignPoint;

public abstract class Ant implements RoadSignAntObject {


	//TODO: is owner a good name? Colony?
	Ant(RoadSignPoint owner) {
		this.owner = owner;
	}

	private RoadSignPoint owner;

	public RoadSignPoint getOwner() {
		return this.owner;
	}


	/* INTERFACE RoadSignAntObject */

	private RoadSignAntModel model;

	public RoadSignAntModel getRoadSignAntModel() {
		return model;
	}

	public void injectRoadSignAntModel(RoadSignAntModel model) {
		this.model = model;
	}

}

// vim: set noexpandtab:
