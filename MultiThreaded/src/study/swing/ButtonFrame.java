package study.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ButtonFrame extends JFrame {

	private JPanel buttonPanel;
	public ButtonFrame() {
		// TODO Auto-generated constructor stub
		setTitle("ButtonTest");
		setSize(300, 200);
		JButton yellowButton = new JButton("Yellow");
		JButton blueButton = new JButton("Blue");
		JButton redButton = new JButton("Red");
		buttonPanel= new JPanel();
		buttonPanel.add(yellowButton);
		buttonPanel.add(blueButton);
		buttonPanel.add(redButton);
		add(buttonPanel,BorderLayout.SOUTH);
		ColorAction yellowAction = new ColorAction(Color.YELLOW);
		ColorAction blueAction = new ColorAction(Color.BLUE);
		ColorAction redAction = new ColorAction(Color.RED);
		yellowButton.addActionListener(yellowAction);
		blueButton.addActionListener(blueAction);
		redButton.addActionListener(redAction);
	}
	
	private  class ColorAction implements ActionListener {

		private Color backgroundColor;
		public ColorAction( Color backgroundColor) {
			this.backgroundColor=backgroundColor;
		}
		public void actionPerformed(ActionEvent arg0) {
			buttonPanel.setBackground(backgroundColor);
		}

	}
	
}
