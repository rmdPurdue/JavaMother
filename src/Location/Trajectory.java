package Location;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.function.Function;

import static Location.SimpsonsRule.integrate;

public class Trajectory {
    private Position startPosition;
    private Position endPosition;
    private Line trajectory;
    private Path trajectoryPath;
    private double totalTime;
    private double totalDistance;
    private double accelerationTime;
    private double decelerationTime;
    private double startVelocity;
    private double endVelocity;
    private double maxVelocity;
    private double accelerationRate;
    private double finalAccelerationPosition;
    private double decelerationRate;
    private double initialDecelerationPosition;
    private double aX;
    private double aY;
    private double bX;
    private double bY;
    private double startVelocityX;
    private double endVelocityX;
    private double startVelocityY;
    private double endVelocityY;

    public Trajectory(Position startPosition, Position endPosition, double pixelRatio) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.trajectory = new Line();
        this.trajectoryPath = new Path();
        this.trajectory.setStartX((this.startPosition.getX().getPosition() / pixelRatio) + this.startPosition.getTranslationX());
        this.trajectory.setStartY(this.startPosition.getTranslationY() - (this.startPosition.getY().getPosition() / pixelRatio));
        this.trajectory.setEndX((this.endPosition.getX().getPosition() / pixelRatio) + this.startPosition.getTranslationX());
        this.trajectory.setEndY(this.startPosition.getTranslationY() - (this.endPosition.getY().getPosition() / pixelRatio));
        this.trajectory.setStroke(Color.GREEN);
    }

    public void updateTrajectory(Position startPosition, Position endPosition, double pixelRatio, double totalTime,
                                 double accelerationTime, double decelerationTime) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.trajectory.setStartX((this.startPosition.getX().getPosition() / pixelRatio) + this.startPosition.getTranslationX());
        this.trajectory.setStartY(this.startPosition.getTranslationY() - (this.startPosition.getY().getPosition() / pixelRatio));
        this.trajectory.setEndX((this.endPosition.getX().getPosition() / pixelRatio) + this.startPosition.getTranslationX());
        this.trajectory.setEndY(this.startPosition.getTranslationY() - (this.endPosition.getY().getPosition() / pixelRatio));
        this.totalTime = totalTime;
        this.accelerationTime = accelerationTime;
        this.decelerationTime = decelerationTime;
        this.trajectory.setStroke(Color.GREEN);

        // TODO: Y position should be in the other direction.
        // TODO: Initial angular position is incorrect.

        calculateMotionProfileCubic();

        Function<Double, Double> fX = x -> 1/6 * aX * Math.pow(x, 2) + bX * x + startVelocityX;
        Function<Double, Double> fY = x -> 1/6 * aY * Math.pow(x, 2) + bY * x + startVelocityY;
        trajectoryPath.setStroke(Color.ORANGE);
        trajectoryPath.setStrokeWidth(2);

        double time = 0;
        double resultX = fX.apply(time);
        double resultY = fY.apply(time);

        trajectoryPath.getElements().add(new MoveTo(resultX / pixelRatio, resultY / pixelRatio));
        System.out.println(resultX / pixelRatio + ", " + resultY / pixelRatio);

        time += 0.1;
        while(time < totalTime) {
            resultX = fX.apply(time);
            resultY = fY.apply(time);
            trajectoryPath.getElements().add(new LineTo(resultX / pixelRatio, resultY / pixelRatio));
            System.out.println(resultX / pixelRatio + ", " + resultY / pixelRatio);
            time += 0.1;
        }

    }

    public Line getTrajectoryLine() {
        return this.trajectory;
    }

    public Path getTrajectory(double pixelRatio) {

        return trajectoryPath;
    }

    public void calculateMotionProfile() {
        int startX = this.startPosition.getX().getPosition();
        int startY = this.startPosition.getY().getPosition();
        int endX = this.endPosition.getX().getPosition();
        int endY = this.endPosition.getY().getPosition();
        int startVelocityX = this.startPosition.getX().getVelocity();
        int startVelocityY = this.startPosition.getY().getVelocity();
        int endVelocityX = this.endPosition.getX().getVelocity();
        int endVelocityY = this.endPosition.getY().getVelocity();
        int startAccelerationX = this.startPosition.getX().getAcceleration();
        int startAccelerationY = this.startPosition.getY().getAcceleration();
        int endAccelerationX = this.endPosition.getX().getAcceleration();
        int endAccelerationY = this.endPosition.getY().getAcceleration();

        this.totalDistance = Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2));

        this.maxVelocity = totalDistance / ((accelerationTime / 2) + (decelerationTime / 2) + (totalTime - (accelerationTime + decelerationTime)));
        this.startVelocity = Math.sqrt(Math.pow(this.startPosition.getX().getVelocity(), 2) + Math.pow(this.startPosition.getY().getVelocity(), 2));
        this.endVelocity = Math.sqrt(Math.pow(this.endPosition.getX().getVelocity(), 2) + Math.pow(this.endPosition.getY().getVelocity(), 2));

        this.accelerationRate = (maxVelocity - startVelocity) / accelerationTime;
        this.finalAccelerationPosition = (Math.pow(maxVelocity, 2) - Math.pow(startVelocity, 2)) / (2 * accelerationRate);

        this.decelerationRate = (maxVelocity - endVelocity) / decelerationTime;
        this.initialDecelerationPosition = totalDistance - (Math.pow(endVelocity, 2) - Math.pow(maxVelocity, 2)) / (2 * decelerationRate);

        System.out.println(this.maxVelocity + " microns/second");
        System.out.println(this.maxVelocity / 25400 + " inches/second");
        System.out.println(this.accelerationRate + " microns/second^2");
        System.out.println(this.accelerationRate / 25400 + " inches/second^2");
        System.out.println(this.finalAccelerationPosition / 25400 + " inches");
        System.out.println(this.decelerationRate + " inches/second^2");
        System.out.println(this.decelerationRate / 25400 + " inches/second^2");
        System.out.println(this.initialDecelerationPosition / 25400 + " inches");

    }

    public void calculateMotionProfileCubic() {
        int startX = this.startPosition.getX().getPosition();
        int startY = this.startPosition.getY().getPosition();
        int endX = this.endPosition.getX().getPosition();
        int endY = this.endPosition.getY().getPosition();

        int startAngle = this.startPosition.getX().getAngularPosition();
        int startAngleDegrees = startAngle / 1000;
        double startAngleRadians = Math.toRadians(startAngleDegrees);
        int endAngle = this.endPosition.getX().getAngularPosition();
        int endAngleDegrees = endAngle / 1000;
        double endAngleRadians = Math.toRadians(endAngleDegrees);

        double startVelocity = 0;
        this.startVelocityX = startVelocity * (Math.cos(startAngleRadians));
        this.startVelocityY = startVelocity * (Math.sin(startAngleRadians));

        double endVelocity = 0;
        this.endVelocityX = endVelocity * (Math.cos(endAngleRadians));
        this.endVelocityY = endVelocity * (Math.sin(endAngleRadians));

        double startTime = 0;
        double endTime = startTime + this.totalTime;

        this.aX = 6 * ((endVelocityX + startVelocityX) * (endTime - startTime) - (2 * (endX - startX))) / (Math.pow(endTime - startTime, 3));
        this.bX = -2 * ((endVelocityX + 2 * startVelocityX) * (endTime - startTime) - (3 * (endX - startX))) / Math.pow(endTime - startTime, 2);

        this.aY = 6 * ((endVelocityY + startVelocityY) * (endTime - startTime) - (2 * (endY - startY))) / (Math.pow(endTime - startTime, 3));
        this.bY = -2 * ((endVelocityY + 2 * startVelocityY) * (endTime - startTime) - (3 * (endY - startY))) / Math.pow(endTime - startTime, 2);

        System.out.println(aX + ", " + bX);
        System.out.println(aY + ", " + bY);
        System.out.println("------------------");

        // from these, we can calculate any (x, y) position and (vX, vY) velocity based on time t:
        // vX(t) = 1/2 aX (t - t0)^2 + bX (t - t0) + vX0
        // vY(t) = 1/2 aY (t - t0)^2 + bY (t - t0) + vY0
        // x(t) = 1/6 aX (t - t0)^3 + 1/2 bX (t - t0)^2 + vX0 (t - t0) + x0
        // y(t) = 1/6 aY (t - t0)^3 + 1/2 bY (t - t0)^2 + vY0 (t - t0) + y0

        // we can calculate tangential linear speed (s) based on time t:
        // s(t) = sqrt(vX(t) ^ 2 + vY(t) ^2 )

        // we can calculate heading (theta) based on time t:
        // theta(t) = arctan(vX(t) / vY(t))

        // we can calculate angular velocity (omega) based on time t:
        // omega(t) = ((ay t + bY) vX(t) - (aX t + bX) vY(t)) / (vX(t)^2 + vY(t)^2)

        double arcLength = integrate(startTime, endTime, aX, bX, startVelocityX, aY, bY, startVelocityY, totalTime);

    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public double getAccelerationRate() {

        return accelerationRate;
    }

    public double getFinalAccelerationPosition() {
        return finalAccelerationPosition;
    }

    public double getDecelerationRate() {
        return decelerationRate;
    }

    public double getInitialDecelerationPosition() {
        return initialDecelerationPosition;
    }

    public double getTotalTime() {

        return totalTime;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getAccelerationTime() {
        return accelerationTime;
    }

    public double getDecelerationTime() {
        return decelerationTime;
    }
}
