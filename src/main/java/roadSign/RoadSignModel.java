package roadSign;

import com.github.rinde.rinsim.core.model.DependencyProvider;
import com.github.rinde.rinsim.core.model.Model;
import com.github.rinde.rinsim.core.model.ModelBuilder;
import com.github.rinde.rinsim.core.model.road.RoadModel;

import java.util.ArrayList;
import java.util.Random;

//TODO: header with dependencies etcetera
public class RoadSignModel extends Model.AbstractModel<RoadSignParcel> {

    RoadSignModel(RoadModel rm)
    {
        roadModel = rm;
        randomGenerator = new Random();
    }

    protected final RoadModel roadModel;


    /* RANDOM GENERATOR */

    private Random randomGenerator;

    public double getRandomDouble() {
        return randomGenerator.nextDouble();
    }


    /* ROADSIGNPARCEL LIST */

    private ArrayList<RoadSignParcel> parcelList = new ArrayList<>();
    private ArrayList<RoadSignParcel> randomParcelList = new ArrayList<>();

    /**
     * Add the given RoadSignParcel to the parcelList
     * @param element the given RoadSignParcel to add to the parcelList
     */
    public void addRoadSignParcel(RoadSignParcel element) {
        parcelList.add(element);
        randomParcelList.add(element);
    }

    /**
     * Remove the given RoadSignParcel from the parcelList
     * @param element the given RoadSignParcel to remove from the parcelList
     */
    public void removeRoadSignParcel(RoadSignParcel element) {
        parcelList.remove(element);
        randomParcelList.remove(element);
    }

    /**
     * Returns a shallow copy of the parcelList meaning that the list contains references to the original
     * RoadSignParcel objects
     * @return a shallow copy of the parcelList
     */
    public ArrayList<RoadSignParcel> getParcelList() {
        return (ArrayList<RoadSignParcel>) parcelList.clone();
    }

    /**
     * Get a random RoadSignParcel.
     * Works by keeping a separate copy of the parcelList. When a RoadSignParcel is returned, it is removed from this
     * list, this has the result that successive calls to this method can't return duplicates until all items have been
     * returned once. At this point a new clone of the original parcelList is copied to the separate list.
     * @return a random RoadSignParcel
     */
    public RoadSignParcel getRandomRoadSignParcel() {
        // if the parcelList is empty, return null
        if (parcelList.isEmpty()) return null;

        // if the randomParcelList is empty, clone the original list
        if (randomParcelList.isEmpty()) {
            randomParcelList = getParcelList();
        }

        int index = randomGenerator.nextInt(randomParcelList.size()); // get random index
        RoadSignParcel rsp = randomParcelList.get(index); // get parcel at index
        randomParcelList.remove(index); // remove parcel at index
        return rsp; // return parcel
    }

    public RoadSignPoint getRandomRoadSignPoint() {
        RoadSignParcel rsp = getRandomRoadSignParcel();
        if (rsp == null)
            return null;
        else
            return getRandomDouble() < 0.5 ? rsp.getPickupLocationRoadSignPoint() : rsp.getDeliveryLocationRoadSignPoint();
    }

    /* INTERFACE IMPLEMENTATIONS */

    /**
     * Register element in a model.
     * @param element the <code>! null</code> should be imposed
     * @return true if the object was successfully registered
     */
    @Override
    public boolean register(RoadSignParcel element){
        addRoadSignParcel(element);
	element.injectRoadSignModel(this);
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
        removeRoadSignParcel(element);
        return true;
    }


    /* DEPENDENCY INJECTION */
    // TODO: dependency injection van deze Model fixen

    public static RoadSignModelBuilder builder() {
        return new RoadSignModelBuilder();
    }

    public static class RoadSignModelBuilder extends ModelBuilder.AbstractModelBuilder<RoadSignModel, RoadSignParcel> {

        RoadSignModelBuilder() {
            setDependencies(RoadModel.class);
        }

        @Override
        public RoadSignModel build(DependencyProvider dependencyProvider) {
            final RoadModel rm = dependencyProvider.get(RoadModel.class);
            return new RoadSignModel(rm);
        }
    }

}
