package roadSignAnt;

public abstract class RoadSignAntObject {

	private RoadSignModel model;

	public RoadSignModel getModel() {
		return this.model;
	}

	public void injectRoadSignModel(RoadSignModel model) {
		this.model = model;
	}

}
