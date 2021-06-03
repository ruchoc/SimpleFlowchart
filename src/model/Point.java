package model;

import javafx.scene.shape.Circle;

public class Point extends Circle {
    private int posid;
    private int directionX;
    private int directionY;
    private int leftX;
    private int leftY;

    public Point(double x, double y, double radius) {
        super(x, y, radius);
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getLeftY() {
        return leftY;
    }

    public void setLeftY(int leftY) {
        this.leftY = leftY;
    }

    public int getPosid() {
        return posid;

    }

    public void setPosid(int posid) {
        this.posid = posid;
    }

    public int getDirectionX() {
        return directionX;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public void setDirectionY(int directionY) {
        this.directionY = directionY;
    }
}
