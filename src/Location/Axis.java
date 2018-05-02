package Location;

public class Axis {
    private int position; // in microns

    private int angularPosition; // in millidegrees

    private int velocity; // in microns / microsecond

    private int angularVelocity; // in millidegrees / microsecond

    private int acceleration; // in microns / microsecond^2

    private int angularAcceleration; // in millidegrees / microsecond^2

    public Axis() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAngularPosition() {
        return angularPosition;
    }

    public void setAngularPosition(int angularPosition) {
        this.angularPosition = angularPosition;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(int angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public int getAngularAcceleration() {
        return angularAcceleration;
    }

    public void setAngularAcceleration(int angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }
}
