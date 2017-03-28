package pl.parser.nbp;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.joda.time.DateTime;

public class CurrencyInfo
{
  private String currencyCode;
  
  private DateTime dateFrom;
  
  private DateTime dateTo;
  
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
  
  private CurrencyInfo() {}
  
  public static CurrencyInfo create(String currencyCode, String dateFrom, String dateTo)
  {
    CurrencyInfo info = getInstance();
    info.setCurrencyCode(currencyCode);
    info.setDateFrom(new DateTime(dateFrom));
    info.setDateTo(new DateTime(dateTo));
    return info;
  }

  
  public void print() throws InterruptedException, ExecutionException
  {
    Future<CurrencyInfoData> await = collectData();
    CurrencyInfoData data = await.get();
    
    printHandler(data, getPrintStream());
  }

  protected Future<CurrencyInfoData> collectData()
  {
    return CurrencyInfoData.getData(this);
  }

  protected void printHandler(CurrencyInfoData data, PrintStream out)
  {
    NumberFormat formatter = getNumberFormatter();
    out.println(formatter.format(data.getAverage()));
    out.println(formatter.format(data.getStdDeviation()));
  }
  
  protected static CurrencyInfo getInstance()
  {
    return new CurrencyInfo();
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
