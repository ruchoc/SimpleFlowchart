package controller;

import javafx.scene.layout.AnchorPane;
import model.BrokenLine;
import model.CurvedRectangle;
import model.Decision;
import model.DoubleBrokenLine;
import model.InputRectangle;
import model.MyCircle;
import model.MyLine;
import model.MyRectangle;
import model.MyShape;
import model.RoundRectangle;

public class ShapeFactory {
    private int countShapeID = 0;
    private AnchorPane drawingArea;
    private DrawController drawController;

    public ShapeFactory(AnchorPane drawingArea, DrawController drawController) {
        this.drawingArea = drawingArea;
        this.drawController = drawController;
    }

    public MyRectangle newMyRectangle(double x, double y) {
        countShapeID++;
        return new MyRectangle(x, y, countShapeID);
    }

    public RoundRectangle newRoundRectangle(double x, double y) {
        countShapeID++;
        return new RoundRectangle(x, y, countShapeID);
    }

    public Decision newDecision(double x, double y) {
        countShapeID++;
        return new Decision(x, y, countShapeID);
    }

    public InputRectangle newInputRectangle(double x, double y) {
        countShapeID++;
        return new InputRectangle(x, y, countShapeID);
    }

    public MyCircle newMyCircle(double x, double y) {
        countShapeID++;
        return new MyCircle(x, y, countShapeID);
    }

    public CurvedRectangle newCurvedRectangle(double x, double y) {
        countShapeID++;
        return new CurvedRectangle(x, y, countShapeID);
    }

    public MyLine newMyLine(double sx, double sy, double ex, double ey) {
        countShapeID++;
        return new MyLine(sx, sy, ex, ey, countShapeID);
    }

    public MyLine newBrokenLine(double sx, double sy, double ex, double ey) {
        countShapeID++;
        return new BrokenLine(sx, sy, ex, ey, countShapeID);
    }

    public MyLine newDoubleBrokenLine(double sx, double sy, double ex, double ey, double ax) {
        countShapeID++;
        return new DoubleBrokenLine(sx, sy, ex, ey, ax, countShapeID);
    }

    public DrawController getDrawController() {
        return drawController;
    }

    public void setDrawController(DrawController drawController) {
        this.drawController = drawController;
    }

    //Compiler
    public void produce(String kind, double x, double y, double width, double height, String text, int id) {
        if (kind == null)
            return;
        if (kind.indexOf("Line") != -1) {
            MyLine line = produceMyLine(kind, x, y, width, height);
            line.getText().setText(text);
            line.setFactoryID(id);
            line.setShape();
        } else {
            MyShape shape = produceMyShape(kind, x, y, width, height, text);
            shape.getText().setText(text);
            shape.setFactoryID(id);
            shape.textUpdate();
        }
    }

    //Compiler
    public void produce(String kind, double x, double y, double ex, double ey, double aX, String text, int id) {
        if (kind == null) return;
        MyLine shape = produceMyLine(kind, x, y, ex, ey, aX, id);
        drawController.registerLine(shape);
    }

    public int getCountShapeID() {
        return countShapeID;
    }

    public void setCountShapeID(int countShapeID) {
        this.countShapeID = countShapeID;
    }

    //rootLayoutController
    public void produce(String kind, double x, double y) {
        if (kind.indexOf("Line") != -1) {
            produceMyLine(kind, x, y);
        } else {
            produceMyShape(kind, x, y);
        }
    }

    //Compiler
    public MyShape produceMyShape(String kind, double x, double y, double width, double height, String text) {
        MyShape shape = null;
        switch (kind) {
            case "CurvedRectangle":
                shape = newCurvedRectangle(x, y);
                break;
            case "Decision":
                shape = newDecision(x, y);
                break;
            case "InputRectangle":
                shape = newInputRectangle(x, y);
                break;
            case "MyCircle":
                shape = newMyCircle(x, y);
                break;
            case "RoundRectangle":
                shape = newRoundRectangle(x, y);
                break;
            case "MyRectangle":
                shape = newMyRectangle(x, y);
                break;
            default:
                break;
        }
        shape.setWidth(width);
        shape.setHeight(height);
        shape.getText().setText(text);
        drawController.register(shape);
        return shape;
    }

    public MyShape produceMyShape(String kind, double x, double y) {
        MyShape shape = null;
        switch (kind) {
            case "CurvedRectangle":
                shape = newCurvedRectangle(x, y);
                break;
            case "Decision":
                shape = newDecision(x, y);
                break;
            case "InputRectangle":
                shape = newInputRectangle(x, y);
                break;
            case "Circular":
                shape = newMyCircle(x, y);
                break;
            case "RoundRectangle":
                shape = newRoundRectangle(x, y);
                break;
            case "Rectangle":
                shape = newMyRectangle(x, y);
                break;
            default:
                break;
        }

        drawController.register(shape);
        return shape;
    }

    public MyLine produceMyLine(String kind, double x, double y) {
        MyLine line = null;
        switch (kind) {
            case "MyLine":
                line = newMyLine(x, y, x, y + 100);
                break;
            case "BrokenLine":
                line = newBrokenLine(x, y, x + 100, y + 100);
                break;
            case "DoubleBrokenLine":
                line = newDoubleBrokenLine(x, y, x, y + 100, x - 100);
                break;
            default:
                break;
        }
        drawController.registerLine(line);
        return line;
    }

    public MyLine produceMyLine(String kind, double x, double y, double ex, double ey, double aX, int id) {
        MyLine shape = newDoubleBrokenLine(x, y, ex, ey, aX);
        shape.setFactoryID(id);
        return shape;
    }

    //Compiler
    public MyLine produceMyLine(String kind, double x, double y, double ex, double ey) {
        MyLine shape = null;
        switch (kind) {
            case "MyLine":
                shape = newMyLine(x, y, ex, ey);
                break;
            case "BrokenLine":
                shape = newBrokenLine(x, y, ex, ey);
                break;
            default:
                break;
        }
        drawController.registerLine(shape);
        return shape;
    }
}
