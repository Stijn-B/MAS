package roadSignAnt.ant;

import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import roadSignAnt.RoadSignModel;

import java.util.List;

public class ExplorationAnt implements TickListener {

	public final static int DEFAULT_STOP_COUNT_LIMIT = 4;

	/**
	 * Creates an ExplorationAnt that explores routes
	 * @param intentionAnt The IntentionAnt that sent out this ExplorationAnt
	 * @param stopCountLimit The amount of stops a planned out route should be
	 */
	public ExplorationAnt(IntentionAnt intentionAnt, int stopCountLimit) {
		this.intentionAnt = intentionAnt;
	}

	private final IntentionAnt intentionAnt;

	/* EXPLORATION */




	/* IMPLEMENTED INTERFACE METHODS */

	@Override
	public void tick(TimeLapse timeLapse) {
		// TODO
	}

	@Override
	public void afterTick(TimeLapse timeLapse) {
		// TODO
	}


	/* DEPENDENCY INJECTION */
	// TODO: dependency injection van deze Model fixen

	void injectRoadSignModel(RoadSignModel m) {
		roadSignModel = m;
	}

	private RoadSignModel roadSignModel;


}
