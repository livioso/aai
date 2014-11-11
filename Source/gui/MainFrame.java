package gui;

import javax.swing.JPanel;
import javax.swing.JFrame;

import manager.Manager;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements KeyListener{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private Manager manager;
	private THFPanel temperaturePanel;
	private ManipulationPanel temperatureManipulationPanel;
	private THFPanel humidityPanel;
	private ManipulationPanel humidityManipulationPanel;
	private THFPanel fanSpeedPanel;
	private ManipulationPanel fanSpeedManipulationPanel;
	private FanPanel fanPanel;
	private Instruction instruction;
	private FanRotation fanRotation;

	public MainFrame(Manager manager)
	{
		super();
		this.addKeyListener(this);
		this.manager=manager;
		temperatureManipulationPanel=new ManipulationPanel(this,1);
		temperaturePanel=new THFPanel(this,1);
		humidityManipulationPanel=new ManipulationPanel(this,2);
		humidityPanel=new THFPanel(this,2);
		fanSpeedManipulationPanel=new ManipulationPanel(this,3);
		fanSpeedPanel=new THFPanel(this,3);
		fanPanel=new FanPanel(this);
		instruction=new Instruction();
		fanRotation=new FanRotation(this);
		initialize();
		this.setVisible(true);
	}

	private void initialize()
	{
		this.setSize(1000, 750);
		this.setContentPane(getJContentPane());
		this.setTitle("FuzzyFan");
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setBackground(Color.DARK_GRAY);
			jContentPane.add(temperaturePanel);
			jContentPane.add(temperatureManipulationPanel);
			jContentPane.add(humidityPanel);
			jContentPane.add(humidityManipulationPanel);
			jContentPane.add(fanSpeedPanel);
			jContentPane.add(fanSpeedManipulationPanel);
			jContentPane.add(fanPanel);
			jContentPane.add(instruction);
			jContentPane.add(fanRotation);
		}
		return jContentPane;
	}

	public Manager getManager()
	{
		return manager;
	}

	public void keyPressed(KeyEvent e)
	{
		int keyCode=e.getKeyCode();
		if(keyCode==38)
		{
			manager.increaseTemperatur();this.repaint();
		}
		else if(keyCode==40)
		{
			manager.decreaseTemperatur();this.repaint();
		}
		else if(keyCode==37)
		{
			manager.decreaseHumidity();this.repaint();
		}
		else if(keyCode==39)
		{
			manager.increaseHumidity();this.repaint();
		}
		else if(keyCode==81 || e.getKeyChar()=='Q' || e.getKeyChar()=='q')
		{
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
