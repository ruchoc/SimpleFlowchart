package model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * 编辑框类
 */
public class Editer {
    private Point[] circles;
    private double x;
    private double y;
    private double width;
    private double height;
    private Line[] lines;

    public Point[] getCircles() {
        return this.circles;
    }

    public Editer(double x, double y, double height, double width) {
        this.x = x;
        this.y = y;
        this.height = height + 10;
        this.width = width + 10;
        initLine(x, y);
        initCicrle(x, y);
        disapperCircle();
        disapperLine();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private void initCicrle(double x, double y) {
        circles = new Point[9];

        for (int i = 0; i < 9; i++) {
            circles[i] = new Point(x, y, 5);
            circles[i].setFill(Color.BLUE);
        }
    }

    private void initLine(double x, double y) {
        lines = new Line[8];
        for (int i = 0; i < 8; ++i) {
            lines[i] = new Line(x, y, x, y);
        }
    }

    private void lineMove(double x, double y) {
        x = x - width;
        y = y - height;
        double pos[][] = {{x, y, x + width, y}, {x + width, y, x + 2 * width, y},
                {x, y, x, y + height}, {x, y + height, x, y + 2 * height},
                {x, y + 2 * height, x + width, y + 2 * height},
                {x + width, y + 2 * height, x + 2 * width, y + 2 * height},
                {x + 2 * width, y, x + 2 * width, y + height},
                {x + 2 * width, y + height, x + 2 * width, y + 2 * height}};
        for (int i = 0; i < 8; i++) {
            lines[i].setStartX(pos[i][0]);
            lines[i].setStartY(pos[i][1]);
            lines[i].setEndX(pos[i][2]);
            lines[i].setEndY(pos[i][3]);
        }
    }

    public void addEditer(Pane pane) {
        pane.getChildren().addAll(circles);
        pane.getChildren().addAll(lines);
    }

    public void delEditer(Pane pane) {
        pane.getChildren().removeAll(circles);
        pane.getChildren().removeAll(lines);
    }

    public void show(double x, double y) {
        lineMove(x, y);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                circles[3 * i + j].setCenterX(x + width * (i - 1));
                circles[3 * i + j].setCenterY(y + height * (j - 1));
                circles[3 * i + j].setRadius(5);
                if (i == 1 && j == 1) {
                    circles[3 * i + j].setRadius(0);
                }
            }
        }

    }

    public void disapperCircle() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                circles[3 * i + j].setRadius(0);
            }
        }
    }

    public void disapperLine() {
        for (int i = 0; i < 8; i++) {
            lines[i].setStartX(0);
            lines[i].setStartY(0);
            lines[i].setEndX(0);
            lines[i].setEndY(0);
        }
    }

    public void disapper() {
        disapperCircle();
        disapperLine();
    }
}
