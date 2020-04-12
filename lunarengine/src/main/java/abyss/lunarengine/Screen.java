package abyss.lunarengine;

import java.util.Arrays;

public class Screen {
	public static int screenSizeX;
	public static int screenSizeY;
	public static int screenCenterX;
	public static int screenCenterY;
	public static int screenCenterXY;

	public static int screenDeviceSizeX;
	public static int screenDeviceSizeY;
	public static int screenRenderSizeX;
	public static int screenRenderSizeY;

	protected static int renderOffsetX;
	protected static int renderOffsetY;

	static {
		setScreensize(1920, 1080);
	}
	
	public static final void setScreensize(int width, int height) {
		screenSizeX=width;
		screenSizeY=height;
		screenCenterX=Screen.screenSizeX>>1;
		screenCenterY=Screen.screenSizeY>>1;
		screenCenterXY=Screen.screenCenterX+Screen.screenCenterY*Screen.screenSizeX;
	}
	
	public static final void setDeviceScreenSize(int width, int height) {
		screenDeviceSizeX=width;
		screenDeviceSizeY=height;

		screenRenderSizeX=width;
		screenRenderSizeY=height;
		
		screenRenderSizeX=screenSizeX;
		screenRenderSizeY=screenSizeY;

	}
	
 	public static final void screenClear(final int[] screendata,final int color) {
		Arrays.fill(screendata,color);
	}
	
	public static final void screenShade_1p(final int[] screendata) {
		for(int index=0;index<screenSizeX*screenSizeY;index++){
			screendata[index]=(screendata[index] & 0xfefefe)>>1;
		}
	}
	
	public static final void screenShade_2p3(final int[] screendata) {
		int pixel=0;
		for(int index=0;index<screenSizeX*screenSizeY;index++){
			pixel=screendata[index];
			screendata[index]=pixel-((pixel & 0xfcfcfc)>>2);
		}
	}
	
	public static final void screenShade_3p7(final int[] screendata) {
		int pixel=0;
		for(int index=0;index<screenSizeX*screenSizeY;index++){
			pixel=screendata[index];
			screendata[index]=pixel-((pixel & 0xf8f8f8)>>3);
		}
	}
	
	public static final void screenShade_4p15(final int[] screendata) {
		int pixel=0;
		for(int index=0;index<screenSizeX*screenSizeY;index++){
			pixel=screendata[index];
			screendata[index]=pixel-((pixel & 0xf0f0f0)>>4);
		}
	}

	static final void xxxevaluateRenderScaleFlag() {
		if(screenRenderSizeX==screenSizeX && screenRenderSizeY==screenSizeY) {
			APart.scaleRenderedImageToScreen=false;
		}else if(LunarEngine.fullscreenExclusiveMode || screenRenderSizeX<screenSizeX || screenRenderSizeY<screenSizeY){
			APart.scaleRenderedImageToScreen=true;
		}else{
			if(LunarEngine.enableScalingOnBiggerScreens) {
				APart.scaleRenderedImageToScreen=true;
			}else {
				APart.scaleRenderedImageToScreen=false;
				renderOffsetX=(screenDeviceSizeX-screenSizeX)>>1;
				renderOffsetY=(screenDeviceSizeY-screenSizeY)>>1;
			}
		}
		if(LunarEngine.DEBUG) {
			System.out.println("Scale: "+APart.scaleRenderedImageToScreen);
			System.out.println("Render offset: "+renderOffsetX+" / "+renderOffsetY);
		}
	}
	
}
