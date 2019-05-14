package roadSignAnt;

public class RoadSignPointOwnerID {

    private static int ID = 0;

    public static int getID() {
        ID = loopAroundIncrement(ID);
        return ID;
    }

    public static int loopAroundIncrement(int id) {
        if (id < Integer.MAX_VALUE)
            return id + 1;
        else
            return 500; // objects that remain forverer (base and AGVs their ID should be under 500), avoid reusing those IDs
    }

}
