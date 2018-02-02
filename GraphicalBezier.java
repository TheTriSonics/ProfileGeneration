import java.awt.*;
import java.awt.geom.*;

public class GraphicalBezier extends Plotable implements Moveable {
    GeneralPath path;
    float stroke = 1f;
    boolean filled = false;
    double[][] controlPoints;
    Mover mover;

    public GraphicalBezier() {

    }

    public GraphicalBezier(GeneralPath p) {
	path = p;
    }

    public void setControlPoints(double[][] points) {
	controlPoints = points;
	path = new GeneralPath();
	path.moveTo(points[0][0], points[0][1]);
	path.curveTo(points[1][0], points[1][1],
		     points[2][0], points[2][1],
		     points[3][0], points[3][1]);
    }

    public void setStroke(float f) {
	stroke = f;
    }

    public void setPath(GeneralPath p) {
	path = p;
    }

    public void setFilled(boolean b) {
	filled = b;
    }

    public void setMover(Mover m) {
	mover = m;
    }

    public Mover getMover() {
	return mover;
    }

    public double[] evaluate(double x, double[][] controlPoints) {
	double[] point = new double[] {0,0};
	point = vsum(point,
		     smult((1-x)*(1-x)*(1-x), controlPoints[0]));
	point = vsum(point,
		     smult(3*x*(1-x)*(1-x), controlPoints[1]));
	point = vsum(point,
		     smult(3*x*x*(1-x), controlPoints[2]));
	point = vsum(point,
		     smult(x*x*x, controlPoints[3]));
	return point;
    }

    public boolean hit(int xp, int yp) {
	double x = 0.1;
	double dx = 0.01;
	double[][] tp = new double[4][2];
	for (int i = 0; i < 4; i++) {
	    Point2D.Double cp =
		new Point2D.Double(controlPoints[i][0],
				   controlPoints[i][1]);
	    panel.getTransform().transform(cp, cp);
	    tp[i][0] = cp.x;
	    tp[i][1] = cp.y;
	}
	    
	while (x < 0.9) {
	    double distance = distance(evaluate(x, tp),
				       new double[] {xp,yp});
	    if (distance < 4) return true;
	    x += dx;
	}

	return false;
    }

    public void plot(Graphics2D g) {
	AffineTransform transform = panel.getTransform();
	Shape s = transform.createTransformedShape(path);
	if (filled) {
	    g.setPaint(color);
	    g.fill(s);
	    g.setPaint(Color.black);
	} else g.setPaint(color);
	BasicStroke bs = new BasicStroke(stroke, BasicStroke.CAP_BUTT,
					 BasicStroke.JOIN_BEVEL, 10f);
	//	g.setStroke(new BasicStroke(stroke));
	g.setStroke(bs);
	g.draw(s);
    }

    public static double[] vdiff(double[] u, double[] v) {
	return new double[] {u[0] - v[0], u[1] - v[1]};
    }

    public static double[] vsum(double[]  u, double[] v) {
	return new double[] {u[0] + v[0], u[1] + v[1]};
    }

    public static double[] smult(double s, double[] v) {
	return new double[] {s*v[0], s*v[1]};
    }

    public static double length(double[] v) {
	return Math.sqrt(v[0]*v[0] + v[1]*v[1]);
    }

    public static double distance(double[] u, double[] v) {
	return length(vdiff(u,v));
    }
    
}

	
