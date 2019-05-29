package model.ant;

import model.roadSignPoint.AGV;
import model.roadSignPoint.Base;
import model.roadSignPoint.RoadSignPoint;
import model.roadSignPoint.parcel.ParcelDelivery;
import model.roadSignPoint.parcel.ParcelPickup;
import model.pheromones.RoadSign;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlannedPath implements Comparable<PlannedPath> {

    public PlannedPath(AGV agv) throws NullPointerException {
        if (agv == null) throw new NullPointerException("given agv can't be nullpointer");
        parcelIDs.addAll(agv.getParcelIDs());
        this.agv = agv;
    }

    public final AGV agv;

    /* HEURISTIC */

    // initialised to -1 -> getHeuristicScore() == -1 means no heuristic was assigned yet
    private double heuristic = -1;

    /**
     * Returns the heuristic assigned to the given PlannedPath.
     * heuristic == -1 means no value was assigned yet.
     * @return he heuristic assigned to the given PlannedPath
     */
    public double getHeuristicScore() {
        return heuristic;
    }

    public void setHeuristicScore(double h) {
        heuristic = h;
    }

    public boolean hasHeuristicScore() {
        return 0 < getHeuristicScore();
    }


    /* PATH EDITING */

    public LinkedList<RoadSign> path = new LinkedList<>();

    /**
     * Appends the given RoadSign to this PlannedPath if possible, otherwise throws an IllegalArgumentException.
     * @param rs The RoadSign to add to this PlannedpATH
     * @throws IllegalArgumentException if the given RoadSign can't be added (see acceptableRS(RoadSign rs))
     */
    public void append(RoadSign rs) throws IllegalArgumentException {
        if (acceptableRS(rs))
            path.add(rs);
        else
            throw new IllegalArgumentException(
                "The given RoadSign cannot be added to this PlannedPath! (see acceptableRS(RoadSign rs))");

        // if the RoadSign destination is a parcel pickup, add this parcel to the list
        if (rs.getDestination() instanceof ParcelPickup)
            addParcel((ParcelPickup) rs.getDestination());
    }

    public void popFirst() {
        if (path.getFirst() == null) return;
        else path.removeFirst();
    }


    /* PATH CONTENT */

    public boolean isEmpty() {
        return path.isEmpty();
    }

    public int getNbRoadSigns() {
        return path.size();
    }

    public int getNbDeliveries() {
        int count = 0;

        Iterator<RoadSign> iterator = getIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getDestination() instanceof ParcelDelivery) count++;
        }

        return count;
    }

    public List<RoadSignPoint> getRoadSignPoint() {
        LinkedList<RoadSignPoint> result = new LinkedList<>();

        Iterator<RoadSign> iter = getIterator();
        while (iter.hasNext()) {
            result.add(iter.next().getDestination()); // TODO: opmerking: hier wordt getDestination gebruikt en niet getLocation
        }

        return result;
    }

    /**
     * Returns the total length of this PlannedPath based on the RoadSigns it contains.
     * @return the total length of this PlannedPath based on the RoadSigns it contains
     */
    public double getTotalPathLength() {
        Iterator<RoadSign> iter = getIterator();
        double sum = 0;
        while (iter.hasNext()) {
            sum += iter.next().getDistance();
        }
        return sum;
    }

    /**
     * Returns the finishing point of the PlannedPath == the destination of the last RoadSign. Null if there is none
     * @return the finishing point of the PlannedPath == the destination of the last RoadSign. Null if there is none
     */
    public RoadSignPoint getFinishPoint() {
        try {
            return path.getLast().getDestination();
        } catch (NoSuchElementException e) { // if the path has no last element, finishing is null
            return null;
        }
    }

    /**
     * Returns the first point of the PlannedPath == the destination of the first RoadSign. Null if there is none
     * @return the first point of the PlannedPath == the destination of the first RoadSign. Null if there is none
     */
    public RoadSignPoint getFirstDest() {
        try {
            return path.getFirst().getDestination();
        } catch (NoSuchElementException e) { // if the path has no last element, finishing is null
            return null;
        }
    }

    /**
     * Returns an Iterator<RoadSign> to iterate over the RoadSigns of this PlannedPath.
     * @return an Iterator<RoadSign> to iterate over the RoadSigns of this PlannedPath
     */
    public Iterator<RoadSign> getIterator() {
        return path.iterator();
    }

    /**
     * Creates a copy of this PlannedPath by creating a new PlannedPath and adding all the RoadSigns to it. Changes
     * to the returned PlannedPath will not affect this PlannedPath, changes however to the RoadSigns of the returned
     * PlannedPath will affect the RoadSigns contained by the original PlannedPath.
     * @return a copy of this PlannedPath
     */
    public PlannedPath getCopy() {
        PlannedPath ap = new PlannedPath(agv);
        for (int i = 0; i < getNbRoadSigns(); i++) {
            ap.append(path.get(i));
        }
        return ap;
    }

    /* PARCELS */

    private HashSet<Integer> parcelIDs = new HashSet<>();

    public HashSet<Integer> getParcelIDs() {
        return parcelIDs;
    }

    public void addParcel(ParcelPickup parcel) {
        addParcelID(parcel.getParcelID());
    }

    public void addParcelID(int id) {
        parcelIDs.add(id);
    }

    public void removeParcelID(int id) {
        parcelIDs.remove(id);
    }

    public boolean carriesParcel(int id) {
        return parcelIDs.contains(id);
    }


    /* PATH CHECKING */

    public boolean acceptableRS(RoadSign rs) {
        return RoadSignContinuesPath(rs) && acceptableDestination(rs.getDestination());
    }

    /**
     * Returns whether the given RoadSign is a continuation of the path
     */
    public boolean RoadSignContinuesPath(RoadSign rs) {
        return getFinishPoint() == null || getFinishPoint() == rs.getLocation();
    }

    /**
     * Returns whether the given destination is acceptable considering the given path.
     */
    public boolean acceptableDestination(RoadSignPoint dest) {
        boolean b = true;
        if (dest instanceof AGV)                 b = acceptableDestination((AGV) dest);
        else if (dest instanceof ParcelPickup)   b = acceptableDestination((ParcelPickup) dest);
        else if (dest instanceof ParcelDelivery) b = acceptableDestination((ParcelDelivery) dest);
        else if (dest instanceof Base)           b = acceptableDestination((Base) dest);
        return !pathContains(dest) && dest.isRegistered() && b;
    }

    public boolean acceptableDestination(AGV dest) {
        return false;
    }

    public boolean acceptableDestination(ParcelPickup dest) {
        return parcelIDs.size() < agv.getMaxParcelCount();
    }

    public boolean acceptableDestination(ParcelDelivery dest) {
        return carriesParcel(dest.getParcelID());
    }

    public boolean acceptableDestination(Base dest) {
        return agv.plansPassingBase();
    }

    /**
     * Returns whether the given path contains the given DeprecatedRoadSignPoint.
     */
    public boolean pathContains(RoadSignPoint rsPoint) {
        // iterate over all list pairs
        Iterator<RoadSign> iterator = getIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getDestination() == rsPoint) return true;
        }
        return false;
    }


    /* INTERFACE Comparable<PlannedPath> */

    @Override
    public int compareTo(@NotNull PlannedPath o) {
        if (this.getHeuristicScore() > o.getHeuristicScore()) { // Highest heuristic should be first in list -> other object is seen as bigger number and thus placed later in list
            return 1;
        } else if (this.getHeuristicScore() == o.getHeuristicScore()) {
            return 0;
        } else {
            return -1;
        }
    }


    /* OHTER */

    @Override
    public String toString() {
        StringBuffer outputBuffer = new StringBuffer();

        Iterator<RoadSign> iterator = getIterator();

        outputBuffer.append("[");
        while (iterator.hasNext()) {
            RoadSign rs = iterator.next();
            outputBuffer.append(rs.getDestination());
            if (iterator.hasNext()) outputBuffer.append(", ");
        }
        outputBuffer.append("]");

        return outputBuffer.toString();
    }
}

// vim: set tabstop=4 shiftwidth=4 expandtab:
