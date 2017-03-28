package pl.parser.nbp;

import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.RegexValidator;

public class MainClass 
{
  public static void main(String[] args)
  {
    try
    {
      if (validateArgs(args))
      {
        String currencyCode = args[0];
        String dateFrom = args[1];
        String dateTo = args[2];
        
        CurrencyInfo ci = CurrencyInfo.create(currencyCode, dateFrom, dateTo);
        ci.print();
      }
    }
    catch (Exception ex)
    {
      System.err.println(ex.getMessage());
    }
    
  }
  
  private static boolean validateArgs(String[] args) throws Exception
  {
    if (args.length != 3)
    {
      printHelp();
      return false;
    }
    else
    {
      RegexValidator currencyValidator = new RegexValidator("[a-z]+", false);
      if (!currencyValidator.isValid(args[0]))
        throw new Exception("Wrong CURRENCY_CODE parameter");
      
      if (!DateValidator.getInstance().isValid(args[1], "yyyy-MM-dd"))
        throw new Exception("Wrong DATE_FROM parameter");
      
      if (!DateValidator.getInstance().isValid(args[2], "yyyy-MM-dd"))
        throw new Exception("Wrong DATE_TO parameter");
      
      return true;
    }
  }

  private static void printHelp()
  {
    System.out.println("Usage: java pl.parser.nbp.MainClass [CURRENCY_CODE] [DATE_FROM:YYYY-MM-DD] [DATE_TO:YYYY-MM-DD]");
  }
}
