package Location;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
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
        this.heading.getPoints().addAll(-10.0, -15.0, 10.0, -15.0, 0.0, 0.0, 0.0, 0.0);
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
        /*
        double xx = this.marker.getLocalToSceneTransform().getMxx();
        double xy = this.marker.getLocalToSceneTransform().getMxy();
        double angle = Math.atan2(-xy, xx);
        angle = Math.toDegrees(angle);
        angle = angle < 0 ? angle + 360 : angle;
*/

        this.position.setTranslateX((getX().getPosition() / pixelRatio) + translationX);
        this.heading.setTranslateX((getX().getPosition() / pixelRatio) + translationX);
        this.position.setTranslateY(translationY - (getY().getPosition() / pixelRatio));
        this.heading.setTranslateY(translationY - (getY().getPosition() / pixelRatio));
/*
        this.marker.getTransforms().add(new Rotate(
                (getX().getAngularPosition() / 1000) - angle,
                (getX().getPosition() / pixelRatio) + translationX,
                translationY - (getY().getPosition() / pixelRatio)));
                */
        this.marker.setRotate(getX().getAngularPosition() / 1000);
        this.position.setTranslateX((Math.sin(Math.toRadians(getX().getAngularPosition() / 1000)) * 5.5) + translationX);
        this.heading.setTranslateX((Math.sin(Math.toRadians(getX().getAngularPosition() / 1000)) * 5.5) + translationX);
        this.position.setTranslateY(Math.ceil((Math.cos(Math.toRadians(getX().getAngularPosition() / (1000 * 2))) * 5.) + translationY));
        this.heading.setTranslateY(Math.ceil((Math.cos(Math.toRadians(getX().getAngularPosition() / (1000 * 2))) * 5.5) + translationY));
        System.out.println((Math.sin(Math.toRadians(getX().getAngularPosition() / 1000)) * 5.5) + translationX);
        System.out.println(Math.ceil((Math.cos(Math.toRadians(getX().getAngularPosition() / (1000 * 2))) * 5.5) + translationY));

        return marker;
    }

    public Group getMarker() {
        return marker;
    }
}
