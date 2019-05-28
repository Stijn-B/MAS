package model.user.owner;

import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;
import model.RoadSignPointModel;
import model.roadSignPoint.RoadSignPoint;

import javax.annotation.Nullable;

public abstract class AbstractRoadSignPointOwner implements RoadSignPointOwner {

    /* CONSTRUCTOR */

    /**
     * Initializes a RoadSignPointOwner
     * @param oType The type of this owner
     * @param points An arroy of Points this object owns
     * @param pTypes An array with the PointTypes of the given Points
     * @param ID The ID of this RoadSignPointOwner
     * @throws IllegalArgumentException if the amount of given Points does not equal the amount of given PointTypes
     */
    public AbstractRoadSignPointOwner(OwnerType oType, Point[] points, RoadSignPoint.PointType[] pTypes, int ID)
            throws IllegalArgumentException {
        if (points.length != pTypes.length) throw new IllegalArgumentException(
                "The amount of given Points does not equal the amount of given PointTypes");

        this.ownerID = ID;
        this.type = oType;

        this.roadSignPoints = new RoadSignPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            roadSignPoints[i] = new RoadSignPoint(this, pTypes[i], points[i]);
        }
    }

    /**
     * Initializes a RoadSignPointOwner
     * @param oType The type of this owner
     * @param points An arroy of Points this object owns
     * @param pTypes An array with the PointTypes of the given Points
     * @throws IllegalArgumentException if the amount of given Points does not equal the amount of given PointTypes
     */
    public AbstractRoadSignPointOwner(OwnerType oType, Point[] points, RoadSignPoint.PointType[] pTypes) {
        this(oType, points, pTypes, getNewID());
    }


    /**
     * Initializes a RoadSignPointOwner
     * @param oType The type of this owner
     * @param point The position of this owner
     * @param pType The type of the RoadSignPoint
     * @param ID The ID of this RoadSignPointOwner
     */
    public AbstractRoadSignPointOwner(OwnerType oType, Point point, RoadSignPoint.PointType pType, int ID) {
        this.ownerID = ID;
        this.type = oType;
        this.roadSignPoints = new RoadSignPoint[]{new RoadSignPoint(this, pType, point)};
    }

    /**
     * Initializes a RoadSignPointOwner
     * @param oType The type of this owner
     * @param point The position of this owner
     * @param pType The type of the RoadSignPoint
     */
    public AbstractRoadSignPointOwner(OwnerType oType, Point point, RoadSignPoint.PointType pType) {
        this(oType, point, pType, getNewID());
    }


    private final int ownerID;
    private final OwnerType type;
    public final RoadSignPoint[] roadSignPoints;

    private RoadSignPointModel roadSignPointModel;


    /* INTERFACE */

    // RoadSignPointUser

    @Override
    public void injectRoadSignPointModel(RoadSignPointModel model) {
        roadSignPointModel = model;
    }

    @Override
    public void removeRoadSignPointModel() {
        roadSignPointModel = null;
    }

    @Override
    public boolean hasRoadSignPointModel() {
        return getRoadSignPointModel() != null;
    }

    public RoadSignPointModel getRoadSignPointModel() {
        return roadSignPointModel;
    }

    // RoadSignPointOwner

    @Override
    public int getID() {
        return ownerID;
    }

    @Override
    public OwnerType getRoadSignPointOwnerType() {
        return type;
    }

    @Override
    public RoadSignPoint[] getRoadSignPoints() {
        return roadSignPoints.clone();
    }

    @Override
    public boolean act(AGV agv, RoadSignPoint rsPoint) {
        return true;
    }

    @Override
    public int hashCode() { return getID(); }

    @Override
    public boolean equals(@Nullable Object other) {
        return other != null
                && other instanceof RoadSignPointOwner
                && ((RoadSignPointOwner) other).getID() == getID();
    }


    /* STATIC ID METHODS */

    static int ID = 0;

    public static int getNewID() {
        int d = ID;
        ID = loopAroundIncrement(ID);
        return d;
    }

    public static int loopAroundIncrement(int id) {
        if (id < Integer.MAX_VALUE)
            return id + 1;
        else
            return 500; // objects that remain forverer (base and AGVs their ID should be under 500), avoid reusing those IDs
    }
}
