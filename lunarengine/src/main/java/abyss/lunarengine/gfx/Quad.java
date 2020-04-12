package abyss.lunarengine.gfx;

public class Quad extends Object2D{

	public Quad() {
		super();
	}

	public Quad(int[] pointA,int[] pointB,int[] pointC,int[] pointD) {
		vo=new int[4][];
		to=new int[4][3];
		vo[0]=pointA;
		vo[1]=pointB;
		vo[2]=pointC;
		vo[3]=pointD;
	}
	
}
