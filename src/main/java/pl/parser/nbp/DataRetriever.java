package pl.parser.nbp;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.w3c.dom.Document;

import pl.parser.nbp.data.ExchangeRatesTable;
import pl.parser.nbp.utils.DownloadUtils;
import pl.parser.nbp.utils.DownloadUtils.DownloadPool;
import pl.parser.nbp.utils.Parameters;

public class DataRetriever
{
  /** Factory for building XML documents. */
  private final static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
  
  /** Pattern to check for correct file and extracting date parts (year, month, and day). */
  private final static Pattern reXmlFileName = Pattern.compile("c[0-9]{3}z([0-9]{2})([0-9]{2})([0-9]{2})");

  /** List of tables containing data. */
  private List<ExchangeRatesTable> tables;
  
  private DataRetriever() 
  {
    this.tables = new CopyOnWriteArrayList<>();
  }
  
  /**
   * Returns instance of DataRetriever class. <br>
   * Method starts downloading immediately and blocks current thread until all data is downloaded.
   * @return object of DataRetriever class with downloaded and parsed data
   * @throws Exception - error during download
   */
  public static DataRetriever getInstance() throws Exception
  {
    DataRetriever retriever = new DataRetriever();
    retriever.downloadData();
    return retriever;
  }
  
  /** Method to handle download data. 
   * @throws ExecutionException */
  private void downloadData() throws IOException, InterruptedException, ExecutionException
  {
    Parameters params = Parameters.getInstance();
    DateTime startDate = params.getStartDate();
    DateTime endDate = params.getEndDate();
    DownloadPool pool = DownloadUtils.getPool();
    
    Logger.getGlobal().info("Starting download.");
    for (int year = startDate.getYear(), to = endDate.getYear(); year <= to; year++)
    {
      // These files are index files - contains list of all files with all data we need.
      String indexFileUrl = String.format("http://www.nbp.pl/kursy/xml/dir%d.txt", year);
      String indexContents = DownloadUtils.download(indexFileUrl);
      for (String line : indexContents.split("[\r\n]+"))
      {
        // look for files with currency buy & sell rates
        Matcher m = reXmlFileName.matcher(line);
        if (!m.matches())
          continue;
        
        int fnYear = Integer.parseInt(m.group(1)) + 2000;
        int fnMonth = Integer.parseInt(m.group(2));
        int fnDay = Integer.parseInt(m.group(3));
        DateTime dateInFileName = new DateTime(fnYear, fnMonth, fnDay, 0, 0);
        
        if (dateInFileName.compareTo(startDate) >= 0 && dateInFileName.compareTo(endDate) <= 0)
        {
          String dataFileUrl = String.format("http://www.nbp.pl/kursy/xml/%s.xml", line);
          pool.submit(() -> readDataFile(dataFileUrl));
        }
      }
    }
    
    pool.waitFor();
    Logger.getGlobal().info("Downloading done.");
  }
  
  /** 
   * Given URL, read XML file and parse it into ExchangeRatesTable class. 
   * Methods adds ready object into resulting list. 
   */
  private void readDataFile(String url)
  {
    try
    {
      Logger.getGlobal().info(String.format("Downloading file %s", url));
      Parameters params = Parameters.getInstance();
      
      Document document = builderFactory.newDocumentBuilder().parse(url);
      ExchangeRatesTable table = ExchangeRatesTable.parse(document, params.getCurrencyCode());
      
      // check again publication date, just to be sure
      DateTime publicationDate = table.getPublicationDate();
      if (publicationDate.compareTo(params.getStartDate()) >= 0 && publicationDate.compareTo(params.getEndDate()) <= 0)
        tables.add(table);
    }
    catch (Exception ex)
    {
      Logger.getGlobal().severe(ex.getMessage());
    }
  }
  
  /**
   * Gets resulting list after download.
   * @return
   */
  public List<ExchangeRatesTable> getResult()
  {
    return tables;
  }
}
