import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.pdp.*;
import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadModelBuilders;
import com.github.rinde.rinsim.geom.Graph;
import com.github.rinde.rinsim.geom.MultiAttributeData;
import com.github.rinde.rinsim.geom.Point;
import com.github.rinde.rinsim.geom.io.DotGraphIO;
import com.github.rinde.rinsim.geom.io.Filters;
import com.github.rinde.rinsim.ui.View;
import com.github.rinde.rinsim.ui.renderers.GraphRoadModelRenderer;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Simulation {

    public static void main(String[] args) {
        run();
    }

    private static final String MAP_FILE = "leuven.dot"; // in intelliJ doe: rechtermuisknop > Copy relative path (Crtl + Alt + Shift + c)


    public static Simulator run() {

        // build view
        View.Builder view = createGui();

        // create simulator and add models
        Simulator sim = Simulator.builder()
                .addModel(RoadModelBuilders.staticGraph(loadGraph(MAP_FILE))) // add map of Leuven
                .addModel(DefaultPDPModel.builder())
                .addModel(view)
                .build();

        sim.start();

        return sim;
    }

    static View.Builder createGui() {

        // TODO : 'RoadUserRenderer' toevoegen (zie TaxiExample vanaf lijn 179)
        View.Builder view = View.builder()
                .with(GraphRoadModelRenderer.builder());

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
