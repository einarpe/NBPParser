package pl.parser.nbp.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class DownloadUtils
{
  public static String download(String strUrl) throws IOException
  {
    URL url = new URL(strUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    return IOUtils.toString(connection.getInputStream());
  }
}
