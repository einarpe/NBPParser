package pl.parser.nbp;

import java.util.List;

import org.apache.commons.math3.stat.StatUtils;

import pl.parser.nbp.data.ExchangeRatesTable;

public class CurrencyStatistics
{
  private double average;
  
  private double stdDeviation;
  
  /**
   * Gets average of buy rates.
   * @return
   */
  public double getAverage()
  {
    return average;
  }

  private void setAverage(double average)
  {
    this.average = average;
  }

  /**
   * Gets standard deviation of sell rates.
   * @return
   */
  public double getStdDeviation()
  {
    return stdDeviation;
  }

  private void setStdDeviation(double stdDeviation)
  {
    this.stdDeviation = stdDeviation;
  }
  
  /**
   * Performs calculations over ready list of buy & sell rates.
   * @param table - input table with data
   * @return object of CurrencyStatitics class with calculated data
   */
  public static CurrencyStatistics calculate(List<ExchangeRatesTable> table)
  {
    CurrencyStatistics result = new CurrencyStatistics();
    result.calculateData(table);
    return result;
  }
  
  /** Method to calculate data based on input table. */
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
    
    double mean = StatUtils.mean(buyRates);
    setAverage(mean);
    
    double stdDev = Math.sqrt(StatUtils.variance(sellRates, average));
    setStdDeviation(stdDev);
  }
  
}
