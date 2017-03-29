package pl.parser.nbp.data;

public class Position
{
  private String currencyName;
  
  private double factor;
  
  private String currencyCode;
  
  private double buyRate;
  
  private double sellRate;

  public String getCurrencyName()
  {
    return currencyName;
  }

  public void setCurrencyName(String currencyName)
  {
    this.currencyName = currencyName;
  }

  public double getFactor()
  {
    return factor;
  }

  public void setFactor(double factor)
  {
    this.factor = factor;
  }

  public String getCurrencyCode()
  {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode)
  {
    this.currencyCode = currencyCode;
  }

  public double getBuyRate()
  {
    return buyRate;
  }

  public void setBuyRate(double buyRate)
  {
    this.buyRate = buyRate;
  }

  public double getSellRate()
  {
    return sellRate;
  }

  public void setSellRate(double sellRate)
  {
    this.sellRate = sellRate;
  }
}
