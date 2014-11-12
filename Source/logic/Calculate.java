package logic;

import manager.Manager;
import definitions.Humidity;
import definitions.Temperature;

public class Calculate
{
	private Manager manager;

	public Calculate(Manager manager)
	{
		this.manager=manager;
	}

	public void calc()
	{
		double[] temperature=getTemperatureValues(manager.getTemperaturValue());
		double[] humidity=getHumidityValues(manager.getHumidityValue());
		double H_WET   = humidity[2];
		double H_MOIST = humidity[1];
		double H_DRY   = humidity[0];

		double T_COOL  = temperature[0];
		double T_WARM  = temperature[1];
		double T_HOT   = temperature[2];
		System.out.println("TEMP: "+T_COOL+"<->"+T_WARM+"<->"+T_HOT);
		System.out.println("HUM: "+H_WET+"<->"+H_MOIST+"<->"+H_DRY);

	  /* initialize output membership values.. */

		double FS_LOW  = 0.0;
		double FS_MED  = 0.0;
		double FS_HIGH = 0.0;

		/* The following executes the rule base used for
		    * the Fuzzy Logic Expert FAN Controller. This algorithm
		    * is based on the "max product" implication. */

		    /* IF H = WET THEN FS = HIGH                 */
		    FS_HIGH = max(H_WET,          FS_HIGH);

		    /* IF T = COOL AND H = DRY THEN FS = MED     */
		    FS_MED  = max(T_COOL*H_DRY,   FS_MED);

		    /* IF T = COOL AND H = MOIST THEN FS = HIGH  */
		    FS_HIGH = max(T_COOL*H_MOIST, FS_HIGH);

		    /* IF T = WARM AND H = DRY THEN FS = LOW     */
		    FS_LOW  = max(T_WARM*H_DRY,   FS_LOW);

		    /* IF T = WARM AND H = MOIST THEN FS = MED   */
		    FS_MED  = max(T_WARM*H_MOIST, FS_MED);

		    /* IF T = HOT AND H = DRY THEN FS = MED      */
		    FS_MED  = max(T_HOT*H_DRY,    FS_MED);

		    /* IF T = HOT AND H = MOIST THEN FS = HIGH   */
		    FS_HIGH = max(T_HOT*H_MOIST,  FS_HIGH);
		    double[] R = new double[manager.getFanSpeed().getLOW().length];               /* R[] - interm inference result        */
		    double[] O = new double[manager.getFanSpeed().getLOW().length];              /* O[] - accumulative inference result  */


		    double[] LOW = manager.getFanSpeed().getLOW();
		    double[] MED = nmanager.getFanSpeed().getMED();
		    double[] HIGH = manager.getFanSpeed().getHIGH();
		    R=DoInferEngine(FS_LOW,  LOW, R);
		    O=GetMaximum(R, O, O);

		    R=DoInferEngine(FS_MED,  MED, R);
		    O=GetMaximum(R, O, O);

		    R=DoInferEngine(FS_HIGH, HIGH, R);
		    O=GetMaximum(R, O, O);

		    /* Ok.. defuzzify and output crisp result */
		    double output=DeFuzzyOutput(manager.getFanSpeed().getFS(), O);
		    manager.setFanValue(output);
		    System.out.println(FS_LOW+"<.>"+FS_MED+"<->"+FS_HIGH);
		    manager.setFan_LOW_MED_HIGH_VALUES(new double[]{FS_LOW,FS_MED,FS_HIGH});
	}

	private double DeFuzzyOutput(double[] Y, double[] B)
	{
		  int   si;
		  double Yo=-1;
		  double yB_sum = 0.0;
		  double B_sum  = 0.0;

		  /* get summations of Y[]B[] and B[] */
		  for (si = 0; si < (Y.length -1); si++) {
		    yB_sum += ((Y[si] + Y[si+1])/2)*((B[si] + B[si+1])/2);
		     B_sum += (B[si] + B[si+1])/2;
		  }

		  /* check for divide by zero */
		  if (B_sum != 0.0) {
		    Yo = yB_sum/B_sum;
		  }

		  return Yo;
	}

	private double[] DoInferEngine(double Wo, double[] C, double[] O)
	{
	  int   si;

	  for (si = 0; si < O.length; si++)
	  {
	    O[si] = Wo*C[si];
	  }
	  return O;
	}

	private double[] GetMaximum(double[] A, double[] B, double[] C)
	{
	  int   si;

	  for (si = 0; si < A.length; si++)
	  {
	    C[si] = max(A[si], B[si]);
	  }

	  return C;
	}

	private double max(double a,double b)
	{
		if (a>b)
		return a;
		return b;
	}

	private double calculateY(double xValue,double[] point1,double[] point2)
	{

		if(point1[1]==point2[1])
		{
			return point1[1];
		}
		double m=(point2[1]-point1[1])/(point2[0]-point1[0]);
		double q=point1[1]-m*point1[0];

		double newY=m*xValue+q;
		return newY;

	}

	private double[] getHumidityValues(double xValue)
	{
		Humidity h=manager.getHumidity();
		int pos=-1;
		double[] values=new double[3];

		for(int i=0;i<h.getH().length-1;i++)
		{
			if(xValue>=h.getH()[i]&&xValue<=h.getH()[i+1])
			{
				pos=i;
			}
		}

		values[0]=calculateY(xValue, new double[]{h.getH()[pos],h.getDRY()[pos]}, new double[]{h.getH()[pos+1],h.getDRY()[pos+1]});
		values[1]=calculateY(xValue, new double[]{h.getH()[pos],h.getMOIST()[pos]}, new double[]{h.getH()[pos+1],h.getMOIST()[pos+1]});
		values[2]=calculateY(xValue, new double[]{h.getH()[pos],h.getWET()[pos]}, new double[]{h.getH()[pos+1],h.getWET()[pos+1]});
		return values;
	}

	private double[] getTemperatureValues(double xValue)
	{
		Temperature t=manager.getTemperature();
		int pos=-1;
		double[] values=new double[3];

		for(int i=0;i<t.getT().length-1;i++)
		{
			if(xValue>=t.getT()[i]&&xValue<=t.getT()[i+1])
			{
				pos=i;
				break;
			}
		}

		values[0]=calculateY(xValue, new double[]{t.getT()[pos],t.getCOOL()[pos]}, new double[]{t.getT()[pos+1],t.getCOOL()[pos+1]});
		values[1]=calculateY(xValue, new double[]{t.getT()[pos],t.getWARM()[pos]}, new double[]{t.getT()[pos+1],t.getWARM()[pos+1]});
		values[2]=calculateY(xValue, new double[]{t.getT()[pos],t.getHOT()[pos]}, new double[]{t.getT()[pos+1],t.getHOT()[pos+1]});
		return values;
	}
}
