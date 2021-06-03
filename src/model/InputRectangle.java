package model;

public class InputRectangle extends MyPolygon {

    public InputRectangle(double x, double y, int id) {
        super(x, y, 100, 50);
        this.factoryID = id;
        setShape();
    }

    @Override
    public void setShape() {
        double upLeftX = this.x - width / 2;
        double upLeftY = this.y - height;
        double upRightX = this.x + width;
        double upRightY = this.y - height;
        double downLeftX = this.x - width;
        double downLeftY = this.y + height;
        double downRightX = this.x + width / 2;
        double downRightY = this.y + height;
        Double[] list = {upLeftX, upLeftY, upRightX, upRightY, downRightX, downRightY, downLeftX, downLeftY};
        polygon.getPoints().setAll(list);
    }

    public void createDrawPoints() {
        double leftMidX = this.x - width * 1.5;
        double leftMidY = this.y;
        double upMidX = this.x;
        double upMidY = this.y - height;
        double rightMidX = this.x + width * 1.5;
        double rightMidY = this.y;
        double downMidX = this.x;
        double downMidY = this.y + height;
        drawPoints.updataLocation(leftMidX, leftMidY, upMidX, upMidY, rightMidX, rightMidY, downMidX, downMidY);
    }
}
