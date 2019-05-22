package model.pheromones;

public abstract class AgingPheromone {

    public static long DEFAULT_PHEROMONE_LIFETIME_MS = 10000;


    public AgingPheromone(long lifeTime, long maxLifeTime) {
        life = lifeTime;
        maxLife = maxLifeTime;
    }

    public AgingPheromone(long lifeTime) {
        this(lifeTime, lifeTime);
    }

    public AgingPheromone() {
        this(DEFAULT_PHEROMONE_LIFETIME_MS);
    }

    private long maxLife;

    private long life;

    /**
     * Get the remaining life time of the model.pheromones.roadSign.RoadSign
     * @return the remaining life time of the model.pheromones.roadSign.RoadSign
     */
    public long getRemainingLifeTime() {
        return life;
    }

    public long getMaxLifeTime() {
        return maxLife;
    }

    /**
     * Returns whether the given RoadSign is still alive.
     * @return whether the given RoadSign is still alive
     */
    public boolean alive() {
        return 0 < getRemainingLifeTime();
    }

    /**
     * Ages the model.pheromones.roadSign.RoadSign by the given amount of milliseconds and returns whether the model.pheromones.roadSign.RoadSign has life time left
     * @param ms how much milliseconds the model.pheromones.roadSign.RoadSign should be aged
     */
    public boolean age(long ms) {
        life -= ms;
        return alive();
    }

    /**
     * Increases the life of this RoadSign with the given amount of milliseconds. Doesn't let the lifetime exceed the
     * max lifetime.
     */
    public void revitalize(long ms) {
        life = Math.min(getMaxLifeTime(), getRemainingLifeTime() + ms);
    }

    /**
     * Increases the life of this RoadSign to its max amount of milliseconds
     */
    public void revitalize() {
        revitalize(getMaxLifeTime());
    }

}
