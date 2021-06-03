package controller;

import java.util.ArrayList;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import model.ConnectionInfo;
import model.MyLine;
import model.MyShape;

public class DrawController {

    private AnchorPane drawingArea = null;
    private ArrayList<MyShape> list = new ArrayList<>();
    private ArrayList<MyLine> listLine = new ArrayList<MyLine>();
    private Object workShape;
    private OperationStack operationStack = new OperationStack();
    private KeyBoardManager keyBoardManager;
    private PropertyController propertyController;
    private Compiler compiler;

    private Circle nearPoint;

    private double maxDistance = 50;

    private boolean isReStoring = false;

    private String copyClipBoard = null;

    public DrawController(AnchorPane drawArea) {
        drawingArea = drawArea;
    }

    public MyLine getMyLine(int id) {
        MyLine line = null;
        for (int i = 0; i < listLine.size(); i++) {
            if (listLine.get(i).getFactoryID() == id) {
                line = listLine.get(i);
            }
        }
        return line;
    }

    public Compiler getCompiler() {
        return compiler;
    }

    public void setCompiler(Compiler compiler) {
        this.compiler = compiler;
    }

    public void updateCodeArea() {
        compiler.getTextArea().setText(getCode());
    }

    public void connect(double x1, double y1, String type, MyLine line) {
        double minDistance = 100000;
        nearPoint = null;
        MyShape nearShape = null;
        int location = 0;
        String part = type;
        for (MyShape nowShape : list) {
            Circle[] circles = nowShape.getDrawPoints().getCircles();
            for (int i = 0; i < 4; i++) {
                Circle nowCircle = circles[i];
                nowCircle.setVisible(false);
                double x2, y2;
                x2 = nowCircle.getCenterX();
                y2 = nowCircle.getCenterY();
                double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                if (distance < maxDistance && distance < minDistance) {
                    nearPoint = nowCircle;
                    nearShape = nowShape;
                    location = i;
                    minDistance = distance;
                }
            }
        }
        for (MyLine nowLine : listLine) {
            if (nowLine == line) continue;
            ArrayList<Circle> middlePoints = nowLine.getMiddlePoints();
            for (Circle nowCircle : middlePoints) {
                nowCircle.setVisible(false);
                double x2, y2;
                x2 = nowCircle.getCenterX();
                y2 = nowCircle.getCenterY();
                double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                if (distance < maxDistance && distance < minDistance) {
                    nearPoint = nowCircle;
                    minDistance = distance;
                }
            }
        }
        if (nearPoint != null) {
            if (nearShape != null) nearShape.addConnectionInfo(new ConnectionInfo(line, location, part, this));
            if (type.equals("end")) {
                line.endMove(nearPoint.getCenterX(), nearPoint.getCenterY());
                line.setTailLink(nearShape);
            } else if (type.equals("start")) {
                line.startMove(nearPoint.getCenterX(), nearPoint.getCenterY());
                line.setHeadLink(nearShape);
            }
        }
        updateCodeArea();
        saveChange();
    }

    public void setNearPoint(Circle nearPoint) {
        this.nearPoint = nearPoint;
    }

    public void checkDistanceToPoints(double x1, double y1, MyLine line) {
        for (MyShape nowShape : list) {
            for (Circle nowCircle : nowShape.getDrawPoints().getCircles()) {
                double x2, y2;
                x2 = nowCircle.getCenterX();
                y2 = nowCircle.getCenterY();
                double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                if (distance < maxDistance) {
                    nowCircle.setVisible(true);
                } else {
                    nowCircle.setVisible(false);
                }
            }
        }
        for (MyLine nowLine : listLine) {
            if (nowLine == line) continue;
            ArrayList<Circle> middlePoints = nowLine.getMiddlePoints();
            for (Circle nowCircle : middlePoints) {
                double x2, y2;
                x2 = nowCircle.getCenterX();
                y2 = nowCircle.getCenterY();
                double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                if (distance < maxDistance) {
                    nowCircle.setVisible(true);
                } else {
                    nowCircle.setVisible(false);
                }
            }
        }
    }

    public ArrayList<MyShape> getList() {
        return list;
    }

    public void setList(ArrayList<MyShape> list) {
        this.list = list;
    }

    public PropertyController getPropertyController() {
        return propertyController;
    }

