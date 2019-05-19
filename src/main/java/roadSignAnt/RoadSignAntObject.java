package roadSignAnt;

public interface RoadSignAntObject {

	private RoadSignAntModel model;

	public RoadSignAntModel getModel() {
		if (this.model == null) {
			//TODO: raise suitable exception (custom NotRegisteredException?);
			throw Exception("not registered");
		}
		return this.model;
	}

	public void injectRoadSignAntModel(RoadSignAntModel model) {
		this.model = model;
	}

}

// vim: set noexpandtab:
