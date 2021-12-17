package ManualControl.Location;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.function.Function;

import static ManualControl.Location.SimpsonsRule.integrate;

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

        drawCurve(pixelRatio);

//        calculateMotionProfileCubic();

        Function<Double, Double> fX = x -> 1/6 * aX * Math.pow(x, 2) + bX * x + startVelocityX;
        Function<Double, Double> fY = x -> 1/6 * aY * Math.pow(x, 2) + bY * x + startVelocityY;
        trajectoryPath.setStroke(Color.ORANGE);
        trajectoryPath.setStrokeWidth(2);

        /*
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
*/
    }

    public Line getTrajectoryLine() {
        return this.trajectory;
    }

    public Path getTrajectory(double pixelRatio) {

        return trajectoryPath;
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
/*
        System.out.println(aX + ", " + bX);
        System.out.println(aY + ", " + bY);
        System.out.println("------------------");
*/
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

    private void drawCurve(double pixelRatio) {

        double startXinPixels = this.startPosition.getX().getPosition() / pixelRatio;
        double finalXinPixels = this.endPosition.getX().getPosition() / pixelRatio;
        double startYinPixels = this.startPosition.getY().getPosition() / pixelRatio;
        double finalYinPixels = this.endPosition.getY().getPosition() / pixelRatio;
        double startXinPixelsTranslated = startXinPixels + this.startPosition.getTranslationX();
        double finalXinPixelsTranslation = finalXinPixels + this.endPosition.getTranslationX();
        double startYinPixelsTranslation = this.startPosition.getTranslationY() - startYinPixels;
        double finalYinPixelsTranslation = this.endPosition.getTranslationY() - finalYinPixels;

        MoveTo moveTo = new MoveTo();
        moveTo.setX(startXinPixelsTranslated);
        moveTo.setY(startYinPixelsTranslation);

        CubicCurveTo curveTo = new CubicCurveTo();

        curveTo.setX(finalXinPixelsTranslation);
        curveTo.setY(finalYinPixelsTranslation);

        double startAngle = this.startPosition.getX().getAngularPosition() / 1000;
        double finalAngle = this.endPosition.getX().getAngularPosition() / 1000;

        double length = Math.hypot(finalXinPixelsTranslation - startXinPixelsTranslated, finalYinPixelsTranslation - startYinPixelsTranslation);

        // TODO: For some reason, the tangent of the curve isn't matching the angle at the end of the trajectory.

        double ax1 = (Math.cos(Math.toRadians(startAngle)) * (finalXinPixels - startXinPixels)) + this.startPosition.getTranslationX();
        double ay1 = this.startPosition.getTranslationY() + (Math.sin(Math.toRadians(startAngle)) * (finalYinPixels - startYinPixels));

        double ax2 = (Math.cos(Math.toRadians(finalAngle + 90)) * (finalXinPixels - startXinPixels)) + this.endPosition.getTranslationX();
        double ay2 = this.startPosition.getTranslationY() - (Math.sin(Math.toRadians(finalAngle + 90)) * (finalYinPixels - startYinPixels));

        System.out.println(length);
        System.out.println(finalXinPixelsTranslation + ", " + startXinPixelsTranslated);
        System.out.println(finalYinPixelsTranslation + ", " + startYinPixelsTranslation);
        System.out.println(finalXinPixelsTranslation - startXinPixelsTranslated);
        System.out.println(finalYinPixelsTranslation - startYinPixelsTranslation);
        System.out.println(this.startPosition.getTranslationX() + ", " + this.startPosition.getTranslationY());
        System.out.println("(" + ax1 + ", " + ay1 + ")");
        System.out.println("(" + ax2 + ", " + ay2 + ")");

        curveTo.setControlX1(ax1); // translationX + finalXinPixelsTranslation
        curveTo.setControlY1(ay1); // translationY
        curveTo.setControlX2(ax2); // translationX + finalXinPixelsTranslation
        curveTo.setControlY2(ay2); // translationY - finalYinPixelsTranslation

        // The above is because of the orientation of the start and final angles:
        // initial angle is 0 degrees, or EAST, which means the first control point is dragged EAST
        // the final angle is 90 degrees, or NORTH, which means the second control point is either dragged SOUTH, or not at all.
        // The ratio of the lengths of the two control points determine the magnitude of the curve.
        // The total length of either control point cannot exceed the linear translation in that direction.

        this.trajectoryPath.getElements().clear();
        this.trajectoryPath.getElements().add(moveTo);
        this.trajectoryPath.getElements().add(curveTo);
    }

    public void setTrajectoryControlPoints(double x1, double y1, double x2, double y2) {
        System.out.println(this.trajectoryPath.getElements().get(1).getClass());
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
