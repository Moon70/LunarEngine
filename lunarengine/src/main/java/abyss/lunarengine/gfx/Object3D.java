package abyss.lunarengine.gfx;

import java.util.Vector;

public class Object3D {
	public static Tools2D tools2D;
	public double alpha;
	public double beta;
	public double gamma;
	public volatile double turnAlpha;
	public volatile double turnBeta;
	public volatile double turnGamma;
	public int focus1;
	public int focus2;
	public int deltaX;
	public int deltaY;
	public int deltaZ;
	public Point3D[] points;

	private static final double DEG2RAD=Math.PI/180.0;
	private Vector<Point3D> vecPoints=new Vector<Point3D>();
	
	public void addPoint(Point3D point) {
		vecPoints.add(point);
	}

	public void createArrays() {
		System.out.println("Object3D.createArrays: "+vecPoints.size());
		points=new Point3D[vecPoints.size()];
		for(int i=0;i<vecPoints.size();i++) {
			Point3D point=vecPoints.get(i);
			point.shiftLeft();
			points[i]=point;
		}
	}

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
		
		int f;
		int deltaXs=deltaX<<Bob.SHIFT;
		int deltaYs=deltaY<<Bob.SHIFT;
		int deltaZs=deltaZ<<Bob.SHIFT;
		for(int i=0;i<points.length;i++) {
			if(!points[i].enabled) {
				continue;
			}
			x=points[i].sx+(deltaXs);
			y=points[i].sy+(deltaYs);
			z=points[i].sz+(deltaZs);
			
			xi=(int)(axx*x + axy*y + axz*z);
			yi=(int)(ayx*x + ayy*y + ayz*z);
			zi=(int)(azx*x + azy*y + azz*z);

			if(focus2==0) {
				points[i].dx=xi>>Bob.SHIFT;
				points[i].dy=yi>>Bob.SHIFT;
				points[i].dz=zi>>Bob.SHIFT;
			}else {
				z=zi >> Bob.SHIFT;
				
				f=(focus2-(z>>1));
				if(f!=0) {
					points[i].dx=(xi >> Bob.SHIFT)*focus1/f;
					points[i].dy=(yi >> Bob.SHIFT)*focus1/f;
				}else {
					points[i].dx=xi >> Bob.SHIFT;
					points[i].dy=yi >> Bob.SHIFT;
				}
				points[i].dz=z;
			}
		}
	}

}
