package model;

public class CurvedRectangle extends MyPolygon {

    public CurvedRectangle(double x, double y, int id) {
        super(x, y, 100, 50);
        this.factoryID = id;
        setShape();
    }

    /*
     * 曲边梯形，重写更新坐标方法
     */
    @Override
    public void setShape() {
        double downLeftX = this.x - width;
        double downLeftY = this.y + 3.0 * height / 4;
        double upLeftX = this.x - width;
        double upLeftY = this.y - height;
        double upRightX = this.x + width;
        double upRightY = this.y - height;

        double x1 = this.x - width / 2;
        double y = downLeftY;
        double x2 = this.x + width / 2;
        double radius = width / 2;
        double A = height / 4;
        double dx = 2 * width / (StandardNum.ARC_NUM);
        double p = Math.PI / width;
        double x = 0;

        Double[] list = new Double[2 * StandardNum.ARC_NUM + 6];
        for (int i = 0; i <= 2 * StandardNum.ARC_NUM; i += 2) {
            y = A * Math.sin(p * x);
            list[i] = downLeftX + x;
            list[i + 1] = downLeftY + y;
            x = x + dx;
        }

        list[2 * StandardNum.ARC_NUM + 2] = upRightX;
        list[2 * StandardNum.ARC_NUM + 3] = upRightY;
        list[2 * StandardNum.ARC_NUM + 4] = upLeftX;
        list[2 * StandardNum.ARC_NUM + 5] = upLeftY;

        polygon.getPoints().setAll(list);
    }

}
