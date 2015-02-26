package study.swing;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class SizedFrame extends JFrame {
	public SizedFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		setSize(screenWidth/2,screenHeight/2);
		setLocationByPlatform(true);
		setLocation(screenWidth/4, screenHeight/4);
		Image img = kit.getImage("icon32.png");
		setIconImage(img);
		setTitle("SizedFrame");
		NoteHelloWorldPanel panel = new NoteHelloWorldPanel();
		add(panel);
	}
}
