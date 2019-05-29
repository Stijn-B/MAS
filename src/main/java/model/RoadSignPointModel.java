package model;

import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.DependencyProvider;
import com.github.rinde.rinsim.core.model.Model;
import com.github.rinde.rinsim.core.model.ModelBuilder;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import model.roadSignPoint.RoadSignPoint;

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

	private HashSet<RoadSignPoint> pointList = new HashSet<>();

	public boolean addPoint(RoadSignPoint owner) {
		return pointList.add(owner);
	}

	public boolean removePoint(RoadSignPoint owner) {
		return pointList.remove(owner);
	}

	public boolean containsPoint(RoadSignPoint owner) {
		return pointList.contains(owner);
	}

	public int pointCount() {
		return pointList.size();
	}

	public boolean containsPointOtherThan(RoadSignPoint point) {
		// if the Model contains no owners at all OR only contains the given owner, return false
		return ! (pointCount() == 0 || (pointCount() == 1 && containsPoint(point)));
	}


	/* RANDOM OWNER */

	private LinkedList<RoadSignPoint> randomQueue = new LinkedList<>();

	public void refreshRandomQueue() {
		randomQueue.clear();
		randomQueue.addAll(pointList);
		Collections.shuffle(randomQueue);
	}

	public RoadSignPoint getNextRandomPoint() {
		if (pointList.isEmpty()) return null; // if no registered owners, return null
		if (randomQueue.isEmpty()) refreshRandomQueue(); // if queue empty, refresh it
		return randomQueue.poll(); // return and remove the head of the randomQueue
	}

	public RoadSignPoint getRandomPointOtherThan(RoadSignPoint owner) {
		while (containsPointOtherThan(owner)) {
			RoadSignPoint curr = getNextRandomPoint();
			if (curr != owner) return curr;
		}
		return null;
	}

	private Simulator simulator;
	public void setSimulator(Simulator sim) {
		simulator = sim;
	}

	/* DEPENDENCY INJECTION */

	@Override
	public boolean register(RoadSignPointUser element) {
		element.injectRoadSignPointModel(this);
		if (element instanceof RoadSignPoint)
			addPoint((RoadSignPoint) element);
		return true;
	}

	@Override
	public boolean unregister(RoadSignPointUser element) {
		element.injectRoadSignPointModel(null);
		if (element instanceof RoadSignPoint)
			removePoint((RoadSignPoint) element);
		simulator.unregister(element);
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
