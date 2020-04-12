package abyss.lunarengine;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Vector;

import javax.swing.JFrame;

import abyss.lunarengine.screenoptionsselector.DataController;
import abyss.lunarengine.screenoptionsselector.DataService;
import abyss.lunarengine.screenoptionsselector.Model;
import abyss.lunarengine.screenoptionsselector.Resolution;

public class LunarEngine{
	private static final String ENGINENAME="LunarEngine 2.2 by Abyss";
	
	public static final String SYSTEMPROPERTY_FULLSCREEN_EXCLUSIVE_MODE="lunarengine.FullscreenExclusiveMode";
	public static final String SYSTEMPROPERTY_ENABLE_SCALING_ON_BIGGER_SCREEN="lunarengine.EnableScalingOnBiggerScreen";
	public static final String SYSTEMPROPERTY__FPS="lunarengine.fps";
	public static String clientName="";
	public static boolean fullscreenExclusiveMode=false;
	public static boolean hideMousePointer=true;

	static final boolean DEBUG=false;
	public static final boolean DEBUG_LOG=false;
	public static final boolean ENABLE_PAUSE=false;
	private static final boolean ENABLE_TOGGLE_FULLSCREEN=false;
	static final boolean ENABLE_CREATEBUFFERSTRATEGY=true;
	static boolean enableScalingOnBiggerScreens=true;
	static int fps=60;
	static APart partVbi1;
	static APart partVbi2;
	static APart partVbi3;
	static APart partWorker1;
	static APart partWorker2;
	static APart partWorker3;

	public volatile static BufferedImage bufferedImageToReset;
	public volatile static int[] screendataToReset;
	public volatile static BufferedImage bufferedImageToWork;
	public volatile static int[] screendataToWork;
	public volatile static BufferedImage bufferedImageToWork2;
	public volatile static int[] screendataToWork2;
	public volatile static BufferedImage bufferedImageToWork3;
	public volatile static int[] screendataToWork3;
	public volatile static BufferedImage bufferedImageToRender;
	public volatile static int[] screendataToRender;
	public static int[] screendataSwap;
	public static BufferedImage bufferedImageSwap;
	
	private volatile static boolean shutdownRequested;

	static Thread threadEngine;
	static WorkerThread workerThread1;
	static WorkerThread workerThread2;
	static WorkerThread workerThread3;
	private static JFrame jFrameDebug;

	public volatile static boolean left1;
	public volatile static boolean right1;
	public volatile static boolean up1;
	public volatile static boolean down1;
	public volatile static boolean button1;
	public volatile static boolean left2;
	public volatile static boolean right2;
	public volatile static boolean up2;
	public volatile static boolean down2;
	public volatile static boolean button2;
	public volatile static int keycode;

	private static Vector<LunarEngineCallback> callback=new Vector<LunarEngineCallback>();
	private volatile static boolean mainloopEnabled=true;
	private static GraphicsDevice defaultScreenDevice;
	private static DisplayMode oldDisplayMode;

	static{
		System.out.println(ENGINENAME);
		/*
		 * 
		 */
		//System.setProperty("sun.java2d.opengl","true");
	}

	public static final void initializeEngine(LunarEngineCallback lunarEngineCallback){
		callback.add(lunarEngineCallback);
		initializeEngine();
	}
	
	public static final void initializeEngine(){
		fullscreenExclusiveMode=System.getProperty(SYSTEMPROPERTY_FULLSCREEN_EXCLUSIVE_MODE,fullscreenExclusiveMode?"true":"false").equalsIgnoreCase("true");
		enableScalingOnBiggerScreens=System.getProperty(SYSTEMPROPERTY_ENABLE_SCALING_ON_BIGGER_SCREEN,enableScalingOnBiggerScreens?"true":"false").equalsIgnoreCase("true");
		fps=Integer.parseInt(System.getProperty(SYSTEMPROPERTY__FPS,String.valueOf(fps)));
		Rectangle screenBounds=getScreenBounds();
		Screen.setDeviceScreenSize(screenBounds.width, screenBounds.height);
		adjustScreenRenderSize();
		
		if(DEBUG) {
			System.out.println("FPS: "+fps);
			System.out.println("FEM: "+fullscreenExclusiveMode);
			System.out.println("SCALE: "+enableScalingOnBiggerScreens);
			System.out.println("renderOffset: "+Screen.renderOffsetX+" / "+Screen.renderOffsetY);
		}

		APart.jFrame=new DefaultJFrame(clientName);
		if(DEBUG) {
			System.out.println("Frame size: "+APart.jFrame.getBounds());
		}

		createScreenBuffers();
		
		if(hideMousePointer) {
			hideMousePointer();
		}
		
		if(LunarEngine.DEBUG){
			jFrameDebug=new Debug(APart.jFrame.getTitle());
			jFrameDebug.setVisible(true);
		}
	}

	public static void startEngine() throws Throwable{
		try{
			if(fullscreenExclusiveMode){
				activateFullscreen();
			}
			APart.jFrame.setVisible(true);
			APart.jFrame.toFront();
			APart.jFrame.setAlwaysOnTop(true);
			mainloop();
		}finally{
			if(fullscreenExclusiveMode){
				deactivateFullscreen();
			}
		}
	}
	
	private static final void createScreenBuffers() {
		LunarEngine.bufferedImageToReset=new BufferedImage(Screen.screenSizeX,Screen.screenSizeY,BufferedImage.TYPE_INT_RGB);
		LunarEngine.screendataToReset=((DataBufferInt)LunarEngine.bufferedImageToReset.getRaster().getDataBuffer()).getData();
		
		LunarEngine.bufferedImageToWork=new BufferedImage(Screen.screenSizeX,Screen.screenSizeY,BufferedImage.TYPE_INT_RGB);
		LunarEngine.screendataToWork=((DataBufferInt)LunarEngine.bufferedImageToWork.getRaster().getDataBuffer()).getData();

		LunarEngine.bufferedImageToWork2=new BufferedImage(Screen.screenSizeX,Screen.screenSizeY,BufferedImage.TYPE_INT_RGB);
		LunarEngine.screendataToWork2=((DataBufferInt)LunarEngine.bufferedImageToWork2.getRaster().getDataBuffer()).getData();

		LunarEngine.bufferedImageToWork3=new BufferedImage(Screen.screenSizeX,Screen.screenSizeY,BufferedImage.TYPE_INT_RGB);
		LunarEngine.screendataToWork3=((DataBufferInt)LunarEngine.bufferedImageToWork3.getRaster().getDataBuffer()).getData();
		
		LunarEngine.bufferedImageToRender=new BufferedImage(Screen.screenSizeX,Screen.screenSizeY,BufferedImage.TYPE_INT_RGB);
		LunarEngine.screendataToRender=((DataBufferInt)LunarEngine.bufferedImageToRender.getRaster().getDataBuffer()).getData();
	}
	
	public static final void setActivePart(APart part){
		if(part!=null) {
			partVbi1=part;
			partVbi2=null;
			partVbi3=null;
			partWorker1=part;
			partWorker2=part;
			partWorker3=part;
		}else {
			partVbi1=null;
			partVbi2=null;
			partVbi3=null;
			partWorker1=null;
			partWorker2=null;
			partWorker3=null;
		}
	}

	public static final void setPartVbi1(APart part){
		partVbi1=part;
	}

	public static final void setPartVbi2(APart part){
		partVbi2=part;
	}

	public static final void setPartVbi3(APart part){
		partVbi3=part;
	}

	public static final void setPartWorker1(APart part){
		partWorker1=part;
	}

	public static final void setPartWorker2(APart part){
		partWorker2=part;
	}

	public static final void setPartWorker3(APart part){
		partWorker3=part;
	}

	private static final void hideMousePointer() {
		APart.jFrame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(1, 1, BufferedImage.TRANSLUCENT),
				new Point(0,0),
				"InvisibleCursor")
				);
	}

	private static final void activateFullscreen(){
		GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
		defaultScreenDevice=graphicsEnvironment.getDefaultScreenDevice();
		if(!defaultScreenDevice.isFullScreenSupported()) {
			fullscreenExclusiveMode=false;
			if(DEBUG_LOG) {
				Debug.log("screen device doesnt support fullscreen");
			}			
		}
		if(DEBUG_LOG) {
			Debug.log("displaymodes: ");
			DisplayMode[] displayModes=defaultScreenDevice.getDisplayModes();
			for(int i=0;i<displayModes.length;i++) {
				DisplayMode displayMode=displayModes[i];
				Debug.log("\t"+i+": "+displayMode.getWidth()+", "+displayMode.getHeight()+", "+displayMode.getBitDepth()+", "+displayMode.getRefreshRate());
			}
		}
		defaultScreenDevice.setFullScreenWindow(APart.jFrame);
		if(ENABLE_CREATEBUFFERSTRATEGY) {
			if(APart.jFrame.getBufferStrategy()==null) {
				APart.jFrame.createBufferStrategy(2);
			}
		}

		if(defaultScreenDevice.isDisplayChangeSupported()) {
			oldDisplayMode=defaultScreenDevice.getDisplayMode();
			try {
				int bitDepth=oldDisplayMode.getBitDepth();
				int refreshRate=oldDisplayMode.getRefreshRate();
				if(DEBUG_LOG) {
					Debug.log("changing display mode: "+Screen.screenRenderSizeX+", "+Screen.screenRenderSizeY+", "+bitDepth+", "+refreshRate);
				}
				DisplayMode displayMode=new DisplayMode(Screen.screenRenderSizeX,Screen.screenRenderSizeY,bitDepth,refreshRate);
				defaultScreenDevice.setDisplayMode(displayMode);
				fullscreenExclusiveMode=true;
			} catch (IllegalArgumentException e) {
				System.out.println("error switching to fullscreen exclusive mode, could not change display mode");
				e.printStackTrace();
				fullscreenExclusiveMode=false;
			}
		}else {
			fullscreenExclusiveMode=false;
			if(DEBUG_LOG) {
				Debug.log("screen device doesnt support display change");
			}
		}
	}

	private static void deactivateFullscreen(){
		if(oldDisplayMode!=null) {
			defaultScreenDevice.setDisplayMode(oldDisplayMode);
			oldDisplayMode=null;
		}
		defaultScreenDevice.setFullScreenWindow(null);
		fullscreenExclusiveMode=false;
		APart.jFrame.setVisible(true);
		APart.jFrame.toFront();
	}

	public static final void toggleFullscreen(){
		if(LunarEngine.ENABLE_TOGGLE_FULLSCREEN) {
			if(fullscreenExclusiveMode){
				deactivateFullscreen();
			}else{
				activateFullscreen();
			}
		}
	}

	private static void mainloop() throws Throwable{
		threadEngine=Thread.currentThread();
		threadEngine.setPriority(8);

		workerThread1=new WorkerThread(1);
		workerThread1.setPriority(8);
		workerThread1.start();

		workerThread2=new WorkerThread(2);
		workerThread2.setPriority(8);
		workerThread2.start();

		workerThread3=new WorkerThread(3);
		workerThread3.setPriority(8);
		workerThread3.start();
		
		VBI.start();
		final BufferStrategy bufferStrategy=APart.jFrame.getBufferStrategy();
		Graphics graphics;
		while(mainloopEnabled){
			if(workerThread1.isReady && workerThread2.isReady && workerThread3.isReady){
				rotateBuffers();
				synchronized(workerThread1){
					if(workerThread1.isAlive()){
						workerThread1.notify();
					}
				}
				synchronized(workerThread2){
					if(workerThread2.isAlive()){
						workerThread2.notify();
					}
				}
				synchronized(workerThread3){
					if(workerThread3.isAlive()){
						workerThread3.notify();
					}
				}
				if(LunarEngine.DEBUG){
					Debug.refresh2();
				}
				if(LunarEngine.DEBUG){
					Debug.beforeRenderThreadWork(0);
				}
				if(bufferedImageToRender!=null) {
					if(fullscreenExclusiveMode && ENABLE_CREATEBUFFERSTRATEGY) {
						graphics=bufferStrategy.getDrawGraphics();
						APart.paintDefault(graphics);
						graphics.dispose();
						bufferStrategy.show();
					}else {
						graphics=APart.jFrame.getGraphics();
						APart.paintDefault(graphics);
						graphics.dispose();
					}
				}
				if(LunarEngine.DEBUG){
					Debug.afterRenderThreadWork(0);
				}
			}
			synchronized(threadEngine){
				try{
					threadEngine.wait();
				}catch(InterruptedException e){
					if(DEBUG_LOG) {
						Debug.log("engine thread: InterruptedException");
					}
					if(DEBUG) {
						System.out.println("engine thread: InterruptedException");
					}
					break;
				}
			}
		}
		if(DEBUG_LOG) {
			Debug.log("engine thread: exiting main loop");
		}
		if(DEBUG) {
			System.out.println("engine thread: exiting main loop");
		}
		doShutdown();
	}

	protected static final void vbi() throws Throwable{
		if(partVbi1!=null) {
			partVbi1.vbi();
		}
		if(partVbi2!=null) {
			partVbi2.vbi();
		}
		if(partVbi3!=null) {
			partVbi3.vbi();
		}
	}

	protected static final void vbiWork(int threadNumber) throws Throwable{
		switch(threadNumber){
		case 1:
			if(partWorker1!=null){
				partWorker1.worker1();
			}
			break;
		case 2:
			if(partWorker2!=null){
				partWorker2.worker2();
			}
			break;
		case 3:
			if(partWorker3!=null){
				partWorker3.worker3();
			}
			break;
		}
	}

	private static final void rotateBuffers(){
		if(partWorker1!=null){
			partWorker1.rotateBuffers();
		}else if(partWorker2!=null){
			partWorker2.rotateBuffers();
		}else if(partWorker3!=null){
			partWorker3.rotateBuffers();
		}
		if(partWorker1!=null){
			partWorker1.rotatePartBuffers();
		}
		if(partWorker2!=null && partWorker2!=partWorker1){
			partWorker2.rotatePartBuffers();
		}
		if(partWorker3!=null && partWorker3!=partWorker1 && partWorker3!=partWorker2){
			partWorker3.rotatePartBuffers();
		}
	}

	public static final void throwableHandler(Throwable throwable){
		if(DEBUG_LOG) {
			Debug.log("throwableHandler: "+throwable);
		}
		//Amiga-like Guru Meditation still not implemented...
		throwable.printStackTrace();
		System.exit(1);
	}

	public static final void shutdown(){
		if(!shutdownRequested){
			if(DEBUG_LOG) {
				Debug.log("begin shutdown...");
			}
			if(DEBUG) {
				System.out.println("begin shutdown...");
			}
			shutdownRequested=true;
			if(fullscreenExclusiveMode) {
				if(DEBUG_LOG) {
					Debug.log("deactivating fullscreen exclusive mode...");
				}
				if(DEBUG) {
					System.out.println("deactivating fullscreen exclusive mode...");
				}
				deactivateFullscreen();
			}
			
			if(DEBUG_LOG) {
				Debug.log("setting mainloopEnabled to false...");
			}
			mainloopEnabled=false;
			if(threadEngine!=null && threadEngine.isAlive()){
				if(DEBUG_LOG) {
					Debug.log("interrupting engine thread...");
				}
				threadEngine.interrupt();
			}
		}
	}

	private static final void doShutdown(){
		VBI.shutdown();

		if(DEBUG_LOG) {
			Debug.log("interrupting worker thread 1...");
		}
		workerThread1.interrupt();
		
		if(DEBUG_LOG) {
			Debug.log("interrupting worker thread 2...");
		}
		workerThread2.interrupt();
		
		if(DEBUG_LOG) {
			Debug.log("interrupting worker thread 3...");
		}
		workerThread3.interrupt();

		APart.jFrame.setVisible(false);
		for(int i=0;i<callback.size();i++) {
			if(DEBUG_LOG) {
				Debug.log("callback "+i+"...");
			}
			callback.get(i).lunarEngineShutdown();
		}
		LunarEngineTools.sleep(30);
		APart.jFrame.dispose();
		if(jFrameDebug!=null){
			jFrameDebug.setVisible(false);
			jFrameDebug.dispose();
		}
		if(DEBUG_LOG) {
			Debug.createLogfile();
		}
	}

	private static final void adjustScreenRenderSize() {
		if(Screen.screenDeviceSizeX==Screen.screenRenderSizeX && Screen.screenDeviceSizeY==Screen.screenRenderSizeY) {
			return;
		}else if(Screen.screenDeviceSizeX>Screen.screenRenderSizeX || Screen.screenDeviceSizeY>Screen.screenRenderSizeY) {
			Model model=DataService.getInstance().getModel();
			model.setWindowTitle(clientName!=null?clientName:ENGINENAME);
			model.addResolution(new Resolution(Screen.screenSizeX,Screen.screenSizeY,true));
			model.addResolution(new Resolution(Screen.screenDeviceSizeX,Screen.screenDeviceSizeY));
			try {
				new DataController().openGUI();
				if(DEBUG_LOG) {
					Debug.log("Selector: "+model);
				}				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			fullscreenExclusiveMode=model.isFullscreenExclusiveModeEnabled();
			if(fullscreenExclusiveMode) {
				Resolution resolution=model.getSelectedResolution();
				Screen.screenRenderSizeX=resolution.getWidth();
				Screen.screenRenderSizeY=resolution.getHeight();
				if(model.isZoomEnabled()) {
					APart.scaleRenderedImageToScreen=true;
				}else {
					APart.scaleRenderedImageToScreen=false;
					Screen.renderOffsetX=(Screen.screenDeviceSizeX-Screen.screenSizeX)>>1;
					Screen.renderOffsetY=(Screen.screenDeviceSizeY-Screen.screenSizeY)>>1;
				}
			}else {
				Screen.screenRenderSizeX=Screen.screenDeviceSizeX;
				Screen.screenRenderSizeY=Screen.screenDeviceSizeY;
				if(model.isZoomEnabled()) {
					APart.scaleRenderedImageToScreen=true;
				}else {
					APart.scaleRenderedImageToScreen=false;
					Screen.renderOffsetX=(Screen.screenDeviceSizeX-Screen.screenSizeX)>>1;
					Screen.renderOffsetY=(Screen.screenDeviceSizeY-Screen.screenSizeY)>>1;
				}
			}
			if(DEBUG_LOG) {
				Debug.log("Device: "+Screen.screenDeviceSizeX+" x "+Screen.screenDeviceSizeY);
				Debug.log("Screen: "+Screen.screenSizeX+" x "+Screen.screenSizeY);
				Debug.log("Render: "+Screen.screenRenderSizeX+" x "+Screen.screenRenderSizeY);
			}
			if(!model.isLaunchEnabled()) {
				System.exit(0);
			}
		}else {
			APart.scaleRenderedImageToScreen=true;
			Screen.screenRenderSizeX=Screen.screenDeviceSizeX;
			Screen.screenRenderSizeY=Screen.screenDeviceSizeY;
		}
	}

	public static final Rectangle getScreenBounds() {
		GraphicsDevice graphicsDevice=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		GraphicsConfiguration graphicsConfiguration=graphicsDevice.getDefaultConfiguration();
		return graphicsConfiguration.getBounds();
	}
	
}
