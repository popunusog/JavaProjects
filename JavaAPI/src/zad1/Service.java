/**
 *
 *  @author Piotrowski Jakub S23184
 *
 */
package zad1;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Service {
    static private String country;
    static private String city;
    static private String cur;
    final static private String API_KEY_FOR_WEATHER = "98b1782b9cfdad948742b04d79cbd898";
    final static private String API_URL_FOR_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=" + API_KEY_FOR_WEATHER;
    final static private String API_ULR_FOR_CUR = "https://api.exchangerate.host/convert?";
    final static private String URL_NBP_A = "http://www.nbp.pl/kursy/kursya.html";
    final static private String URL_NBP_B = "http://www.nbp.pl/kursy/kursyb.html";

    public Service(String country) {
        this.country = country;
    }

    public static String getCountryCode(String countryName) {
        String countryCode = null;
        String[] locales = Locale.getISOCountries();

        for (String isoCountryCode : locales) {
            Locale locale = new Locale("", isoCountryCode);
            String name = locale.getDisplayCountry(Locale.ENGLISH);

            if (name.equalsIgnoreCase(countryName)) {
                countryCode = isoCountryCode;
                break;
            }
        }

        return countryCode;
    }

    public static String getCurrencyCode(String countryCode) {
        cur = Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
        return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
    }


    public static String getWeather(String cityName) {
        city = cityName;
        StringBuilder result = new StringBuilder();
        try {
            String apiUrl = String.format(API_URL_FOR_WEATHER, cityName);
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                result.append(output);
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public Double getRateFor(String cur) {
        StringBuilder sb = new StringBuilder();
        sb.append(API_ULR_FOR_CUR);
        sb.append("from=").append(getCurrencyCode(getCountryCode(country)));
        sb.append("&to=").append(cur);
        String req_result = "";
        String url_str = String.valueOf(sb);
        try {
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            req_result = jsonobj.get("result").getAsString();
        } catch (Exception e) {

        }
        return Double.parseDouble(req_result);
    }

    public Double getNBPRate() {
        try {
            String url = URL_NBP_B;
            String currencyCode = cur;
            String exchangeRate = "";
            Document doc = Jsoup.connect(url).get();
            Elements tableRows = doc.select("table:not(.footable) tr");
            for (Element row : tableRows) {
                Elements cells = row.select("td");
                if (cells.size() >= 3 && cells.get(1).text().contains(currencyCode)) {
                     exchangeRate = cells.get(2).text();
                }
            }
            if(exchangeRate == ""){
                String url2 = URL_NBP_A;
                Document doc2 = Jsoup.connect(url2).get();
                Elements tableRows2 = doc2.select("table:not(.footable) tr");
                for (Element row2 : tableRows2) {
                    Elements cells2 = row2.select("td");
                    if (cells2.size() >= 3 && cells2.get(1).text().contains(currencyCode)) {
                        exchangeRate = cells2.get(2).text();
                    }
            }
                exchangeRate = exchangeRate.replace(",",".");
                return Double.parseDouble(exchangeRate);
        }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}