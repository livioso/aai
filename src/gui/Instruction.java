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
		g.drawString("Fuzzy Fan Controls: ",xpos ,10);
		g.drawString("<Cursor Up> Increase Temperature ",xpos, start+=step);
		g.drawString("<Cursor Down> Decrease Temperature",xpos, start+=step);
		g.drawString("<Cursor Left> Increase Humidity",xpos, start+=step);
		g.drawString("<Cursor Right> Decrease Humidity",xpos, start+=step);
		g.drawString("<q> Quit program",xpos, start+=step);
	}
}
