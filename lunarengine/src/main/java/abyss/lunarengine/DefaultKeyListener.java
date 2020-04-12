package abyss.lunarengine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DefaultKeyListener extends KeyAdapter{
	private int keyCodePressed;
	private int keyCodeReleased;

	@Override
	public void keyPressed(KeyEvent event){
		keyCodePressed=event.getKeyCode();
		if(keyCodePressed==KeyEvent.VK_ESCAPE){
			LunarEngine.shutdown();
		}else if(keyCodePressed==KeyEvent.VK_RIGHT){
			LunarEngine.right1=true;
		}else if(keyCodePressed==KeyEvent.VK_DOWN){
			LunarEngine.down1=true;
		}else if(keyCodePressed==KeyEvent.VK_LEFT){
			LunarEngine.left1=true;
		}else if(keyCodePressed==KeyEvent.VK_UP){
			LunarEngine.up1=true;
		}else if(keyCodePressed==KeyEvent.VK_X){
			LunarEngine.button1=true;
			LunarEngine.keycode=keyCodePressed;
		}else if(keyCodePressed==KeyEvent.VK_NUMPAD6){
			LunarEngine.right2=true;
		}else if(keyCodePressed==KeyEvent.VK_NUMPAD2){
			LunarEngine.down2=true;
		}else if(keyCodePressed==KeyEvent.VK_NUMPAD4){
			LunarEngine.left2=true;
		}else if(keyCodePressed==KeyEvent.VK_NUMPAD8){
			LunarEngine.up2=true;
		}else if(keyCodePressed==KeyEvent.VK_Y){
			LunarEngine.button2=true;
		}else if(event.getKeyCode()==KeyEvent.VK_F11){
			LunarEngine.toggleFullscreen();
		}else if(event.getKeyCode()==KeyEvent.VK_P){
			VBI.togglePause();
		}else{
			LunarEngine.keycode=keyCodePressed;
		}
	}

	@Override
	public void keyReleased(KeyEvent event){
		keyCodeReleased=event.getKeyCode();
		if(keyCodeReleased==KeyEvent.VK_RIGHT){
			LunarEngine.right1=false;
		}else if(keyCodeReleased==KeyEvent.VK_DOWN){
			LunarEngine.down1=false;
		}else if(keyCodeReleased==KeyEvent.VK_LEFT){
			LunarEngine.left1=false;
		}else if(keyCodeReleased==KeyEvent.VK_UP){
			LunarEngine.up1=false;
		}else if(keyCodeReleased==KeyEvent.VK_NUMPAD6){
			LunarEngine.right2=false;
		}else if(keyCodeReleased==KeyEvent.VK_NUMPAD2){
			LunarEngine.down2=false;
		}else if(keyCodeReleased==KeyEvent.VK_NUMPAD4){
			LunarEngine.left2=false;
		}else if(keyCodeReleased==KeyEvent.VK_NUMPAD8){
			LunarEngine.up2=false;
		}else{
			LunarEngine.keycode=0;
		}
	}

}
