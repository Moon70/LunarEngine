package abyss.lunarengine;

import java.awt.Color;

import javax.swing.JFrame;

public class DefaultJFrame extends JFrame {

	public DefaultJFrame(String title) {
		super(title);
		setBounds(0,0,Screen.screenRenderSizeX,Screen.screenRenderSizeY);
		setBackground(Color.BLACK);
		setResizable(false);
		setUndecorated(true);
		setIgnoreRepaint(true);
		addWindowListener(new DefaultWindowListener());
		addKeyListener(new DefaultKeyListener());
		enableInputMethods(false);
		setVisible(false);
	}

}
