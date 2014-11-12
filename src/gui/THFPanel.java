package gui;


import java.awt.Color;
import java.awt.Graphics;


import javax.swing.JPanel;

public class THFPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	private int xScale=400;
	private int yScale=200;
	private int leftBorder=20;
	private int bottomBorder=20;
	private int upperBorder=0;
	private int type;

	/**
	 * This is the default constructor
	 */
	public THFPanel(MainFrame mainFrame,int type)
	{
		super();
		this.type=type;

		this.mainFrame=mainFrame;
		if(type==1){this.setLocation(520, 20);}
		if(type==2){this.setLocation(520, 250);}
		if(type==3){this.setLocation(520, 480);}

		initialize();
		this.setVisible(true);
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(xScale+leftBorder+40, yScale+bottomBorder+upperBorder);
		this.setLayout(null);
		this.setBackground(Color.WHITE);

	}

	private double[] getScale()
	{

		if(type==1)return mainFrame.getManager().getTemperature().getT();
		if(type==2)return mainFrame.getManager().getHumidity().getH();
		if(type==3)return mainFrame.getManager().getFanSpeed().getFS();
		return null;
	}

	private double[] getDefinition1()
	{
		if(type==1)return mainFrame.getManager().getTemperature().getCOOL();
		if(type==2)return mainFrame.getManager().getHumidity().getDRY();
		if(type==3)return mainFrame.getManager().getFanSpeed().getLOW();
		return null;
	}

	private double[] getDefinition2()
	{
		if(type==1)return mainFrame.getManager().getTemperature().getWARM();
		if(type==2)return mainFrame.getManager().getHumidity().getMOIST();
		if(type==3)return mainFrame.getManager().getFanSpeed().getMED();
		return null;
	}

	private double[] getDefinition3()
	{
		if(type==1)return mainFrame.getManager().getTemperature().getHOT();
		if(type==2)return mainFrame.getManager().getHumidity().getWET();
		if(type==3)return mainFrame.getManager().getFanSpeed().getHIGH();
		return null;
	}

	private String[] getDescriptions()
	{
		if(type==1)return mainFrame.getManager().getTemperature().getDescriptions();
		if(type==2)return mainFrame.getManager().getHumidity().getDescriptions();
		if(type==3)return mainFrame.getManager().getFanSpeed().getDescriptions();
		return null;
	}

	private double getPosition()
	{
		if(type==1)return mainFrame.getManager().getTemperaturValue();
		if(type==2)return mainFrame.getManager().getHumidityValue();
		if(type==3)return mainFrame.getManager().getFanValue();
		return 0;
	}

	public void paint(Graphics g)
	{
		//PositionBar
		g.setColor(Color.WHITE);
		g.fillRect((int)realXpos(getPosition())-1, upperBorder+0, 2, yScale);

		//X Scale
		for(int i=0;i<getScale().length;i++)
		{
			int xpos=(int)realXpos(getScale()[i]);

			g.drawString(""+(int)getScale()[i],xpos-10 , yScale+20);
		}
		//Y Scale
		g.drawString("0" ,0, (int)realYpos(0)+5);
		g.drawString("0.5" ,0, (int)realYpos(0.5)+5);
		g.drawString("1" ,0, (int)realYpos(1)+10);

		//Values
		for(int i=0;i<getScale().length-1;i++)
		{
			Color c=g.getColor();
			double x1=realXpos(getScale()[i]);
			double y1=realYpos(getDefinition1()[i]);
			double x2=realXpos(getScale()[i+1]);
			double y2=realYpos(getDefinition1()[i+1]);
			g.setColor(Color.BLUE);
			g.drawString(getDescriptions()[1],leftBorder+10, 20);
			g.drawLine((int)x1, (int)y1,(int) x2,(int) y2);

			x1=realXpos(getScale()[i]);
			y1=realYpos(getDefinition2()[i]);
			x2=realXpos(getScale()[i+1]);
			y2=realYpos(getDefinition2()[i+1]);
			g.setColor(Color.ORANGE);
			g.drawString(getDescriptions()[2],leftBorder+10, 40);
			g.drawLine((int)x1, (int)y1,(int) x2,(int) y2);

			x1=realXpos(getScale()[i]);
			y1=realYpos(getDefinition3()[i]);
			x2=realXpos(getScale()[i+1]);
			y2=realYpos(getDefinition3()[i+1]);
			g.setColor(Color.RED);
			g.drawString(getDescriptions()[3],leftBorder+10, 60);
			g.drawLine((int)x1, (int)y1,(int) x2,(int) y2);

			g.setColor(c);
		}

		//X|Y Bar
		g.fillRect(leftBorder, yScale,xScale,2);
		g.fillRect(leftBorder, 0,2,yScale);
	}

	private double realXpos(double x)
	{
		return leftBorder+xScale/(getScale()[getScale().length-1]-getScale()[0])*(x-getScale()[0]);
	}

	private double realYpos(double y)
	{
		return upperBorder+yScale-(yScale/(1-0)*y);
	}
}
