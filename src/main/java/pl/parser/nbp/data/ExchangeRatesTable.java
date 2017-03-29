package pl.parser.nbp.data;

import java.util.List;

public class ExchangeRatesTable
{
  private String tableNumber;
  
  private String quotationDate;
  
  private String publicationDate;
  
  private List<Position> positions;
  
  public String getTableNumber()
  {
    return tableNumber;
  }

  public void setTableNumber(String tableNumber)
  {
    this.tableNumber = tableNumber;
  }

  public String getQuotationDate()
  {
    return quotationDate;
  }

  public void setQuotationDate(String quotationDate)
  {
    this.quotationDate = quotationDate;
  }

  public String getPublicationDate()
  {
    return publicationDate;
  }

  public void setPublicationDate(String publicationDate)
  {
    this.publicationDate = publicationDate;
  }

  public List<Position> getPositions()
  {
    return positions;
  }

  public void setPositions(List<Position> positions)
  {
    this.positions = positions;
  }

  
  
}
