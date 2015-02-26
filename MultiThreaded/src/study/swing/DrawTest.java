package study.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class DrawTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				DrawFrame df = new DrawFrame();
				df.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				df.setVisible(true);
			}
		});
	}

}
