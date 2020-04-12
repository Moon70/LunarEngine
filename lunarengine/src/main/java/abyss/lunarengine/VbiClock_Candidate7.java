package abyss.lunarengine;

public class VbiClock_Candidate7 extends Thread implements IClock{
	private boolean enabled=true;
	private int framecount;
	private long framesizeNanos;
	
	public VbiClock_Candidate7() {
		super(null,null,"LunarEngineClock");
		setPriority(Thread.MAX_PRIORITY);
		start();
	}
	
	public void run(){
		long sleepMilli;
		long nanoDelta;
		long nanotime=System.nanoTime();
		while(enabled){
			try{
				nanotime+=framesizeNanos;
				nanoDelta=nanotime-System.nanoTime();
				if(nanoDelta>0) {
					sleepMilli=nanoDelta/1000000;
					if(--sleepMilli>0) {
						sleep(sleepMilli);
					};
					while(System.nanoTime()<nanotime) {}
				}
				
				framecount++;

				synchronized(VBI.vbiThread){
					VBI.vbiThread.workload++;
					VBI.vbiThread.notify();
				}

				if(VBI.pause) {
					if(LunarEngine.DEBUG ) {
						while(VBI.pause && !VBI.step){
							Thread.sleep(1000/30);
						}
						VBI.step=false;
					}else {
						while(VBI.pause){
							Thread.sleep(1000/30);
						}
					}
					nanotime=System.nanoTime();
				}
			}catch(InterruptedException e){
				if(LunarEngine.DEBUG_LOG) {
					Debug.log("Clock Thread interrupted!");
				}
				if(LunarEngine.DEBUG) {
					System.out.println("Clock Thread interrupted!");
				}
				break;
			}catch(Throwable throwable){
				if(LunarEngine.DEBUG_LOG) {
					Debug.log("error clock thread: "+throwable);
				}
				if(LunarEngine.DEBUG) {
					throwable.printStackTrace();
					LunarEngine.shutdown();
					break;
				}
			}
		}
		if(LunarEngine.DEBUG_LOG) {
			Debug.log("exiting clock thread...");
		}
		if(LunarEngine.DEBUG) {
			System.out.println("exiting clock thread...");
		}
	}

	public void setFrameRate(int fps) {
		framesizeNanos=1000000000/fps;
		if(LunarEngine.DEBUG_LOG) {
			System.out.println("FrameSizeNan: "+framesizeNanos);
		}
	}

	public void terminate() {
		if(LunarEngine.DEBUG_LOG) {
			Debug.log("interrupting clock thread...");
		}
		if(LunarEngine.DEBUG) {
			System.out.println("interrupting clock thread...");
		}
		interrupt();
		enabled=false;
	}

}
