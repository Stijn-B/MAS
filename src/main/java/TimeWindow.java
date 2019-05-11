import com.github.rinde.rinsim.core.Simulator;

public class TimeWindow {

    /**
     * Arguments are given in ms
     * Get current time with .getCurrentTime() on the Simulator object
     * @param start start of the time window
     * @param end end of the time window
     */
    public TimeWindow(long start, long end) {
        this.start = start;
        this.end = end;
    }

    final private long start;
    final private long end;

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }


    /**
     * Returns whether the given time falls in the time window.
     * @param time The time in milliseconds
     * @return whether the given time falls in the time window
     */
    public boolean inWindow(long time) {
        return getStart() <= time && time <= getEnd();
    }


    /**
     * Returns how much milliseconds are left before the time window starts, 0 if the time window has already started.
     * @param simulator the Simulator object that runs the simulation (used for getting the current time)
     * @return how much milliseconds are left before the time window starts, 0 if the time window has already started
     */
    public long timeLeftToStart(Simulator simulator) {
        return timeLeftToStart(simulator.getCurrentTime());
    }

    /**
     * Returns how much milliseconds are left before the time window starts, 0 if the time window has already started.
     * @param now the current time in milliseconds
     * @return how much milliseconds are left before the time window starts, 0 if the time window has already started
     */
    public long timeLeftToStart(long now) {
        if (getStart() <= now) {
            return 0;
        } else {
            return getStart() - now;
        }
    }


    /**
     * Returns how much milliseconds are left before the time window is over, 0 if the time window has already ended.
     * @param simulator the Simulator object that runs the simulation (used for getting the current time)
     * @return how much milliseconds are left before the time window is over, 0 if the time window has already ended
     */
    public long timeLeftToEnd(Simulator simulator) {
        return timeLeftToEnd(simulator.getCurrentTime());
    }

    /**
     * Returns how much milliseconds are left before the time window is over, 0 if the time window has already ended.
     * @param now the current time in milliseconds
     * @return how much milliseconds are left before the time window is over, 0 if the time window has already ended
     */
    public long timeLeftToEnd(long now) {
        if (getEnd() <= now) {
            return 0;
        } else {
            return getEnd() - now;
        }
    }

}
