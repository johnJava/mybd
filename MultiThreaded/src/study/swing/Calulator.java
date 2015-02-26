package study.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;


public class Calulator {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				CalulatorFrame frame = new CalulatorFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
