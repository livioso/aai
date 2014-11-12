package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Instruction extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public Instruction()
	{
		super();
		initialize();
		this.setVisible(true);
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(300, 200);
		this.setLayout(null);
		this.setLocation(40, 600);
	}

	public void paint(Graphics g)
	{
		int start=20;
		int step=15;
		int xpos=0;
		g.setColor(Color.WHITE);
		g.drawString("FUZZY FAN CONTROLLER",xpos ,10);
		g.drawString("Increase Temperature      -Cursor UP",xpos, start+=step);
		g.drawString("Decrease Temperature    -Cursor DOWN",xpos, start+=step);
		g.drawString("Increase Humidity              -Cursor LEFT",xpos, start+=step);
		g.drawString("Decrease Humidity            -CursorRIGHT",xpos, start+=step);
		g.drawString("Quit program                       -Q,q",xpos, start+=step);
	}
}
