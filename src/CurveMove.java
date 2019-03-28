import com.illposed.osc.OSCMessage;

/*
Created: 3/28/2019
Author: Dustin Andree
Description: a class that can be used to generate an OSCMessage for a curve move to izzy. The added points use x,y
             coordinates to calculate the relative angle and distance for each point. size and total distance is
             automatically tracked
Assumptions:
    at least two points must be added in order to generate the message (checked in generateMessage)
    two points should not cause a turn greater than 180 degrees (unchecked)

Known Errors:
    small truncation errors when calculating angle and distance to points since they must be integers, error may cascade


 */

import java.util.List;

public class CurveMove {

    private int size;
    private int totalDistance;
    private List<CurvePoint> points;

    CurveMove(){
        this.size = 0;
        this.totalDistance = 0;
    }
    private class CurvePoint {
        private double x;
        private double y;
        private int angle;
        private int distance;

        CurvePoint(double x, double y) {
            this.x = x;
            this.y = y;
            this.angle = 0; //absolute angle of point
            this.distance = 0; //distance to next point

        }

        void calcMove() {
            CurvePoint nextPoint = points.get(points.indexOf(this) + 1);
            double diffX = this.x - nextPoint.x;
            double diffY = this.y - nextPoint.y;

            this.distance = (int) Math.sqrt(diffX * diffX + diffY * diffY);
            if (diffY == 0) {
                this.angle = 90;
            } else {
                this.angle = (int) Math.tan(diffX / diffY);
            }

            if (diffY < 0 && diffX < 0) {
                this.angle = 180 + this.angle;
            } else if (diffY < 0) {
                this.angle = 180 - this.angle;
            } else if (diffX < 0) {
                this.angle = 360 - this.angle;
            }


            CurveMove.this.updateDistance(this.distance);
        }

    }

    private void updateDistance(int distance) {
        this.totalDistance += distance;
    }

    public void addPoint(double x, double y) {

        points.add(new CurvePoint(x, y));
        if (this.size != 0) {
            points.get(this.size - 1).calcMove();
        }

        this.size++;
    }

    public OSCMessage generateMessage() {
        OSCMessage outgoingMessage = new OSCMessage();
        int i;
        int tempAngle = this.points.get(0).angle;
        int runningDistance;
        if (this.size < 2) {
            System.out.println("ERROR, CurveMove needs at least 2 points to generate message");
            return outgoingMessage;
        }

        runningDistance = 0;
        outgoingMessage.setAddress("/IZZY/CurveMove");
        outgoingMessage.addArgument(this.totalDistance);
        outgoingMessage.addArgument(this.size);
        //start adding points
        //point 0 which does not have a distance
        outgoingMessage.addArgument(this.points.get(0).angle);
        outgoingMessage.addArgument(0);


        for (i = 1; i < this.size - 1; i++) {
            tempAngle = this.points.get(i).angle;
            tempAngle = tempAngle - this.points.get(i - 1).angle; // turn absolute angle into relative angle

            if (tempAngle > 180) {
                tempAngle = tempAngle - 360; //correct for left turns over the 0 degree mark
            }
            else if(tempAngle < -180) {
                tempAngle = 360 + tempAngle; // correct for right turn over the 0 degree mark
            }
            outgoingMessage.addArgument(tempAngle);

            runningDistance = runningDistance + this.points.get(i - 1).distance;
            outgoingMessage.addArgument(runningDistance); // add overall distance along move
        }

        //add last point which does not have a turn
        outgoingMessage.addArgument(0);
        outgoingMessage.addArgument(this.totalDistance);


        return outgoingMessage;
    }

}
