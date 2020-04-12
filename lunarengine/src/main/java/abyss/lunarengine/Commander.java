package abyss.lunarengine;

import java.util.ArrayList;

public class Commander{
	private static ArrayList<ICommanderAction> commands=new ArrayList<ICommanderAction>();
	private static ArrayList<Object> objects=new ArrayList<Object>();
	private static ArrayList<Integer> framecounters=new ArrayList<Integer>();
	private static int indexCommand=0;
	public static int framecounter=0;
	private static int nextframecounter=-1;
	public static int totalFrameCounter;
	private static long timestamp;
	public static ICommanderAction lastCommnand;
	
	private Commander(){}

	public static int addCommanderAction(int frames, ICommanderAction action){
		return addCommanderAction(frames, action, null);
	}

	public static int addCommanderAction(int frames, ICommanderAction action, Object object){
		commands.add(action);
		objects.add(object);
		framecounters.add(Integer.valueOf(frames));
		return commands.size()-1;
	}

	protected static void vbi(){
		totalFrameCounter++;
		if(framecounter!=0 && --framecounter==0){
			if(nextframecounter>=0){
				framecounter=nextframecounter;
				nextframecounter=-1;
			}else{
				framecounter=framecounters.get(indexCommand).intValue();
			}
			if(objects.size()>indexCommand) {
				if(LunarEngine.DEBUG_LOG) {
					Debug.log("!CMD "+totalFrameCounter+";"+commands.get(indexCommand).getClass().getName()+";"+(System.currentTimeMillis()-timestamp));
				}
				lastCommnand=commands.get(indexCommand);
				lastCommnand.doAction(objects.get(indexCommand));
				if(++indexCommand==commands.size()){
					framecounter=0;	
				}
			}
		}
	}

	public static void start(int delayBeforeExecutingFirstCommandInFrames){
		System.gc();
		indexCommand=0;
		framecounter=delayBeforeExecutingFirstCommandInFrames<1?1:delayBeforeExecutingFirstCommandInFrames;
		if(LunarEngine.DEBUG_LOG) {
			timestamp=System.currentTimeMillis();
		}
	}

	public static void jump(int command){
		indexCommand=command;
		framecounter=1;
	}

	public static void jump(int command,int jumpInFrames, int framecount){
		indexCommand=command;
		framecounter=jumpInFrames;
		nextframecounter=framecount;
	}

	public static void skip() {
		framecounter=1;
	}

}
