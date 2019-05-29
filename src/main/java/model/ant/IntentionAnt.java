package model.ant;

import model.pheromones.RoadSign;
import model.roadSignPoint.AGV;

import java.util.Iterator;

/**
 * Transporting AVG that sends out scouting AVGs and transports packages based on their intel
 */
public class IntentionAnt extends Ant {

    public IntentionAnt(AGV agv) throws NullPointerException {
        if (agv == null) throw new NullPointerException("given agv can't be nullpointer");
        this.agv = agv;
    }


    /* AGV */

    public final AGV agv;


    /* INTENTION */

    /**
     * Declares the intention of the given AGV over the given path. Returns whether the path is still valid. A path is
     * invalid if it contains 1 or more RoadSigns that are invalid (see RoadSign.isValid()) or another agv would be first
     */
    public boolean declareIntention(PlannedPath path, long now) {

        if (path == null || path.isEmpty()) return true;

        double totalDist = 0;

        // iterate over the complete PlannedPath
        Iterator<RoadSign> iter = path.getIterator();
        while (iter.hasNext()) {
            RoadSign curr = iter.next();


            // if the current RoadSign is not valid, return false
            if (!curr.isValid())
                return false;

            // refresh the roadSign
            //curr.refresh();

            totalDist += curr.getDistance();

            // revitalize RoadSign
            curr.revitalize(5000);

            // Calculate and check ETA
            long ETA = now + agv.calculateTravelTime(totalDist);
            if (!curr.getDestination().wouldAgvArriveInTime(agv, ETA))
                return false;

            curr.getDestination().addIntention(agv, ETA);
            System.out.println("[" + agv + "] registered intention " + curr.getDestination() + " now:" + now + ", ETA:" + ETA + " distance:" + totalDist);  // 192 - 2
        }
        return true;
    }

}
