import java.awt.geom.*;
import java.awt.*;

public class Robot extends GraphicalPath implements Moveable {

    double x, y;
    public static double height = 27.5;
    public static double width = 32.5;
    Shape shape;
    Mover mover;
    public Robot(double x, double y) {
	this.x = x;
	this.y = y;
	updatePath();
	setColor(new Color(0xff, 0xa5, 0x00));
    }

    public void setPoint(double x, double y) {
	this.x = x;
	this.y = y;
	updatePath();
    }

    void updatePath() {
	path = new GeneralPath();
	path.moveTo(x,y);
	path.lineTo(x,y-height);
	path.lineTo(x+width,y-height);
	path.lineTo(x+width,y);
	path.closePath();
    }

    public void plot(Graphics2D g) {
	shape = panel.getTransform().createTransformedShape(path);
	super.plot(g);
    }

    public boolean hit(int x, int y) {
	return shape.contains(x,y);
    }

    public void setMover(Mover m) {
	mover = m;
    }
    
    public Mover getMover() {
	return mover;
    }

}
