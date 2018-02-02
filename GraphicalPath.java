import java.awt.*;
import java.awt.geom.*;

public class GraphicalPath extends Plotable {
    GeneralPath path;
    float stroke = 1f;
    boolean filled = true;

    public GraphicalPath() {

    }

    public GraphicalPath(GeneralPath p) {
	path = p;
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
}

	
