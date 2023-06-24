/**
 *
 *  @author Piotrowski Jakub S23184
 *
 */

package zad1;

import javafx.application.Application;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Italy");
    String weatherJson = s.getWeather("Rome");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();

    System.out.println(weatherJson);
    System.out.println(rate1);
    System.out.println(rate2);

    Application.launch(App.class);
  }
}
