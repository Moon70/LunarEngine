package abyss.lunarengine.gfx;

public class Point2D {
	public int x;
	public int y;
	
	public Point2D(int x,int y) {
		this.x=x;
		this.y=y;
	}
	
	public String toString() {
		return "Point2D: "+x+"/"+y;
	}
}
