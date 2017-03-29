package pl.parser.nbp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.RegexValidator;
import org.joda.time.DateTime;
import org.w3c.dom.Document;

import pl.parser.nbp.data.ExchangeRatesTable;
import pl.parser.nbp.utils.DownloadUtils;

public class DataRetriever
{
  private List<Document> xmlDataFiles;
  
//  private List<String> xmlDataFiles;
  
  private ExchangeRatesTable table;

  private CurrencyController ctrl;

  private DataRetriever(CurrencyController ctrl) 
  { 
    this.ctrl = ctrl;
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
    ForkJoinPool fjp = new ForkJoinPool(0x08);
    
    xmlDataFiles = new LinkedList<Document>();
//    xmlDataFiles = new LinkedList<String>();
    
    for (int year = dateFrom.getYear(), to = ctrl.getDateTo().getYear(); year <= to; year++)
    {
      String indexFileUrl = String.format("http://www.nbp.pl/kursy/xml/dir%d.txt", year);
      String indexContents = DownloadUtils.download(indexFileUrl);
      String[] lines = indexContents.split("[\r\n]+");
      
      for (String line : lines)
      {
        Matcher m = Pattern.compile("c[0-9]{3}z[0-9]{2}([0-9]{2})([0-9]{2})").matcher(line);
        if (m.matches())
        {
          int mMonth = Integer.parseInt(m.group(1));
          
          // TODO!
          int nDay = Integer.parseInt(m.group(2));
          
          boolean monthOK = true;
          boolean dayOK = true;
          if (year == dateFrom.getYear())
            monthOK = mMonth >= dateFrom.getMonthOfYear();
          else if (year == dateTo.getYear())
            monthOK = mMonth <= dateTo.getMonthOfYear();
          
          if (monthOK)
          {
            String dataFileUrl = String.format("http://www.nbp.pl/kursy/xml/%s.xml", line);
            fjp.submit(() -> addDataFile(dataFileUrl));
          }
        }
      }
    }
    
    fjp.awaitQuiescence(2, TimeUnit.MINUTES);
    System.out.println("Done!");
  }
  
  final static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
  
  private void addDataFile(String url)
  {
    try
    {
      System.out.println("Downloading " + url);
      Document doc = builderFactory.newDocumentBuilder().parse(url);
      xmlDataFiles.add(doc);
//      xmlDataFiles.add(DownloadUtils.download(url));
      
    }
    catch (Exception ex)
    {
      System.err.println(ex.getMessage());
    }
  }

  public ExchangeRatesTable getResult()
  {
    return table;
  }
  
}