    public void setPropertyController(PropertyController propertyController) {
        this.propertyController = propertyController;
    }

    public void addDrawArea() {
        int index = list.size() - 1;
        MyShape shape = (list.get(index));
        shape.getPane(drawingArea, this);
    }

    public void register(MyShape shape) {
        if (shape != null) {
            list.add(shape);
            addDrawArea();
            if (!isReStoring)
                saveChange();
        }
    }

    public void addLineDrawArea() {
        int index = listLine.size() - 1;
        MyLine line = (listLine.get(index));
        line.getPane(drawingArea, this);
    }

    public void registerLine(MyLine shape) {
        if (shape != null) {
            listLine.add(shape);
            addLineDrawArea();
            if (!isReStoring)
                saveChange();
        }
    }

    public void delete(MyShape shape) {
        int index = list.indexOf(shape);
        shape.delet();
        list.remove(index);
    }

    public void delete() {
        boolean remain = false;
        while (true) {
            remain = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isSelected()) {
                    list.get(i).delet();
                    list.remove(i);
                    remain = true;
                    break;
                }
            }
            if (!remain) {
                break;
            }
        }
        while (true) {
            remain = false;
            for (int i = 0; i < listLine.size(); i++) {
                if (listLine.get(i).isSelected()) {
                    listLine.get(i).delete();
                    listLine.remove(i);
                    remain = true;
                    break;
                }
            }
            if (!remain) {
                break;
            }
        }
        saveChange();
    }

    public void clearAllOnEdit() {
        if (!keyBoardManager.isCtrl()) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setIsSelected(false);
                list.get(i).getEditer().disapper();
            }
            for (int i = 0; i < listLine.size(); i++) {
                listLine.get(i).setSelected(false);
            }
        }
    }

    public Object workingShape() {
        Object shape = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                if (shape == null) {
                    shape = list.get(i);
                } else {
                    shape = null;
                    break;
                }
            }
        }
        for (int i = 0; i < listLine.size(); i++) {
            if (listLine.get(i).isSelected()) {
                if (shape == null) {
                    shape = listLine.get(i);
                } else {
                    shape = null;
                    break;
                }
            }
        }
        workShape = shape;
        return shape;
    }

    public void copyManager() {
        String code = "";
        int num = compiler.getShapeFactory().getCountShapeID();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                num++;
                code = code + list.get(i).toString(num);

            }
        }
        for (int i = 0; i < listLine.size(); i++) {
            if (listLine.get(i).isSelected()) {
                num++;
                code = code + listLine.get(i).toString(num);
            }
        }
        copyClipBoard = code;
    }

    public void copy() {
        String code = getCode();
        if (copyClipBoard == null) return;
        code = code + copyClipBoard;
        isReStoring = true;
        compiler.getTextArea().setText(code);
        compiler.compileProduce(code);
        isReStoring = false;
        saveChange();
    }

    public String getCopyClipBoard() {
        return copyClipBoard;
    }

    public void setCopyClipBoard(String copyClipBoard) {
        this.copyClipBoard = copyClipBoard;
    }

    public void reset() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).delet();
        }
        for (int i = 0; i < listLine.size(); i++) {
            listLine.get(i).delete();
        }
        listLine.clear();
        list.clear();
    }

    public KeyBoardManager getKeyBoardManager() {
        return keyBoardManager;
    }

    public void setKeyBoardManager() {
        this.keyBoardManager = new KeyBoardManager(this);
    }

    public AnchorPane getDrawingArea() {
        return drawingArea;
    }

    public void setDrawingArea(AnchorPane drawingArea) {
        this.drawingArea = drawingArea;
    }

    public void saveChange() {
        String code = getCode();
        operationStack.addOperation(code);
        compiler.getTextArea().setText(code);
    }

    public String getCode() {
        String code = "";
        for (int i = 0; i < list.size(); i++) {
            code = code + list.get(i).toString();
        }
        for (int i = 0; i < listLine.size(); i++) {
            code = code + listLine.get(i).toString();
        }
        return code;
    }

    public void restore() {
        isReStoring = true;
        String code = operationStack.restoreOperation();
        code = operationStack.getTop();
        compiler.getTextArea().setText(code);
        compiler.compileProduce(code);
        isReStoring = false;
    }
}
