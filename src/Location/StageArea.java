package Location;

public class StageArea {
    final double INCHES_TO_MICRONS = 25400;
    double widthInInches;
    double widthInFeet;
    double upstageDepthInInches;
    double upstageDepthInFeet;
    double downstageDepthInInches;
    double downstageDepthInFeet;
    double pixelRatio;

    public StageArea() {
    }

    public double getWidthInInches() {
        return widthInInches;
    }

    public void setWidthInInches(double widthInInches) {
        this.widthInInches = widthInInches;
    }

    public double getWidthInFeet() {
        return widthInFeet;
    }

    public void setWidthInFeet(double widthInFeet) {
        this.widthInFeet = widthInFeet;
    }

    public double getUpstageDepthInInches() {
        return upstageDepthInInches;
    }

    public void setUpstageDepthInInches(double upstageDepthInInches) {
        this.upstageDepthInInches = upstageDepthInInches;
    }

    public double getUpstageDepthInFeet() {
        return upstageDepthInFeet;
    }

    public void setUpstageDepthInFeet(double upstageDepthInFeet) {
        this.upstageDepthInFeet = upstageDepthInFeet;
    }

    public double getDownstageDepthInInches() {
        return downstageDepthInInches;
    }

    public void setDownstageDepthInInches(double downstageDepthInInches) {
        this.downstageDepthInInches = downstageDepthInInches;
    }

    public double getDownstageDepthInFeet() {
        return downstageDepthInFeet;
    }

    public void setDownstageDepthInFeet(double downstageDepthInFeet) {
        this.downstageDepthInFeet = downstageDepthInFeet;
    }

    public int getWidthInMicrons() {
        return (int)((widthInInches + (widthInFeet * 12)) * INCHES_TO_MICRONS);
    }

    public int getUpstageDepthInMicrons() {
        return (int)((upstageDepthInInches + (upstageDepthInFeet * 12)) * INCHES_TO_MICRONS);
    }

    public int getDownstageDepthInMicrons() {
        return (int)((downstageDepthInInches + (downstageDepthInFeet * 12)) * INCHES_TO_MICRONS);
    }

    public void updatePixelRatio() {
        if((getWidthInMicrons() / 600) <= ((getDownstageDepthInMicrons() + getUpstageDepthInMicrons()) / 400)) {
            pixelRatio = getWidthInMicrons() / 600;
        } else {
            pixelRatio = (getDownstageDepthInMicrons() + getUpstageDepthInMicrons()) / 400;
        }
    }

    public double getPixelRatio() {
        return pixelRatio;
    }
}
