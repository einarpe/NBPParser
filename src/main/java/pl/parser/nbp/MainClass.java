package pl.parser.nbp;

import java.util.logging.Level;
import java.util.logging.Logger;

import pl.parser.nbp.utils.Parameters;

public class MainClass 
{
  /**
   * Main entry point.
   * @param args - arguments passed to program: <br>
   *  [0] - currency code <br>
   *  [1] - date from <br>
   *  [2] - date to <br>
   * 
   */
  public static void main(String[] args)
  {
    try
    {
      setLogger();
      
      if (Parameters.init(args))
      {
        Parser ci = new Parser();
        ci.handle();
      }
      else
      {
        printHelp();
      }
    }
    catch (Exception ex)
    {
      Logger.getGlobal().severe(ex.getMessage());
    }
  }
  
  /**
   * Set default logger. <br>
   * Method sets default logging level of SEVERE.
   * When there is VM argument in format -Dlog.level=... it sets level to value of that argument.
   */
  private static void setLogger()
  {
    Level logLevel = Level.SEVERE;
    String newLogLevel = System.getProperty("log.level"); 
    if (newLogLevel != null)
      logLevel = Level.parse(newLogLevel.toUpperCase());
    
    Logger.getGlobal().setLevel(logLevel);
  }
  
  /**
   * Just simple method to print help.
   */
  private static void printHelp()
  {
    System.out.println("Usage: java pl.parser.nbp.MainClass [CURRENCY_CODE] [DATE_FROM:YYYY-MM-DD] [DATE_TO:YYYY-MM-DD]");
  }  
}
