package roadSignAnt;

import javafx.util.Pair;

import java.util.Iterator;
import java.util.List;

public class AntPath {

    public List<Pair<RoadSignPoint,Double>> path;

    public void append(RoadSign rs) {
        append(new Pair(rs.getDestination(), rs.getDistance()));
    }

    public void append(Pair<RoadSignPoint,Double> a) {
        path.add(a);
    }

    public AntPath copy() {
        AntPath ap = new AntPath();
        for (int i = 0; i < getLength(); i++) {
            ap.append(path.get(i));
        }
        return ap;
    }


    public int getLength() {
        return path.size();
    }

    public Iterator<Pair<RoadSignPoint,Double>> getIterator() {
        return path.iterator();
    }


    /* PATH CHECKING */

    /**
     * Returns whether the given destination is acceptable considering the given path.
     */
    public boolean acceptableNextHop(RoadSignPoint dest) {

        // if dest is an AGV -> not ok
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
        Iterator<Pair<RoadSignPoint,Double>> iterator = getIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getKey() == rsPoint) return true;
        }
        return false;
    }

    /**
     * Returns whether the given path contains a RoadSignPoint of which the RoadSignPointOwners ID equals the given ID
     * (used for checking whether a given path contains the pickup location)
     */
    public boolean pathContainsOwnerID(int ID) {
        // iterate over all list pairs
        Iterator<Pair<RoadSignPoint,Double>> iterator = getIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getKey().getRoadSignPointOwner().getID() == ID) return true;
        }
        return false;
    }
}
