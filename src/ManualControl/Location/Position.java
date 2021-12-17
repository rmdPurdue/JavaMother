package ManualControl.Location;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class Position {

    private Axis x = new Axis();
    private Axis y = new Axis();
    private Axis z = new Axis();
    private SimpleBooleanProperty updated = new SimpleBooleanProperty(false);
    private Group marker;
    private Circle position;
    private Polygon heading;
    private int translationX = 0;
    private int translationY = 0;

    public Position(Color color) {
        this.marker = new Group();
        this.position = new Circle(0,0,4, color);
        this.heading = new Polygon();
        this.heading.getPoints().addAll(20.0, -8.0, 20.0, 8.0, 0.0, 0.0);
        this.heading.setFill(color);
        this.heading.setOpacity(0.25);
        this.marker.getChildren().addAll(position, heading);
        this.marker.getTransforms().add(new Rotate(x.getAngularPosition(), 0,0));
    }

    public Axis getX() {
        return x;
    }

    public void setX(Axis x) {
        this.x = x;
    }

    public Axis getY() {
        return y;
    }

    public void setY(Axis y) {
        this.y = y;
    }

    public Axis getZ() {
        return z;
    }

    public void setZ(Axis z) {
        this.z = z;
    }

    public boolean isUpdated() {
        return updated.get();
    }

    public SimpleBooleanProperty updatedProperty() {
        return updated;
    }

    public void setTranslation(int x, int y) {
        this.translationX = x;
        this.translationY = y;
        this.position.setTranslateX(x);
        this.heading.setTranslateX(x);
        this.position.setTranslateY(y);
        this.heading.setTranslateY(y);
    }

    public int getTranslationX() {
        return translationX;
    }

    public int getTranslationY() {
        return translationY;
    }

    public void setUpdated(boolean updated) {
        this.updated.set(updated);
    }


    public Group updatePosition(double pixelRatio) {

        this.marker.setRotate(getX().getAngularPosition() / 1000);
        double translationHypotenuse = Math.sin(Math.toRadians(getX().getAngularPosition() / (1000 * 2.0))) * 16.0;
        double xRotationTranslation = - Math.sin(Math.toRadians(getX().getAngularPosition() / (1000 * 2.0))) * translationHypotenuse;
        double yRotationTranslation = Math.cos(Math.toRadians(getX().getAngularPosition() / (1000 * 2.0))) * translationHypotenuse;

        this.position.setTranslateX((getX().getPosition() / pixelRatio) + xRotationTranslation + translationX);
        this.heading.setTranslateX((getX().getPosition() / pixelRatio) + xRotationTranslation + translationX);
        this.position.setTranslateY(yRotationTranslation + translationY - (getY().getPosition() / pixelRatio));
        this.heading.setTranslateY(yRotationTranslation + translationY - (getY().getPosition() / pixelRatio));

        return marker;
    }

    public Group getMarker() {
        return marker;
    }
}
