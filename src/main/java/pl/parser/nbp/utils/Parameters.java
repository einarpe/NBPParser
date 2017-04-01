package pl.parser.nbp.utils;

import org.joda.time.DateTime;

/**
 * Global class for parsing & holding input parameters
 */
public class Parameters
{
  private static Parameters instance;

  private String currencyCode;
  
  private DateTime dateFrom;
  
  private DateTime dateTo;
  
  /**
   * Gets currency code.
   * @return
   */
  public String getCurrencyCode()
  {
    return currencyCode;
  }

  private void setCurrencyCode(String currencyCode)
  {
    this.currencyCode = currencyCode;
  }
  
  /**
   * Gets date from.
   * @return
   */
  public DateTime getDateFrom()
  {
    return dateFrom;
  }

  private void setDateFrom(DateTime dateFrom)
  {
    this.dateFrom = dateFrom;
  }

  /**
   * Gets date to.
   * @return
   */
  public DateTime getDateTo()
  {
    return dateTo;
  }

  private void setDateTo(DateTime dateTo)
  {
    this.dateTo = dateTo;
  }
  
  /**
   * Initialization based on arguments passed from main entry method.
   * @param args - input argumens.
   * @return true - all arguments are correct / false - otherwise
   * @throws ParametersException - when there is error reading input parameters
   */
  public static boolean init(String[] args) throws ParametersException
  {
    if (args == null || args.length != 3)
      return false;
    
    if (args[0] == null || args[0].trim().isEmpty())
      throw new ParametersException("Currency code cannot be null or empty");
    
    if (args[1] == null)
      throw new ParametersException("Date from cannot be null");
    
    if (args[2] == null)
      throw new ParametersException("Date to cannot be null");
      
    if (instance == null)
      instance = new Parameters();
    
    instance.setCurrencyCode(args[0]);
    instance.setDateFrom(new DateTime(args[1]));
    instance.setDateTo(new DateTime(args[2]));
    
    if (instance.getDateFrom().compareTo(instance.getDateTo()) > 0)
      throw new ParametersException("Date from must me less than or equal to date to.");
    
    return true;
  }
  
  /**
   * Returns singleton instance.
   * @return
   */
  public static Parameters getInstance()
  {
    return instance;
  }
  
  public static class ParametersException extends Exception
  {
    private static final long serialVersionUID = -2727092303174542724L;
    
    public ParametersException(String message)
    {
      super(message);
    }
  }
}
