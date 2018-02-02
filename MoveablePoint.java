import java.awt.*;
import java.awt.geom.*;

public class MoveablePoint {
    int r = 3;
    Shape shape;
    Point2D.Float point;
    public MoveablePoint(int x, int y) {
	point = new Point2D.Float(x, y);
	setLocation(x, y);
    }
    
    public void setLocation(int x, int y) {
	point.setLocation(x, y);
	shape = new Ellipse2D.Float(x - r, y - r, 2*r, 2*r);
    }
    
    public Point2D.Float getPoint() {
	return point;
    }
    
    public boolean hit(int x, int y) {
	return shape.contains(x, y);
    }
    
    public Shape getShape() {
	return shape;
    }
}
	
