package pl.parser.nbp;

import pl.parser.nbp.data.ExchangeRatesTable;

public class CurrencyStatistics
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
  
  public static CurrencyStatistics calculate(ExchangeRatesTable table)
  {
    CurrencyStatistics result = new CurrencyStatistics();
    result.calculateData(table);
    return result;
  }
  
  private void calculateData(ExchangeRatesTable table)
  {
    // TODO Auto-generated method stub
  }
  
}
