package model.user.ant;

import model.roadSignPoint.PlannedPath;
import model.roadSignPoint.pheromones.RoadSign;
import model.user.owner.AGV;

import java.util.Iterator;

/**
 * Transporting AVG that sends out scouting AVGs and transports packages based on their intel
 */
public class IntentionAnt extends Ant {

    /**
     * Declares the intention of the given AGV over the given path. Returns whether the path is still valid. A path is
     * invalid if it contains 1 or more RoadSigns that are invalid (see RoadSign.isValid())
     */
    public boolean declareIntention(AGV agv, PlannedPath path, long now) {

        double totalDist = 0;

        // iterate over the complete PlannedPath
        Iterator<RoadSign> iter = path.getIterator();
        while (iter.hasNext()) {
            RoadSign curr = iter.next();

            // if the current RoadSign is not valid, return false
            if (!curr.isValid()) return false;

            totalDist += curr.getDistance();

            // revitalize roadSignPoint and register intention at its destination
            curr.revitalize(5000);
            long ETA = now + agv.distanceToDuration(totalDist);
            curr.getDestination().registerIntention(agv, ETA);
        }
        return true;
    }

}
