package org.example.controllers.ahmed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.*;
import java.util.List;
import java.util.ResourceBundle;

public class NewsViewController implements Initializable {
    private static final String ACCESS_KEY = "679ab71c2d43f574011f91df519103e2";
    private static final int LIMIT = 5;

    @FXML private ListView<NewsResponse.NewsData> listNews;
    @FXML private Button refreshButton;
    @FXML private ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1) CellFactory virtualisée pour n'afficher que les cellules visibles
        listNews.setCellFactory(lv -> new ListCell<>() {
            private final HBox root;
            private final ImageView iv;
            private final Label title, meta;

            {
                iv = new ImageView();
                iv.setFitWidth(120);
                iv.setPreserveRatio(true);
                title = new Label(); title.getStyleClass().add("news-title");
                meta  = new Label(); meta .getStyleClass().add("news-meta");
                VBox vbox = new VBox(title, meta);
                HBox.setHgrow(vbox, Priority.ALWAYS);
                root = new HBox(10, iv, vbox);
                root.getStyleClass().add("news-card");
            }

            @Override
            protected void updateItem(NewsResponse.NewsData item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    title.setText(item.title);
                    meta.setText(item.source + " • " + item.published_at);
                    if (item.image != null) {
                        // Chargement en background pour ne pas bloquer l'UI
                        iv.setImage(new Image(item.image, 120, 0, true, true));
                    } else {
                        iv.setImage(null);
                    }
                    setGraphic(root);
                }
            }
        });

        // 2) Rafraîchir au clic
        refreshButton.setOnAction(e -> fetchNews());

        // 3) Premier chargement
        fetchNews();
    }

    @FXML
    private void fetchNews() {
        progressIndicator.setVisible(true);
        String url = String.format(
                "http://api.mediastack.com/v1/news?access_key=%s&languages=fr&limit=%d",
                ACCESS_KEY, LIMIT
        );

        HttpClient.newHttpClient()
                .sendAsync(
                        HttpRequest.newBuilder(URI.create(url)).GET().build(),
                        HttpResponse.BodyHandlers.ofString()
                )
                .thenApply(HttpResponse::body)
                .thenAccept(this::parseAndDisplay)
                .exceptionally(ex -> { ex.printStackTrace(); return null; });
    }

    private void parseAndDisplay(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper()
                    // Ignore pagination & autres champs inconnus
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            NewsResponse resp = mapper.readValue(json, NewsResponse.class);

            Platform.runLater(() -> {
                listNews.getItems().setAll(resp.data);
                progressIndicator.setVisible(false);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Mappage JSON
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewsResponse {
        public List<NewsData> data;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NewsData {
            public String author, title, description, url, source, image, published_at;
        }
    }


}
