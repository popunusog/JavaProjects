package zad1;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;

public class Wiki extends Region {
    private final WebView browser = new WebView();
    private final WebEngine webEngine = browser.getEngine();

    public Wiki() {
        webEngine.load("https://en.wikipedia.org/wiki/Main_Page");
        getChildren().add(browser);
    }

    public void cityPage(String city) {
        webEngine.load("https://en.wikipedia.org/wiki/"+city);
    }

    @Override
    protected void layoutChildren() {
        double width = getWidth(), height = getHeight();
        double margin = 10;
        double x = margin;
        double y = margin;
        double w = width - (margin * 2);
        double h = height - (margin * 2);

        layoutInArea(browser, x, y, w, h, 0, HPos.CENTER, VPos.CENTER);
    }
}