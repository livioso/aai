package definitions;

public class FanSpeed
{
	private String[]  desc	=new String[]{"FANSPEED","LOW","MED","HIGH"};
	private double[]  FS 	= new double[]{ 250,  500,  750, 1000, 1250};
	private double[]  LOW 	= new double[]{ 1.0,  1.0,    0,    0,    0};
	private double[]  MED	= new double[]{   0,    0,  1.0,    0,    0};
	private double[]  HIGH	= new double[]{   0,    0,    0,  1.0,  1.0};

	public FanSpeed()
	{

	}

	public double[] getFS()
	{
		return FS;
	}

	public double[] getHIGH()
	{
		return HIGH;
	}

	public double[] getLOW()
	{
		return LOW;
	}

	public double[] getMED()
	{
		return MED;
	}

	public String[] getDescriptions()
	{
		return desc;
	}
}
