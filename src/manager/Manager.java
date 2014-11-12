package manager;

import logic.Calculate;
import gui.MainFrame;
import definitions.FanSpeed;
import definitions.Humidity;
import definitions.Temperature;

public class Manager
{
	private Temperature temperature;
	private Humidity humidity;
	private FanSpeed fanSpeed;
	private MainFrame mainFrame;

	private double temperaturValue=80;
	private double fanValue=750;
	private double humidityValue=60;
	private Calculate calculate;
	private double[] fan_LOW_MED_HIGH_values;

	public Manager()
	{
		calculate=new Calculate(this);
		temperature=new Temperature();

		humidity=new Humidity();
		fanSpeed=new FanSpeed();
		mainFrame=new MainFrame(this);
		calculate.calc();

	}

	public void setFan_LOW_MED_HIGH_VALUES(double[] fan_LOW_MED_HIGH_values)
	{
		this.fan_LOW_MED_HIGH_values=fan_LOW_MED_HIGH_values;
	}

	public double[] getFan_LOW_MED_HIGH_VALUES()
	{
		return fan_LOW_MED_HIGH_values;
	}

	public FanSpeed getFanSpeed()
	{
		return fanSpeed;
	}

	public Humidity getHumidity()
	{
		return humidity;
	}

	public MainFrame getMainFrame()
	{
		return mainFrame;
	}

	public Temperature getTemperature()
	{
		return temperature;
	}

	public double getFanValue()
	{
		return fanValue;
	}

	public void setFanValue(double fanValue)
	{
		this.fanValue = fanValue;
	}

	public double getHumidityValue()
	{
		return humidityValue;
	}

	public void setHumidityValue(double humidityValue)
	{
		this.humidityValue = humidityValue;
	}

	public double getTemperaturValue()
	{
		return temperaturValue;
	}

	public void setTemperaturValue(double temperaturValue)
	{
		this.temperaturValue = temperaturValue;
	}

	public void decreaseTemperatur()
	{
		if(temperature.getT()[0]<temperaturValue)
		{
			temperaturValue--;
			updateCalulation();
		}
	}
	public void increaseTemperatur()
	{
		if(temperature.getT()[temperature.getT().length-1]>temperaturValue)
		{
			temperaturValue++;
			updateCalulation();
		}
	}

	public void decreaseHumidity()
	{
		if(humidity.getH()[0]<humidityValue)
		{
			humidityValue--;
			updateCalulation();
		}
	}
	public void increaseHumidity()
	{
		if(humidity.getH()[humidity.getH().length-1]>humidityValue)
		{
			humidityValue++;
			updateCalulation();
		}
	}

	private void updateCalulation()
	{
		calculate.calc();
	}

}
