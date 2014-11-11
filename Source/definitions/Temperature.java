package definitions;

public class Temperature
{

	private String[]  desc	=new String[]{"TEMPERATURE","COOL","WARM","HOT"};
	private double[]  T 	= new double[]{  40,   60,   80,  100,  120};
	private double[]  COOL 	= new double[]{ 1.0,  1.0,    0,    0,    0};
	private double[]  WARM	= new double[]{   0,    0,  1.0,    0,    0};
	private double[]  HOT	= new double[]{   0,    0,    0,  1.0,  1.0};

	public Temperature()
	{

	}

	public double[] getCOOL()
	{
		return COOL;
	}

	public double[] getHOT()
	{
		return HOT;
	}

	public double[] getT()
	{
		return T;
	}

	public double[] getWARM()
	{
		return WARM;
	}

	public String[] getDescriptions()
	{
		return desc;
	}
}
