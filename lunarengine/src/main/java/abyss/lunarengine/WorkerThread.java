package abyss.lunarengine;

public class WorkerThread extends Thread{
	public volatile boolean enabled=true;
	public volatile boolean isReady;
	volatile boolean signal;
	private int workerNumber;

	protected WorkerThread(int workerNumber){
		super(null,null,"LunarEngineWorker-"+workerNumber);
		this.workerNumber=workerNumber;
	}

	public void run(){
		while(enabled){
			try{
				isReady=true;
				synchronized(this){
					this.wait();
				}
				isReady=false;
				if(LunarEngine.DEBUG){
					Debug.beforeRenderThreadWork(workerNumber);
				}
				LunarEngine.vbiWork(workerNumber);
				signal=false;
				if(LunarEngine.DEBUG){
					Debug.afterRenderThreadWork(workerNumber);
				}
			}catch(InterruptedException e){
				if(LunarEngine.DEBUG_LOG) {
					Debug.log("Worker Thread "+workerNumber+" interrupted!");
				}
				if(LunarEngine.DEBUG) {
					System.out.println("Worker Thread "+workerNumber+" interrupted!");
				}
				break;
			}catch(Throwable throwable){
				if(LunarEngine.DEBUG_LOG) {
					Debug.log("error worker thread "+workerNumber+": "+throwable);
				}
				if(LunarEngine.DEBUG) {
					throwable.printStackTrace();
					LunarEngine.shutdown();
					break;
				}
			}
		}
		if(LunarEngine.DEBUG_LOG) {
			Debug.log("exiting worker thread "+workerNumber+"...");
		}
		if(LunarEngine.DEBUG) {
			System.out.println("exiting worker thread "+workerNumber+"...");
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
		enabled=false;
	}
	
}
