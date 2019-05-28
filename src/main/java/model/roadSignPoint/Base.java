package model.roadSignPoint;

import com.github.rinde.rinsim.core.Simulator;
import com.github.rinde.rinsim.core.model.time.TickListener;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import model.ant.FeasibilityAnt;
import model.pheromones.RoadSign;

import java.util.LinkedList;
import java.util.Queue;

public class Base extends AbstractRoadSignPoint implements TickListener {

    public Base(Point position, Simulator simulator) {
        super(position);
        this.simulator = simulator;
    }


    /* SIMULATOR */

    private final Simulator simulator;

    public Simulator getSimulator() {
        return simulator;
    }


    /* FEASIBILITY ANTS */

    Queue<FeasibilityAnt> ants = new LinkedList<>();

    // adding

    /**
     * Adds 'n' amount of FeasibilityAnts
     */
    public void addFeasibilityAnts(int n) {
        for (int i = 0; i < n; i++) {
            addFeasibilityAnt();
        }
    }

    public void addFeasibilityAnt() {
        FeasibilityAnt ant = new FeasibilityAnt(this);
        ants.add(ant);
        getSimulator().register(ant);
    }

    // removing

    /**
     * Removes 'n' amount of FeasibilityAnts if possible
     */
    public void removeFeasibilityAnts(int n) {
        for (int i = 0; i < n; i++) {
            removeFeasibilityAnt();
        }
    }

    public void removeFeasibilityAnt() {
        FeasibilityAnt ant = ants.poll();
        if (ant != null) {
            getSimulator().unregister(ant);
        }
    }

    // amount

    public int getFeasibilityAntCount() {
        return ants.size();
    }

    /**
     * Adds or removes FeasibilityAnts until the amount of FeasibilityAnts in the system
     * equals the given amount 'n'.
     * if n == getFeasibilityAntCount() || n < 0, does nothing
     */
    public void setFeasibilityAntCount(int n) {
        if (n < 0) return;

        // difference between wanted ants and ants in system now
        int delta = n - getFeasibilityAntCount();

        // if n is positive, add ants. else, remove ants
        if (0 <= n) {
            addFeasibilityAnts(delta);
        } else {
            removeFeasibilityAnts(-delta);
        }
    }

    // balancing

    /**
     * Balances the amount of FeasibilityAnts based on the amount of RoadSignPointOwners in the
     * system and the given balancingConstant.
     * Returns the new amount of FeasibilityAnts
     */
    public int balanceFeasibilityAntCount(double balancingConstant) {
        int n = (int) Math.round(Math.ceil(balancingConstant*getMaxEdgeCount()));
        setFeasibilityAntCount(n);
        return n;
    }

    /**
     * calculates a balancing constant based on the FeasibilityAnt.MS_PER_HOP and
     * RoadSign.DEFAULT_LIFETIME
     */
    public int  balanceFeasibilityAntCount() {
        // the amount of RoadSigns 1 ant would theoretically keep alive
        double rsCountPerAnt = (1.0 * RoadSign.DEFAULT_LIFETIME) / FeasibilityAnt.MS_PER_HOP;

        return balanceFeasibilityAntCount(3/rsCountPerAnt);
    }

    // edge count

    /**
     * Returns the amount of possible edges in the RoadSignPoint graph.
     * In a complete graph, every pair of vertices is connected by an edge.
     * So the number of edges is just the number of pairs of vertices. That's (1/2)n(n−1)
     */
    public int getMaxEdgeCount() {
        int n = getRoadSignPointModel().pointCount();
        return (n*(n-1))/2;
    }


    /* TickListener INTERFACE */

    @Override
    public void tick(TimeLapse timeLapse) {
        // Do nothing
    }

    /**
     * After the tick, the base reviews the amount of, and removes or adds Feasibility Ants.
     */
    @Override
    public void afterTick(TimeLapse timeLapse) {
        age(timeLapse.getTickLength());

        int n = balanceFeasibilityAntCount();
        //System.out.println("[Base.afterTick()] getMaxEdgeCount(): " + String.valueOf(getMaxEdgeCount()));
        //System.out.println("[Base.afterTick()] feasibility ant count: " + String.valueOf(n));
    }

}
