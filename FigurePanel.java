import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

public class FigurePanel extends JPanel {
    AffineTransform transform;
    int width = 0, height = 0;
    public BoundingBox bbox;
    Vector plotables, moveables, backgroundPlotables;
    BufferedImage background;
    Moveable moving = null;
    Insets insets = new Insets(0, 0, 0, 0);
    
    public FigurePanel(double llx, double lly, double urx, double ury,
		       BufferedImage background) {
        setBackground(Color.white);
        bbox = new BoundingBox(llx, lly, urx, ury);
        plotables = new Vector();
        moveables = new Vector();
	backgroundPlotables = new Vector();
	this.background = background;
	addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent me) {
		    for (int i = 0; i < moveables.size(); i++) {
			Moveable m = (Moveable) moveables.elementAt(i);
			if (m.hit(me.getX(), me.getY())) {
			    moving = m; 
			    moveTo(me.getX(), me.getY()); 
			    repaint();
			    break;
			}
		    }
		}
		public void mouseReleased(MouseEvent me) {
		    if (moving == null) return;
		    moveTo(me.getX(), me.getY());
		    moving = null;
		    repaint();
		}
	    });
	addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent me) {
		    if (moving == null) return;
		    moveTo(me.getX(), me.getY());
		    repaint();
		}
	    });
	setDoubleBuffered(true);
    }

    public void setInsets(Insets i) {  insets = i;  } 

    public void moveTo(int x, int y) {
	Point2D.Double point = new Point2D.Double(x, y);
	try {
	    transform.inverseTransform(point, point);
	} catch(NoninvertibleTransformException ex) { 
	    return;
	}
	moving.getMover().move(moving, point.x, point.y);
    }

    public void paintComponent(Graphics gfx) {
        super.paintComponent(gfx);
        Rectangle size = getBounds();

        if (transform == null) {
            width = size.width;  height = size.height;
            float ratioX = (float) ((width-insets.left-insets.right) / 
                                    (bbox.urx - bbox.llx));
            float ratioY = (float) (-(height-insets.top-insets.bottom) /
                                    (bbox.ury - bbox.lly));
            transform = new AffineTransform();
            transform.translate(insets.left, insets.top);
            transform.scale(ratioX, ratioY);
            transform.translate(-bbox.llx, -bbox.ury);
	}
	
        Graphics2D g = (Graphics2D) gfx;
	g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
			   RenderingHints.VALUE_STROKE_PURE);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			   RenderingHints.VALUE_ANTIALIAS_ON);
	if (background != null) g.drawImage(background, 0, 0, this);
        for (int i = 0; i < plotables.size(); i++)
            ((Plotable) plotables.elementAt(i)).plot(g);
    }

    public BoundingBox getBoundingBox() {
        return bbox;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void addToBackground(Plotable p) {
	backgroundPlotables.addElement(p);
	p.setFigurePanel(this);
    }

    public void add(Plotable p) {
        plotables.addElement(p);
        p.setFigurePanel(this);
    }

    public void removeAll() {
	plotables = new Vector();
	moveables = new Vector();
    }

    public void remove(Plotable p) {
	plotables.remove(p);
    }

    public void addMoveable(Moveable m, Mover mover) {
	moveables.addElement(m);
	m.setMover(mover);
    }

    public void removeMoveable(Moveable m) {
	moveables.removeElement(m);
    }

}

