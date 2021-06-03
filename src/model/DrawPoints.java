package model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class DrawPoints {
    private Double[] list;
    private Circle[] points;

    public DrawPoints() {
        points = new Circle[4];
        for (int i = 0; i < 4; i++) {
            points[i] = new Circle();
            points[i].setRadius(StandardNum.DRAW_POINTS_RADIUS);
        }
    }

    public void updataLocation(double leftMidX, double leftMidY, double upMidX, double upMidY,
                               double rightMidX, double rightMidY, double downMidX, double downMidY) {
        Double[] list = {leftMidX, leftMidY, upMidX, upMidY, rightMidX, rightMidY, downMidX, downMidY};
        this.list = list;
        for (int i = 0, j = 0; i < 4; i++) {
            points[i].setCenterX(list[j++]);
            points[i].setCenterY(list[j++]);
        }
    }

    public void delPoint(Pane pane) {
        pane.getChildren().removeAll(points);
    }

    public Circle[] getCircles() {
        return points;
    }

    public void setAllVisiable(boolean ready) {
        for (int i = 0; i < points.length; i++) {
            points[i].setVisible(ready);
        }
    }
}
