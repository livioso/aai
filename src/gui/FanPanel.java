package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class FanPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	private int leftBorder=20;
	private int upperBorder=0;
	private int xScale=400;
	private int yScale=200;
	private int bottomBorder=20;


	/**
	 * This is the default constructor
	 */
	public FanPanel(MainFrame mainFrame)
	{
		super();
		this.mainFrame=mainFrame;
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
		this.setSize(xScale+leftBorder, yScale+bottomBorder+upperBorder);
		this.setLayout(null);
		this.setLocation(20, 20);
	}

	public void paint(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillRect((int)realXpos(getPosition())-1, upperBorder+0, 2, yScale);
		g.setColor(Color.WHITE);
		//Y Scale
		g.drawString("0" ,0, (int)realYpos(0)+5);
		g.drawString("0.5" ,0, (int)realYpos(0.5)+5);
		g.drawString("1" ,0, (int)realYpos(1)+10);

		//X|Y Bar
		g.fillRect(leftBorder, yScale,xScale,2);
		g.fillRect(leftBorder, 0,2,yScale);

		//LOW,MED,HIGH
		double[] positions=mainFrame.getManager().getFan_LOW_MED_HIGH_VALUES();
		g.drawLine((int)realXpos(getScale()[0]),  (int)realYpos(positions[0]), (int)realXpos(getScale()[1]),  (int)realYpos(positions[0]));
		g.drawLine((int)realXpos(getScale()[1]),  (int)realYpos(positions[0]), (int)realXpos(getScale()[2]),  (int)realYpos(positions[1]));
		g.drawLine((int)realXpos(getScale()[2]),  (int)realYpos(positions[1]), (int)realXpos(getScale()[3]),  (int)realYpos(positions[2]));
		g.drawLine((int)realXpos(getScale()[3]),  (int)realYpos(positions[2]), (int)realXpos(getScale()[4]),  (int)realYpos(positions[2]));

		String[] descriptions=mainFrame.getManager().getFanSpeed().getDescriptions();
		g.drawString(descriptions[1], leftBorder+70, yScale+20);
		g.drawString(descriptions[2], leftBorder+180, yScale+20);
		g.drawString(descriptions[3], leftBorder+310, yScale+20);
	}

	private double realYpos(double y)
	{
		return upperBorder+yScale-(yScale/(1-0)*y);
	}

	private double realXpos(double x)
	{
		return leftBorder+xScale/(getScale()[getScale().length-1]-getScale()[0])*(x-getScale()[0]);
	}

	private double[] getScale()
	{
		return mainFrame.getManager().getFanSpeed().getFS();
	}

	private double getPosition()
	{
		return mainFrame.getManager().getFanValue();
	}
}
