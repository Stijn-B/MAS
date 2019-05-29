package model.roadSignPoint;

import com.github.rinde.rinsim.core.model.road.RoadModel;
import com.github.rinde.rinsim.core.model.road.RoadUser;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import model.pheromones.AgingPheromone;
import model.pheromones.IntentionData;
import model.pheromones.RoadSign;
import model.RoadSignPointModel;

import java.util.*;

public abstract class AbstractRoadSignPoint implements RoadSignPoint, RoadUser, TickListener {

    public static long ID_COUNTER = 0;

    public static long getNewID() {
        long d = ID_COUNTER;
        ID_COUNTER = loopAroundIncrement(ID_COUNTER);
        return d;
    }

    public static long loopAroundIncrement(long id) {
        if (id < Long.MAX_VALUE)
            return id + 1;
        else
            return 0;
    }

    public AbstractRoadSignPoint(Point position) throws NullPointerException {
        if (position == null) throw new NullPointerException("given position can't be nullpointer");
        pos = position;
        this.ID = getNewID();
    }

    public final long ID;

    public long getID() {
        return this.ID;
    }

    private Point pos;


    public boolean act(AGV agv) {
        return true;
    }

    /* REGISTRATION */

    public boolean isRegistered() {
        return hasRoadModel() && hasRoadSignPointModel();
    }

    public void unregister() {
        // unregister from both models
        getRoadModel().unregister(this);
        getRoadSignPointModel().unregister(this);

        // set the to null just to be sure
        injectRoadSignPointModel(null);
        initRoadUser(null);
    }

    /* ROADSIGNS */

    /**
     * The RoadSigns are saved in a SortedSet keeping them sorted by distance to destination (ascending order)
     */
    SortedSet<RoadSign> roadSigns = new TreeSet<>();

    /**
     * Returns whether this DeprecatedRoadSignPoint holds the given RoadSign.
     */
    public boolean holds(RoadSign rs) {
        return roadSigns.contains(rs);
    }

    /**
     * Adds a new RoadSing pointing to the given destination with given distance.
     * @param destination destination of the RoadSign
     * @param dist distance to the given destination
     */
    @Override
    public void addRoadSign(RoadSignPoint destination, double dist) {
        addRoadSign(new RoadSign(this, destination, dist));
    }

    /**
     * Adds the given RoadSign to this DeprecatedRoadSignPoint
     * @param newSign the RoadSign to add
     */
    public void addRoadSign(RoadSign newSign) {

        // if a RoadSign equal to the given one is already contained, remove it (so it gets replaced)
        if (roadSigns.contains(newSign)) roadSigns.remove(newSign);

        roadSigns.add(newSign);
    }

    /**
     * Get an Iterator of all the RoadSigns this DeprecatedRoadSignPoint contains.
     * @return an Iterator of all the RoadSigns this DeprecatedRoadSignPoint contains
     */
    @Override
    public Iterator<RoadSign> getRoadSignsIterator() {
        return roadSigns.iterator();
    }

    public SortedSet<RoadSign> getRoadSigns() {
        return roadSigns;
    }

    /**
     * Returns the first n RoadSigns (pointing the n closest destinations)
     * If there are less than n RoadSigns, less RoadSigns will be returned.
     * @param n the amount of RoadSigns to return
     * @return the first n RoadSigns, if there are less than n RoadSigns, less RoadSigns will be returned
     */
    public List<RoadSign> getNRoadSigns(int n) {
        Iterator<RoadSign> iter = roadSigns.iterator();
        List<RoadSign> result = new ArrayList<>();

        // add the first n elements to the result
        for(int i = 0; i < n; i ++) {
            try {
                result.add(iter.next());
            } catch (NoSuchElementException e) { // no elements left
                return result;
            }
        }
        return result;
    }

    /**
     * Get the amount of RoadSign roadSignPoint this DeprecatedRoadSignPoint has
     * @return the amount of RoadSign roadSignPoint this DeprecatedRoadSignPoint has
     */
    public int getRoadSignCount() {
        return roadSigns.size();
    }


    /* INTENTIONS */

    private SortedSet<IntentionData> intentions = new TreeSet<>();

    public SortedSet<IntentionData> getIntentions() {
        return intentions;
    }

    /**
     * Registers an intention for the given AGV who will arrive at the given time.
     */
    public void addIntention(IntentionData intention) {
        intentions.add(intention);
    }

    public void addIntention(AGV agv, long ETA) {
        addIntention(new IntentionData(agv, ETA));
    }

    public Iterator<IntentionData> getIntentionIterator() {
        return intentions.iterator();
    }

    public boolean wouldAgvArriveInTime(AGV agv, long ETA) {
        return true;
    }


    /* AGING */

    @Override
    public void tick(TimeLapse timeLapse) {
        // do nothing
    }

    @Override
    public void afterTick(TimeLapse timeLapse) {
        age(timeLapse.getTickLength());

        // print roadsigns of this point
        /*
        System.out.println("/| /| /| [" + this + "]");  // PRINT

        System.out.print("RoadSigns to: ");
        for (RoadSign rs : getRoadSigns()) {
            System.out.print(rs.getDestination() + " " + rs.getRemainingLifeTime() + " | ");
        }
        System.out.println();
        */
    }

    /**
     * Ages all the RoadSigns that this RoadSignPoint holds
     * @param ms
     */
    @Override
    public void age(long ms) {
        age(roadSigns, ms);
        age(intentions, ms);
    }

    /**
     * Ages all AgingPheromone in the given agingSet, removes AgingPheromone from the set if they are expired.
     */
    public void age(Iterable<? extends AgingPheromone> agingSet, long ms) {
        // Iterate over all AgingPheromones and age them
        Iterator<? extends AgingPheromone> iter = agingSet.iterator();
        while(iter.hasNext()) {
            // if the next RoadSign doesn't survive the aging, remove it
            if (!iter.next().age(ms)) {
                iter.remove(); // removes the last item given by iter.next() from the underlying collection
            }
        }
    }


    /* ROADSIGNPOINT MODEL */

    private RoadSignPointModel rspModel;

    @Override
    public void injectRoadSignPointModel(RoadSignPointModel model) {
        rspModel = model;
    }

    @Override
    public RoadSignPointModel getRoadSignPointModel() {
        return rspModel;
    }

    @Override
    public boolean hasRoadSignPointModel() {
        return rspModel != null;
    }


    /* ROADUSER MODEL */

    public RoadModel roadModel;

    @Override
    public void initRoadUser(RoadModel model) {
        roadModel = model;
        if (model != null) model.addObjectAt(this, pos);
    }

    @Override
    public RoadModel getRoadModel() {
        return roadModel;
    }

    @Override
    public boolean hasRoadModel() {
        return roadModel != null;
    }

    /* NAME */

    public String getName() {
        return "RSP";
    }

    public String toString() {
        return getName() + "(" + getID() + ")";
    }
}
