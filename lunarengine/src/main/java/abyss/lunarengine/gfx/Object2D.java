package abyss.lunarengine.gfx;

public class Object2D {
	public static Tools2D tools2D;
	public double alpha;
	public double beta;
	public double gamma;
	public volatile double turnAlpha;
	public volatile double turnBeta;
	public volatile double turnGamma;
	public int focus1;
	public int focus2;
	public int[][] vo;
	public int[][] to;
	public int[][] lo;
	
	private static final double DEG2RAD=Math.PI/180.0;

	public void rotateVBI() {
		alpha+=turnAlpha;
		beta+=turnBeta;
		gamma+=turnGamma;
	}

	public void rotateCALC() {
		int x=0;
		int y=0;
		int z=0;
		int xi=0;
		int yi=0;
		int zi=0;
		double sina=Math.sin(DEG2RAD*alpha);
		double cosa=Math.cos(DEG2RAD*alpha);

		double sinb=Math.sin(DEG2RAD*beta);
		double cosb=Math.cos(DEG2RAD*beta);

		double sinc=Math.sin(DEG2RAD*gamma);
		double cosc=Math.cos(DEG2RAD*gamma);

		double axx=cosa*cosb;
		double axy=cosa*sinb*sinc - sina*cosc;
		double axz=cosa*sinb*cosc + sina*sinc;
		
		double ayx=sina*cosb;
		double ayy=sina*sinb*sinc + cosa*cosc;
		double ayz=sina*sinb*cosc - cosa*sinc;
		
		double azx=-sinb;
		double azy=cosb*sinc;
		double azz=cosb*cosc;
		
		for(int i=0;i<vo.length;i++) {
			x=vo[i][0];
			y=vo[i][1];
			z=vo[i][2];
			
			xi=(int)(axx*x + axy*y + axz*z);
			yi=(int)(ayx*x + ayy*y + ayz*z);
			zi=(int)(azx*x + azy*y + azz*z);

			if(focus2==0) {
				to[i][0]=xi>>Bob.SHIFT;
				to[i][1]=yi>>Bob.SHIFT;
				to[i][2]=zi>>Bob.SHIFT;
			}else {
				x=xi >> Bob.SHIFT;
				y=yi >> Bob.SHIFT;
				z=zi >> Bob.SHIFT;
				
				x=x*focus1/(focus2+z);
				y=y*focus1/(focus2+z);
				
				to[i][0]=x;
				to[i][1]=y;
				to[i][2]=z;
			}
		}
	}

}
