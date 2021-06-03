package controller;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextArea;

public class Compiler {
    private ShapeFactory shapeFactory;
    private DrawController drawController;
    private String code;
    private TextArea textArea;

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public Double[] getRound(String shape) {
        ArrayList<Double> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
        Matcher matcher = pattern.matcher(shape);
        if (matcher.find()) {
            String temp = matcher.group();
            String tt[] = temp.split(",");
            for (int i = 0; i < tt.length; i++) {
                list.add(Double.valueOf(tt[i]));
            }
        }
        if (list.size() == 0) {
            return null;
        }
        Double[] temp = new Double[list.size()];
        list.toArray(temp);
        return temp;
    }

    public int getID(String shape) {
        int id = 0;
        Pattern pattern = Pattern.compile("(?<=\\<)[^\\>]+");
        Matcher matcher = pattern.matcher(shape);
        if (matcher.find()) {
            String temp = matcher.group();
            temp = temp.trim();
            id = Integer.valueOf(temp);
        }
        return id;
    }

    public String getCss(String shape) {
        String temp = null;
        Pattern pattern = Pattern.compile("(?<=\\{)[^\\}]+");
        Matcher matcher = pattern.matcher(shape);
        if (matcher.find()) {
            temp = matcher.group();
        }
        return temp;
    }

    public String getText(String shape) {
        Pattern pattern = Pattern.compile("(?<=\\[)[^\\]]+");
        Matcher matcher = pattern.matcher(shape);
        String temp = " ";
        if (matcher.find()) {
            temp = matcher.group();
            temp = temp.trim();
            return temp + " ";
        }
        return temp;
    }

    public String getShapeType(String shape) {
        Pattern pattern = Pattern.compile("[^\\<]+");
        Matcher matcher = pattern.matcher(shape);
        String temp = null;
        if (matcher.find()) {
            temp = matcher.group();
        }
        return temp;
    }

    public String[] getItem() {
        String[] item = code.split(";");
        return item;
    }

    public void compileProduce(String code) {
        this.code = code.trim();
        textArea.setText(this.code);
        shapeFactory.setCountShapeID(0);
        drawController.reset();
        String items[] = getItem();
        this.code = code.replaceAll("\n|\r", "");
        ArrayList<String> cssList = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].trim();
            if (items[i] == null && items[i].length() == 0) continue;
            Double list[] = getRound(items[i]);
            String text = getText(items[i]);
            int id = getID(items[i]);
            String css = getCss(items[i]);
            String kind = getShapeType(items[i]);
            if (kind == null) return;
            if (kind.indexOf("Line") == -1) {
                cssList.add(css);
            }
            if (list.length == 4) shapeFactory.produce(kind, list[0], list[1], list[2], list[3], text, id);
            else shapeFactory.produce(kind, list[0], list[1], list[2], list[3], list[4], text, id);
        }
        for (int i = 0; i < cssList.size(); i++) {
            drawController.getList().get(i).setCSS(cssList.get(i));
        }
        drawController.updateCodeArea();
    }

    public ShapeFactory getShapeFactory() {
        return shapeFactory;
    }

    public void setShapeFactory(ShapeFactory shapeFactory) {
        this.shapeFactory = shapeFactory;
        this.drawController = shapeFactory.getDrawController();
    }
}
