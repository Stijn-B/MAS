package model.roadSignPoint;

import model.roadSignPoint.pheromones.RoadSign;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class PlannedPath implements Comparable<PlannedPath> {

    public LinkedList<RoadSign> path = new LinkedList<>();


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


    /* EDITING */

    /**
     * Appends the given RoadSign to this PlannedPath if possible, otherwise throws an IllegalArgumentException.
     * @param rs The RoadSign to add to this PlannedpATH
     * @throws IllegalArgumentException if the given RoadSign can't be added (see acceptableRS(RoadSign rs))
     */
    public void append(RoadSign rs) throws IllegalArgumentException {
        if (acceptableRS(rs)) path.add(rs);
        else throw new IllegalArgumentException(
                "The given RoadSign cannot be added to this PlannedPath! (see acceptableRS(RoadSign rs))");
    }


    /* CONTENT */

    /**
     * Returns the amount of RoadSigns this PlannedPath contains.
     * @return the amount of RoadSigns this PlannedPath contains
     */
    public int getNbRoadSigns() {
        return path.size();
    }

    public int getNbDeliveries() {
        int count = 0;
        Iterator<RoadSign> iterator = getIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getDestination().getPointType()
                == RoadSignPoint.PointType.PARCEL_DELIVERY
            ) {
                count++;
            }
        }
        return count;
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
    public PlannedPath copy() {
        PlannedPath ap = new PlannedPath();
        for (int i = 0; i < getNbRoadSigns(); i++) {
            ap.append(path.get(i));
        }
        return ap;
    }


    /* PATH CHECKING */

    public boolean acceptableRS(RoadSign rs) {
        return (getFinishPoint() == null || getFinishPoint() == rs.getLocation()) // it must continue the current path
                && acceptableHop(rs.getDestination()); // AND its destination must be acceptable
    }

    /**
     * Returns whether the given destination is acceptable considering the given path.
     */
    public boolean acceptableHop(RoadSignPoint dest) {

        // if dest is an model.user.owner.AGV -> not ok
        if (dest.getPointType() == RoadSignPoint.PointType.AGV) return false;

        // if dest was already passed -> not ok
        if (pathContains(dest)) return false;

        // if dest is a delivery point AND the dest owner has never been passed (so pickup was never passed) -> not ok
        if (dest.getPointType() == RoadSignPoint.PointType.PARCEL_DELIVERY
                && !pathContainsOwnerID(dest.getRoadSignPointOwner().getID())) return false;

        // otherwise the destination is ok
        return true;
    }

    /**
     * Returns whether the given path contains the given RoadSignPoint.
     */
    public boolean pathContains(RoadSignPoint rsPoint) {
        // iterate over all list pairs
        Iterator<RoadSign> iterator = getIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getDestination() == rsPoint) return true;
        }
        return false;
    }

    /**
     * Returns whether the given path contains a RoadSignPoint of which the RoadSignPointOwners ID equals the given ID
     * (used for checking whether a given path contains the pickup location)
     */
    public boolean pathContainsOwnerID(int ID) {
        // iterate over all list pairs
        Iterator<RoadSign> iterator = getIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getDestination().getRoadSignPointOwner().getID() == ID) return true;
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
}

// vim: set tabstop=4 shiftwidth=4 expandtab:
