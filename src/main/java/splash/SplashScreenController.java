package main.java.splash;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController implements Initializable {

    @FXML
    private Label progress;

    public static Label label;


    @FXML
    private ProgressBar progressBar;

    public static ProgressBar statProgressBar;

    /*@FXML
    private ImageView imageView; */

    @FXML
    private void handleButtonAction(ActionEvent event) {


    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label = progress ;
        statProgressBar = progressBar;

     /*
       try {
            Image image = new Image(new FileInputStream("src\\splashscreenproject\\sample.png"));

            imageView.setImage(image);
            imageView.setCache(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(splashScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
      */

    }

}
