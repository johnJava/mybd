package study;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BounceFrame extends JFrame {
	private BallComponent comp;
	private static final int DEFAULT_WIDTH=450;
	private static final int DEFAULT_HEIGHT=350;
	private static final int STEPS = 1000;
	private static final int DELAY = 3;
	
	public BounceFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setTitle("Bounce");
		comp=new BallComponent();
		add(comp,BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		addButton(buttonPanel, "start", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					addBall();
			}
		});
		addButton(buttonPanel, "close", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
			}
		});
		add(buttonPanel,BorderLayout.SOUTH);
	}
	public void addButton(Container c,String title,ActionListener listener){
		JButton button = new JButton(title);
		c.add(button);
		button.addActionListener(listener);
	}
	public void addBall(){
		try{
			Ball ball = new Ball();
			comp.add(ball);
			for (int i = 0; i <= STEPS; i++) {
				ball.move(comp.getBounds());
				comp.paint(comp.getGraphics());
				Thread.sleep(DELAY);
			}
		}catch(InterruptedException e){
			
		}
	}
}
