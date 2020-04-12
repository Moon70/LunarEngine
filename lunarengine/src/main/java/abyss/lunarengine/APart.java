package abyss.lunarengine;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public abstract class APart{
	public static JFrame jFrame;

	protected static boolean scaleRenderedImageToScreen;

	private static boolean dressedInBlack;
	
	public void precalc(){}

	public void initialize(){}

	static final void paintDefault(Graphics graphics) throws Throwable{
		if(APart.scaleRenderedImageToScreen) {
			graphics.drawImage(LunarEngine.bufferedImageToRender,
					0,0,Screen.screenRenderSizeX,Screen.screenRenderSizeY,
					0,0,Screen.screenSizeX,Screen.screenSizeY,
					null);
		}else {
			if(!dressedInBlack) {
				dressedInBlack=true;
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, jFrame.getWidth(), jFrame.getHeight());
			}
			graphics.drawImage(LunarEngine.bufferedImageToRender,
					Screen.renderOffsetX,Screen.renderOffsetY,null);
		}
	}

	public abstract void vbi();

	public void worker1() throws Throwable{}

	public void worker2() throws Throwable{}

	public void worker3() throws Throwable{}

	public void rotateBuffers() {
		rotateBuffers3();
	}
	
	public void rotatePartBuffers() {}
	
	public final void rotateBuffers3() {
		//Work-->Render-->Reset
		LunarEngine.bufferedImageSwap=LunarEngine.bufferedImageToRender;
		LunarEngine.bufferedImageToRender=LunarEngine.bufferedImageToWork;
		LunarEngine.bufferedImageToWork=LunarEngine.bufferedImageToReset;
		LunarEngine.bufferedImageToReset=LunarEngine.bufferedImageSwap;
		LunarEngine.screendataSwap=LunarEngine.screendataToRender;
		LunarEngine.screendataToRender=LunarEngine.screendataToWork;
		LunarEngine.screendataToWork=LunarEngine.screendataToReset;
		LunarEngine.screendataToReset=LunarEngine.screendataSwap;
	}
	
	public final void rotateBuffers4() {
		//Work-->Work2-->Render-->Reset
		LunarEngine.bufferedImageSwap=LunarEngine.bufferedImageToRender;
		LunarEngine.bufferedImageToRender=LunarEngine.bufferedImageToWork2;
		LunarEngine.bufferedImageToWork2=LunarEngine.bufferedImageToWork;
		LunarEngine.bufferedImageToWork=LunarEngine.bufferedImageToReset;
		LunarEngine.bufferedImageToReset=LunarEngine.bufferedImageSwap;
		LunarEngine.screendataSwap=LunarEngine.screendataToRender;
		LunarEngine.screendataToRender=LunarEngine.screendataToWork2;
		LunarEngine.screendataToWork2=LunarEngine.screendataToWork;
		LunarEngine.screendataToWork=LunarEngine.screendataToReset;
		LunarEngine.screendataToReset=LunarEngine.screendataSwap;
	}
	
	public final void rotateBuffers5() {
		//Work-->Work2-->Work3-->Render-->Reset
		LunarEngine.bufferedImageSwap=LunarEngine.bufferedImageToRender;
		LunarEngine.bufferedImageToRender=LunarEngine.bufferedImageToWork3;
		LunarEngine.bufferedImageToWork3=LunarEngine.bufferedImageToWork2;
		LunarEngine.bufferedImageToWork2=LunarEngine.bufferedImageToWork;
		LunarEngine.bufferedImageToWork=LunarEngine.bufferedImageToReset;
		LunarEngine.bufferedImageToReset=LunarEngine.bufferedImageSwap;
		LunarEngine.screendataSwap=LunarEngine.screendataToRender;
		LunarEngine.screendataToRender=LunarEngine.screendataToWork3;
		LunarEngine.screendataToWork3=LunarEngine.screendataToWork2;
		LunarEngine.screendataToWork2=LunarEngine.screendataToWork;
		LunarEngine.screendataToWork=LunarEngine.screendataToReset;
		LunarEngine.screendataToReset=LunarEngine.screendataSwap;
	}
	
}
