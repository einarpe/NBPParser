package pl.parser.nbp;

import static pl.parser.nbp.utils.TestUtils.arr;

import java.io.File;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.parser.nbp.utils.Parameters;

public class ParserTest
{
  private final static double DELTA = 0.001;
  
  @Rule 
  public ExpectedException exception = ExpectedException.none();
  
  @Test
  public void handle1Test() throws Exception
  {
    Parser p = getParser("EUR", "2013-01-28", "2013-01-31", 
        (stats) -> {
          // expected correct data are located at: http://api.nbp.pl/api/exchangerates/rates/C/EUR/2013-01-28/2013-01-31
          Assert.assertEquals(4.1505, stats.getAverage(), DELTA);
          Assert.assertEquals(0.0144, stats.getStdDeviation(), DELTA);
        });
    p.handle();
  }
  
  @Test
  public void handle2Test() throws Exception
  {
    Parser p = getParser("USD", "2013-06-01", "2014-06-01", 
        (stats) -> {
          // expected correct data are located at: http://api.nbp.pl/api/exchangerates/rates/C/USD/2013-06-01/2014-06-01
          Assert.assertEquals(3.0808, stats.getAverage(), DELTA);
          Assert.assertEquals(0.0910, stats.getStdDeviation(), DELTA);
        });
    p.handle();
  }
  
  @Test
  /**
   * Example how to use Parser class to produce CSV files.
   * @throws Exception
   */
  public void exportToCSVTest() throws Exception
  {
    final String CSV_HEADER = "Average;Standard deviation\r\n";
    final File tmpFile = File.createTempFile("nbpparser", ".csv");
    
    Assert.assertTrue(Parameters.init(arr("CHF", "2014-05-02", "2014-09-14")));
    Parser p = new Parser()
    {
      @Override
      protected PrintStream getPrintStream()
      {
        try
        {
          return new PrintStream(tmpFile);
        }
        catch (Exception ex)
        {
          Assert.fail("Something wrong with creating stream");
          return null;
        }
      }
      
      @Override
      protected void printHandler(CurrencyStatistics stats)
      {
        NumberFormat nf = getNumberFormatter();
        println(CSV_HEADER);
        println(nf.format(stats.getAverage()));
        println(nf.format(stats.getStdDeviation()));
      }
      
    };
    p.handle();
    
    String contents = FileUtils.readFileToString(tmpFile);
    Assert.assertNotNull(contents);
    Assert.assertTrue(contents.startsWith(CSV_HEADER));
  }
  
  private Parser getParser(String code, String dateFrom, String dateTo, Consumer<CurrencyStatistics> handler) 
  {
    try
    {
      Assert.assertTrue(Parameters.init(arr(code, dateFrom, dateTo))); 
      return new MockParser(handler);
    }
    catch (Exception ex)
    {
      Assert.fail("MUST NOT throw any exception");
      return null;
    }
  }

  static class MockParser extends Parser
  {
    private final Consumer<CurrencyStatistics> handler;

    public MockParser(Consumer<CurrencyStatistics> handler)
    {
      this.handler = handler;
    }
    
    @Override
    protected void printHandler(CurrencyStatistics stats)
    {
      handler.accept(stats);
    }
  }
}
