import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.pdp.*;
import com.github.rinde.rinsim.core.model.road.GraphRoadModelImpl;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadModelBuilders;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.examples.taxi.TaxiRenderer;
import com.github.rinde.rinsim.geom.Graph;
import com.github.rinde.rinsim.geom.MultiAttributeData;
import com.github.rinde.rinsim.geom.io.DotGraphIO;
import com.github.rinde.rinsim.geom.io.Filters;
import com.github.rinde.rinsim.ui.View;
import com.github.rinde.rinsim.ui.renderers.GraphRoadModelRenderer;
import com.github.rinde.rinsim.ui.renderers.RoadUserRenderer;
import heuristic.DeliveredPerDistanceHeuristic;
import model.user.owner.AGV;
import model.user.owner.Base;
import org.apache.commons.math3.random.RandomGenerator;

import model.RoadSignPointModel;
import model.user.owner.RoadSignParcel;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;


public class Simulation {

	private static final int AGV_COUNT = 5;

	private static final double PARCEL_SPAWN_CHANCE = 0.006;

	public static void main(String[] args) {
		run();
	}

	// in intelliJ doe: rechtermuisknop > Copy relative path (Crtl + Alt + Shift + c)
	private static final String MAP_FILE = "leuven.dot";


	public static Simulator run() {

		/* * MODELS ETC. * */
		System.out.println("/* * MODELS ETC. * */");

		// build view
		View.Builder view = createGui();

		// create simulator and add models
		final Simulator simulator = Simulator.builder()
			.addModel(RoadModelBuilders.staticGraph(loadGraph(MAP_FILE))) // add map of Leuven
			//.addModel(DefaultPDPModel.builder()) // doet niets ?
			.addModel(RoadSignPointModel.builder())
			.addModel(view)
			.build();

		// get rng object
		final RandomGenerator rng = simulator.getRandomGenerator();

		// get Models
		final RoadModel roadModel = simulator.getModelProvider().getModel(RoadModel.class);
		// -> is of type GraphRoadModelImpl


		/* * REGISTER ENTITIES * */

		// register Base
		simulator.register(new Base(roadModel.getRandomPosition(rng)));

		// register AGVs
		for (int i = 0; i < AGV_COUNT; i++) {
			simulator.register(new AGV(roadModel.getRandomPosition(rng), new DeliveredPerDistanceHeuristic()));
		}

		// register RoadSignParcel random generation
		simulator.addTickListener(new TickListener() {
			@Override
			public void tick(TimeLapse time) {
				// if chance hit, register new RoadSignParcel
				if (rng.nextDouble() < PARCEL_SPAWN_CHANCE) {
					ParcelDTO dto = Parcel.builder(roadModel.getRandomPosition(rng),roadModel.getRandomPosition(rng)).buildDTO();
					simulator.register(new RoadSignParcel(dto));
				}
			}

			@Override
			public void afterTick(TimeLapse timeLapse) {}
		});

		// register 1 parcel
        ParcelDTO dto = Parcel.builder(roadModel.getRandomPosition(rng),roadModel.getRandomPosition(rng)).buildDTO();
        simulator.register(new RoadSignParcel(dto));

		System.out.println();
		System.out.println("INFO");
        System.out.print("RoadUser Count ");
		System.out.println(roadModel.getObjectsOfType(RoadUser.class).size());
        System.out.print("  AGV Count ");
		System.out.println(roadModel.getObjectsOfType(AGV.class).size());
        System.out.print("  Parcel Count ");
        System.out.println(roadModel.getObjectsOfType(RoadSignParcel.class).size());
        System.out.print("  Base Count ");
        System.out.println(roadModel.getObjectsOfType(Base.class).size());
		System.out.println();


		/* * START * */
		System.out.println("/* * START * */");

		simulator.start();

		return simulator;
	}



	static View.Builder createGui() {

		View.Builder view = View.builder()
			.with(GraphRoadModelRenderer.builder())
			.with(RoadUserRenderer.builder()
					.withImageAssociation(
							Depot.class, "/graphics/perspective/tall-building-64.png")
					.withImageAssociation(
							Base.class, "/images/saturnus.png")
					.withImageAssociation(
							AGV.class, "/images/ufo.png")
					.withImageAssociation(
							RoadSignParcel.class, "/images/parcel.png"))
			.withTitleAppendix("MAS");

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
