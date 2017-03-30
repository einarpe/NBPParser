package pl.parser.nbp;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;

import org.joda.time.DateTime;

import pl.parser.nbp.data.ExchangeRatesTable;

public class CurrencyController
{
  private String currencyCode;
  
  private DateTime dateFrom;
  
  private DateTime dateTo;
  
  protected CurrencyController() { }
  
  protected static CurrencyController getInstance()
  {
    return new CurrencyController();
  }
  
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
  
  public static CurrencyController create(String currencyCode, String dateFrom, String dateTo) throws Exception
  {
    if (currencyCode == null || currencyCode.trim().isEmpty())
      throw new Exception("Currency code cannot be null or empty");
    
    if (dateFrom == null)
      throw new Exception("Date from cannot be null");
    
    if (dateTo == null)
      throw new Exception("Date to cannot be null");
      
    CurrencyController info = getInstance();  
    info.setCurrencyCode(currencyCode);
    info.setDateFrom(new DateTime(dateFrom));
    info.setDateTo(new DateTime(dateTo));
    
    if (info.getDateFrom().compareTo(info.getDateTo()) > 0)
      throw new Exception("Date from must me less than or equal to date to.");
    
    return info;
  }
  
  public void print() throws Exception
  {
    // retrieve remote data
    List<ExchangeRatesTable> table = DataRetriever.getInstance(this).getResult();
    
    // and then calculate what we need
    CurrencyStatistics stats = CurrencyStatistics.calculate(table);
    
    // results are printed into some output print stream
    printHandler(stats, getPrintStream());
  }
  
  protected void printHandler(CurrencyStatistics stats, PrintStream out)
  {
    NumberFormat formatter = getNumberFormatter();
    out.println(getCurrencyCode());
    out.println(formatter.format(stats.getAverage()));
    out.println(formatter.format(stats.getStdDeviation()));
  }
  
  protected PrintStream getPrintStream()
  {
    return System.out;
  }
  
  protected NumberFormat getNumberFormatter()
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(4);
    return nf;
  }
}
