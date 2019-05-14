package roadSignAnt.ant;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import roadSignAnt.AntPath;
import roadSignAnt.RoadSign;
import roadSignAnt.RoadSignPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExplorationAnt extends Ant implements TickListener {

	public final static int DEFAULT_HOP_COUNT_LIMIT = 4;
	public final static int DEFAULT_BRANCH_AMOUNT = 3;


	/* CONSTRUCTOR */

	/**
	 * Creates an ExplorationAnt that explores routes
	 * @param hopCountLimit The amount of hops a planned out route should be
	 * @param branchLimit The amount of ExplorationAnt that should be created at each point
	 */
	public ExplorationAnt(int hopCountLimit, int branchLimit) {
		hopAmount = hopCountLimit;
		branchAmount = branchLimit;
	}

	public ExplorationAnt(int hopCountLimit) {
		this(hopCountLimit, DEFAULT_BRANCH_AMOUNT);
	}

	public ExplorationAnt() {
		this(DEFAULT_HOP_COUNT_LIMIT);
	}


	/* HOP AND BRANCH COUNT */

	private final int hopAmount;
	public int getHopAmount() {
		return hopAmount;
	}

	private final int branchAmount;
	public int getBranchAmount() {
		return branchAmount;
	}


	/* EXPLORATION */

	/**
	 * Returns a List of paths
	 * A path is a list of Pair<RoadSignPoint,Double distance>
	 */
	public List<AntPath> explore(RoadSignPoint point, AntPath path) {

		List<AntPath> result = new ArrayList<>();

		// if no hop amount left, return the current path
		if (getHopAmount() <= 0) {
			result.add(path);
			return result;
		}

		// send out ants until no more roadsigns are left or until the max amount of ants is sent out
		int sentAntCount = 0;
		Iterator<RoadSign> iterator = point.getRoadSignsIterator();
		while (iterator.hasNext() && sentAntCount < getBranchAmount()) {

			RoadSign roadSign = iterator.next();

			// if the RoadSign destination is suitable, send a new explorer ant to explore it
			if (path.acceptableNextHop(roadSign.getDestination())) {

				// create a copy of the AntPath with the RoadSign destination appended to it
				AntPath pathCopy = path.copy();
				pathCopy.append(roadSign);

				// send an ant (with hopCount - 1) to this destination
				ExplorationAnt newAnt = new ExplorationAnt(getHopAmount()-1);
				List<AntPath> returnedList = newAnt.explore(roadSign.getDestination(), pathCopy);

				// save the returned list of AntPaths
				result.addAll(returnedList);
			}
		}

		return result;
	}


	/* IMPLEMENTED INTERFACE METHODS */

	@Override
	public void tick(TimeLapse timeLapse) {
		// TODO
	}

	@Override
	public void afterTick(TimeLapse timeLapse) {
		// TODO
	}

}
