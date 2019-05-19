package roadSignAnt;

import com.github.rinde.rinsim.core.model.DependencyProvider;
import com.github.rinde.rinsim.core.model.Model;
import com.github.rinde.rinsim.core.model.ModelBuilder;
import com.github.rinde.rinsim.core.model.road.RoadModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//TODO: header with dependencies etcetera
public class RoadSignAntModel extends Model.AbstractModel<RoadSignAntObject> {

	RoadSignAntModel(RoadModel rm)
	{
		roadModel = rm;
		randomGenerator = new Random();
	}

	protected final RoadModel roadModel;


	/* RANDOM GENERATOR */

	private Random randomGenerator;

	public double getRandomDouble() {
		return randomGenerator.nextDouble();
	}


	/* ROADSIGNPOINT OWNER LIST */

	private List<RoadSignPointOwner> ownerList = new ArrayList<>();

	public void addOwner(RoadSignPointOwner owner) {
		ownerList.add(owner);
	}

	public void removeOwner(RoadSignPointOwner owner) {
		ownerList.remove(owner);
	}

	public RoadSignPointOwner getRandomOwner() {
		if (ownerList.isEmpty()) return null;
		else return ownerList.get(randomGenerator.nextInt(ownerList.size()));
	}


	/* DEPENDENCY INJECTION */

	/**
	 * Register element in a model.
	 * @param element the <code>! null</code> should be imposed
	 * @return true if the object was successfully registered
	 */
	@Override
	public boolean register(RoadSignAntObject element) {
		element.injectRoadSignAntModel(this);
		return true;
	}

	public boolean register(RoadSignAntParcel parcel){
		parcel.injectRoadSignAntModel(this);
		register(parcel.getPickupPoint());
		register(parcel.getDeliveryPoint());
		//TODO: store parcel
		return true;
	}

	public boolean register(RoadSignPoint point){
		point.injectRoadSignAntModel(this);
		//TODO: store point
		return true;
	}

	/**
	 * Unregister element from a model.
	 * @param element the <code>! null</code> should be imposed
	 * @return true if the unregistration changed the model (element was part of
	 *		 the model and it was successfully removed)
	 */
	@Override
	public boolean unregister(RoadSignAntObject element) {
		return true;
	}


	/* MODELBUILDER */

	public static RoadSignAntModelBuilder builder() {
		return new RoadSignAntModelBuilder();
	}

	public static class RoadSignAntModelBuilder extends ModelBuilder.AbstractModelBuilder<RoadSignAntModel, RoadSignPointObject> {

		RoadSignAntModelBuilder() {
			setDependencies(RoadModel.class);
		}

		@Override
		public RoadSignAntModel build(DependencyProvider dependencyProvider) {
			final RoadModel rm = dependencyProvider.get(RoadModel.class);
			return new RoadSignAntModel(rm);
		}
	}

}

// vim: set noexpandtab:
