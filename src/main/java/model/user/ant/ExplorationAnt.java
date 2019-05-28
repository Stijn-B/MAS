package model.user.ant;

import model.roadSignPoint.PlannedPath;
import model.roadSignPoint.pheromones.RoadSign;
import model.roadSignPoint.RoadSignPoint;
import model.user.owner.AGV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	public ExplorationAnt(AGV agv, int hopCountLimit, int branchLimit) {
		this.agv = agv;
		hopAmount = hopCountLimit;
		branchAmount = branchLimit;
	}

	public ExplorationAnt(AGV agv, int hopCountLimit) {
		this(agv, hopCountLimit, DEFAULT_BRANCH_AMOUNT);
	}

	public ExplorationAnt(AGV agv) {
		this(agv, DEFAULT_HOP_COUNT_LIMIT);
	}

	/* AGV */

	private final AGV agv;

	public AGV getAgv() {
		return this.agv;
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


	/* RECURSIVE EXPLORATION */

	/**
	 * Returns a list of explored PlannedPaths starting from the given RoadSignPoint.
	 * @param point The starting RoadSignPoint
	 * @return a list of explored PlannedPaths starting from the given RoadSignPoint
	 */
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

		//System.out.println("[ExplorationAnt.explore()] " + whiteSpace(getHopAmount())
		//		+ "hopCount: " + getHopAmount() + " - branch: " + getBranchAmount()
		//		+ " - given path: " + path);


		// create an ArrayList for the resulting AntPaths
		List<PlannedPath> result = new ArrayList<>();



		// if there are still hops left, explore
		if (0 < getHopAmount()) { // otherwise, explore (make another hop)
			// send out ants until no more roadsigns are left or until the max amount of ants is sent out
			int sentAntCount = 0;
			Iterator<RoadSign> iterator = point.getRoadSignsIterator();

			while (iterator.hasNext() && sentAntCount < getBranchAmount()) {

				RoadSign roadSign = iterator.next();

				//System.out.print("[ExplorationAnt.explore()] " + whiteSpace(getHopAmount())
				//		+ "("+getHopAmount()+") "+ "iter.next " + roadSign.getDestination() + " -->");


				// check whether the RoadSign destination is suitable
				if (path.acceptableRS(roadSign)) {

					// extend a copy of the current path with the new destination
					PlannedPath pathCopy = path.copyPath();
					pathCopy.append(roadSign);

					// calculate ETA to roadSign destination
					long ETA = getAgv().distanceToETA(pathCopy.getTotalPathLength());

					// if AGV would be first, send an ant (with hopCount - 1) to this destination
					if (roadSign.getDestination().wouldAgvArriveInTime(getAgv(), ETA)
							|| (roadSign.getDestination().getPointType() == RoadSignPoint.PointType.BASE && getAgv().planPassingBase())) { // not a base OR plan passing base
						//System.out.println(" YES");

						// send an ant (with hopCount - 1) to this destination
						ExplorationAnt newAnt = new ExplorationAnt(getAgv(), getHopAmount() - 1, getBranchAmount());
						List<PlannedPath> returnedList = newAnt.explore(roadSign.getDestination(), pathCopy);
						sentAntCount++;

						// add the returned list of AntPaths to the result
						result.addAll(returnedList);
					} else {
						//System.out.println(" wouldn't be first");
					}
				} else {
					//System.out.println(" RS not accepted");
				}
			}
		}

		// if nothing was added, just return the given path
		if (result.size() == 0) {
			result.add(path);
		}

		/*System.out.println("[ExplorationAnt.explore()] " + whiteSpace(getHopAmount())
				+ "("+getHopAmount()+") "+ "results:");
		for (PlannedPath p : result) {
			System.out.println("[ExplorationAnt.explore()] " + whiteSpace(getHopAmount())
					+ "("+getHopAmount()+") " +p);
		}
		*/

		return result;
	}

	public static String whiteSpace(int n) {
		StringBuffer outputBuffer = new StringBuffer(n);
		for (int i = 0; i < n; i++){
			outputBuffer.append("  ");
		}
		return outputBuffer.toString();
	}


}
