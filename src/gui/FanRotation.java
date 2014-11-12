package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class FanRotation extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame;
	private Polygon p=new Polygon();  //  @jve:decl-index=0:
	private Polygon p2=new Polygon();  //  @jve:decl-index=0:
	private Polygon p3=new Polygon();
	private Polygon pp=new Polygon();  //  @jve:decl-index=0:
	private Thread th;

	private double rotMin=0.0;
	private double rotMax=0.3;
	private double rot=0;


	/**
	 * This is the default constructor
	 */
	public FanRotation(MainFrame mainFrame)
	{
		super();
		this.mainFrame=mainFrame;
		th=new Thread(this);

		p.addPoint(0,25);
		p.addPoint(100,75);
		p.addPoint(100,25);
		p.addPoint(0,75);

		p2.addPoint(0,30);
		p2.addPoint(100,70);
		p2.addPoint(100,30);
		p2.addPoint(0,70);

		p3.addPoint(0,40);
		p3.addPoint(100,60);
		p3.addPoint(100,40);
		p3.addPoint(0,60);

		pp.addPoint(40,50);
		pp.addPoint(48,48);
		pp.addPoint(50,40);
		pp.addPoint(52,48);
		pp.addPoint(60,50);
		pp.addPoint(52,52);
		pp.addPoint(50,60);
		pp.addPoint(48,52);


		initialize();
		this.setVisible(true);
		th.start();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(100, 100);
		this.setLayout(null);
		this.setLocation(400, 600);
	}


	public void paint(Graphics g)
	{
		Graphics2D g2d=(Graphics2D)g;
		g.fillRect(0, 0,200, 200);

		AffineTransform x=g2d.getTransform();
		g2d.rotate(rot,50,50);

		g.setColor(Color.BLUE);
		g2d.fillPolygon(p);

		g.setColor(Color.ORANGE);
		g2d.fillPolygon(p2);

		g.setColor(Color.RED);
		g2d.fillPolygon(p3);



		g2d.setTransform(x);
		g.setColor(Color.YELLOW);
		g.fillOval(40, 40, 20, 20);

		g2d.rotate(rot,50,50);
		g.setColor(Color.darkGray);
		g2d.fillPolygon(pp);

		g2d.setTransform(x);
		g.setColor(Color.BLACK);
		g.fillOval(48, 48, 4, 4);

	}

	public void run()
	{
		while (true)
		{
			rot+=getRotationStep();
			this.repaint();
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	private double getRotationStep()
	{
		double fanValue=mainFrame.getManager().getFanValue();
		double fanMax=mainFrame.getManager().getFanSpeed().getFS()[mainFrame.getManager().getFanSpeed().getFS().length-1];
		double fanMin=mainFrame.getManager().getFanSpeed().getFS()[0];

		double rangeFan=fanMax-fanMin;
		double rangeRot=rotMax-rotMin;
		double realRot=rangeRot/rangeFan*(fanValue-fanMin);
		return realRot;
	}
}
