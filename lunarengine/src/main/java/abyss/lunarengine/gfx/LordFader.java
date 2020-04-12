package abyss.lunarengine.gfx;

public class LordFader {
	public int[] colors;
	private int size;
	private int currentIndex;
	
	public LordFader(int color1, int color2, int size) {
		this.size=size;
		colors=new int[size];
		
		double red1=(color1>>16)&0xff;
		double green1=(color1>>8)&0xff;
		double blue1=color1&0xff;
		
		double red2=(color2>>16)&0xff;
		double green2=(color2>>8)&0xff;
		double blue2=color2&0xff;
		
		double redDelta=(red2-red1)/(size-1);
		double greenDelta=(green2-green1)/(size-1);
		double blueDelta=(blue2-blue1)/(size-1);
	
		for(int i=0;i<size;i++) {
			colors[i]=(((int)(red1+0.5))<<16) + (((int)(green1+0.5))<<8) + (int)(blue1+0.5);
			red1+=redDelta;
			green1+=greenDelta;
			blue1+=blueDelta;
		}
	}

	public void fade(int delta) {
		currentIndex+=delta;
		if(currentIndex>=size) {
			currentIndex=size-1;
		}else if(currentIndex<0) {
			currentIndex=0;
		}
	}
	
	public int getCurrentColor() {
		return colors[currentIndex];
	}
	
	public void setIndex(int index) {
		currentIndex=index;
	}
	
	public static int colorHalfBright(int color) {
		return (color & 0xfefefe)>>1;
	}
	
}
