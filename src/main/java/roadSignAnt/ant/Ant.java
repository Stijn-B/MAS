package roadSignAnt.ant;

public abstract class Ant implements RoadSignAntObject {


	//TODO: is owner a good name? Colony?
	Ant(RoadSignPoint owner) {
		this.owner = owner;
	}

	private RoadSignPoint owner;

	public RoadSignPoint getOwner() {
		return this.owner;
	}

}

// vim: set noexpandtab:
