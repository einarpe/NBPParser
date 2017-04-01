package pl.parser.nbp;

import static pl.parser.nbp.utils.TestUtils.arr;

import java.util.function.Consumer;

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
          Assert.assertEquals(4.1505, stats.getAverage(), DELTA);
          Assert.assertEquals(0.0144, stats.getStdDeviation(), DELTA);
        });
    p.handle();
  }
  
  @Test
  public void handle2Test() throws Exception
  {
    Parser p = getParser("USD", "2013-01-01", "2014-06-13", 
        (stats) -> {
          Assert.assertEquals(3.0938, stats.getAverage(), DELTA);
          Assert.assertEquals(0.0861, stats.getStdDeviation(), DELTA);
        });
    p.handle();
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
