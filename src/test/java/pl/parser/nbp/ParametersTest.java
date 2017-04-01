package pl.parser.nbp;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.parser.nbp.utils.Parameters;
import pl.parser.nbp.utils.Parameters.ParametersException;
import static pl.parser.nbp.utils.TestUtils.arr;

public class ParametersTest
{
  @Rule 
  public ExpectedException exception = ExpectedException.none();
  
  @Test
  public void test1Init() throws Exception
  {
    Assert.assertFalse(Parameters.init(null));
    Assert.assertFalse(Parameters.init(arr()));
    Assert.assertFalse(Parameters.init(arr("1")));
    Assert.assertFalse(Parameters.init(arr("1", "2")));
    Assert.assertFalse(Parameters.init(arr("1", "2", "3", "4")));
  }
  
  @Test
  public void test2Init() throws Exception
  {
    exception.expect(ParametersException.class);
    Parameters.init(arr("", "2", "3"));
  }
  
  @Test
  public void test3Init() throws Exception
  {
    exception.expect(ParametersException.class);
    Parameters.init(arr("EUR", null, null));
  }
  
  @Test
  public void test4Init() throws Exception
  {
    exception.expect(ParametersException.class);
    Parameters.init(arr("EUR", "2018-01-01", "2017-01-01"));
  }
  
  @Test
  public void test5Init() throws Exception
  {
    Assert.assertTrue(Parameters.init(arr("USD", "2013-12-31", "2015-06-13")));
    Parameters params = Parameters.getInstance();
    Assert.assertNotNull(params);
    
    Assert.assertEquals("USD", params.getCurrencyCode());
    Assert.assertTrue(new DateTime("2013-12-31").equals(params.getStartDate()));
    Assert.assertTrue(new DateTime("2015-06-13").equals(params.getEndDate()));
    
  }
}
