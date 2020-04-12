package abyss.lunarengine.screenoptionsselector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import abyss.lunarengine.LunarEngine;
import abyss.lunarengine.tools.ObservableJDialog;

public class EditorView extends ObservableJDialog implements ActionListener{
	private Model model;
	private ButtonGroup buttongroupResolutions;
	private JRadioButton[] radiobuttonResolutions;
	private JCheckBox checkboxFullscreenExclusiveMode;
	private JCheckBox checkboxZoom;
	private JButton buttonOk;
	private JButton buttonCancel;
	private static final double SECTIOAUREA=1.6180339887;
	
	public EditorView(JFrame dummyframe,String title,Model model) {
		super(dummyframe,true);
		this.model=model;
		setTitle(title);
		setResizable(true);
		setBackground(new Color(0xdcdcdc));
		setLayout(null);
		setUndecorated(true);
		int dialogWidth=370;
		int dialogHeight=(int)(dialogWidth/SECTIOAUREA);
		Rectangle screenBounds=LunarEngine.getScreenBounds();
		Rectangle dialogBounds=new Rectangle((screenBounds.width-dialogWidth)>>1,(screenBounds.height-dialogHeight)>>1,dialogWidth,dialogHeight);
		setBounds(dialogBounds);

		
		int x=86;
		int y=34;
		JLabel label=new JLabel(title);
		int stringWidth=getFontMetrics(label.getFont()).stringWidth(title);
		label.setBounds(dialogBounds.width-stringWidth-8,dialogBounds.height-(label.getFont().getSize()<<1),200,20);
		add(label);

		int delta=20;

		checkboxFullscreenExclusiveMode=new JCheckBox("fullscreen exclusive mode");
		checkboxFullscreenExclusiveMode.setBounds(x,y,200,20);
		checkboxFullscreenExclusiveMode.setSelected(true);
		model.setFullscreenExclusiveMode(true);
		checkboxFullscreenExclusiveMode.addActionListener(this);
		add(checkboxFullscreenExclusiveMode);
		
		y+=delta;
		checkboxZoom=new JCheckBox("zoom");
		checkboxZoom.setBounds(x,y,200,20);
		checkboxZoom.setSelected(true);
		model.setZoom(true);
		checkboxZoom.setEnabled(true);
		checkboxZoom.addActionListener(this);
		add(checkboxZoom);

		y+=delta+10;
		buttongroupResolutions=new ButtonGroup();
		radiobuttonResolutions=new JRadioButton[2];
		String[] resolutionSelection=model.getResolutionLables();
		for(int i=0;i<resolutionSelection.length;i++) {
			radiobuttonResolutions[i]=new JRadioButton(resolutionSelection[i]);
			radiobuttonResolutions[i].setBounds(x,y,200,20);
			radiobuttonResolutions[i].setSelected(i==0);
			radiobuttonResolutions[i].addActionListener(this);
			add(radiobuttonResolutions[i]);
			buttongroupResolutions.add(radiobuttonResolutions[i]);
			y+=delta;
		}
		y+=30;
		
		int sizeButtonOkX=52;
		int sizeGap=4;
		int sizeButtonCancelX=272;
		x=(dialogBounds.width-(sizeButtonOkX+sizeGap+sizeButtonCancelX))>>1;
		buttonOk=new JButton("Ok");
		buttonOk.setBounds(x,y,sizeButtonOkX,20);
		buttonOk.addActionListener(this);
		add(buttonOk);
		
		buttonCancel=new JButton("Cancel, but i´ll come back later. Promise!");
		buttonCancel.setBounds(x+sizeButtonOkX+sizeGap,y,sizeButtonCancelX,20);
		buttonCancel.addActionListener(this);
		add(buttonCancel);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				setChanged();
				notifyObservers("ESC");
			}
		});
		
		setFocusable(true);
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent event){
				if(event.getKeyCode()==KeyEvent.VK_ESCAPE){
					setChanged();
					notifyObservers("ESC");
				}
			}
		});

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(0x9999bb));
		g.drawLine(0,0,getBounds().width,0);
		g.drawLine(0,1,getBounds().width,1);
		g.drawLine(0,0,0,getBounds().height);
		g.drawLine(1,0,1,getBounds().height);
		g.drawLine(getBounds().width-1,0,getBounds().width-1,getBounds().height);
		g.drawLine(getBounds().width-2,0,getBounds().width-2,getBounds().height);
		g.drawLine(0,getBounds().height-1,getBounds().width,getBounds().height-1);
		g.drawLine(0,getBounds().height-2,getBounds().width,getBounds().height-2);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source=e.getSource();
		if(source instanceof JRadioButton) {
			for(int i=0;i<radiobuttonResolutions.length;i++) {
				if(source==radiobuttonResolutions[i]) {
					model.setSelectedResolutionIndex(i);
					return;
				}
			}
		}
		if(source==checkboxFullscreenExclusiveMode) {
			if(checkboxFullscreenExclusiveMode.isSelected()) {
				model.setFullscreenExclusiveMode(true);
				model.setZoom(true);
				checkboxZoom.setSelected(true);
				for(int i=0;i<radiobuttonResolutions.length;i++) {
					radiobuttonResolutions[i].setEnabled(true);
				}
			}else {
				model.setFullscreenExclusiveMode(false);
				for(int i=0;i<radiobuttonResolutions.length;i++) {
					radiobuttonResolutions[i].setEnabled(false);
				}
			}
		}else if(source==checkboxZoom) {
			model.setZoom(checkboxZoom.isSelected());
		}else if(source==buttonOk) {
			model.setLaunch(true);
			setChanged();
			notifyObservers("ESC");
		}else if(source==buttonCancel) {
			model.setLaunch(false);
			setChanged();
			notifyObservers("ESC");
		}
	}
	
}
