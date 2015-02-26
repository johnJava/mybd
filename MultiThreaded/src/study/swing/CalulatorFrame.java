package study.swing;

import javax.swing.JFrame;

public class CalulatorFrame extends JFrame {
	public CalulatorFrame() {
		// TODO Auto-generated constructor stub
		setTitle("Calulator");
		CalulatorPanel panel = new CalulatorPanel();
		add(panel);
		pack();
	}
}
