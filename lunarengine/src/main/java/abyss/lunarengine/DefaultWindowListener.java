package abyss.lunarengine;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DefaultWindowListener extends WindowAdapter{
	public void windowClosing(WindowEvent event){
		LunarEngine.shutdown();
	}
}
