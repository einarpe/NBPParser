package pl.parser.nbp.data;

import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExchangeRatesTable
{
  private DateTime publicationDate;
  
  private Position position;
  
  public DateTime getPublicationDate()
  {
    return publicationDate;
  }

  public void setPublicationDate(DateTime publicationDate)
  {
    this.publicationDate = publicationDate;
  }

  public Position getPosition()
  {
    return position;
  }

  public void setPosition(Position position)
  {
    this.position = position;
  }
  
  public static ExchangeRatesTable fromXml(Document document, String currencyCode)
  {
    try
    {
      /*
      <numer_tabeli>73/C/NBP/2007</numer_tabeli>
      <data_notowania>2007-04-12</data_notowania>
      <data_publikacji>2007-04-13</data_publikacji>
      <pozycja>
        <nazwa_waluty>dolar amerykański</nazwa_waluty>
        <przelicznik>1</przelicznik>
        <kod_waluty>USD</kod_waluty>
        <kurs_kupna>2,8210</kurs_kupna>
        <kurs_sprzedazy>2,8780</kurs_sprzedazy>
      </pozycja>
      */ 
      
      ExchangeRatesTable table = new ExchangeRatesTable();
      XPath xpath = XPathFactory.newInstance().newXPath();
      
      String pubDate = (String) xpath.evaluate("//data_publikacji/text()", document, XPathConstants.STRING);
      table.setPublicationDate(new DateTime(pubDate));
      
      Element xmlCurrencyCode = (Element) xpath.evaluate(String.format("//pozycja/kod_waluty[text()='%s']", currencyCode), document, XPathConstants.NODE);
      if (xmlCurrencyCode != null)
      {
        Position position = new Position();
        Element xmlPosition = (Element) xmlCurrencyCode.getParentNode();
        
        String posBuyRate =  (String) xpath.evaluate("kurs_kupna/text()", xmlPosition, XPathConstants.STRING);
        position.setBuyRate(Double.parseDouble(posBuyRate.replace(',', '.')));
        
        String posSellRate = (String) xpath.evaluate("kurs_sprzedazy/text()", xmlPosition, XPathConstants.STRING);
        position.setSellRate(Double.parseDouble(posSellRate.replace(',', '.')));
        
        table.setPosition(position);
      }
      else
        throw new Exception("Element 'kod_waluty' with given currency code was not found");
      
      return table;
    }
    catch (Exception ex)
    {
      Logger.getGlobal().severe(ex.getMessage());
      return null;
    }
  }
  
}
