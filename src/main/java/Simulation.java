import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.pdp.*;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadModelBuilders;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Graph;
import com.github.rinde.rinsim.geom.MultiAttributeData;
import com.github.rinde.rinsim.geom.io.DotGraphIO;
import com.github.rinde.rinsim.geom.io.Filters;
import com.github.rinde.rinsim.ui.View;
import com.github.rinde.rinsim.ui.renderers.GraphRoadModelRenderer;
import com.github.rinde.rinsim.ui.renderers.RoadUserRenderer;
import org.apache.commons.math3.random.RandomGenerator;

import roadSignAnt.RoadSignModel;
import roadSignAnt.RoadSignParcel;
import roadSignAnt.ant.IntentionAnt;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Simulation {

	private static final double PARCEL_SPAWN_CHANCE = 0.006;

	public static void main(String[] args) {
		run();
	}

	// in intelliJ doe: rechtermuisknop > Copy relative path (Crtl + Alt + Shift + c)
	private static final String MAP_FILE = "leuven.dot";


	public static Simulator run() {

		// build view
		View.Builder view = createGui();

		// create simulator and add models
		final Simulator simulator = Simulator.builder()
			.addModel(RoadModelBuilders.staticGraph(loadGraph(MAP_FILE))) // add map of Leuven
			.addModel(DefaultPDPModel.builder())
			.addModel(RoadSignModel.builder())
			.addModel(view)
			.build();


		// get rng object
		final RandomGenerator rng = simulator.getRandomGenerator();

		// get RoadModel
		final RoadModel roadModel = simulator.getModelProvider().getModel(RoadModel.class);

		// register depot (uitvalsbasis vd AGVs)
		simulator.register(new Depot(roadModel.getRandomPosition(rng)));

		//TODO: register AGV's
		/*
		for (int i = 0; i < 10; i++) {
			simulator.register(new IntentionAnt(roadModel.getRandomPosition(rng)));
		}
		*/

		// random package generation
		simulator.addTickListener(new TickListener() {
			@Override
			public void tick(TimeLapse time) {
				if (rng.nextDouble() < PARCEL_SPAWN_CHANCE) {
					simulator.register(new RoadSignParcel(
						Parcel.builder(roadModel.getRandomPosition(rng),roadModel.getRandomPosition(rng))
							.buildDTO()));
				}
			}

			@Override
			public void afterTick(TimeLapse timeLapse) {}
		});


		simulator.start();

		roadModel.getObjects();

		return simulator;
	}



	static View.Builder createGui() {

		View.Builder view = View.builder()
			.with(GraphRoadModelRenderer.builder())
			.with(RoadUserRenderer.builder()
				.withImageAssociation(
					Depot.class, "/images/saturnus.png")
				.withImageAssociation(
					IntentionAnt.class, "/images/ufo.png")
				.withImageAssociation(
					RoadSignParcel.class, "/images/parcel.png"));

		return view;
	}


	static Graph<MultiAttributeData> loadGraph(String name) {
		try {
			final Graph<MultiAttributeData> g = DotGraphIO
				.getMultiAttributeGraphIO(
					Filters.selfCycleFilter())
				.read(
					Simulation.class.getResourceAsStream(name));
			return g;
		} catch (final FileNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}



	// TODO: time windows
	static class Package extends Parcel {
		Package(ParcelDTO dto) {
			super(dto);
		}

		@Override
		public void initRoadPDP(RoadModel pRoadModel, PDPModel pPdpModel) {}
	}

}

// vim: noexpandtab:
