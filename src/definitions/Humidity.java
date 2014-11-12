package definitions;

public class Humidity
{
	private String[]  desc	=new String[]{"HUMIDITY","DRY","MOIST","WET"};
	private double[]  H 	= new double[]{  20,   40,   60,   80,  100};
	private double[]  DRY 	= new double[]{ 1.0,  1.0,    0,    0,    0};
	private double[]  MOIST	= new double[]{   0,    0,  1.0,    0,    0};
	private double[]  WET	= new double[]{   0,    0,    0,  1.0,  1.0};

	public Humidity()
	{

	}

	public double[] getDRY()
	{
		return DRY;
	}

	public double[] getH()
	{
		return H;
	}

	public double[] getMOIST()
	{
		return MOIST;
	}

	public double[] getWET()
	{
		return WET;
	}

	public String[] getDescriptions()
	{
		return desc;
	}
}
