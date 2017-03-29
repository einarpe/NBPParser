package pl.parser.nbp;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import pl.parser.nbp.data.ExchangeRatesTable;

public class CurrencyController
{
  private String currencyCode;
  
  private DateTime dateFrom;
  
  private DateTime dateTo;
  
  private CurrencyController() {}
  
  public String getCurrencyCode()
  {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode)
  {
    this.currencyCode = currencyCode;
  }

  public DateTime getDateFrom()
  {
    return dateFrom;
  }

  public void setDateFrom(DateTime dateFrom)
  {
    this.dateFrom = dateFrom;
  }

  public DateTime getDateTo()
  {
    return dateTo;
  }

  public void setDateTo(DateTime dateTo)
  {
    this.dateTo = dateTo;
  }
  
  public static CurrencyController create(String currencyCode, String dateFrom, String dateTo)
  {
    CurrencyController info = getInstance();
    info.setCurrencyCode(currencyCode);
    info.setDateFrom(new DateTime(dateFrom));
    info.setDateTo(new DateTime(dateTo));
    return info;
  }

  
  public void print() throws Exception
  {
    // retrieve remote data
    ExchangeRatesTable table = DataRetriever.getInstance(this).getResult();
    
    // and then calculate what we need
    CurrencyStatistics stats = CurrencyStatistics.calculate(table);
    
    // results are printed into some output print stream
    printHandler(stats, getPrintStream());
  }
  
  protected void printHandler(CurrencyStatistics stats, PrintStream out)
  {
    NumberFormat formatter = getNumberFormatter();
    out.println(formatter.format(stats.getAverage()));
    out.println(formatter.format(stats.getStdDeviation()));
  }
  
  protected static CurrencyController getInstance()
  {
    return new CurrencyController();
  }
  
  protected PrintStream getPrintStream()
  {
    return System.out;
  }
  
  protected NumberFormat getNumberFormatter()
  {
    return NumberFormat.getNumberInstance(); 
  }
}
