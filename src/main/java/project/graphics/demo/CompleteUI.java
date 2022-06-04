package project.graphics.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompleteUI extends Application {
    private static Scene backScene;
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("EU Trust Service Dashboard");
        primaryStage.getIcons().add(new Image("https://i.imgur.com/xm62NkC.png"));
        primaryStage.setResizable(false);
        stage = primaryStage;

        swapScene(LoadingUI.getScene());
        primaryStage.show();

        Service<Scene> process = new Service<>() {
            @Override
            protected Task<Scene> createTask() {
                return new Task<Scene>() {
                    @Override
                    protected Scene call() throws Exception {
                        return SearchUI.getScene();
                    }
                };
            }
        };

        process.setOnSucceeded( e -> {
            swapScene(process.getValue());
        });

        process.start();

        //swapScene("s", null);
    }

    public static void swapScene(Scene newScene) {
        Scene tmpScene = stage.getScene();
        stage.setScene(newScene);
        backScene = tmpScene;
    }

    public static void swapScene(String sceneType, AtomicBoolean darkMode) throws IOException {
        Scene newScene = null;
        switch (sceneType) {
            case "l":
                newScene = LoadingUI.getScene();
                break;
            case "s":
                newScene = SearchUI.getScene();
                break;
            case "r":
                newScene = ResultUI.result(stage, darkMode);
                break;
        }
        Scene tmpScene = stage.getScene();
        stage.setScene(newScene);
        backScene = tmpScene;
    }

    public static void backScene() {
        Scene tmpScene = stage.getScene();
        stage.setScene(backScene);
        backScene = tmpScene;
    }
}
