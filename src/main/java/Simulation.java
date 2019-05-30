import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.pdp.*;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadModelBuilders;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Graph;
import com.github.rinde.rinsim.geom.MultiAttributeData;
import com.github.rinde.rinsim.geom.Point;
import com.github.rinde.rinsim.geom.io.DotGraphIO;
import com.github.rinde.rinsim.geom.io.Filters;
import com.github.rinde.rinsim.ui.View;
import com.github.rinde.rinsim.ui.renderers.GraphRoadModelRenderer;
import com.github.rinde.rinsim.ui.renderers.PlaneRoadModelRenderer;
import com.github.rinde.rinsim.ui.renderers.RoadUserRenderer;
import heuristic.*;
import model.roadSignPoint.Base;
import model.roadSignPoint.AGV;
import model.roadSignPoint.parcel.ParcelDelivery;
import model.roadSignPoint.parcel.ParcelPickup;
import model.roadSignPoint.parcel.AbstractParcelPoint;
import org.apache.commons.math3.random.RandomGenerator;

import model.RoadSignPointModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Function;
import java.util.Arrays;

public class Simulation {

	public enum Map {
		VIERKANT, LEUVEN
	}

	////   SETTINGS   ////  ////  ////  ////  ////

	private static Map MAP = Map.VIERKANT;

	private static final int AGV_COUNT = 3;
	private static final int INIT_PARCEL_COUNT = 5;
	private static final double PARCEL_SPAWN_CHANCE = 0.04;
	private static final double EXPERIMENT_TIME_SPAN = 30 * 60 * 1000d;

	private static final String MAP_FILE = "leuven.dot"; // Vector map of Leuven

	////  ////  ////  ////  ////  ////  ////  ////


	public static void main(String[] args) {
		run();
	}

	/* RUN */

	public static Simulator run() {

		/* * MODELS ETC. * */

		// build view
		View.Builder view = createGui();

		// create simulator and add models
		final Simulator simulator;
		switch (MAP) {
			case LEUVEN:
				simulator = Simulator.builder()
						.addModel(RoadModelBuilders.staticGraph(loadGraph(MAP_FILE))) // MAP of Leuven
						.addModel(RoadSignPointModel.builder())
						.addModel(view)
						.build();
				break;
			case VIERKANT:
				simulator = Simulator.builder()
						.addModel(RoadModelBuilders.plane().withMinPoint(new Point(0, 0)).withMaxPoint(new Point(1000, 1000)).withMaxSpeed(50000d)) // vierkant
						.addModel(RoadSignPointModel.builder())
						.addModel(view)
						.build();
				break;
			default:
				throw new IllegalArgumentException("have to choose one of the maps");
		}


		// get rng object
		final RandomGenerator rng = simulator.getRandomGenerator();

		// get Models
		final RoadModel roadModel = simulator.getModelProvider().getModel(RoadModel.class);
		// -> is of type GraphRoadModelImpl


		final RoadSignPointModel rspModel = simulator.getModelProvider().getModel(RoadSignPointModel.class);
		rspModel.setSimulator(simulator);

		/* * REGISTER ENTITIES * */

		// register Base
		simulator.register(new Base(roadModel.getRandomPosition(rng), simulator));

		// register AGVs
		for (int i = 0; i < AGV_COUNT; i++) {
			simulator.register(new AGV(roadModel.getRandomPosition(rng), new DeliveredPerDistanceHeuristic()));
		}

		// register parcels
		for (int i = 0; i < INIT_PARCEL_COUNT; i++) {
			AbstractParcelPoint.ParcelCreator.registerNewParcel(simulator);
		}

		// register RoadSignParcel random generation
		simulator.addTickListener(new TickListener() {
			@Override
			public void tick(TimeLapse time) {
				// if chance hit, register new RoadSignParcel
				if (rng.nextDouble() < PARCEL_SPAWN_CHANCE) {
					AbstractParcelPoint.ParcelCreator.registerNewParcel(simulator);
				}
			}

			@Override
			public void afterTick(TimeLapse timeLapse) {}
		});

		// register experiment tick listener
		simulator.addTickListener(new TickListener() {
			private boolean triggered = false;

			@Override
			public void tick(TimeLapse time) {
				if (! triggered && time.getTime() > EXPERIMENT_TIME_SPAN) {
					String deliveredString =
						"Delivered " + rspModel.parcelsDelivered() + " parcels in " +
						((int) Math.round(EXPERIMENT_TIME_SPAN / 1000)) + " seconds.";
					int txtWidth = deliveredString.length();
					char frameChar = '=';
					Function<String,String> frame = (msg) -> String.format(frameChar + " %-" + txtWidth + "s " + frameChar, msg);
					char[] frameLineChars = new char[txtWidth + 4];
					Arrays.fill(frameLineChars, frameChar);
					String frameLine = new String(frameLineChars);

					System.out.println(frameLine);
					System.out.println(frame.apply("EXPERIMENT RESULTS:"));
					System.out.println(frame.apply(deliveredString));
					System.out.println(frame.apply("Delivery rate: " + ((double) rspModel.parcelsDelivered() / time.getTime() * 1000 * 60) + " parcels/min"));
					System.out.println(frameLine);

					triggered = true;
				}
			}

			@Override
			public void afterTick(TimeLapse timeLapse) {}
		});



		/* * START * */

		simulator.start();
		System.out.println("Simulation started.");

		return simulator;
	}


	/* GUI */

	static View.Builder createGui() {
		View.Builder view;

		switch(MAP) {
			case LEUVEN:
				view = View.builder()
						.with(GraphRoadModelRenderer.builder()) // MAP of leuven
						.with(RoadUserRenderer.builder()
								.withImageAssociation(
										Base.class, "/images/saturnus.png")
								.withImageAssociation(
										AGV.class, "/images/ufo.png")
								.withImageAssociation(
										ParcelPickup.class, "/images/mannetje.png")
								.withImageAssociation(
										ParcelDelivery.class, "/images/paarse_vlag.png"))
						.withTitleAppendix("MAS");
				break;
			case VIERKANT:
				view = View.builder()
						.with(PlaneRoadModelRenderer.builder()) // vierkant
						.with(RoadUserRenderer.builder()
								.withImageAssociation(
										Base.class, "/images/saturnus.png")
								.withImageAssociation(
										AGV.class, "/images/ufo.png")
								.withImageAssociation(
										ParcelPickup.class, "/images/mannetje.png")
								.withImageAssociation(
										ParcelDelivery.class, "/images/paarse_vlag.png"))
						.withTitleAppendix("MAS");
				break;
			default:
				throw new IllegalArgumentException("have to choose one of the maps");
		}

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
}

// vim: noexpandtab:
