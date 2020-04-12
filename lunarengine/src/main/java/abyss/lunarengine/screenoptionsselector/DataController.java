package abyss.lunarengine.screenoptionsselector;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

public class DataController implements Observer{
	private DataService dataService;
	private static EditorView editorView;

	private JFrame dummyframe;
	
	public DataController() throws IOException {
		dataService=DataService.getInstance();
		dummyframe=new JFrame();
		editorView=new EditorView(dummyframe,dataService.getModel().getWindowTitle(),dataService.getModel());
		editorView.addObserver(this);
	}

	public void openGUI()throws Exception{
		editorView.setVisible(true);
		dummyframe.dispose();
	}
	
	public void update(Observable o, Object arg) {
		if(arg instanceof String) {
			if(arg.equals("ESC")){
				exit();
			}
			return;
		}
	}
	
	public void exit() {
		editorView.setVisible(false);
		editorView.dispose();
	}

}
