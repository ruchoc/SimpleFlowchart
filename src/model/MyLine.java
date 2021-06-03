package model;

import java.text.DecimalFormat;
import java.util.ArrayList;

import controller.DrawController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class MyLine extends Line {
    protected int factoryID;
    protected AnchorPane drawingArea;
    protected DrawController drawController;
    protected double startX;
    protected double startY;
    protected double endX;
    protected double endY;
    protected double lastX;
    protected double lastY;

    protected Polygon triangle;
    protected Text text;
    protected Line line;
    protected Circle circle;

    protected MyShape headLinkShape;
    protected MyShape tailLinkShape;
    protected boolean isOnTheLine = false;
    protected boolean isSelected;
    protected BooleanProperty booleanProperty;
    protected ArrayList<Circle> middlePoints;

    public MyLine() {
        this.booleanProperty = new SimpleBooleanProperty(false);
    }

    public MyLine(double startX, double startY, double endX, double endY, int factoryID) {
        this(startX, startY, endX, endY);
        this.factoryID = factoryID;
    }

    public MyLine(double startX, double startY, double endX, double endY) {
        middlePoints = new ArrayList<>();
        line = new Line(startX, startY, endX, endY);
        circle = new Circle();
        this.booleanProperty = new SimpleBooleanProperty(false);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        circle.setCenterX(startX);
        circle.setCenterY(startY);
        circle.setRadius(5);
        line.setStrokeWidth(3);
        triangle = new Polygon();
        text = new Text();
        setShape();
        this.isSelected = false;
        startListening();
    }

    public double getSX() {
        return startX;
    }

    public void setSX(double startX) {
        this.startX = startX;
    }

    public double getSY() {
        return startY;
    }

    public void setSY(double startY) {
        this.startY = startY;
    }

    public double getEX() {
        return endX;
    }

    public void setEX(double endX) {
        this.endX = endX;
    }

    public double getEY() {
        return endY;
    }

    public void setEY(double endY) {
        this.endY = endY;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setHeadLink(MyShape shape) {
        this.headLinkShape = shape;
    }

    public void setTailLink(MyShape shape) {
        this.tailLinkShape = shape;
    }

    public Circle getCircle() {
        return this.circle;
    }

    public Line getLine() {
        return this.line;
    }

    public Polygon getTriangle() {
        return this.triangle;
    }

    public BooleanProperty getBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(BooleanProperty booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public ArrayList<Circle> getMiddlePoints() {
        return middlePoints;
    }

    public void setShape() {
        double dx = endX - startX;
        double dy = endY - startY;
        double k = 1 / Math.sqrt(dx * dx + dy * dy);
        double u = (double) Math.sqrt(3) * StandardNum.TRIANGLE_LEN / (Math.sqrt(dx * dx + dy * dy));
        double v = (double) StandardNum.TRIANGLE_LEN / Math.sqrt(dx * dx + dy * dy);
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
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(this.endX);
        line.setEndY(this.endY);
        isSelected = true;
        booleanProperty.setValue(true);

        middlePoints.clear();
        Circle tempCircle = new Circle((startX + endX) / 2, (startY + endY) / 2, StandardNum.DRAW_POINTS_RADIUS);
        tempCircle.setStrokeWidth(5);
        tempCircle.setVisible(false);
        middlePoints.add(tempCircle);
        text.setX((startX + endX) / 2);
        text.setY((startY + endY) / 2);
        if (drawController != null && drawController.getPropertyController() != null) {
            drawController.getPropertyController().setWorkShape(this);
            drawController.getPropertyController().update();
        }
    }

    public void getPane(AnchorPane drawingArea, DrawController drawController) {
        drawingArea.getChildren().addAll(middlePoints);
        drawingArea.getChildren().add(line);
        drawingArea.getChildren().add(circle);
        drawingArea.getChildren().add(triangle);
        drawingArea.getChildren().add(text);
        this.drawingArea = drawingArea;
        this.drawController = drawController;
    }

    public void setToTop() {
        delete();
        getPane(drawingArea, drawController);
    }

    public void endMove(double x, double y) {
        endX = x;
        endY = y;
        setShape();
    }

    public void move(double dx, double dy) {
        startX = startX + dx;
        startY = startY + dy;
        endX = endX + dx;
        endY = endY + dy;
        setShape();
    }

    public void startMove(double x, double y) {
        startX = x;
        startY = y;
        setShape();
    }

    public void delete() {
        drawingArea.getChildren().removeAll(middlePoints);
        drawingArea.getChildren().remove(line);
        drawingArea.getChildren().remove(circle);
        drawingArea.getChildren().remove(triangle);
        drawingArea.getChildren().remove(text);
    }

    public void changeListener() {
        booleanProperty.addListener(e -> {
            if (booleanProperty.getValue() == false) {
                drawController.getPropertyController().setWorkShape(this);
                drawController.getPropertyController().update();
                drawController.saveChange();
            }
        });
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    protected void startListening() {
        changeListener();
        triangle.setCursor(Cursor.HAND);
        circle.setCursor(Cursor.HAND);
        line.setCursor(Cursor.MOVE);

        triangle.setOnMouseDragged(e -> {
            e.setDragDetect(true);
            drawController.checkDistanceToPoints(e.getX(), e.getY(), this);
            endMove(e.getX(), e.getY());
        });
        triangle.setOnMouseReleased(e -> {
            this.setToTop();
            if (tailLinkShape != null)
                tailLinkShape.delConnectionInfo(this);
            drawController.connect(e.getX(), e.getY(), "end", this);
            booleanProperty.setValue(false);
        });
        circle.setOnMouseDragged(e -> {
            drawController.checkDistanceToPoints(e.getX(), e.getY(), this);
            startMove(e.getX(), e.getY());
        });
        circle.setOnMouseReleased(e -> {
            this.setToTop();
            booleanProperty.setValue(false);
            if (headLinkShape != null)
                headLinkShape.delConnectionInfo(this);
            drawController.connect(e.getX(), e.getY(), "start", this);
        });


        line.setOnMouseEntered(e -> {
            if (!isOnTheLine) {
                lastX = e.getX();
                lastY = e.getY();
                isOnTheLine = true;
            }
        });
        line.setOnMouseExited(e -> {
            isOnTheLine = false;
        });
        line.setOnMouseDragged(e -> {
            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            lastX = e.getX();
            lastY = e.getY();
            move(dx, dy);
            isSelected = false;
        });
        line.setOnMouseReleased(e -> {
            this.setToTop();
            if (headLinkShape != null)
                headLinkShape.delConnectionInfo(this);
            if (tailLinkShape != null)
                tailLinkShape.delConnectionInfo(this);
            if (isSelected == false) {
                drawController.clearAllOnEdit();
                isSelected = true;
            } else {
                isSelected = false;
            }
        });
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        String tostring = getClass().getSimpleName() + "< " + factoryID + " >" + "(" + df.format(this.startX) + ","
                + df.format(startY) + "," + df.format(endX) + "," + df.format(endY) + ")"
                + "[ " + text.getText() + " ]" + " ;\n";
        return tostring;
    }

    public String toString(int factoryID) {
        DecimalFormat df = new DecimalFormat("#.00");
        String tostring = getClass().getSimpleName() + "< " + factoryID + " >" + "(" + df.format(this.startX) + ","
                + df.format(startY) + "," + df.format(endX) + "," + df.format(endY) + ")"
                + "[ " + text.getText() + " ]" + " ;\n";
        return tostring;
    }

    public int getFactoryID() {
        return factoryID;
    }

    public void setFactoryID(int factoryID) {
        this.factoryID = factoryID;
    }
}
