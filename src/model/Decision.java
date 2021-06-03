package model;

import javafx.scene.shape.Polygon;

public class Decision extends MyPolygon {
    public Decision(double x, double y, int id) {
        super(x, y, 100, 50);
        this.factoryID = id;
        setShape();
    }

    @Override
    public void setShape() {
        double upLeftX = this.x;
        double upLeftY = this.y - height;
        double upRightX = this.x + width;
        double upRightY = this.y;
        double downLeftX = this.x - width;
        double downLeftY = this.y;
        double downRightX = this.x;
        double downRightY = this.y + height;
        Double[] list = {upLeftX, upLeftY, upRightX, upRightY, downRightX, downRightY, downLeftX, downLeftY};
        polygon.getPoints().setAll(list);
    }

}
