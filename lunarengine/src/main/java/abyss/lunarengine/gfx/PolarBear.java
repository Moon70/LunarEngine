package abyss.lunarengine.gfx;

public class PolarBear {
	public static final double DEG2RAD=Math.PI/180.0;
	public static final double RAD2DEG=180.0/Math.PI;
	
	public static double calcAlpha(int x, int y) {
		return Math.atan((double)y/(double)x)*RAD2DEG;
	}

	public static double calcAlpha(double x, double y) {
		return Math.atan(y/x)*RAD2DEG;
	}

	public static double calcRadius(int x,int y) {
		return Math.sqrt(x*x+y*y);
	}

	public static double calcRadius(double x,double y) {
		return Math.sqrt(x*x+y*y);
	}

	public static int calcXint(double radius, double alpha) {
		return (int)(radius*Math.cos(alpha*DEG2RAD)+0.5);
	}
	
	public static int calcYint(double radius, double alpha) {
		return (int)(radius*Math.sin(alpha*DEG2RAD)+0.5);
	}
}
