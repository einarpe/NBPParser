package pl.parser.nbp;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.w3c.dom.Document;

import pl.parser.nbp.data.ExchangeRatesTable;
import pl.parser.nbp.utils.DownloadUtils;

public class DataRetriever
{
  private List<ExchangeRatesTable> tables;

  private CurrencyController ctrl;
  
  final static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
  
  final static Pattern reXmlFileName = Pattern.compile("c[0-9]{3}z([0-9]{2})([0-9]{2})([0-9]{2})");

  private DataRetriever(CurrencyController ctrl) 
  { 
    this.ctrl = ctrl;
    this.tables = new LinkedList<ExchangeRatesTable>();
  }
  
  public static DataRetriever getInstance(CurrencyController ctrl) throws Exception
  {
    DataRetriever retriever = new DataRetriever(ctrl);
    retriever.downloadData();
    return retriever;
  }
  
  private void downloadData() throws IOException, InterruptedException
  {
    DateTime dateFrom = ctrl.getDateFrom();
    DateTime dateTo = ctrl.getDateTo();
    ForkJoinPool fjp = new ForkJoinPool(8); 
    
    Logger.getGlobal().info("Starting download.");
    for (int year = dateFrom.getYear(), to = dateTo.getYear(); year <= to; year++)
    {
      String indexFileUrl = String.format("http://www.nbp.pl/kursy/xml/dir%d.txt", year);
      String indexContents = DownloadUtils.download(indexFileUrl);
      for (String line : indexContents.split("[\r\n]+"))
      {
        Matcher m = reXmlFileName.matcher(line);
        if (!m.matches())
          continue;
        
        int fnYear = Integer.parseInt(m.group(1)) + 2000;
        int fnMonth = Integer.parseInt(m.group(2));
        int fnDay = Integer.parseInt(m.group(3));
        DateTime dateInFileName = new DateTime(fnYear, fnMonth, fnDay, 0, 0);
        
        if (dateInFileName.compareTo(dateFrom) >= 0 && dateInFileName.compareTo(dateTo) <= 0)
        {
          String dataFileUrl = String.format("http://www.nbp.pl/kursy/xml/%s.xml", line);
          fjp.submit(() -> addDataFile(dataFileUrl));
        }
      }
    }
    fjp.awaitQuiescence(2, TimeUnit.MINUTES);
    Logger.getGlobal().info("Downloading done.");
  }
  
  private void addDataFile(String url)
  {
    try
    {
      Logger.getGlobal().info("Downloading file " + url);
      
      Document document = builderFactory.newDocumentBuilder().parse(url);
      ExchangeRatesTable table = ExchangeRatesTable.fromXml(document, ctrl.getCurrencyCode());
      if (table == null)
        return;
      
      // check again publication date  
      DateTime publicationDate = table.getPublicationDate();
      if (publicationDate.compareTo(ctrl.getDateFrom()) >= 0 && publicationDate.compareTo(ctrl.getDateTo()) <= 0)
      {
        synchronized (tables)
        {
          tables.add(table);
        }
      }
    }
    catch (Exception ex)
    {
      Logger.getGlobal().severe(ex.getMessage());
    }
  }

  public List<ExchangeRatesTable> getResult()
  {
    return tables;
  }
  
}
