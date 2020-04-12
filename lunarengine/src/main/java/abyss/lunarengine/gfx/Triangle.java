package abyss.lunarengine.gfx;

import abyss.lunarengine.Screen;

public class Triangle extends Object2D{

	public Triangle() {
		super();
	}

	public Triangle(int[] pointA,int[] pointB,int[] pointC) {
		vo=new int[3][];
		to=new int[3][3];
		vo[0]=pointA;
		vo[1]=pointB;
		vo[2]=pointC;
	}

	public void draw(int color) {
		draw(Screen.screenCenterX,Screen.screenCenterY,color);
	}
	
	public void draw(int xOffset,int yOffset,int color) {
		tools2D.pixel=color;
		tools2D.lineClip(to[0][0]+xOffset,to[0][1]+yOffset,to[1][0]+xOffset,to[1][1]+yOffset);
		tools2D.lineClip(to[1][0]+xOffset,to[1][1]+yOffset,to[2][0]+xOffset,to[2][1]+yOffset);
		tools2D.lineClip(to[2][0]+xOffset,to[2][1]+yOffset,to[0][0]+xOffset,to[0][1]+yOffset);
	}
	
	public void drawFilled(int color) {
		drawFilled(Screen.screenCenterX,Screen.screenCenterY,color);
	}
	
	public void drawFilled(int posX,int posY,int color) {
		sort();
		
		int x1s=to[0][0] << Bob.SHIFT;
		int x2s=x1s;
		int deltaY1=to[2][1]-to[0][1];
		int deltaX1s=deltaY1==0?	((to[2][0]-to[0][0]) << Bob.SHIFT):	((to[2][0]-to[0][0]) << Bob.SHIFT)/deltaY1;
		int deltaY2=to[1][1]-to[0][1];
		int deltaX2s=deltaY2==0?	((to[1][0]-to[0][0]) << Bob.SHIFT):	((to[1][0]-to[0][0]) << Bob.SHIFT)/deltaY2;
		int offsetY;
		int offsetX1;
		int offsetX2;
		int i;
		for(int y=to[0][1];y<to[1][1];y++) {
			offsetY=(y+posY);
			if(offsetY>=0 && offsetY<Screen.screenSizeY) {
				offsetY*=Screen.screenSizeX;
				offsetX1=posX+(x1s>>Bob.SHIFT);
				offsetX2=posX+(x2s>>Bob.SHIFT);
				if(offsetX1<offsetX2) {
					if(offsetX1<0) {
						offsetX1=0;
					}
					if(offsetX2>=Screen.screenSizeX) {
						offsetX2=Screen.screenSizeX-1;
					}
					for(i=offsetX1+offsetY;i<offsetX2+offsetY;i++) {
						tools2D.screendataWorking[i]=color;
					}
				}else {
					if(offsetX2<0) {
						offsetX2=0;
					}
					if(offsetX1>=Screen.screenSizeX) {
						offsetX1=Screen.screenSizeX-1;
					}
					for(i=offsetX2+offsetY;i<offsetX1+offsetY;i++) {
						tools2D.screendataWorking[i]=color;
					}
				}
			}			
			x1s+=deltaX1s;
			x2s+=deltaX2s;
		}
		x2s=to[1][0] << Bob.SHIFT;
		deltaY2=to[2][1]-to[1][1];
		deltaX2s=deltaY2==0?	((to[2][0]-to[1][0]) << Bob.SHIFT):	((to[2][0]-to[1][0]) << Bob.SHIFT)/deltaY2;
		for(int y=to[1][1];y<to[2][1];y++) {
			offsetY=(y+posY);
			if(offsetY>=0 && offsetY<Screen.screenSizeY) {
				offsetY*=Screen.screenSizeX;
				offsetX1=posX+(x1s>>Bob.SHIFT);
				offsetX2=posX+(x2s>>Bob.SHIFT);
				if(offsetX1<offsetX2) {
					for(i=offsetX1+offsetY;i<offsetX2+offsetY;i++) {
						tools2D.screendataWorking[i]=color;
					}
				}else {
					for(i=offsetX2+offsetY;i<offsetX1+offsetY;i++) {
						tools2D.screendataWorking[i]=color;
					}
				}
			}
			x1s+=deltaX1s;
			x2s+=deltaX2s;
		}
	}
	
	private void sort() {
		int swap=0;
		for(int a=0;a<2;a++) {
			for(int b=a+1;b<3;b++) {
				if(to[a][1]>to[b][1]) {
					swap=to[a][0];
					to[a][0]=to[b][0];
					to[b][0]=swap;

					swap=to[a][1];
					to[a][1]=to[b][1];
					to[b][1]=swap;
				}
			}
		}
	}
	
}
