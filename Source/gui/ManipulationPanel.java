package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ManipulationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int upperBorder=20;
	private int bottomBorder=20;
	private int leftBorder=20;
	private int xScale=120;
	private int yScale=200;
	private MainFrame mainFrame;
	private int type;

	/**
	 * This is the default constructor
	 */
	public ManipulationPanel(MainFrame mainFrame,int type)
	{
		super();
		this.type=type;
		this.mainFrame=mainFrame;
		if(type==1)	this.setLocation(40, 350);
		if(type==2)	this.setLocation(180, 350);
		if(type==3)	this.setLocation(320, 350);
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
		this.setSize(xScale, yScale+bottomBorder+upperBorder);
		this.setLayout(null);
	}

	public void paint(Graphics g)
	{
		g.setColor(Color.WHITE);
		Color c=g.getColor();
		for(int i=0;i<=10;i++)
		{
			g.drawString(""+i*10+"%", 0, (int)realYpos(i*10)+2);
		}

		if(type==1)
		{
			double value=mainFrame.getManager().getTemperaturValue();
			g.setColor(Color.BLUE);
			g.fillRect(leftBorder+20, (int)calculateBarPosition(value), 50, upperBorder+yScale-(int)calculateBarPosition(value));
			g.setColor(c);
			g.drawString((int)mainFrame.getManager().getTemperaturValue()+"", leftBorder+30, 20);

		}
		else if(type==2)
		{
			double value=mainFrame.getManager().getHumidityValue();
			g.setColor(Color.ORANGE);
			g.fillRect(leftBorder+20, (int)calculateBarPosition(value), 50, upperBorder+yScale-(int)calculateBarPosition(value));
			g.setColor(c);
			g.drawString((int)mainFrame.getManager().getHumidityValue()+"", leftBorder+30, 20);
		}
		else if(type==3)
		{
			double value=mainFrame.getManager().getFanValue();
			g.setColor(Color.RED);
			g.fillRect(leftBorder+20, (int)calculateBarPosition(value), 50, upperBorder+yScale-(int)calculateBarPosition(value));
			g.setColor(c);
			g.drawString((int)mainFrame.getManager().getFanValue()+"", leftBorder+30, 20);

		}
		g.setColor(c);
	}

	private double realYpos(double y)
	{
		return upperBorder+yScale-(yScale/(100-0)*y);
	}

	private double calculateBarPosition(double value)
	{
		double[] maxmin= getMaxMin();
		return upperBorder+yScale-(yScale/(maxmin[0]-maxmin[1])*(value-maxmin[1]));
	}

	private double[] getMaxMin()
	{
		if(type==1)return new double[]{mainFrame.getManager().getTemperature().getT()[mainFrame.getManager().getTemperature().getT().length-1],mainFrame.getManager().getTemperature().getT()[0]};
		if(type==2)return new double[]{mainFrame.getManager().getHumidity().getH()[mainFrame.getManager().getHumidity().getH().length-1],mainFrame.getManager().getHumidity().getH()[0]};
		if(type==3)return new double[]{mainFrame.getManager().getFanSpeed().getFS()[mainFrame.getManager().getFanSpeed().getFS().length-1],mainFrame.getManager().getFanSpeed().getFS()[0]};
		return null;
	}
}
