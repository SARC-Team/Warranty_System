package main.java.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.Home.HomePane.HomeUIController;
import main.java.SQLDatabase.SQLConnection;
import main.java.editable.branchList;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class LoginController implements Initializable {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;



    @FXML
    private Pane content_area;
    @FXML
    private Pane register_area;
    @FXML
    private JFXButton btn_login;
    @FXML
    private AnchorPane loginpane;
    @FXML
    private PasswordField txtpass1sign;
    @FXML
    private JFXButton btnsign;
    @FXML
    private JFXTextField txtemailsign;
    @FXML
    private JFXTextField txtloaction;
    @FXML
    private JFXTextField txtaddress;
    @FXML
    private JFXTextField txtphone;
    @FXML
    private JFXTextField txtskype;
    @FXML
    private JFXTextField txtemaillogin;
    @FXML
    private JFXPasswordField txtpasslogin;
    @FXML
    private PasswordField txtpass1sign1;
    @FXML
    private Label txtcompany;
    @FXML
    private JFXComboBox<String> txtbranch;
    @FXML
    private ImageView companylogo;




    @Override
    public void initialize(URL url, ResourceBundle rb) {

        branchList branch = new branchList();
        txtbranch.setItems(branch.getbranch());

        SQLConnection conclass = new SQLConnection();
        connection= SQLConnection.createConnection();


    }

    @FXML
    private void forgetpass(MouseEvent event) {

        btnsign.getScene().getWindow().hide();
        Stage stage = new Stage();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/main/java/dialogBox/forgetPassword.fxml"));

            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        }catch(IOException ex){
            JOptionPane.showMessageDialog(null, "Loading page is Fail \n Error : "+ex);
        }


        loginpane.setVisible(Boolean.FALSE);

    }


    @FXML
    public void signup(ActionEvent event){
        String txtb = "Horana";


        String query = "insert into companyinfo (Company_Name, Branch_Name, Email, Password, Address, Phone, Web) values (?,?,?,?,?,?,?) ";
        try {
            ps = connection.prepareStatement(query);



            ps.setString(1, txtcompany.getText());

            if(txtbranch.getValue().equals("")){
                ps.setString(2, txtb);
            }else{
                ps.setString(2, txtbranch.getValue());
            }

            ps.setString(3, txtemailsign.getText());
            ps.setString(4, txtpass1sign.getText());
            ps.setString(5, txtaddress.getText());
            ps.setString(6, txtphone.getText());
            ps.setString(7, txtskype.getText());

            ps.execute();

            JOptionPane.showMessageDialog(null, "Signin Successfully");



        } catch (Exception ex) {
            System.out.print("Do not connect to DB - Error:"+ex);
        }


    }
    public String email1;
    @FXML
    public void sendEmail(MouseEvent event){
        this.email1 = txtemaillogin.getText();

    }

    @FXML
    public void login(ActionEvent event) {
        int count = 0;
        String Email1 = txtemaillogin.getText().trim();
        String Password1 = txtpasslogin.getText().trim();


        String query = "SELECT * FROM companyinfo";
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery(query);
            count++;
            boolean check = false;
            while(rs.next()){

                String user = rs.getString("Email");
                String pass = rs.getString("Password");

                if(user.equals(Email1) && pass.equals(Password1)){
                    check = true;
                    //ps.close();
                    JOptionPane.showMessageDialog(null, "Signup Successfully");
                    /*
                    Stage stage = new Stage();
                    Parent root1 = FXMLLoader.load(getClass().getResource("/Home/homeUI.fxml"));
                    //stage.initStyle(StageStyle.UNDECORATED);

                    */

                    btn_login.getScene().getWindow().hide();

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/main/java/Home/HomePane/homeUI.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ex){
                        Logger.getLogger(user, Email1);
                    }

                    HomeUIController display = loader.getController();
                    display.setTexr(Email1);
                    Parent root1 = loader.getRoot();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root1);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.showAndWait();

                    loginpane.setVisible(Boolean.FALSE);
                }

            }
            if(check == false){
                JOptionPane.showMessageDialog(null, "Signup Fail");
            }
            else if(count>3){
                JOptionPane.showMessageDialog(null, "Signup Denied");
            }



        } catch (Exception ex) {
            System.out.print("Do not connect to DB - Error:"+ex);
        }

    }

    @FXML
    private void back(MouseEvent event) {
        content_area.setVisible(true);
        register_area.setVisible(false);
    }




    @FXML
    private void btnclose(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void btnmini(MouseEvent event) {
        Stage stage1 = (Stage)loginpane.getScene().getWindow();
        stage1.setIconified(true);
    }

    @FXML
    private void signuplink(MouseEvent event) {
        content_area.setVisible(false);
        register_area.setVisible(true);
    }

}
