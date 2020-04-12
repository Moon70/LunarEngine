package abyss.lunarengine.gfx;

import abyss.lunarengine.Screen;

public class Font {
	protected String characterset;
	protected int height;
	protected int[][] index;
	public int[] fontdata;
	protected int fontWidth;
	public int space=70;
	
	public int writeposX;
	public int writeposY;
	public boolean noclipping;
	
	public Font(String fontname) {
		FontFactory.loadFont(this, fontname);
	}

	public void prepare() {}
	
	public void render(String s,int x,int y,int[] screendataWorking) {
		render(s,x,y,screendataWorking,Screen.screenSizeX);
	}
	
	public void render(String s,int x,int y,int[] screendataWorking, int screenSizeX) {
		writeposX=x;
		writeposY=y;
		for(int i=0;i<s.length();i++) {
			render(s.charAt(i),screendataWorking,screenSizeX);
		}
	}
	
	private int render(char c,int[] screendataWorking,int screenSizeX) {
		int i=characterset.indexOf(c);
		int pixelPosX;
		int pixelPosY;
		int pixel;
		int offsetY;
		if(i!=-1) {
			for(int y=0;y<height;y++) {
				pixelPosY=writeposY+y;
				offsetY=pixelPosY*screenSizeX;
				for(int x=0;x<index[i][2];x++) {
					pixel=fontdata[index[i][0]+x+(index[i][1]+y)*fontWidth];
					pixelPosX=writeposX+x;
					if((pixel & 0xffffff) !=0) {
						if(noclipping || (pixelPosX>=0 && pixelPosX<Screen.screenSizeX && pixelPosY>=0 && pixelPosY<Screen.screenSizeY)) {
							screendataWorking[pixelPosX+offsetY]=pixel;
						}
					}
				}
			}
			writeposX+=index[i][2]+10;
			return index[i][2]+10;
		}else {
			for(int y=0;y<height;y++) {
				offsetY=(writeposY+y)*screenSizeX;
				for(int x=0;x<space;x++) {
					screendataWorking[writeposX+x+offsetY]=0;
				}
			}
			writeposX+=space;
			return space;
		}
	}

	public int render(char c,int x,int y,int[] screendataWorking) {
		writeposX=x;
		writeposY=y;
		return render(c,screendataWorking,Screen.screenSizeX);
	}
	
	public int render(char c,int x,int y,int[] screendataWorking,int screenSizeX) {
		writeposX=x;
		writeposY=y;
		return render(c,screendataWorking,screenSizeX);
	}
	
	public int getCharWidth(char c) {
		int i=characterset.indexOf(c);
		return i!=-1?index[i][2]:space;
	}
	
}
