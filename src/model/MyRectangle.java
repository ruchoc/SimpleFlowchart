package model;

import java.text.DecimalFormat;

public class MyRectangle extends MyPolygon {

    public MyRectangle(double x, double y, int id) {
        this(x, y, 100, 50, id);
        setShape();
    }

    public MyRectangle(double x, double y, double witdh, double height, int id) {
        super(x, y, witdh, height);
        this.factoryID = id;
    }

    @Override
    public void setShape() {
        double upLeftX = this.x - width;
        double upLeftY = this.y - height;
        double upRightX = this.x + width;
        double upRightY = this.y - height;
        double downLeftX = this.x - width;
        double downLeftY = this.y + height;
        double downRightX = this.x + width;
        double downRightY = this.y + height;
        Double[] list = {upLeftX, upLeftY, upRightX, upRightY, downRightX, downRightY, downLeftX, downLeftY};
        polygon.getPoints().setAll(list);
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.000");
        String tostring = getClass().getSimpleName() + "< " + factoryID + " >" + "(" + df.format(this.x)
                + "," + df.format(this.y) + "," + df.format(this.width) + ","
                + df.format(this.height) + ")" + "[ " + text.getText() + " ]" + "{" + getCSS() + "}" + " ;\n";
        return tostring;
    }
}
