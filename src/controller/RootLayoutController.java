package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootLayoutController implements Initializable {
    @FXML
    private BorderPane rootLayout;
    @FXML
    private AnchorPane drawingArea;
    @FXML
    private AnchorPane leftArea;
    @FXML
    private AnchorPane shapeArea;
    @FXML
    private AnchorPane codeArea;

    @FXML
    private TextArea codeTextArea;
    @FXML
    private MenuItem ModelMenuItem;
    @FXML
    private TextField textfield;
    @FXML
    private Button Button;

    // shape
    @FXML
    private ImageView RoundRectangle;
    @FXML
    private ImageView Rectangle;
    @FXML
    private ImageView Decision;
    @FXML
    private ImageView InputRectangle;
    @FXML
    private ImageView Circular;
    @FXML
    private ImageView CurvedRectangle;
    // line
    @FXML
    private ImageView MyLine;
    @FXML
    private ImageView BrokenLine;
    @FXML
    private ImageView DoubleBrokenLine;
    // 右侧栏
    @FXML
    private TextField textFieldH;
    @FXML
    private TextField textFieldW;
    @FXML
    private TextField textFieldX;
    @FXML
    private TextField textFieldY;
    @FXML
    private TextArea textArea;
    @FXML
    private ColorPicker textColor;
    @FXML
    private ColorPicker fillColor;
    @FXML
    private ComboBox<String> textSize;
    @FXML
    private Button button2;
    @FXML
    private Button shapeORLine;
    @FXML
    private VBox shapeVBox;
    @FXML
    private VBox lineVBox;
    @FXML
    private ImageView workingImageView;
    @FXML
    private Button codeButton;

    // 图层相关的控制器，工厂
    private Compiler compiler;
    private ShapeFactory shapeFactory;
    private DrawController drawController;
    private PropertyController propertyController;
    private FileMenuController menuController;

    public void menuNew() {
        menuController.newDrawingArea(compiler);
    }

    public void menuSave() {
        menuController.saveDrawingArea(drawController);
    }

    public void menuOpen() {
        menuController.getDrawingArea(compiler);
    }

    public void menuExport() {
        menuController.exportDrawingArea(drawingArea);
    }

    public void menuAbout() {
        Stage helpStage = new Stage();
        helpStage.initModality(Modality.APPLICATION_MODAL);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/controller/about.fxml"));
            Pane Layout = (Pane) loader.load();
            Scene scene = new Scene(Layout);
            helpStage.setScene(scene);
            helpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isCodeModel = false;

    public void changeModel() {
        if (isCodeModel == true) {
            isCodeModel = false;
            ModelMenuItem.setText("Change To Code Model");
            shapeArea.setVisible(true);
            codeArea.setVisible(false);
            leftArea.setPrefWidth(200);
            codeButton.setPrefWidth(200);
        } else {
            isCodeModel = true;
            ModelMenuItem.setText("Change To Draw Model");
            shapeArea.setVisible(false);
            codeArea.setVisible(true);
            leftArea.setPrefWidth(400);
            codeButton.setPrefWidth(400);
        }
    }

    private boolean isShape;

    public void changeShapeORLine() {
        if (isShape) {
            isShape = false;
            shapeORLine.setText("Line");

            shapeVBox.setVisible(false);
            lineVBox.setVisible(true);
        } else {
            isShape = true;
            shapeORLine.setText("Shape");
            shapeVBox.setVisible(true);
            lineVBox.setVisible(false);
        }
    }

    private String selectShape = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 设置鼠标手势
        RoundRectangle.setCursor(Cursor.HAND);
        Rectangle.setCursor(Cursor.HAND);
        Decision.setCursor(Cursor.HAND);
        InputRectangle.setCursor(Cursor.HAND);
        Circular.setCursor(Cursor.HAND);
        CurvedRectangle.setCursor(Cursor.HAND);
        MyLine.setCursor(Cursor.HAND);
        BrokenLine.setCursor(Cursor.HAND);
        DoubleBrokenLine.setCursor(Cursor.HAND);
        // 初始化控制器和工厂
        drawController = new DrawController(drawingArea);
        shapeFactory = new ShapeFactory(drawingArea, drawController);
        compiler = new Compiler();
        compiler.setShapeFactory(shapeFactory);
        compiler.setTextArea(codeTextArea);
        codeButton.setOnMouseClicked(e -> {
            compiler.compileProduce(codeTextArea.getText());
        });
        drawController.setCompiler(compiler);
        propertyController = new PropertyController(textFieldX, textFieldY, textFieldW, textFieldH, textArea,
                textColor, fillColor, textSize);
        propertyController.setButton(button2);
        propertyController.setWorkingImaging(workingImageView);
        propertyController.edit();
        propertyController.setDrawController(drawController);
        drawController.setPropertyController(propertyController);
        drawController.setKeyBoardManager();
        menuController = new FileMenuController();

        // 模式初始化
        shapeArea.setVisible(true);
        codeArea.setVisible(false);
        ModelMenuItem.setText("Change To Code Model");
        isShape = true;
        shapeVBox.setVisible(true);
        lineVBox.setVisible(false);

        // 设置添加图形的动作，需要绘图区和图标区
        drawingArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                drawingArea.requestFocus();
                if (event.getClickCount() == 1 && selectShape != null) {
                    double x, y;
                    x = event.getX();
                    y = event.getY();
                    shapeFactory.produce(selectShape, x, y);
                    selectShape = null;
                } else if (event.getClickCount() == 1 && selectShape == null) {
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node == drawingArea)
                        drawController.clearAllOnEdit();
                    drawController.getPropertyController().setWorkShape(drawController.workingShape());
                    drawController.getPropertyController().update();
                }
            }
        });

        shapeArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (event.getTarget().getClass() == ImageView.class) {
                        int x, y;
                        x = 300;
                        y = 300;
                        selectShape = ((ImageView) event.getTarget()).getId();
                        selectShape = selectShape.replaceAll("Image", "");
                        shapeFactory.produce(selectShape, x, y);
                        selectShape = null;
                    }
                } else if (event.getClickCount() == 1) {
                    if (event.getTarget().getClass() == ImageView.class) {
                        ImageView nowImage = (ImageView) event.getTarget();
                        selectShape = nowImage.getId();
                        selectShape = selectShape.replaceAll("Image", "");
                        selectShape.replaceAll("Image", "");
                    }
                }
            }
        });
    }
}
