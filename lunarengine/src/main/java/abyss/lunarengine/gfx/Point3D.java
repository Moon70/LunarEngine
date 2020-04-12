package abyss.lunarengine.gfx;

public class Point3D {
	public int sx;
	public int sy;
	public int sz;

	public int dx;
	public int dy;
	public int dz;

	public int data;
	public boolean enabled=true;
	
	public Point3D(int x,int y, int z, int data) {
		this(x,y,z);
		this.data=data;
	}
	
	public Point3D(int x,int y, int z) {
		this.sx=x;
		this.sy=y;
		this.sz=z;
	}
	
	public void shiftLeft() {
		sx=sx << Bob.SHIFT;
		sy=sy << Bob.SHIFT;
		sz=sz << Bob.SHIFT;
	}
	
	public void move() {}
	
	public String toString() {
		return "Point3D: "+sx+"/"+sy+"/"+sz;
	}
	
}
