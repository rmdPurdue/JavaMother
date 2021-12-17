package ManualControl.Location;

import javafx.beans.property.SimpleIntegerProperty;

public class Axis {

    private SimpleIntegerProperty position = new SimpleIntegerProperty(); // in microns

    private SimpleIntegerProperty angularPosition = new SimpleIntegerProperty(); // in millidegrees

    private SimpleIntegerProperty velocity = new SimpleIntegerProperty(); // in microns / microsecond

    private SimpleIntegerProperty angularVelocity = new SimpleIntegerProperty(); // in millidegrees / microsecond

    private SimpleIntegerProperty acceleration = new SimpleIntegerProperty(); // in microns / microsecond^2

    private SimpleIntegerProperty angularAcceleration = new SimpleIntegerProperty(); // in millidegrees / microsecond^2

    public Axis() {
    }

    public final int getPosition() {
        return position.get();
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public SimpleIntegerProperty positionProperty() {
        return position;
    }

    public final int getAngularPosition() {
        return angularPosition.get();
    }

    public void setAngularPosition(int angularPosition) {
        this.angularPosition.set(angularPosition);
    }

    public SimpleIntegerProperty angularPositionProperty() {
        return angularPosition;
    }

    public int getVelocity() {
        return velocity.get();
    }

    public SimpleIntegerProperty velocityProperty() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity.set(velocity);
    }

    public int getAngularVelocity() {
        return angularVelocity.get();
    }

    public SimpleIntegerProperty angularVelocityProperty() {
        return angularVelocity;
    }

    public void setAngularVelocity(int angularVelocity) {
        this.angularVelocity.set(angularVelocity);
    }

    public int getAcceleration() {
        return acceleration.get();
    }

    public SimpleIntegerProperty accelerationProperty() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration.set(acceleration);
    }

    public int getAngularAcceleration() {
        return angularAcceleration.get();
    }

    public SimpleIntegerProperty angularAccelerationProperty() {
        return angularAcceleration;
    }

    public void setAngularAcceleration(int angularAcceleration) {
        this.angularAcceleration.set(angularAcceleration);
    }
}
