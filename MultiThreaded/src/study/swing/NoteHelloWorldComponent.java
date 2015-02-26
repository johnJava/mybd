package study.swing;

import java.awt.Graphics;

import javax.swing.JComponent;

public class NoteHelloWorldComponent extends JComponent {

	@Override
	protected void paintComponent(Graphics g) {
		g.drawString("Note a Hello World program", 75, 100);
	}

}
