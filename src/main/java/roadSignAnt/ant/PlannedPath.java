package roadSignAnt.ant;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import roadSignAnt.roadSignPoint.RoadSign;
import roadSignAnt.roadSignPoint.RoadSignPoint;

import java.util.Iterator;
import java.util.List;

public class PlannedPath implements Comparable<PlannedPath> {

    public List<RoadSign> path;


    /* HEURISTIC */

    // initialised to -1 -> getHeuristic() == -1 means no heuristic was assigned yet
    private double heuristic = -1;

    /**
     * Returns the heuristic assigned to the given roadSignAnt.ant.PlannedPath.
     * heuristic == -1 means no value was assigned yet.
     * @return he heuristic assigned to the given roadSignAnt.ant.PlannedPath
     */
    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double h) {
        heuristic = h;
    }


    /* EDITING */

    public void append(RoadSign rs) {
        append(new RoadSign(rs.getDestination(), rs.getDistance()));
    }

    public void append(RoadSignPoint point, double dist) {
        append(new RoadSign(point, dist));
    }


    /* CONTENT */

    public int getLength() {
        return path.size();
    }

    public Iterator<RoadSign> getIterator() {
        return path.iterator();
    }

    public PlannedPath copy() {
        PlannedPath ap = new PlannedPath();
        for (int i = 0; i < getLength(); i++) {
            ap.append(path.get(i));
        }
        return ap;
    }


    /* PATH CHECKING */

    /**
     * Returns whether the given destination is acceptable considering the given path.
     */
    public boolean acceptableNextHop(RoadSignPoint dest) {

        // if dest is an roadUser.AGV -> not ok
        if (dest.getType() == RoadSignPoint.Type.AGV) return false;

        // if dest was already passed -> not ok
        if (pathContains(dest)) return false;

        // if dest is a delivery point AND the dest owner has never been passed (so pickup was never passed) -> not ok
        if (dest.getType() == RoadSignPoint.Type.PARCEL_DELIVERY
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


    /* INTERFACE Comparable<roadSignAnt.ant.PlannedPath> */

    @Override
    public int compareTo(@NotNull PlannedPath o) {
        if (this.getHeuristic() > o.getHeuristic()) { // Highest heuristic should be first in list -> other object is seen as bigger number and thus placed later in list
            return 1;
        } else if (this.getHeuristic() == o.getHeuristic()) {
            return 0;
        } else {
            return -1;
        }
    }
}
