import java.awt.*;
import java.awt.geom.*;

public class GraphicalPoint extends Plotable implements Moveable {
    public static final int CIRCLE = 0;
    public static final int SQUARE = 1;
    int style = CIRCLE;
    double size = 2;
    public double x, y;
    Shape shape;
    Mover mover;
    
    public GraphicalPoint(double xp, double yp) {
	x = xp;  y = yp;
    }

    public void setPoint(double xp, double yp) {
	x = xp;  y = yp;
    }

    public void setMover(Mover m) {
	mover = m;
    }

    public Mover getMover() {
	return mover;
    }

    public double[] getPoint() {
	return new double[] {x,y};
    }

    public void setSize(double s) { size = s; }
    
    public void setStyle(int s) { style = s; }

    public boolean hit(int xp, int yp) {
	return shape.contains(xp, yp);
    }

    public void toPixels(Point2D p) {
	AffineTransform transform = panel.getTransform();
	panel.getTransform().transform(p, p);
    }	

    public void plot(Graphics2D g) {
	Point2D.Double point = new Point2D.Double(x, y);
	toPixels(point);
	if (style == CIRCLE) 
	    shape = 
		new Ellipse2D.Double(point.x - size, point.y - size,
				    2*size, 2*size);
	else shape = 
		new Rectangle2D.Double(point.x - size, point.y - size,
				    2*size, 2*size);
	g.setPaint(color);
	g.fill(shape);
	g.setPaint(Color.black);
	g.draw(shape);
    }
}

	
