package pl.parser.nbp;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CurrencyControllerTest
{
  
  @Rule 
  public ExpectedException exception = ExpectedException.none();
  
  @Test
  public void create1Test() throws Exception
  {
    exception.expect(Exception.class);
    CurrencyController.create(null, null, null);
  }
  
  @Test
  public void create2Test() throws Exception
  {
    exception.expect(Exception.class);
    CurrencyController.create("  ", null, null);
  }
  
  @Test
  public void create3Test() throws Exception
  {
    exception.expect(Exception.class);
    CurrencyController.create("USD", null, null);
  }
  
  @Test
  public void create4Test() throws Exception
  {
    exception.expect(Exception.class);
    CurrencyController.create("USD", "2014-12-31", null);
  }
  
  @Test
  public void create5Test() throws Exception
  {
    exception.expect(Exception.class);
    CurrencyController.create("USD", "alamakota", null);
  }
  
  @Test
  public void create6Test() throws Exception
  {
    exception.expect(Exception.class);
    CurrencyController.create("USD", null, "2013-11-03");
  }
  
  @Test
  public void create7Test() throws Exception
  {
    exception.expect(Exception.class);
    CurrencyController.create("USD", "2018-12-09", "2013-11-03");
  }
  
  @Test
  public void print1Test() throws Exception 
  {
    // EUR 2013-01-28 2013-01-31
    CurrencyController.create("EUR", "2013-01-28", "2013-01-31");
  }
  
  public static class MockController extends CurrencyController
  {
    @Override
    public void print() throws Exception
    {
      
    }
  }
}
