package pl.parser.nbp;

import java.util.concurrent.Future;

public class CurrencyInfoData
{
  private double average;
  
  private double stdDeviation;
  
  public double getAverage()
  {
    return average;
  }

  public void setAverage(double average)
  {
    this.average = average;
  }

  public double getStdDeviation()
  {
    return stdDeviation;
  }

  public void setStdDeviation(double stdDeviation)
  {
    this.stdDeviation = stdDeviation;
  }
  
  public static Future<CurrencyInfoData> getData(CurrencyInfo currencyInfo)
  {
    
    return null;
  }
  
}
