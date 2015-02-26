package study.swing;

import javax.swing.JFrame;

public class DrawFrame extends JFrame {
	public DrawFrame() {
		// TODO Auto-generated constructor stub
		setTitle("DrawTest");
		setSize(400,400);
		DrawComponent comp = new DrawComponent();
		add(comp);
	}
}
