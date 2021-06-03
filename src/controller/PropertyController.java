package controller;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.MyLine;
import model.MyShape;

public class PropertyController {

    private TextField textFieldH;
    private TextField textFieldW;
    private TextField textFieldX;
    private TextField textFieldY;
    private TextArea textArea;
    private ColorPicker textColor;
    private ColorPicker fillColor;
    private ComboBox<String> textSize;

    private Object shape;
    private Button button;
    private DrawController drawController;
    private ImageView workingImaging;

    public ImageView getWorkingImaging() {
        return workingImaging;
    }

    public void setWorkingImaging(ImageView workingImaging) {
        this.workingImaging = workingImaging;
    }

    public PropertyController(TextField x, TextField y, TextField W, TextField H, TextArea textArea
            , ColorPicker textColor, ColorPicker fillColor, ComboBox<String> textSize) {
        this.textFieldH = H;
        this.textFieldW = W;
        this.textFieldY = y;
        this.textFieldX = x;
        this.textArea = textArea;
        this.textColor = textColor;
        this.fillColor = fillColor;
        this.textSize = textSize;
        initialize();
    }

    public void initialize() {
        textColor.setValue(new Color(0, 0, 0, 1));
        ObservableList<String> size = FXCollections.observableArrayList(
                "tiny",
                "small",
                "middle",
                "large",
                "giant"
        );
        textSize.setItems(size);
        textSize.setValue("small");
    }

    public void setWorkShape(Object object) {
        this.shape = object;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void update() {
        if (shape != null) {
            setImage(shape.getClass().getSimpleName());
            if (shape instanceof MyShape) {
                textFieldH.setText(String.valueOf((((MyShape) shape).getHeight())));
                textFieldW.setText(String.valueOf((((MyShape) shape).getWidth())));
                textFieldX.setText(String.valueOf((((MyShape) shape).getX())));
                textFieldY.setText(String.valueOf((((MyShape) shape).getY())));
                textArea.setText(String.valueOf((((MyShape) shape).getText().getText())));
            } else {
                textFieldH.setText(String.valueOf((((MyLine) shape).getEY())));
                textFieldW.setText(String.valueOf((((MyLine) shape).getEX())));
                textFieldX.setText(String.valueOf((((MyLine) shape).getSX())));
                textFieldY.setText(String.valueOf((((MyLine) shape).getSY())));
                textArea.setText(String.valueOf((((MyLine) shape).getText().getText())));
            }
        } else {
            textArea.setText("");
            textFieldH.setText("");
            textFieldW.setText("");
            textFieldX.setText("");
            textFieldY.setText("");
            setImage("null");
        }
    }

    public void setImage(String kind) {
        switch (kind) {
            case "CurvedRectangle":
                workingImaging.setImage(new Image(new File("src/image/document.png").getAbsoluteFile().toURI().toString()));
                break;
            case "Decision":
                workingImaging.setImage(new Image(new File("src/image/decision.png").getAbsoluteFile().toURI().toString()));
                break;
            case "InputRectangle":
                workingImaging.setImage(new Image(new File("src/image/data.png").getAbsoluteFile().toURI().toString()));
                break;
            case "MyCircle":
                workingImaging.setImage(new Image(new File("src/image/prepare.png").getAbsoluteFile().toURI().toString()));
                break;
            case "RoundRectangle":
                workingImaging.setImage(new Image(new File("src/image/start.png").getAbsoluteFile().toURI().toString()));
                break;
            case "MyRectangle":
                workingImaging.setImage(new Image(new File("src/image/process.png").getAbsoluteFile().toURI().toString()));
                break;
            case "MyLine":
                workingImaging.setImage(new Image(new File("src/image/arrow.png").getAbsoluteFile().toURI().toString()));
                break;
            case "BrokenLine":
                workingImaging.setImage(new Image(new File("src/image/BrokenLine.png").getAbsoluteFile().toURI().toString()));
                break;
            case "DoubleBrokenLine":
                workingImaging.setImage(new Image(new File("src/image/doubleBrokenLine.png").getAbsoluteFile().toURI().toString()));
                break;
            default:
                workingImaging.setImage(null);
                break;
        }
    }

    public String getStyle() {
        String style = null;
        int size = 10;
        switch (textSize.getValue()) {
            case "tiny":
                size = 7;
                break;
            case "small":
                size = 14;
                break;
            case "middle":
                size = 22;
                break;
            case "large":
                size = 35;
                break;
            case "giant":
                size = 50;
                break;
            default:
                break;
        }
        style = "-fx-font: " + String.valueOf(size) + " arial;";
        return style;
    }

    public void edit() {
        button.setOnMouseClicked(e -> {
            if (shape != null) {
                if (shape instanceof MyShape) {
                    MyShape shape = (MyShape) this.shape;
                    shape.setX(Double.parseDouble(textFieldX.getText()));
                    shape.setY(Double.parseDouble(textFieldY.getText()));
                    shape.setWidth(Double.parseDouble(textFieldW.getText()));
                    shape.setHeight(Double.parseDouble(textFieldH.getText()));

                    Text text = shape.getText();
                    text.setText(textArea.getText());
                    text.setFill(textColor.getValue());
                    text.setStyle(getStyle());

                    shape.updateLocation(shape.getX(), shape.getY());
                    shape.textUpdate();
                    drawController.saveChange();
                } else {
                    MyLine shape = (MyLine) this.shape;
                    shape.setSX(Double.parseDouble(textFieldX.getText()));
                    shape.setSY(Double.parseDouble(textFieldY.getText()));
                    shape.setEX(Double.parseDouble(textFieldW.getText()));
                    shape.setEY(Double.parseDouble(textFieldH.getText()));
                    shape.getText().setText(textArea.getText());
                    shape.setShape();
                    drawController.saveChange();
                }
            }
        });

    }

    public DrawController getDrawController() {
        return drawController;
    }

    public void setDrawController(DrawController drawController) {
        this.drawController = drawController;
    }

}
