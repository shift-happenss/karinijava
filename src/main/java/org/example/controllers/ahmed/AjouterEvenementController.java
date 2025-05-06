package org.example.controllers.ahmed;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.entities.Evenement;
import org.example.services.ServiceEvenement;


import javafx.scene.text.Text;
import org.example.utils.APIConnector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterEvenementController {

    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfType;
    @FXML
    private DatePicker dpDateDebut;
    @FXML
    private DatePicker dpDateFin;
    @FXML
    private TextArea taContenu;
    @FXML
    private TextField tfLieu;
    @FXML
    private TextField tfLatitude;
    @FXML
    private TextField tfLongitude;

    private ServiceEvenement serviceEvenement;
    private Runnable onEvenementAjoute;

    private Evenement currentEvent; // pour conserver l’objet en cours

    // NOUVEAUX CHAMPS POUR LA MÉTÉO
    @FXML private Button btnMeteo;
    @FXML private Text weatherInfo;
 // pour conserver l’objet courant

    // URL de l’API MetaWeather
    private final String cityAPI    = "https://www.metaweather.com/api/location/search/?query=";
    private final String weatherAPI = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&appid=%s";

    private final String API_KEY = "bcc9ba15d67eaa0396e6ab86dc628e81"; // ← Remplacez ceci


    @FXML private Button btnGenerate;
    @FXML private ImageView ivPreview;
    private String generatedImageUrl;

    public AjouterEvenementController() {
        try {
            serviceEvenement = new ServiceEvenement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    public void setOnEvenementAjoute(Runnable callback) {
        this.onEvenementAjoute = callback;
    }

    @FXML
    private void handleAjouter() {
        if (!validateInputs()) return;

        Evenement ev = new Evenement(
                tfNom.getText(), tfType.getText(),
                dpDateDebut.getValue(), dpDateFin.getValue(),
                taContenu.getText().trim(),
                tfLieu.getText(),
                Double.parseDouble(tfLatitude.getText()),
                Double.parseDouble(tfLongitude.getText()),
                generatedImageUrl // Ajouter l'URL générée
        );

        serviceEvenement.ajouterEvenement(ev);
        if (onEvenementAjoute != null) onEvenementAjoute.run();
        closeWindow();
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private boolean validateInputs() {
        boolean valid = true;
        // Réinitialiser les styles
        resetField(tfNom);
        resetField(tfType);
        resetField(dpDateDebut);
        resetField(dpDateFin);
        resetField(taContenu);
        resetField(tfLieu);
        resetField(tfLatitude);
        resetField(tfLongitude);

        if (tfNom.getText() == null || tfNom.getText().trim().isEmpty()) {
            setFieldError(tfNom, "Nom obligatoire");
            valid = false;
        }
        if (tfType.getText() == null || tfType.getText().trim().isEmpty()) {
            setFieldError(tfType, "Type obligatoire");
            valid = false;
        }
        if (dpDateDebut.getValue() == null) {
            setFieldError(dpDateDebut, "Date début obligatoire");
            valid = false;
        }
        if (dpDateFin.getValue() == null) {
            setFieldError(dpDateFin, "Date fin obligatoire");
            valid = false;
        }
        if (taContenu.getText() == null || taContenu.getText().trim().isEmpty()) {
            setFieldError(taContenu, "Contenu obligatoire");
            valid = false;
        }
        if (tfLieu.getText() == null || tfLieu.getText().trim().isEmpty()) {
            setFieldError(tfLieu, "Lieu obligatoire");
            valid = false;
        }
        if (tfLatitude.getText() == null || tfLatitude.getText().trim().isEmpty()) {
            setFieldError(tfLatitude, "Latitude obligatoire");
            valid = false;
        }
        if (tfLongitude.getText() == null || tfLongitude.getText().trim().isEmpty()) {
            setFieldError(tfLongitude, "Longitude obligatoire");
            valid = false;
        }
        return valid;
    }

    private void setFieldError(Control field, String errorMessage) {
        field.setStyle("-fx-border-color: red;");
        // Pour TextField ou TextArea, on met à jour le promptText pour indiquer l'erreur
        if (field instanceof TextInputControl) {
            ((TextInputControl) field).setPromptText(errorMessage);
        } else if (field instanceof DatePicker) {
            ((DatePicker) field).setPromptText(errorMessage);
        }
    }

    private void resetField(Control field) {
        field.setStyle(""); // Réinitialise le style
        // On peut remettre le promptText par défaut si désiré (si vous avez défini un prompt initial dans le FXML)
    }

    private void closeWindow() {
        Stage stage = (Stage) tfNom.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void handleVoirMeteo() {
        String city = tfLieu.getText().trim();
        LocalDate targetDate = dpDateDebut.getValue(); // Date de début

        if (city.isEmpty() || targetDate == null) {
            showAlert("Erreur", "Veuillez saisir un lieu et une date de début.");
            return;
        }

        Task<JSONObject> task = new Task<>() {
            @Override
            protected JSONObject call() throws Exception {
                return getWeatherForDate(city, targetDate);
            }
        };

        task.setOnSucceeded(e -> {
            JSONObject weather = task.getValue();
            if (weather != null) {
                double min = (Double) weather.get("temp_min");
                double current = (Double) weather.get("temp");
                double max = (Double) weather.get("temp_max");
                weatherInfo.setText(String.format("Min: %.1f°C\nActuelle: %.1f°C\nMax: %.1f°C", min, current, max));
            } else {
                weatherInfo.setText("Données non disponibles.");
            }
        });

        task.setOnFailed(e -> {
            weatherInfo.setText("Erreur de connexion.");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    // Nouvelle méthode pour récupérer les données par date
    private JSONObject getWeatherForDate(String city, LocalDate targetDate) throws Exception {
        String url = String.format(weatherAPI, city, API_KEY);
        APIConnector connector = new APIConnector(url);
        JSONObject response = connector.getJSONObject("");

        if (response == null || !response.containsKey("list")) {
            throw new Exception("Ville ou API indisponible.");
        }

        JSONArray forecasts = (JSONArray) response.get("list");
        for (Object item : forecasts) {
            JSONObject forecast = (JSONObject) item;
            String dateStr = (String) forecast.get("dt_txt");
            LocalDate forecastDate = LocalDate.parse(dateStr.substring(0, 10));

            if (forecastDate.isEqual(targetDate)) {
                JSONObject main = (JSONObject) forecast.get("main");
                return main;
            }
        }

        throw new Exception("Aucune donnée pour cette date.");
    }

    /** Récupère le WOEID via cityAPI */
    private String getWoeid(String city) throws Exception {
        APIConnector apiCity = new APIConnector(cityAPI);
        JSONArray arr = apiCity.getJSONArray(city);
        if (arr == null || arr.isEmpty())
            throw new Exception("Ville introuvable !");
        JSONObject data = (JSONObject) arr.get(0);
        return data.get("woeid").toString();
    }

    /** Récupère l’info météo du jour via weatherAPI */
    private JSONObject getTodayWeather(String woeid) throws Exception {
        APIConnector apiWeather = new APIConnector(weatherAPI);
        JSONObject root = apiWeather.getJSONObject(woeid + "/");
        JSONArray list = (JSONArray) root.get("consolidated_weather");
        return (JSONObject) list.get(0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);

        // Créer une zone de texte déroulante
        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    @FXML
    private void handleGenerateImage() {
        String contenu = taContenu.getText().trim();
        if (contenu.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir un contenu pour générer l'image");
            return;
        }

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return generateImage(contenu);
            }
        };

        task.setOnSucceeded(e -> {
            generatedImageUrl = task.getValue();
            if(generatedImageUrl != null) {
                ivPreview.setImage(new Image(generatedImageUrl));
            }
        });

        task.setOnFailed(e -> {
            showAlert("Erreur API", task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private String generateImage(String text) throws Exception {
        String apiUrl = "https://api.unsplash.com/photos/random?query=" + URLEncoder.encode(text, StandardCharsets.UTF_8) + "&client_id=lYM8sWeuGxZ8j0WR--8zoBgcNUgfBn6S0tGAa9xteds";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.body());
            JSONObject urls = (JSONObject) json.get("urls");
            return (String) urls.get("regular");
        } else {
            throw new Exception("Erreur Unsplash: " + response.body());
        }
    }

}
