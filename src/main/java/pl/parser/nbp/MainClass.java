package pl.parser.nbp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass 
{
  public static void main(String[] args)
  {
    try
    {
      setLogger();
      
      if (args.length != 3)
      {
        printHelp();
      }
      else
      {
        String currencyCode = args[0];
        String dateFrom = args[1];
        String dateTo = args[2];
        
        CurrencyController ci = CurrencyController.create(currencyCode, dateFrom, dateTo);
        ci.print();
      }
    }
    catch (Exception ex)
    {
      Logger.getGlobal().severe(ex.getMessage());
    }
  }

  private static void printHelp()
  {
    System.out.println("Usage: java pl.parser.nbp.MainClass [CURRENCY_CODE] [DATE_FROM:YYYY-MM-DD] [DATE_TO:YYYY-MM-DD]");
  }
  
  private static void setLogger()
  {
    Level logLevel = Level.SEVERE;
    String newLogLevel = System.getProperty("log.level"); 
    if (newLogLevel != null)
      logLevel = Level.parse(System.getProperty("log.level"));
    
    Logger.getGlobal().setLevel(logLevel);
  }
  
}
