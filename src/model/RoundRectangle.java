package model;

import javafx.css.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class RoundRectangle extends MyRectangle {
    private Rectangle rectangle;

    public RoundRectangle(double x, double y, int id) {
        this(x, y, 100, 50, id);
    }

    public RoundRectangle(double x, double y, double width, double height, int id) {
        super(x, y, width, height, id);
        rectangle = new Rectangle();
        setMyShape(rectangle);
        setShape();
    }

    public void setShape() {
        rectangle.setWidth(2 * width);
        rectangle.setHeight(2 * height);
        rectangle.setX(x - width);
        rectangle.setY(y - height);
        rectangle.setStyle("-fx-arc-height: 50;-fx-arc-width: 50;");
    }
}
