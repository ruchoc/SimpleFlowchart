package controller;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import model.MyLine;
import model.MyShape;

public class KeyBoardManager {
    private boolean isCtrl;
    private boolean isShift;
    private boolean isAlt;

    private DrawController drawController;
    private AnchorPane drawingArea;

    public KeyBoardManager(DrawController drawController) {
        this.drawController = drawController;
        this.drawingArea = drawController.getDrawingArea();
        addListener();
    }

    public void addListener() {
        keyboardListener();
        drawingArea.setOnKeyReleased(e -> {
            this.isAlt = false;
            this.isCtrl = false;
            this.isShift = false;
        });
    }

    public void keyboardListener() {
        drawingArea.setOnKeyPressed(e -> {
            if (e.isControlDown()) {
                this.isCtrl = true;
            }
            if (e.getCode() == KeyCode.DELETE) {
                drawController.delete();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                drawController.clearAllOnEdit();
                drawController.setCopyClipBoard(null);
            }
            if (isCtrl && e.getCode() == KeyCode.Z) {
                drawController.restore();
            }
            if (isCtrl && e.getCode() == KeyCode.C) {
                drawController.copyManager();
            }
            if (isCtrl && e.getCode() == KeyCode.V) {
                drawController.copy();
            }
            if (e.getCode() == KeyCode.UP) {
                keyMove(0, -5);
            }
            if (e.getCode() == KeyCode.DOWN) {
                keyMove(0, 5);
            }
            if (e.getCode() == KeyCode.LEFT) {
                keyMove(-5, 0);
            }
            if (e.getCode() == KeyCode.RIGHT) {
                keyMove(5, 0);
            }
            e.consume();
        });
    }

    public void keyMove(double dx, double dy) {
        Object shape = drawController.workingShape();
        if (shape instanceof MyShape) {
            double x = ((MyShape) shape).getX();
            double y = ((MyShape) shape).getY();
            ((MyShape) shape).move(x + dx, y + dy);
        }
        if (shape instanceof MyLine) {
            ((MyLine) shape).move(dx, dy);
        }
    }

    public boolean isCtrl() {
        return isCtrl;
    }

    public void setCtrl(boolean isCtrl) {
        this.isCtrl = isCtrl;
    }

    public boolean isShift() {
        return isShift;
    }

    public void setShift(boolean isShift) {
        this.isShift = isShift;
    }

    public boolean isAlt() {
        return isAlt;
    }

    public void setAlt(boolean isAlt) {
        this.isAlt = isAlt;
    }

}
