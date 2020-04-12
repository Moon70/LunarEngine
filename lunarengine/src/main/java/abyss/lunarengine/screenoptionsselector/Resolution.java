package abyss.lunarengine.screenoptionsselector;

public class Resolution {
	private boolean defaultResolution;
	private int width;
	private int height;
	
	public Resolution(int width, int height) {
		this.width=width;
		this.height=height;
	}
	
	public Resolution(int width, int height,boolean defaultResolution) {
		this(width,height);
		this.defaultResolution=defaultResolution;
	}

	public boolean isDefault() {
		return defaultResolution;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public String toString() {
		return getWidth()+" x "+getHeight();
	}
}
