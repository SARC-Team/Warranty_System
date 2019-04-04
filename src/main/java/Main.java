package main.java;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.splash.MyPreloader;

public class Main extends Application {

    private static final int COUNT_LIMIT = 4;

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, MyPreloader.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login/Login.fxml"));

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {


        for (int i = 1; i <= COUNT_LIMIT; i++) {
            double progress = (double) i / 10;
            System.out.println("progress: " + progress);
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
            Thread.sleep(2000);
        }

    }
}
