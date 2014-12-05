package LiftModel;



/**
 * A data structure
 * Used to store calculated lift traveling schedule
 * @author louis
 * @version 1.1
 */
public class TravelSchedule {
    private float speedUpTime;
    private float speedDownTime;
    private float maxSpeedTime;

    /**
     *
     * @return speed up time
     */
    public float getSpeedUpTime() {
        return speedUpTime;
    }

    /**
     *
     * @param speedUpTime speed up time
     */
    public void setSpeedUpTime(float speedUpTime) {
        this.speedUpTime = speedUpTime;
    }

    /**
     *
     * @return speed down time
     */
    public float getSpeedDownTime() {
        return speedDownTime;
    }

    /**
     *
     * @param speedDownTime speed down time
     */
    public void setSpeedDownTime(float speedDownTime) {
        this.speedDownTime = speedDownTime;
    }

    /**
     *
     * @return maximum speed time
     */
    public float getMaxSpeedTime() {
        return maxSpeedTime;
    }

    /**
     *
     * @param maxSpeedTime maximum speed time
     */
    public void setMaxSpeedTime(float maxSpeedTime) {
        this.maxSpeedTime = maxSpeedTime;
    }
    
    
}
