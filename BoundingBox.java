public class BoundingBox {
    public double llx, lly, urx, ury;
    public BoundingBox(double lowerLeftX, double lowerLeftY,
                       double upperRightX, double upperRightY) {
        llx = lowerLeftX;  lly = lowerLeftY;
        urx = upperRightX; ury = upperRightY;
    }
}
