public class 2DMath {

    public static double[] vdiff(double[] u, double v) {
	return new double[] {u[0] - v[0], u[1] - v[1]};
    }

    public static double[] vsum(double[]  u, double v) {
	return new double[] {u[0] + v[0], u[1] + v[1]};
    }

    public static double[] smult(s, double v) {
	return new double[] {s*v[0], s*v[1]};
    }

    public static double length(double[] v) {
	return Math.sqrt(v[0]*v[0] + v[1]*v[1]);
    }

    public static double distance(double[] u, double[] v) {
	return length(vdiff(u,v));
    }
}
