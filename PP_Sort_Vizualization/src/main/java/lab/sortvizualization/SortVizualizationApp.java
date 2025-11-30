package lab.sortvizualization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SortVizualizationApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SortVizualizationApp.class.getResource("sort-visual.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setTitle("Визуализация сортировок");

        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}