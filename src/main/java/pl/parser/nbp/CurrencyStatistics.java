package pl.parser.nbp;

import java.util.List;

import org.apache.commons.math3.stat.StatUtils;

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
  
  public static CurrencyStatistics calculate(List<ExchangeRatesTable> table)
  {
    CurrencyStatistics result = new CurrencyStatistics();
    result.calculateData(table);
    return result;
  }
  
  private void calculateData(List<ExchangeRatesTable> table)
  {
    double[] buyRates = table
      .stream()
      .map((ert) -> ert.getPosition().getBuyRate())
      .mapToDouble(Double::doubleValue).toArray();
    
    double[] sellRates = table
        .stream()
        .map((ert) -> ert.getPosition().getSellRate())
        .mapToDouble(Double::doubleValue).toArray();
    
    average = StatUtils.mean(buyRates);
    stdDeviation = Math.sqrt(StatUtils.variance(sellRates, average));
  }
  
}
