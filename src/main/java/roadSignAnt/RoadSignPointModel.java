package roadSignAnt;

import com.github.rinde.rinsim.core.model.DependencyProvider;
import com.github.rinde.rinsim.core.model.Model;
import com.github.rinde.rinsim.core.model.ModelBuilder;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import roadSignAnt.roadSignPoint.RoadSignPointOwner;
import roadSignAnt.roadSignPoint.RoadSignPointUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//TODO: rename to RoadSignAntModel
public class RoadSignPointModel extends Model.AbstractModel<RoadSignPointUser> {

	RoadSignPointModel(RoadModel rm)
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
	public boolean register(RoadSignPointUser element) {
		element.injectRoadSignModel(this);
		if (element instanceof RoadSignPointOwner) {
			register((RoadSignPointOwner) element); // [spaghetti] voor als de overloading niet werkt
		}
		return true;
	}

	public boolean register(RoadSignPointOwner element){
		element.injectRoadSignModel(this);
		addOwner(element);
		return true;
	}

	/**
	 * Unregister element from a model.
	 * @param element the <code>! null</code> should be imposed
	 * @return true if the unregistration changed the model (element was part of
	 *		 the model and it was successfully removed)
	 */
	@Override
	public boolean unregister(RoadSignPointUser element) {
		if (element instanceof RoadSignPointOwner) {
			unregister((RoadSignPointOwner) element); // [spaghetti] voor als de overloading niet werkt
		}
		return true;
	}

	public boolean unregister(RoadSignPointOwner owner) {
		removeOwner(owner);
		return true;
	}


	/* MODELBUILDER */

	public static RoadSignModelBuilder builder() {
		return new RoadSignModelBuilder();
	}

	public static class RoadSignModelBuilder extends ModelBuilder.AbstractModelBuilder<RoadSignPointModel, RoadSignPointUser> {

		RoadSignModelBuilder() {
			setDependencies(RoadModel.class);
		}

		@Override
		public RoadSignPointModel build(DependencyProvider dependencyProvider) {
			final RoadModel rm = dependencyProvider.get(RoadModel.class);
			return new RoadSignPointModel(rm);
		}
	}

}

// vim: set noexpandtab:
