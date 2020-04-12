package abyss.lunarengine;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFrame;

import abyss.lunarengine.tools.Settings;

public class Debug extends JFrame implements ActionListener{
	private static final String PROPERTY_SCREENBOUNDS = "LunarEngineDebugScreenbounds";
	private Settings settings;
	private static final Rectangle DEFAULT_SCREEN_BOUNDS=new Rectangle(0,0,194,435);
	
	private static final int MAXTHREADS=5;
	public volatile static long runtime_realtime=0;
	public volatile static long[] runtime_render=new long[MAXTHREADS];
	private static long timestamp;
	private static long timestampDelta;
	private static long timestampLastRefresh;
	private int screenSizeX=450,screenSizeY=170;
	private static Panel panelData;
	private static long[] timestamp_Work=new long[MAXTHREADS];
	private static long timestamp_VBI;
	private long refreshTimestamp;

	public Debug(String title){
		super("DEBUG: "+title);
		System.out.println("Create debug window...");
		settings=new Settings(new File("c:/temp/LunarEngineDebug.properties"),"LunarEngine Debug Tool");
		setBounds(settings.getRectangle(PROPERTY_SCREENBOUNDS, DEFAULT_SCREEN_BOUNDS));
		setResizable(true);
		setBackground(Color.BLACK);
		super.setBackground(Color.BLACK);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				setVisible(false);
				dispose();
			}
		});

		
		addKeyListener(new DefaultKeyListener());
		setLayout(null);
		panelData=new PanelData();

		panelData.setBackground(new Color(0x000000));

		int x1=4;
		int x2=70;
		int y=4;
		int lineHeight=20;

		Button buttonExit=new Button("Shutdown");
		buttonExit.setActionCommand("EXIT");
		buttonExit.addActionListener(this);
		buttonExit.setBounds(x1, y, 100, 20);
		add(buttonExit);
		y+=lineHeight;

		Button buttonClose=new Button("Close Debug");
		buttonClose.setActionCommand("CLOSE");
		buttonClose.addActionListener(this);
		buttonClose.setBounds(x1, y, 100, 20);
		add(buttonClose);
		y+=lineHeight;

		Button buttonTitle=new Button("Restart");
		buttonTitle.setActionCommand("RESTART");
		buttonTitle.addActionListener(this);
		buttonTitle.setBounds(x1, y, 100, 20);
		add(buttonTitle);
		y+=lineHeight;
		
		Button buttonFreeze=new Button("Freeze");
		buttonFreeze.setActionCommand("FREEZE");
		buttonFreeze.addActionListener(this);
		buttonFreeze.setBounds(x1, y, 100, 20);
		add(buttonFreeze);
		y+=lineHeight;
		
		Button buttonStep=new Button("Step");
		buttonStep.setActionCommand("STEP");
		buttonStep.addActionListener(this);
		buttonStep.setBounds(x1, y, 100, 20);
		add(buttonStep);
		y+=lineHeight;

		Label labelFPS=new Label("VBI FPS: ");
		labelFPS.setBounds(x1, y, 50, 20);
		add(labelFPS);
		TextField textfieldFPS=new TextField("60",1);
		textfieldFPS.setName("FPS");
		textfieldFPS.addActionListener(this);
		textfieldFPS.setBounds(x2, y, 30, 18);
		add(textfieldFPS);
		y+=lineHeight;

		panelData.setBounds(0,y,180,280);
		add(panelData);
		
		refreshTimestamp=System.currentTimeMillis();

		setAlwaysOnTop(true);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event){
		if(event.getSource() instanceof TextField){
			String feldName=((TextField)event.getSource()).getName();
			System.out.println("Debug: TextField: "+feldName+": "+event.getActionCommand());
			if(feldName.equals("FPS")){
				try{
					VBI.setFramerate(Integer.parseInt(event.getActionCommand()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}else{
			String buttonName=event.getActionCommand();
			System.out.println("Debug: ActionEvent: "+buttonName);
			if(buttonName.equals("EXIT")){
				LunarEngine.shutdown();
			}else if(buttonName.equals("CLOSE")){
				this.dispose();
			}else if(buttonName.equals("RESTART")){
				Commander.start(0);
			}else if(buttonName.equals("FREEZE")){
				VBI.togglePause();
			}else if(buttonName.equals("STEP")){
				VBI.setSetp(true);
			}
		}
	}

	public void refresh(){
		long now=System.currentTimeMillis();
		if(refreshTimestamp+(1000/3)<now){
			refreshTimestamp=now;
		}
		panelData.repaint();
	}

	//TODO: Read framecount from VBI
	public static void refresh2(){
		Debug.timestampDelta=System.currentTimeMillis()-Debug.timestamp;
		Debug.timestamp=System.currentTimeMillis();
		if(System.currentTimeMillis()-timestampLastRefresh>500){
			timestampLastRefresh=System.currentTimeMillis();
			if(panelData!=null) panelData.repaint();
		}
	}

	private class PanelData extends Panel{
		private static final long serialVersionUID = 1L;
		private Image dbImage;
		private Graphics dbGraphics;
		private int dataPosY;
		
		private void print(Graphics g,String txt){
			g.drawString(txt,4,dataPosY);
			dataPosY=dataPosY+14;
		}
		
		public void update(Graphics g){
			if (dbImage == null) {
				dbImage = createImage(this.getSize().width,this.getSize().height);
				dbGraphics = dbImage.getGraphics();
			}
			dbGraphics.setColor(getBackground());
			dbGraphics.fillRect(0,0,this.getSize().width,this.getSize().height);
			dbGraphics.setColor(getForeground());
			paint(dbGraphics);
			g.drawImage(dbImage,0,0,this);
		}

		public void paint(Graphics g){
			g.setColor(this.getBackground());
			g.fillRect(0,0,screenSizeX,screenSizeY);
			g.setColor(new Color(0xbbbbbb));
			dataPosY=16;
			print(g,"Frame: "+Commander.totalFrameCounter);
			print(g,"Time: "+Debug.timestampDelta);
			if(Debug.timestampDelta>0){
				print(g,"FPS: "+1000/Debug.timestampDelta);
			}else{
				print(g,"FPS: unknown");
			}
			print(g,"VBI: "+Debug.runtime_realtime);
			print(g,"Worker S: "+Debug.runtime_render[0]);
			print(g,"Worker 1: "+Debug.runtime_render[1]);
			print(g,"Worker 2: "+Debug.runtime_render[2]);
			print(g,"Worker 3: "+Debug.runtime_render[3]);
			print(g,"Comm: "+Commander.framecounter);
			String s="";
			if(Commander.lastCommnand!=null) {
				s=Commander.lastCommnand.getClass().getName();
				int p=s.lastIndexOf('.');
				s=s.substring(p+1);
			}
			print(g,"Comm: "+s);
			print(g,"1 VBI: "+(LunarEngine.partVbi1!=null?LunarEngine.partVbi1.getClass().getSimpleName():""));
			print(g,"1 Work: "+(LunarEngine.partWorker1!=null?LunarEngine.partWorker1.getClass().getSimpleName():""));
			print(g,"2 VBI: "+(LunarEngine.partVbi2!=null?LunarEngine.partVbi2.getClass().getSimpleName():""));
			print(g,"2 Work: "+(LunarEngine.partWorker2!=null?LunarEngine.partWorker2.getClass().getSimpleName():""));
			print(g,"3 VBI: "+(LunarEngine.partVbi3!=null?LunarEngine.partVbi3.getClass().getSimpleName():""));
			print(g,"3 Work: "+(LunarEngine.partWorker3!=null?LunarEngine.partWorker3.getClass().getSimpleName():""));
			print(g,"freeMemory [mb]: "+Runtime.getRuntime().freeMemory()/1024/1024);
			print(g,"totalMemory [mb]: "+Runtime.getRuntime().totalMemory()/1024/1024);
		}
	}
	

	public static void beforeRenderThreadWork(int threadnumber){
		timestamp_Work[threadnumber]=System.currentTimeMillis();
	}

	public static void afterRenderThreadWork(int threadnumber){
		runtime_render[threadnumber]=System.currentTimeMillis()-timestamp_Work[threadnumber];
	}

	public static void beforeVBIWork(){
		timestamp_VBI=System.currentTimeMillis();
	}

	public static void afterVBIWork(){
		runtime_realtime=System.currentTimeMillis()-timestamp_VBI;
	}

	private void saveScreenBounds() {
		settings.setRectangle(PROPERTY_SCREENBOUNDS,getBounds());
		try {
			settings.saveSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void dispose() {
		saveScreenBounds();
		super.dispose();
	}
	
	private static Vector<String> vecLog=new Vector<String>();
	public static void log(String message) {
		vecLog.add(message);
		System.out.println(message);
	}
	
	public static synchronized void createLogfile() {
		File fileTempfolder=new File(System.getProperty("java.io.tmpdir"));
		File fileLog=new File(fileTempfolder,"LunarEngineDebug.log");
		System.out.println("Logfile: "+fileLog.getAbsolutePath());
		if(fileLog.exists()) {
			fileLog.delete();
		}
		try {
			FileWriter fileWriter=new FileWriter(fileLog,true);
			for(int i=0;i<vecLog.size();i++) {
				fileWriter.write(vecLog.get(i));
				fileWriter.write("\n");
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
}
