package roadSignAnt.ant;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import roadSignAnt.roadSignPoint.RoadSign;
import roadSignAnt.roadSignPoint.RoadSignPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//TODO: still needs to be refactored. Requires RoadSignAntPath to be refactored first
public class ExplorationAnt extends Ant {

	// aantal explored paden =  branch amount ** hop count(4 ** 4 = 256)
	public final static int DEFAULT_BRANCH_AMOUNT = 4; // breedte
	public final static int DEFAULT_HOP_COUNT_LIMIT = 4; // diepte


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

	public List<PlannedPath> explore(RoadSignPoint point) {
		return explore(point, new PlannedPath());
	}

	/**
	 * Returns a List of paths
	 * A path is a list of Pair<RoadSignPoint,Double distance>
	 */
	public List<PlannedPath> explore(RoadSignPoint point, PlannedPath path) {

		// if no path given, execute method withouth path argument
		if (path == null) return explore(point);


		// create an ArrayList for the resulting AntPaths
		List<PlannedPath> result = new ArrayList<>();

		// if there are still hops left, explore
		if (0 < getHopAmount()) { // otherwise, explore (make another hop)
			// send out ants until no more roadsigns are left or until the max amount of ants is sent out
			int sentAntCount = 0;
			Iterator<RoadSign> iterator = point.getRoadSignsIterator();
			while (iterator.hasNext() && sentAntCount < getBranchAmount()) {

				RoadSign roadSign = iterator.next();

				// if the RoadSign destination is suitable, send a new explorer ant to explore it
				if (path.acceptableNextHop(roadSign.getDestination())) {

					// extend a copy of the current path with the new destination
					PlannedPath pathCopy = path.copy();
					pathCopy.append(roadSign);

					// send an ant (with hopCount - 1) to this destination
					ExplorationAnt newAnt = new ExplorationAnt(getHopAmount()-1);
					List<PlannedPath> returnedList = newAnt.explore(roadSign.getDestination(), pathCopy);
					sentAntCount++;

					// add the returned list of AntPaths to the result
					result.addAll(returnedList);
				}
			}

		} else { // else (no hops left), just add the current path to the list
			result.add(path);
			// TODO: overwegen om deze buiten de 'else' te zetten
			// zou als gevolg hebben dat ook de korte paden in de lijst staan ipv alleen de path met lengte = hopCount
		}

		return result;
	}

}

// vim: set noexpandtab:
