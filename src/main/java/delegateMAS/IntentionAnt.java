import com.github.rinde.rinsim.core.model.pdp.Vehicle;
import com.github.rinde.rinsim.core.model.pdp.VehicleDTO;
import com.github.rinde.rinsim.core.model.time.TimeLapse;
import com.github.rinde.rinsim.geom.Point;

/**
 * Transporting AVG that sends out scouting AVGs and transports packages based on their intel
 * TODO:
 */
public class Ufo extends Vehicle {

    private static final double DEFAULT_SPEED = 1000d;
    private static final int DEFAULT_CAPACITY = 5;

    Ufo(Point startPosition) {
        this(startPosition, DEFAULT_SPEED, DEFAULT_CAPACITY);
    }

    Ufo(Point startPosition, double speed, int capacity) {
        super(VehicleDTO.builder().capacity(capacity).startPosition(startPosition).speed(speed).build());
    }

    @Override
    protected void tickImpl(TimeLapse time) {

    }
}
