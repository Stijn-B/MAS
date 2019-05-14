package roadSignAnt;

import com.github.rinde.rinsim.core.model.DependencyProvider;
import com.github.rinde.rinsim.core.model.Model;
import com.github.rinde.rinsim.core.model.ModelBuilder;
import com.github.rinde.rinsim.core.model.road.RoadModel;

import java.util.ArrayList;
import java.util.Random;

//TODO: remove


//TODO: header with dependencies etcetera
//TODO: rename to RoadSignAntModel
public class RoadSignModel extends Model.AbstractModel<RoadSignAntObject> {

	RoadSignModel(RoadModel rm)
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

	private ArrayList<RoadSignPointOwner> ownerList = new ArrayList<>();

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

	/* INTERFACE IMPLEMENTATIONS */

	/**
	 * Register element in a model.
	 * @param element the <code>! null</code> should be imposed
	 * @return true if the object was successfully registered
	 */
	@Override
	public boolean register(RoadSignAntObject element) {
		element.injectRoadSignModel(this);
		return true;
	}
	//TODO: add register for RoadSignPointHolders / RoadSignParcels
	/*
	//TODO: should we add override here?
	public boolean register(RoadSignParcel element){
		element.injectRoadSignModel(this);
		//TODO: return false if element has already been registered
		addRoadSignParcel(element);
		return true;
	}
	*/

	/**
	 * Unregister element from a model.
	 * @param element the <code>! null</code> should be imposed
	 * @return true if the unregistration changed the model (element was part of
	 *		 the model and it was successfully removed)
	 */
	@Override
	public boolean unregister(RoadSignAntObject element) {
		//TODO: remove injection?
		return true;
	}
	//TODO: add unregister for RoadSignPointHolders / RoadSignParcels
	/*
	//TODO: should we add override here?
	public boolean unregister(RoadSignParcel element) {
		//TODO: remove injection?
		removeRoadSignParcel(element);
		return true;
	}
	*/

	public static RoadSignModelBuilder builder() {
		return new RoadSignModelBuilder();
	}

	public static class RoadSignModelBuilder extends ModelBuilder.AbstractModelBuilder<RoadSignModel, RoadSignAntObject> {

		RoadSignModelBuilder() {
			setDependencies(RoadModel.class);
		}

		@Override
		public RoadSignModel build(DependencyProvider dependencyProvider) {
			final RoadModel rm = dependencyProvider.get(RoadModel.class);
			return new RoadSignModel(rm);
		}
	}

}

// vim: set noexpandtab:
