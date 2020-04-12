package abyss.lunarengine.gfx;

import abyss.lunarengine.LunarEngine;
import abyss.lunarengine.Screen;
import abyss.lunarengine.tools.Random;

public class Tools2D {
	public int[] screendataWorking;
	public int pixel;
	public int background;
	public int rnd=20;
	public int lightningLineParts=3;

	private static final boolean BOLD=true;
	private static final int SHIFT=12;
	private static int clipX1;
	private static int clipY1;
	private static int clipX2;
	private static int clipY2;
	private static int clipX1shift;
	private static int clipY1shift;
	private static int clipY1screen;
	private static int clipX2shift;
	private static int clipY2shift;
	private static int clipY2screen;

	public Tools2D() {
		setClippingArea(0,0,Screen.screenSizeX-1,Screen.screenSizeY-1);
		screendataWorking=LunarEngine.screendataToWork;
	}
	
	public void line(int x1,int y1, int x2, int y2) {
		int xd;
		int xs;
		int yd;
		int ys;
		int deltaX=x2-x1;
		int deltaY=y2-y1;
		int index;
		if( Math.abs(deltaX) > Math.abs(deltaY) ) {
			if(deltaX>0) {
				ys=y1<<SHIFT;
				yd=(deltaY<<SHIFT)/deltaX;
				for(int x=x1;x<x2;x++,ys+=yd) {
					index=x+(ys>>SHIFT)*Screen.screenSizeX;
					screendataWorking[index]=pixel;
					if(BOLD) {
						screendataWorking[index+1]=pixel;
					}
				}
			}else if(deltaX<0) {
				ys=y2<<SHIFT;
				yd=(deltaY<<SHIFT)/deltaX;
				for(int x=x2;x<x1;x++,ys+=yd) {
					index=x+(ys>>SHIFT)*Screen.screenSizeX;
					screendataWorking[index]=pixel;
					if(BOLD) {
						screendataWorking[index+1]=pixel;
					}
				}
			}
		}else {
			if(deltaY>0) {
				xs=x1<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y1*Screen.screenSizeX;y<y2*Screen.screenSizeX;y+=Screen.screenSizeX,xs+=xd) {
					index=(xs>>SHIFT)+y;
					screendataWorking[index]=pixel;
					if(BOLD) {
						screendataWorking[index+1]=pixel;
					}
				}
			}else if(deltaY<0) {
				xs=x2<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y2*Screen.screenSizeX;y<y1*Screen.screenSizeX;y+=Screen.screenSizeX,xs+=xd) {
					index=(xs>>SHIFT)+y;
					screendataWorking[index]=pixel;
					if(BOLD) {
						screendataWorking[index+1]=pixel;
					}
				}
			}
		}
	}

	public void dottyLine(int x1,int y1, int x2, int y2) {
		int xd;
		int xs;
		int yd;
		int ys;
		int deltaX=x2-x1;
		int deltaY=y2-y1;
		int index;
		int xRnd,yRnd;
		if( Math.abs(deltaX) > Math.abs(deltaY) ) {
			if(deltaX>0) {
				ys=y1<<SHIFT;
				yd=(deltaY<<SHIFT)/deltaX;
				for(int x=x1;x<x2;x++,ys+=yd) {
					xRnd=x+getRND();
					yRnd=((ys>>SHIFT)+getRND());
					if(xRnd>=clipX1 && xRnd<clipX2 && yRnd>=clipY1 && yRnd<clipY2) {
						index=xRnd+yRnd*Screen.screenSizeX;
						screendataWorking[index]=pixel;
						if(BOLD) {
							screendataWorking[index+1]=pixel;
						}
					}
				}
			}else if(deltaX<0) {
				ys=y2<<SHIFT;
				yd=(deltaY<<SHIFT)/deltaX;
				for(int x=x2;x<x1;x++,ys+=yd) {
					xRnd=x+getRND();
					yRnd=((ys>>SHIFT)+getRND());
					if(xRnd>=clipX1 && xRnd<clipX2 && yRnd>=clipY1 && yRnd<clipY2) {
						index=xRnd+yRnd*Screen.screenSizeX;
						screendataWorking[index]=pixel;
						if(BOLD) {
							screendataWorking[index+1]=pixel;
						}
					}
				}
			}
		}else {
			if(deltaY>0) {
				xs=x1<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y1;y<y2;y++,xs+=xd) {
					xRnd=((xs>>SHIFT)+getRND());
					yRnd=(y+getRND());
					if(xRnd>=clipX1 && xRnd<clipX2 && yRnd>=clipY1 && yRnd<clipY2) {
						index=xRnd+yRnd*Screen.screenSizeX;
						screendataWorking[index]=pixel;
						if(BOLD) {
							screendataWorking[index+1]=pixel;
						}
					}
				}
			}else if(deltaY<0) {
				xs=x2<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y2;y<y1;y++,xs+=xd) {
					xRnd=((xs>>SHIFT)+getRND());
					yRnd=(y+getRND());
					if(xRnd>=clipX1 && xRnd<clipX2 && yRnd>=clipY1 && yRnd<clipY2) {
						index=xRnd+yRnd*Screen.screenSizeX;
						screendataWorking[index]=pixel;
						if(BOLD) {
							screendataWorking[index+1]=pixel;
						}
					}
				}
			}
		}
	}

	public void lineClip(int x1,int y1, int x2, int y2) {
		int xd;
		int xs;
		int yd;
		int ys;
		int deltaX=x2-x1;
		int deltaY=y2-y1;
		int index;
		if( Math.abs(deltaX) > Math.abs(deltaY) ) {
			if(deltaX>0) {
				ys=y1<<SHIFT;
				yd=(deltaY<<SHIFT)/deltaX;
				for(int x=x1;x<x2;x++,ys+=yd) {
					index=x+(ys>>SHIFT)*Screen.screenSizeX;
					if(x>=clipX1 && x<clipX2 && ys>=clipY1shift && ys<clipY2shift) {
						screendataWorking[index]=pixel;
						if(BOLD) {
							screendataWorking[index+1]=pixel;
						}
					}
				}
			}else if(deltaX<0) {
				ys=y2<<SHIFT;
				yd=(deltaY<<SHIFT)/deltaX;
				for(int x=x2;x<x1;x++,ys+=yd) {
					index=x+(ys>>SHIFT)*Screen.screenSizeX;
					if(x>=clipX1 && x<clipX2 && ys>=clipY1shift && ys<clipY2shift) {
						screendataWorking[index]=pixel;
						if(BOLD) {
							screendataWorking[index+1]=pixel;
						}
					}
				}
			}
		}else {
			if(deltaY>0) {
				xs=x1<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y1*Screen.screenSizeX;y<y2*Screen.screenSizeX;y+=Screen.screenSizeX,xs+=xd) {
					if(y>=clipY1screen && y<clipY2screen && xs>=clipX1shift && xs<clipX2shift) {
						index=(xs>>SHIFT)+y;
						screendataWorking[index]=pixel;
						if(BOLD) {
							screendataWorking[index+1]=pixel;
						}
					}
				}
			}else if(deltaY<0) {
				xs=x2<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y2*Screen.screenSizeX;y<y1*Screen.screenSizeX;y+=Screen.screenSizeX,xs+=xd) {
					if(y>=clipY1screen && y<clipY2screen && xs>=clipX1shift && xs<clipX2shift) {
						index=(xs>>SHIFT)+y;
						screendataWorking[index]=pixel;
						if(BOLD) {
							screendataWorking[index+1]=pixel;
						}
					}
				}
				return;
			}
		}
	}

	public void lineClipBold(int x1,int y1, int x2, int y2) {
		int xd;
		int xs;
		int yd;
		int ys;
		int deltaX=x2-x1;
		int deltaY=y2-y1;
		int index;
		if( Math.abs(deltaX) > Math.abs(deltaY) ) {
			if(deltaX>0) {
				ys=y1<<SHIFT;
				yd=(deltaY<<SHIFT)/deltaX;
				for(int x=x1;x<x2;x++,ys+=yd) {
					index=x+(ys>>SHIFT)*Screen.screenSizeX;
					if(x>=clipX1 && x<clipX2 && ys>=clipY1shift && ys<clipY2shift) {
						screendataWorking[index]=pixel;
						screendataWorking[++index]=pixel;
						screendataWorking[index+=Screen.screenSizeX]=pixel;
						screendataWorking[--index]=pixel;
					}
				}
			}else if(deltaX<0) {
				ys=y2<<SHIFT;
				yd=(deltaY<<SHIFT)/deltaX;
				for(int x=x2;x<x1;x++,ys+=yd) {
					index=x+(ys>>SHIFT)*Screen.screenSizeX;
					if(x>=clipX1 && x<clipX2 && ys>=clipY1shift && ys<clipY2shift) {
						screendataWorking[index]=pixel;
						screendataWorking[++index]=pixel;
						screendataWorking[index+=Screen.screenSizeX]=pixel;
						screendataWorking[--index]=pixel;
					}
				}
			}
		}else {
			if(deltaY>0) {
				xs=x1<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y1*Screen.screenSizeX;y<y2*Screen.screenSizeX;y+=Screen.screenSizeX,xs+=xd) {
					if(y>=clipY1screen && y<clipY2screen && xs>=clipX1shift && xs<clipX2shift) {
						index=(xs>>SHIFT)+y;
						screendataWorking[index]=pixel;
						screendataWorking[++index]=pixel;
						screendataWorking[index+=Screen.screenSizeX]=pixel;
						screendataWorking[--index]=pixel;
					}
				}
			}else if(deltaY<0) {
				xs=x2<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y2*Screen.screenSizeX;y<y1*Screen.screenSizeX;y+=Screen.screenSizeX,xs+=xd) {
					if(y>=clipY1screen && y<clipY2screen && xs>=clipX1shift && xs<clipX2shift) {
						index=(xs>>SHIFT)+y;
						screendataWorking[index]=pixel;
						screendataWorking[++index]=pixel;
						screendataWorking[index+Screen.screenSizeX]=pixel;
						screendataWorking[--index]=pixel;
					}
				}
				return;
			}
		}
	}

	public void lineB(int x1,int y1, int x2, int y2) {
		int xd;
		int xs;
		int deltaX;
		int deltaY;
		int index;
		if(y1<y2) {
			if((deltaY=y2-y1)==0) {
				return;
			}
			if(x1<x2) {
				deltaX=x2-x1;
				xs=x1<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y1*Screen.screenSizeX;y<y2*Screen.screenSizeX;y+=Screen.screenSizeX,xs+=xd) {
					index=(xs>>SHIFT)+y;
					if(screendataWorking[index]!=background) {
						screendataWorking[index]=background;
					}else {
						screendataWorking[index]=pixel;
					}
				}
			}else {
				deltaX=x1-x2;
				xs=x1<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y1*Screen.screenSizeX;y<y2*Screen.screenSizeX;y+=Screen.screenSizeX,xs-=xd) {
					index=(xs>>SHIFT)+y;
					if(screendataWorking[index]!=background) {
						screendataWorking[index]=background;
					}else {
						screendataWorking[index]=pixel;
					}
				}
			}
		}else {
			if((deltaY=y1-y2)==0){
				return;
			}
			if(x1<x2) {
				deltaX=x2-x1;
				xs=x2<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y2*Screen.screenSizeX;y<y1*Screen.screenSizeX;y+=Screen.screenSizeX,xs-=xd) {
					index=(xs>>SHIFT)+y;
					if(screendataWorking[index]!=background) {
						screendataWorking[index]=background;
					}else {
						screendataWorking[index]=pixel;
					}
				}
			}else {
				deltaX=x1-x2;
				xs=x2<<SHIFT;
				xd=(deltaX<<SHIFT)/deltaY;
				for(int y=y2*Screen.screenSizeX;y<y1*Screen.screenSizeX;y+=Screen.screenSizeX,xs+=xd) {
					index=(xs>>SHIFT)+y;
					if(screendataWorking[index]!=background) {
						screendataWorking[index]=background;
					}else {
						screendataWorking[index]=pixel;
					}
				}
			}
		}
	}

	public void lightningLine(int x1,int y1, int x2, int y2) {
		int stepX=(x2-x1)/lightningLineParts;
		int stepY=(y2-y1)/lightningLineParts;

		int xPos1=x1;
		int yPos1=y1;
		
		x1+=stepX;
		y1+=stepY;
		
		int xPos2=xPos1+stepX+getRND();
		int yPos2=yPos1+stepY+getRND();

		for(int i=1;i<lightningLineParts;i++) {
			lineClip(xPos1,yPos1,xPos2,yPos2);
			
			xPos1=xPos2;
			yPos1=yPos2;

			x1+=stepX;
			y1+=stepY;
			
			xPos2=x1+getRND();
			yPos2=y1+getRND();
		}
		lineClip(xPos1,yPos1,x2,y2);
	}

	private int getRND() {
		return (int)(Random.random()*(rnd+1))-(rnd>>1);
	}

	public static void setClippingArea(int x1,int y1,int x2,int y2){
		clipX1=x1;
		clipY1=y1;
		if(BOLD) {
			clipX2=x2-1;
			clipY2=y2-1;
		}else {
			clipX2=x2;
			clipY2=y2;
		}
		clipX1shift=x1<<SHIFT;
		clipY1shift=y1<<SHIFT;
		clipX2shift=x2<<SHIFT;
		clipY2shift=y2<<SHIFT;
		clipY1screen=y1*Screen.screenSizeX;
		clipY2screen=y2*Screen.screenSizeX;
	}


	public void fill() {
		int index;
		for(int y=0;y<Screen.screenSizeY*Screen.screenSizeX;y+=Screen.screenSizeX){
			pixel=background;
			for(index=y;index<y+Screen.screenSizeX;index++){
				if(screendataWorking[index]==background){
					screendataWorking[index]=pixel;
				}else {
					if(pixel==background) {
						pixel=screendataWorking[index];
					}else {
						pixel=background;
					}
				}
			}
		}
	}

}
