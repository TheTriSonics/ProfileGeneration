import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class PlanPanel extends FigurePanel implements Mover {

    GraphicalPoint p0, p1, p2, p3;
    GraphicalPath path;
    ArrayList<GraphicalPoint> knotPoints;
    ArrayList<GraphicalBezier> beziers;
    double[][][] bezier;
    public static JLabel modeLabel;

    static PlanPanel panel;
    static final int MOVE = 0;
    static final int ADD = 1;
    static final int REMOVE = 2;
    static int state = MOVE;

    
    static BufferedImage field = null;

    public PlanPanel() {
	this(false, null);
    }

    public PlanPanel(boolean addRobot, String filename) {
	super(0, 0, 54*12, 27*12, field);

	knotPoints = new ArrayList<GraphicalPoint>();
	if (filename == null) {
	    p0 = new GraphicalPoint(Robot.width/2.0, 70);
	    p1 = new GraphicalPoint(40, 30);
	    p2 = new GraphicalPoint(100, 10);
	    p3 = new GraphicalPoint(200, 30);
	    knotPoints.add(p0);
	    knotPoints.add(p1);
	    knotPoints.add(p2);
	    knotPoints.add(p3);
	} else {
	    CSVReader reader = new CSVReader(filename);
	    double[][] points = reader.parseCSV();
	    for (int i = 0; i < points.length; i++) {
		GraphicalPoint gp =
		    new GraphicalPoint(points[i][0], points[i][1]);
		knotPoints.add(gp);
	    }
	}

	double[] point = knotPoints.get(0).getPoint();

	if (addRobot) {
	    Robot robot = new Robot(point[0]-Robot.width/2.0,
				    point[1]+Robot.height/2.0);
	    add(robot);
	    addMoveable(robot,this);
	}

	bezier = buildTrajectory();
	createBeziers();

	for (int i = 0; i < knotPoints.size(); i++) {
	    GraphicalPoint p = knotPoints.get(i);
	    p.setSize(4);
	    p.setColor(Color.red);
	    add(p);
	    addMoveable(p, this);
	}

    }

    public void createBeziers() {
	if (beziers != null) {
	    for (int i = 0; i < beziers.size(); i++) {
		remove(beziers.get(i));
		removeMoveable(beziers.get(i));
	    }
	}
	for (int i = 0; i < knotPoints.size(); i++) {
	    remove(knotPoints.get(i));
	    removeMoveable(knotPoints.get(i));
	}
	beziers = new ArrayList<GraphicalBezier>();
	for (int i = 0; i < bezier.length; i++) {
	    GraphicalBezier curve = new GraphicalBezier();
	    curve.setControlPoints(bezier[i]);
	    curve.setColor(Color.blue);
	    beziers.add(curve);
	    add(curve);
	    addMoveable(curve, this);
	}

	for (int i = 0; i < knotPoints.size(); i++) {
	    add(knotPoints.get(i));
	    addMoveable(knotPoints.get(i), this);
	}
	    
    }
	

    public void setPath() {
	for (int i = 0; i < bezier.length; i++) {
	    beziers.get(i).setControlPoints(bezier[i]);
	}
    }

    public double[][][] buildTrajectory() {
	double[][] points = new double[knotPoints.size()][2];
	for (int i = 0; i < knotPoints.size(); i++) {
	    points[i] = knotPoints.get(i).getPoint();
	}
	Spline spline = new Spline(points);
	return spline.buildTrajectory();
    }

    public static void printPoints() {
	JFileChooser chooser = new JFileChooser();
	chooser.setCurrentDirectory(new File("./profiles"));
	FileNameExtensionFilter filter =
	    new FileNameExtensionFilter("CSV", "csv");
	chooser.addChoosableFileFilter(filter);

	int v = chooser.showOpenDialog(panel);
	if (v == JFileChooser.APPROVE_OPTION) {
	    File file = chooser.getSelectedFile();

	    FileWriter writer;
	    try {
		writer = new FileWriter(file);
		for (int i = 0; i < panel.knotPoints.size(); i++) {
		    GraphicalPoint p = panel.knotPoints.get(i);
		    double[] point = p.getPoint();
		    writer.append(point[0] + ", " + point[1] + "\n");
		}
		writer.flush();
		writer.close();
	    } catch(IOException ex) {
		System.out.println(ex);
		return;
	    }
	}
    }

    public void move(Moveable m, double x, double y) {
	if (state == ADD) {
	    if (m instanceof GraphicalBezier) {
		for (int i = 0; i < beziers.size(); i++) {
		    if (m == beziers.get(i)) {
			GraphicalPoint gp = new GraphicalPoint(x,y);
			gp.setSize(4);
			gp.setColor(Color.red);
			knotPoints.add(i+1, gp);
			add(gp);
			addMoveable(gp, this);
		    }
		}
		bezier = buildTrajectory();
		createBeziers();
		setPath();
	    }
	    return;
	}
			
	if (state == REMOVE) {
	    if (m instanceof GraphicalPoint) {
		if (knotPoints.size() <= 3) return;
		knotPoints.remove(m);
		remove((GraphicalPoint) m);
		removeMoveable(m);
		bezier = buildTrajectory();
		createBeziers();
	    }
	    return;
	}

	if (state == MOVE) {
	    if (m instanceof Robot) {
		if (y > 295 || y < 55) return;
		((Robot) m).setPoint(0,y);
		knotPoints.get(0).setPoint(Robot.width/2.0,
					   y-Robot.height/2.0);
		bezier = buildTrajectory();
		setPath();		
	    }
	    
	    if (m instanceof GraphicalPoint == false) return;
	    ((GraphicalPoint) m).setPoint(x,y);
	    bezier = buildTrajectory();
	    setPath();
	}
    }

    public static void setMode(int mode) {
	state = mode;
	switch(state) {
	case MOVE:
	    panel.modeLabel.setText("Move");
	    return;
	case ADD:
	    panel.modeLabel.setText("Add");
	    return;
	case REMOVE:
	    panel.modeLabel.setText("Remove");
	    return;
	}
    }

    public static void main(String[] args) {
	try {
	    field = ImageIO.read(new File("field.cropped.png"));
	} catch(IOException ex) {
	    System.out.println(ex);
	}

	DrawFrame frame = new DrawFrame();

	switch(args.length) {
	case 0: {
	    panel = new PlanPanel();
	    break;
	}
	case 1: {
	    if (args[0].equals("-r")) panel = new PlanPanel(true, null);
	    else panel = new PlanPanel(false, args[0]);
	    break;
	}
	case 2: {
	    panel = new PlanPanel(true, args[1]);
	    break;
	}
	default:
	    System.out.println("Usage:  java PlanPanel -r filename");
	    return;
	}

	
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(1199, 611));

	JPanel buttonPanel = new JPanel();

	JButton add = new JButton("Add");
	add.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    setMode(ADD);
		}
	    });
	

	JButton remove = new JButton("Remove");
	remove.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    setMode(REMOVE);
		}
	    });
	
	JButton move = new JButton("Move");
	move.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    setMode(MOVE);
		}
	    });
	
	JButton print = new JButton("Print");
	print.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    printPoints();
		}
	    });

	modeLabel = new JLabel("Move");
	
	buttonPanel.add(add);
	buttonPanel.add(remove);
	buttonPanel.add(move);
	buttonPanel.add(print);
	buttonPanel.add(modeLabel);

	frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.show();
    }


}
	
    
