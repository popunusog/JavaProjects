package zad1;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;


public class App extends Application {
    private String[] countryCity;
    private String currency;
    private Service service;
    private  JsonParser jsonParser = new JsonParser();

    @Override
    public void start(Stage stage) {
        stage.setTitle("TPO2");

        Wiki wikiPage = new Wiki();
        BorderPane bp = new BorderPane();
        bp.setCenter(wikiPage);
        BorderPane demandedData = new BorderPane();

        HBox firstBox = new HBox();
        HBox secondBox = new HBox();
        secondBox.setSpacing(5);

        Label weather = new Label("Weather");
        Label labelPLN = new Label("PLN: no data passed");
        Label currencyRate = new Label("Currency rate: no data passed");

        TextField CountryField = new TextField("Country City");
        Label CountryLabel = new Label("Country City:");
        CountryField.setOnAction(event -> {
            countryCity = CountryField.getText().split(" ");
            wikiPage.cityPage(countryCity[1]);
            service = new Service(countryCity[0]);
            JsonElement jsonElement = jsonParser.parse(service.getWeather(countryCity[1]));
            weather.setText(jsonElement.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsDouble() + " temp");
            labelPLN.setText("PLN: " + service.getNBPRate());
        });

        TextField currencyField = new TextField("USD");
        Label currencyLabel = new Label("Currency:");
        currencyField.setOnAction(event -> {
            currency = currencyField.getText();
            currencyRate.setText("Currency rate: " + service.getRateFor(currency));
        });

        firstBox.getChildren().addAll(CountryField,CountryLabel,currencyLabel,currencyField,labelPLN, currencyRate);
        secondBox.getChildren().addAll(labelPLN, currencyRate);

        demandedData.setLeft(firstBox);
        demandedData.setRight(secondBox);
        demandedData.setCenter(weather);
        bp.setTop(demandedData);

        Scene scene = new Scene(bp,1200,800);
        stage.setScene(scene);
        stage.show();
    }
}