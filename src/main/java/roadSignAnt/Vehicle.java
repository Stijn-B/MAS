package roadUser;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import heuristic.Heuristic;
import roadSignAnt.*;
import roadSignAnt.ant.*;
import roadSignAnt.roadSignPoint.RoadSignPoint;
import roadSignAnt.roadSignPoint.RoadSignPointOwner;
import roadSignAnt.roadSignPoint.RoadSignPointOwnerID;

import java.util.List;

//TODO: this class still needs to be refactored
public class Vehicle implements TickListener {

	public Vehicle(Point position, Heuristic heuristic) {
		roadSignPoint = new RoadSignPoint(this, RoadSignPoint.Type.Vehicle, position);
		this.heuristic = heuristic;

		ID = RoadSignPointOwnerID.getNewID();
	}


	private final Heuristic heuristic;

	public Heuristic getHeuristic() {
		return heuristic;
	}


	/* EXPLORATION */

	/**
	 * Explores viable paths using ExplorationAnts
	 * @return a List<roadSignAnt.ant.PlannedPath> containing viable paths (can be empty)
	 */
	public List<PlannedPath> explorePaths() {
		return new ExplorationAnt().explore(roadSignPoint);
	}


	/* INTENTIONS */

	private IntentionAnt currIntention;

	public void commit(PlannedPath path) {
		// TODO
	}


	/* INTERFACE TickListener */

	@Override
	public void tick(TimeLapse timeLapse) {
		// TODO
		// EXPLORE: List<roadSignAnt.ant.PlannedPath> paths = explorePaths();
		// HEURISTICS: getHeuristic().sortAntPathList(paths); // works on the given list so returns no value
	}

	@Override
	public void afterTick(TimeLapse timeLapse) {
		// TODO
	}


	/* INTERFACE RoadSignPointOwner */

	private int ID;

	private RoadSignPoint roadSignPoint;

	private RoadSignModel roadSignModel;

	@Override
	public void injectRoadSignModel(RoadSignModel model) {
		roadSignModel = model;
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public Type getRoadSignPointOwnerType() {
		return Type.Vehicle;
	}

	@Override
	public RoadSignPoint[] getRoadSignPoints() {
		return new RoadSignPoint[]{ roadSignPoint };
	}
}

// vim: set noexpandtab:
