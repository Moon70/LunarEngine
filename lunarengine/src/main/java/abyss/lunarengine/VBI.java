package abyss.lunarengine;

public class VBI {
	private static IClock clock;
	protected static VbiThread vbiThread;
	static volatile boolean pause;
	static volatile boolean step;
	
	public static final void start() {
		vbiThread=new VbiThread();
		vbiThread.setPriority(8);
		vbiThread.start();

		clock=new VbiClock_Candidate7();
		setFramerate(LunarEngine.fps);
	}

	public static final void shutdown() {
		clock.terminate();
		
		if(LunarEngine.DEBUG_LOG) {
			Debug.log("interrupting vbi thread...");
		}
		if(LunarEngine.DEBUG) {
			System.out.println("interrupting vbi thread...");
		}
		vbiThread.interrupt();
	}

	public static final void setFramerate(int fps) {
		clock.setFrameRate(fps);
	}

	public static void setPause(boolean pause) {
		if(LunarEngine.ENABLE_PAUSE) {
			VBI.pause=pause;
		}
	}

	public static void setSetp(boolean step) {
		if(LunarEngine.ENABLE_PAUSE) {
			VBI.step=step;
		}
	}

	public static final void togglePause() {
		setPause(!pause);
	}

}
