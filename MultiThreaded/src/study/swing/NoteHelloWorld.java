package study.swing;

import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class NoteHelloWorld {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				SizedFrame frame = new SizedFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

}
