package project.graphics.demo;

import project.framework.HttpRequest;
import project.framework.Provider;
import project.framework.Service;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;


import java.io.IOException;
import java.util.*;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Carico file xml
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        //Creazione scena
        Scene scene = new Scene(fxmlLoader.load(), 1250, 750);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);

        HttpRequest fetchContriesList = new HttpRequest("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/countries_list");
        JSONArray jsonCountriesList = new JSONArray(fetchContriesList.getResponse());

        Vector<String> countries = new Vector<>();
        Map<String, String> countryNameToCode = new HashMap<>();

        for (int i = 0; i<jsonCountriesList.length(); i++) {
            countries.add(jsonCountriesList.getJSONObject(i).getString("countryName"));
            countryNameToCode.put(jsonCountriesList.getJSONObject(i).getString("countryName"), jsonCountriesList.getJSONObject(i).getString("countryCode"));
        }

        HttpRequest fetchAllProviders = new HttpRequest("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/tsp_list");
        JSONArray jsonProvidersList = new JSONArray(fetchAllProviders.getResponse());
        Provider[] all_tsp = new Provider[jsonProvidersList.length()];

        Multimap<String, Provider> countryMap = ArrayListMultimap.create();
        Multimap<String, Provider> typeMap = ArrayListMultimap.create();
        Vector<String> typesOfService = new Vector<>();
        Vector<Provider> providers = new Vector<>();
        Vector<String> statuses = new Vector<>();

        // Riempimento mappe e vettori iniziali
        for (int i = 0; i<jsonProvidersList.length(); i++) {

            //Decodifica json
            all_tsp[i] = new Provider(jsonProvidersList.getJSONObject(i).toString());

            //inserimento mappa key Country value provider
            countryMap.put(all_tsp[i].getCountryCode(), all_tsp[i]);

            //inserimento vector provider e vettore copia
            providers.add(all_tsp[i]);

            //inserimento vector statuses
            for (int j = 0; j<all_tsp[i].getServices().length; j++) {
                Service[] s = all_tsp[i].getServices();
                typeMap.put(s[j].getCurrentStatus(), all_tsp[i]);
                if (!statuses.contains(s[j].getCurrentStatus()))
                    statuses.add(s[j].getCurrentStatus());
            }

            //Inserimento mappa Service Types
            for (int j = 0; j<all_tsp[i].getServiceTypes().length; j++) {
                String[] s = all_tsp[i].getServiceTypes();
                typeMap.put(s[j], all_tsp[i]);
                if (!typesOfService.contains(s[j]))
                    typesOfService.add(s[j]);
            }
        }

        FilterController filter = new FilterController(countryNameToCode, typesOfService, statuses, providers);

        // Countries
        AnchorPane countriesPane = (AnchorPane) fxmlLoader.getNamespace().get("countriesAnchorPane");

        VBox countriesCheckBoxesLeft = new VBox();
        VBox countriesCheckBoxesRight = new VBox();

        Timeline countriesTPane = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {

            List<CheckBox> filteredCountries = filter.filterCountries();
            countriesCheckBoxesLeft.getChildren().clear();
            countriesCheckBoxesRight.getChildren().clear();
            int separator = 0;
            for (CheckBox CBox : filteredCountries) {
                if (separator < (filteredCountries.size()/2))
                    countriesCheckBoxesLeft.getChildren().add(CBox);
                else
                    countriesCheckBoxesRight.getChildren().add(CBox);
                separator++;
            }
        }));

        countriesTPane.setCycleCount(Animation.INDEFINITE);
        countriesTPane.play();

        HBox countriesCheckBoxes = new HBox(countriesCheckBoxesLeft, countriesCheckBoxesRight);
        countriesCheckBoxes.setSpacing(15);

        AnchorPane.setTopAnchor(countriesCheckBoxes, 75.0);
        AnchorPane.setLeftAnchor(countriesCheckBoxes, 22.0);

        countriesCheckBoxes.setSpacing(55);
        countriesCheckBoxes.setAlignment(Pos.CENTER_LEFT);

        // Types
        TypeCheckBoxController typeCheckBoxController = new TypeCheckBoxController(typesOfService);

        AnchorPane tosAnchorPane = (AnchorPane) fxmlLoader.getNamespace().get("tosAnchorPane");

        VBox tosCheckBoxesLeft = new VBox();
        VBox tosCheckBoxesRight = new VBox();

        Timeline tosPane = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            List<CheckBox> tosCBoxes = filter.filterTypes();

            tosCheckBoxesLeft.getChildren().clear();
            tosCheckBoxesRight.getChildren().clear();

            int separator = 0;
            for (CheckBox CBox : tosCBoxes) {
                if (separator < tosCBoxes.size()/2)
                    tosCheckBoxesLeft.getChildren().add(CBox);
                else
                    tosCheckBoxesRight.getChildren().add(CBox);
                separator++;
            }

        }));
        tosPane.setCycleCount(Animation.INDEFINITE);
        tosPane.play();


        HBox tosCheckBoxes = new HBox(tosCheckBoxesLeft, tosCheckBoxesRight);
        tosCheckBoxes.setSpacing(15);

        AnchorPane.setTopAnchor(tosCheckBoxes, 75.0);
        AnchorPane.setLeftAnchor(tosCheckBoxes, 22.0);


        tosCheckBoxes.setSpacing(55);
        tosCheckBoxes.setAlignment(Pos.CENTER_LEFT);

        // Statuses
        AnchorPane ssAnchorPane = (AnchorPane) fxmlLoader.getNamespace().get("ssAnchorPane");
        StatusCheckBoxController statusCheckBoxController = new StatusCheckBoxController(statuses);

        VBox ssCheckBoxesLeft = new VBox();
        VBox ssCheckBoxesRight = new VBox();

        Timeline ssPane = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            List<CheckBox> ssCBoxes = filter.filterStatuses();

            ssCheckBoxesLeft.getChildren().clear();
            ssCheckBoxesRight.getChildren().clear();

            System.out.println(ssCBoxes);

            int separator = 0;
            for (CheckBox CBox : ssCBoxes) {
                if (separator < ssCBoxes.size()/2)
                    ssCheckBoxesLeft.getChildren().add(CBox);
                else
                    ssCheckBoxesRight.getChildren().add(CBox);
                separator++;
            }

        }));
        ssPane.setCycleCount(Animation.INDEFINITE);
        ssPane.play();

        HBox ssCheckBoxes = new HBox(ssCheckBoxesLeft, ssCheckBoxesRight);
        ssCheckBoxes.setSpacing(15);

        AnchorPane.setTopAnchor(ssCheckBoxes, 75.0);
        AnchorPane.setLeftAnchor(ssCheckBoxes, 22.0);


        ssCheckBoxes.setSpacing(55);
        ssCheckBoxes.setAlignment(Pos.CENTER_LEFT);



        // Provider
        AnchorPane scrollPane = (AnchorPane) fxmlLoader.getNamespace().get("providersScrollPAne");

        VBox providersCheckBoxes = new VBox();

        Timeline pane = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            List<CheckBox> filtered_providers = filter.filterProviders();

            providersCheckBoxes.getChildren().clear();
            providersCheckBoxes.getChildren().addAll(filtered_providers);
        }));

        pane.setCycleCount(Animation.INDEFINITE);
        pane.play();

        AnchorPane.setTopAnchor(providersCheckBoxes, 10.0);
        AnchorPane.setLeftAnchor(providersCheckBoxes, 22.0);

        providersCheckBoxes.setSpacing(10);
        providersCheckBoxes.setAlignment(Pos.CENTER_LEFT);

        countriesPane.getChildren().add(countriesCheckBoxes);
        tosAnchorPane.getChildren().add(tosCheckBoxes);
        ssAnchorPane.getChildren().add(ssCheckBoxes);
        scrollPane.getChildren().add(providersCheckBoxes);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/stylesheet.css")).toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}