package pl.parser.nbp;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.List;

import pl.parser.nbp.data.ExchangeRatesTable;
import pl.parser.nbp.utils.Parameters;

public class Parser
{
  /**
   * Handle the situation. <br>
   * Method downloads data and calculates stats.
   * @throws Exception - error occured during download or calculating statistics
   */
  public void handle() throws Exception
  {
    // retrieve remote data
    List<ExchangeRatesTable> table = DataRetriever.getInstance().getResult();
    
    // and then calculate what we need
    CurrencyStatistics stats = CurrencyStatistics.calculate(table);
    
    // results are printed into some output print stream
    printHandler(stats);
  }
  
  /**
   * Handler method for printing resulting values.
   * @param stats - already calculated statistics
   */
  protected void printHandler(CurrencyStatistics stats)
  {
    NumberFormat formatter = getNumberFormatter();
    PrintStream out = getPrintStream();
    Parameters params = Parameters.getInstance();
    
    out.println(params.getCurrencyCode());
    out.println(formatter.format(stats.getAverage()));
    out.println(formatter.format(stats.getStdDeviation()));
  }
  
  /**
   * Gets default output stream to print results onto. <br>
   * @return System.out stream by default
   */
  protected PrintStream getPrintStream()
  {
    return System.out;
  }
  
  /**
   * Gets formatter of double values.
   * @return
   */
  protected NumberFormat getNumberFormatter()
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(4);
    return nf;
  }
}
