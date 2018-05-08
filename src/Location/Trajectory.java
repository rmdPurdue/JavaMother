package Location;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Trajectory {
    private Position startPosition;
    private Position endPosition;
    private Line trajectory;

    public Trajectory(Position startPosition, Position endPosition, double pixelRatio) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.trajectory = new Line();
        this.trajectory.setStartX((this.startPosition.getX().getPosition() / pixelRatio) + this.startPosition.getTranslationX());
        this.trajectory.setStartY(this.startPosition.getTranslationY() - (this.startPosition.getY().getPosition() / pixelRatio));
        this.trajectory.setEndX((this.endPosition.getX().getPosition() / pixelRatio) + this.startPosition.getTranslationX());
        this.trajectory.setEndY(this.startPosition.getTranslationY() - (this.endPosition.getY().getPosition() / pixelRatio));
        this.trajectory.setStroke(Color.GREEN);
    }

    public void updateTrajectory(Position startPosition, Position endPosition, double pixelRatio) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.trajectory.setStartX((this.startPosition.getX().getPosition() / pixelRatio) + this.startPosition.getTranslationX());
        this.trajectory.setStartY(this.startPosition.getTranslationY() - (this.startPosition.getY().getPosition() / pixelRatio));
        this.trajectory.setEndX((this.endPosition.getX().getPosition() / pixelRatio) + this.startPosition.getTranslationX());
        this.trajectory.setEndY(this.startPosition.getTranslationY() - (this.endPosition.getY().getPosition() / pixelRatio));
        this.trajectory.setStroke(Color.GREEN);
    }

    public Line getTrajectoryLine() {
        return this.trajectory;
    }
}
