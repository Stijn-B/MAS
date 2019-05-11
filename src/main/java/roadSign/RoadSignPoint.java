import com.github.rinde.rinsim.geom.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class RoadSignPoint extends Point {


    public RoadSignPoint(double pX, double pY) {
        super(pX, pY);
    }

    public RoadSignPoint(Point point) {
        super(point.x, point.y);
    }

    /**
     * RoadSigns are stored sorted by ascending distance in a LinkedList
     */
    List<RoadSign> roadSigns = new LinkedList<>();

    /**
     * Adds the given RoadSign to the list while keeping the list sorted by ascending distance
     * @param newSign the RoadSign to add
     */
    public void addRoadSign(RoadSign newSign) {
        ListIterator<RoadSign> iter = roadSigns.listIterator();

        while(iter.hasNext()) {
            RoadSign nextSign = iter.next();
            if (newSign.compareTo(nextSign) <= 0) { // distance to newSign is equal or smaller than distance to nextSign
                iter.previous(); // go back 1 so iter.add() adds in the right place
                break; // break loop so newSign is added in right position
            }
        }

        iter.add(newSign); // if the list is empty, the new sign is added at the start of the list
    }

    /**
     * Ages all the RoadSigns that this RoadSignPoint holds
     * @param ms
     */
    public void age(long ms) {
        ListIterator<RoadSign> iter = roadSigns.listIterator();

        while(iter.hasNext()) {
            // if the next RoadSign doesn't survive the aging, remove it
            if (!iter.next().age(ms)) {
                iter.remove();
            }
        }
    }

    /**
     * Get the size of the RoadSign list
     * @return the size of the RoadSign list
     */
    public int getRoadSignCount() {
        return roadSigns.size();
    }

    /**
     * Returns the RoadSign at the specified position in the RoadSign list.
     * @param index index of the RoadSign to return
     * @return the RoadSign at the specified position in the RoadSign list
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= getRoadSignCount())
     */
    public RoadSign getRoadSign(int index) {
        return roadSigns.get(index);
    }

    // ROAD SIGN POINT TEST
    /*
    public static void main(String[] args) {

        RoadSignPoint rsPoint = new RoadSignPoint(5.0, 5.0);

        for (int i = 0; i < 5; i++) {
            rsPoint.addRoadSign(new RoadSign(new Point(i, i), 10*i, 200*i+50));
        }

        for (int i = 0; i < rsPoint.getRoadSignCount(); i++) {
            System.out.println(rsPoint.getRoadSign(i));
        }
        System.out.println("-- add more roadsigns");


        for (int i = 0; i < 5; i++) {
            rsPoint.addRoadSign(new RoadSign(new Point(i, i), 50/(i+1), 200*i));
        }

        for (int i = 0; i < rsPoint.getRoadSignCount(); i++) {
            System.out.println(rsPoint.getRoadSign(i));
        }
        System.out.println("-- age 50 ms");

        rsPoint.age(50);

        for (int i = 0; i < rsPoint.getRoadSignCount(); i++) {
            System.out.println(rsPoint.getRoadSign(i));
        }
        System.out.println("-- age 500 ms");

        rsPoint.age(500);

        for (int i = 0; i < rsPoint.getRoadSignCount(); i++) {
            System.out.println(rsPoint.getRoadSign(i));
        }
    }
    */

}
