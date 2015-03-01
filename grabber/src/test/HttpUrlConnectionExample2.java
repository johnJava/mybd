package test;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpUrlConnectionExample2 {

  private List<String> cookies;
  private HttpURLConnection conn;

  private final String USER_AGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";

  public static void main(String[] args) throws Exception {

    String url = "http://s.5173.com/__tra.gif?referrer=&r=0.8926788993015564&url=https%3A%2F%2Fpassport.5173.com%2F&domain=passport.5173.com&title=%E7%99%BB%E5%BD%95%7C5173.com%7C%E8%A3%85%E5%A4%87%E4%BA%A4%E6%98%93%E6%B8%B8%E6%88%8F%E5%B8%81%E4%BA%A4%E6%98%93%E5%B8%90%E5%8F%B7%E4%BA%A4%E6%98%93%7C%E5%AF%84%E5%94%AE%E6%8B%85%E4%BF%9D%E4%BA%A4%E6%98%93%7C%E6%B8%B8%E6%88%8F%E7%82%B9%E5%8D%A1%7C%E4%BB%A3%E7%BB%83%E4%B8%AD%E4%BB%8B%7C%E5%B0%8F%E9%A2%9D%E4%BA%A4%E6%98%93&height=800&width=1280&colorDepth=32&language=&agent=Mozilla%2F4.0%20(compatible%3B%20MSIE%209.0%3B%20Windows%20NT%206.1%3B%20Trident%2F5.0%3B%20SLCC2%3B%20.NET%20CLR%202.0.50727%3B%20.NET%20CLR%203.5.30729%3B%20.NET";
   // String url = "https://passport.csdn.net/";
    String gmail = "https://mail.google.com/mail/";

    HttpUrlConnectionExample2 http = new HttpUrlConnectionExample2();

    // make sure cookies is turn on
    CookieHandler.setDefault(new CookieManager());

    // 1. Send a "GET" request, so that you can extract the form's data.
    String page = http.GetPageContent(url);
//    String postParams = http.getFormParams(page, "username@gmail.com", "password");
//
//    // 2. Construct above post's content and then send a POST request for
//    // authentication
//    http.sendPost(url, postParams);
//
//    // 3. success then go to gmail.
//    String result = http.GetPageContent(gmail);
//    System.out.println(result);
  }

  private void sendPost(String url, String postParams) throws Exception {

    URL obj = new URL(url);
    conn = (HttpURLConnection) obj.openConnection();

    // Acts like a browser
    conn.setUseCaches(false);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Host", "accounts.google.com");
    conn.setRequestProperty("User-Agent", USER_AGENT);
    conn.setRequestProperty("Accept",
        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    for (String cookie : this.cookies) {
        conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
    }
    conn.setRequestProperty("Connection", "keep-alive");
    conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

    conn.setDoOutput(true);
    conn.setDoInput(true);

    // Send post request
    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
    wr.writeBytes(postParams);
    wr.flush();
    wr.close();

    int responseCode = conn.getResponseCode();
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + postParams);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in =
             new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();
    // System.out.println(response.toString());

  }

  private String GetPageContent(String url) throws Exception {
    URL obj = new URL(url);
    conn = (HttpURLConnection) obj.openConnection();
    // default is GET
    conn.setRequestMethod("GET");

    conn.setUseCaches(false);

    // act like a browser
    conn.setRequestProperty("User-Agent", USER_AGENT);
    conn.setRequestProperty("Accept",
        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    if (cookies != null) {
        for (String cookie : this.cookies) {
            conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        }
    }

    BufferedReader in =
            new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    int responseCode = conn.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();

    // Get the response cookies
    setCookies(conn.getHeaderFields().get("Set-Cookie"));
    System.out.println("conn.getHeaderFields():"+conn.getHeaderFields());
    return response.toString();

  }

  public String getFormParams(String html, String username, String password)
        throws UnsupportedEncodingException {

    System.out.println("Extracting form's data...");

    Document doc = Jsoup.parse(html);

    // Google form id
    Element loginform = doc.getElementById("gaia_loginform");
    Elements inputElements = loginform.getElementsByTag("input");
    List<String> paramList = new ArrayList<String>();
    for (Element inputElement : inputElements) {
        String key = inputElement.attr("name");
        String value = inputElement.attr("value");

        if (key.equals("Email"))
            value = username;
        else if (key.equals("Passwd"))
            value = password;
        paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
    }

    // build parameters list
    StringBuilder result = new StringBuilder();
    for (String param : paramList) {
        if (result.length() == 0) {
            result.append(param);
        } else {
            result.append("&" + param);
        }
    }
    return result.toString();
  }

  public List<String> getCookies() {
    return cookies;
  }

  public void setCookies(List<String> cookies) {
    this.cookies = cookies;
    System.out.println("this.cookies =="+ this.cookies );
  }

}
