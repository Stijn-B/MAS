package roadSign;

import com.github.rinde.rinsim.core.model.DependencyProvider;
import com.github.rinde.rinsim.core.model.Model;
import com.github.rinde.rinsim.core.model.ModelBuilder;
import com.github.rinde.rinsim.core.model.road.RoadModel;


public class RoadSignModel extends Model.AbstractModel<RoadSignParcel> {

    protected final RoadModel roadModel;

    RoadSignModel(RoadModel rm) {
        roadModel = rm;
    }

    /**
     * Register element in a model.
     * @param element the <code>! null</code> should be imposed
     * @return true if the object was successfully registered
     */
    @Override
    public boolean register(RoadSignParcel element){
        System.out.println("roadSign.RoadSignParcel registered");
        return true;
    }

    /**
     * Unregister element from a model.
     * @param element the <code>! null</code> should be imposed
     * @return true if the unregistration changed the model (element was part of
     *         the model and it was successfully removed)
     */
    @Override
    public boolean unregister(RoadSignParcel element) {
        System.out.println("roadSign.RoadSignParcel unregistered");
        return true;
    }

    public static RoadSignModelBuilder builder() {
        return new RoadSignModelBuilder();
    }


    public static class RoadSignModelBuilder extends ModelBuilder.AbstractModelBuilder<RoadSignModel, RoadSignParcel> {

        RoadSignModelBuilder() {
            setProvidingTypes(RoadSignModel.class);
            setDependencies(RoadSignModel.class);
        }

        @Override
        public RoadSignModel build(DependencyProvider dependencyProvider) {
            final RoadModel rm = dependencyProvider.get(RoadModel.class);
            return new RoadSignModel(rm);
        }
    }

}
