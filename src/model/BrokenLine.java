package model;

import java.util.ArrayList;

import controller.DrawController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class BrokenLine extends MyLine {
    private Line xLine;
    private Line yLine;

    public BrokenLine(double startX, double startY, double endX, double endY, int factoryID) {
        this(startX, startY, endX, endY);
        this.factoryID = factoryID;
    }

    public BrokenLine(double startX, double startY, double endX, double endY) {
        middlePoints = new ArrayList<>();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.booleanProperty = new SimpleBooleanProperty(false);
        super.line = new Line();
        xLine = new Line(startX, startY, endX, startY);
        yLine = new Line(endX, startY, endX, endY);
        xLine.setStrokeWidth(3);
        yLine.setStrokeWidth(3);
        circle = new Circle();
        circle.setCenterX(startX);
        circle.setCenterY(startY);
        circle.setRadius(5);
        triangle = new Polygon();
        text = new Text();
        setShape();
        this.isSelected = false;
        super.startListening();
        addLineListening();
    }

    public void delete() {
        drawingArea.getChildren().remove(xLine);
        drawingArea.getChildren().remove(yLine);
        drawingArea.getChildren().remove(circle);
        drawingArea.getChildren().remove(triangle);
        drawingArea.getChildren().remove(text);
    }

    @Override
    public void setShape() {

        double dx = 0;
        double dy = endY - startY;
        double k = 1 / Math.sqrt(dx * dx + dy * dy);
        double u = (double) Math.sqrt(3) * StandardNum.TRIANGLE_LEN / (Math.sqrt(dx * dx + dy * dy));
        double v = (double) StandardNum.TRIANGLE_LEN / (Math.sqrt(dx * dx + dy * dy));
        double mX = endX - u * dx;
        double mY = endY - u * dy;

        double aX = v * dy + mX;
        double aY = v * (-1 * dx) + mY;

        double bX = mX - v * dy;
        double bY = mY - v * (-1 * dx);
        double endX = this.endX + 5 * k * dx;
        double endY = this.endY + 5 * k * dy;
        Double[] list = {aX, aY, endX, endY, bX, bY};
        triangle.getPoints().setAll(list);
        circle.setCenterX(startX);
        circle.setCenterY(startY);
        xLine.setStartX(startX);
        xLine.setStartY(startY);
        xLine.setEndX(this.endX);
        xLine.setEndY(startY);
        yLine.setStartX(this.endX);
        yLine.setStartY(startY);
        yLine.setEndX(this.endX);
        yLine.setEndY(this.endY);
        middlePoints.clear();
        text.setX((startX + endX) / 2);
        text.setY(startY);
        Circle tempCircle = new Circle((startX + endX) / 2, startY, StandardNum.DRAW_POINTS_RADIUS);
        tempCircle.setStrokeWidth(5);
        tempCircle.setVisible(false);
        middlePoints.add(tempCircle);
        tempCircle = new Circle(endX, (startY + endY) / 2, StandardNum.DRAW_POINTS_RADIUS);
        tempCircle.setStrokeWidth(5);
        tempCircle.setVisible(false);
        middlePoints.add(tempCircle);
        isSelected = true;
        booleanProperty.setValue(true);
        if (drawController != null && drawController.getPropertyController() != null) {
            drawController.getPropertyController().setWorkShape(this);
            drawController.getPropertyController().update();
        }
    }

    @Override
    public void getPane(AnchorPane drawingArea, DrawController drawController) {
        drawingArea.getChildren().addAll(middlePoints);
        drawingArea.getChildren().add(xLine);
        drawingArea.getChildren().add(yLine);
        drawingArea.getChildren().add(circle);
        drawingArea.getChildren().add(triangle);
        drawingArea.getChildren().add(text);
        this.drawingArea = drawingArea;
        this.drawController = drawController;
    }

    @Override
    public void setToTop() {
        drawingArea.getChildren().removeAll(middlePoints);
        drawingArea.getChildren().remove(xLine);
        drawingArea.getChildren().remove(yLine);
        drawingArea.getChildren().remove(circle);
        drawingArea.getChildren().remove(triangle);
        drawingArea.getChildren().remove(text);
        getPane(drawingArea, drawController);
    }

    public void addLineListening() {
        xLine.setCursor(Cursor.HAND);
        yLine.setCursor(Cursor.HAND);
        xLine.setOnMouseEntered(e -> {
            if (!isOnTheLine) {
                lastX = e.getX();
                lastY = e.getY();
                isOnTheLine = true;
            }
        });
        xLine.setOnMouseExited(e -> {
            isOnTheLine = false;
        });
        xLine.setOnMouseDragged(e -> {
            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            lastX = e.getX();
            lastY = e.getY();
            move(dx, dy);
        });
        xLine.setOnMouseReleased(e -> {
            this.setToTop();
            if (headLinkShape != null) headLinkShape.delConnectionInfo(this);
            if (tailLinkShape != null) tailLinkShape.delConnectionInfo(this);
            booleanProperty.setValue(false);
        });


        yLine.setOnMouseEntered(e -> {
            if (!isOnTheLine) {
                lastX = e.getX();
                lastY = e.getY();
                isOnTheLine = true;
            }
        });
        yLine.setOnMouseExited(e -> {
            isOnTheLine = false;
        });
        yLine.setOnMouseDragged(e -> {
            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            lastX = e.getX();
            lastY = e.getY();
            move(dx, dy);
        });
        yLine.setOnMouseReleased(e -> {
            this.setToTop();
            if (headLinkShape != null) headLinkShape.delConnectionInfo(this);
            if (tailLinkShape != null) tailLinkShape.delConnectionInfo(this);
            booleanProperty.setValue(false);
        });
    }
}
