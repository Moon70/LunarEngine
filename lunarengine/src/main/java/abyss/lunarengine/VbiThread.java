package abyss.lunarengine;

public class VbiThread extends Thread{
	volatile boolean enabled=true;
	volatile int workload;
	
	public VbiThread() {
		super(null,null,"LunarEngineVbi");
	}
	
	public void run(){
		while(enabled){
			try{
				if(workload==0) {
					synchronized(this){
						this.wait();
					}
				}
				workload--;
				if(LunarEngine.DEBUG){
					Debug.beforeVBIWork();
				}
				Commander.vbi();
				LunarEngine.vbi();
				synchronized(LunarEngine.threadEngine){
					LunarEngine.threadEngine.notify();
				}
				if(LunarEngine.DEBUG){
					Debug.afterVBIWork();
				}
			}catch(InterruptedException e){
				if(LunarEngine.DEBUG_LOG) {
					Debug.log("Vbi Thread interrupted!");
				}
				if(LunarEngine.DEBUG) {
					System.out.println("Vbi Thread interrupted!");
				}
				break;
			}catch(Throwable throwable){
				if(LunarEngine.DEBUG_LOG) {
					Debug.log("error vbi thread: "+throwable);
				}
				if(LunarEngine.DEBUG) {
					throwable.printStackTrace();
					LunarEngine.shutdown();
					break;
				}
			}
		}
		if(LunarEngine.DEBUG_LOG) {
			Debug.log("exiting vbi thread...");
		}
		if(LunarEngine.DEBUG) {
			System.out.println("exiting vbi thread...");
		}
	}

	@Override
	public void interrupt() {
		super.interrupt();
		enabled=false;
	}
	
}
