package controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileMenuController {

    public void newDrawingArea(Compiler compiler) {
        compiler.compileProduce("");
    }

    public void saveDrawingArea(DrawController drawController) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save Directory");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DAT", "*.dat"));
        File saveFile = fileChooser.showSaveDialog(stage);
        if (saveFile == null)
            return;
        if (saveFile != null) {
            try (PrintWriter saver = new PrintWriter(saveFile)) {
                saver.println(drawController.getCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getDrawingArea(Compiler compiler) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DAT", "*.dat"));
        File file = fileChooser.showOpenDialog(stage);
        if (file == null)
            return;
        try (Scanner geter = new Scanner(file)) {
            String code = "";
            while (geter.hasNextLine()) code += geter.nextLine();
            compiler.compileProduce(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportDrawingArea(AnchorPane drawingArea) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save Directory");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File saveFile = fileChooser.showSaveDialog(stage);
        if (saveFile == null)
            return;

        WritableImage image = drawingArea.snapshot(new SnapshotParameters(), null);
        BufferedImage source = SwingFXUtils.fromFXImage(image, null);

        try {
            String suffix = saveFile.getName();
            suffix = suffix.substring(saveFile.getName().lastIndexOf("."));
            if (suffix.equals(".png"))
                ImageIO.write(source, "png", saveFile);
            else {
                BufferedImage newBufferedImage = new BufferedImage(source.getWidth(),
                        source.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = newBufferedImage.createGraphics();
                graphics.drawImage(source, 0, 0, null);
                ImageIO.write(newBufferedImage, "jpg", saveFile);
                graphics.dispose();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
