package model;

import com.github.rinde.rinsim.core.model.DependencyProvider;
import com.github.rinde.rinsim.core.model.Model;
import com.github.rinde.rinsim.core.model.ModelBuilder;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import model.user.RoadSignPointUser;
import model.user.owner.RoadSignPointOwner;

import java.util.*;


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

	private HashSet<RoadSignPointOwner> ownerList = new HashSet<>();

	/**
	 * Adds the RoadSignPointOwner if it is not already present. If this Model already contains the RoadSignPointOwner,
	 * the call leaves the Model unchanged and returns false.
	 * @param owner The RoadSignPointOwner to add
	 * @return whether the RoadSignPointOwner was added
	 */
	public boolean addOwner(RoadSignPointOwner owner) {
		return ownerList.add(owner);
	}

	/**
	 * Removes the given RoadSignPointOwner from the Model
	 * Returns true if this Model contained the RoadSignPointOwner (or equivalently, if this Model changed as a result of the call).
	 * @param owner The RoadSignPointOwner to remove from the Model
	 * @returns Whether the Model was changed.
	 */
	public boolean removeOwner(RoadSignPointOwner owner) {
		return ownerList.remove(owner);
	}

	public boolean containsOwner(RoadSignPointOwner owner) {
		return ownerList.contains(owner);
	}

	public int ownerCount() {
		return ownerList.size();
	}

	/**
	 * Returns whether there is atleast 1 other RoadSignPointOwner other than the given one registered to this Model.
	 */
	public boolean containsOwnerOtherThan(RoadSignPointOwner owner) {
		// if the Model contains no owners at all OR only contains the given owner, return false
		return ! (ownerCount() == 0 || (ownerCount() == 1 && containsOwner(owner)));
	}

	/* RANDOM OWNER */

	private LinkedList<RoadSignPointOwner> randomQueue = new LinkedList<>();

	public void refreshRandomQueue() {
		randomQueue.clear();
		randomQueue.addAll(ownerList);
		Collections.shuffle(randomQueue);
	}

	/**
	 * Returns the next owner form the random owner queue. Returns null if there are no registered owners.
	 * @return the next owner form the random owner queue
	 */
	public RoadSignPointOwner getNextRandomOwner() {
		if (ownerList.isEmpty()) return null; // if no registered owners, return null
		if (randomQueue.isEmpty()) refreshRandomQueue(); // if queue empty, refresh it
		return randomQueue.poll(); // return and remove the head of the randomQueue
	}

	/**
	 * Returns a random RoadSignPointOwner that is registered to this Model and not equal to the
	 * given owner. Returns null if there is no other RoadSignPointOwner registered to this Model.
	 */
	public RoadSignPointOwner getRandomOwnerOtherThan(RoadSignPointOwner owner) {

		// while the Model contains an owner other than the given one, loop for a random one
		while (containsOwnerOtherThan(owner)) {
			RoadSignPointOwner curr = getNextRandomOwner();
			if (curr != owner) return curr;
		}

		return null;
	}

	/* DEPENDENCY INJECTION */

	/**
	 * Register element in a user.
	 * @param element the <code>! null</code> should be imposed
	 * @return true if the object was successfully registered
	 */
	@Override
	public boolean register(RoadSignPointUser element) {
		element.injectRoadSignPointModel(this);
		if (element instanceof RoadSignPointOwner) {
			addOwner((RoadSignPointOwner) element);
		}
		return true;
	}

	/**
	 * Unregister element from a user.
	 * @param element the <code>! null</code> should be imposed
	 * @return true if the unregistration changed the user (element was part of
	 *		 the user and it was successfully removed)
	 */
	@Override
	public boolean unregister(RoadSignPointUser element) {
		element.removeRoadSignPointModel();
		if (element instanceof RoadSignPointOwner) {
			removeOwner((RoadSignPointOwner) element);
		}
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
