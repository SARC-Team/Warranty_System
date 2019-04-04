package main.java.dialogBox;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.SQLDatabase.SQLConnection;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;


public class ForgetPasswordController implements Initializable {

    @FXML
    private AnchorPane recoverypane;
    @FXML
    private JFXTextField txtrecoveryemail;

    private Connection connection = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    @FXML
    private JFXTextField txtsecorecode;
    @FXML
    private Pane emailpane;
    @FXML
    private Pane codepane;
    public String email;
    public String secure;
    @FXML
    private Pane passwordpane;
    @FXML
    private JFXPasswordField txtnewpass;
    @FXML
    private JFXPasswordField txtconfirmpass;
    @FXML
    private JFXButton btnrecoveryfinish;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SQLConnection conclass = new SQLConnection();
        connection= SQLConnection.createConnection();
        
       
    }    

    @FXML
    private void btnclose(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void btnmini(MouseEvent event) {
         Stage stage1 = (Stage)recoverypane.getScene().getWindow();
         stage1.setIconified(true);
    }

    @FXML
    private void btnresetpass(MouseEvent event) {
        this.email = txtrecoveryemail.getText();
        
        String query1 = "SELECT * FROM companyinfo";
        boolean check = false;
        try {
            ps = connection.prepareStatement(query1);   
            rs = ps.executeQuery(query1);
            
            while(rs.next()){
               
                String user = rs.getString("Email");
                
                
                if(user.equals(this.email)){
                    check = true;
                    break;
                }
            }
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Database is not connected");
        }
        
        if(check){
            this.emailmethod();
            codepane.setVisible(true);
            emailpane.setVisible(false);
            

        }else{
            txtrecoveryemail.setText("Email is wrong, try again!");
            
        }
        
    }

    
    @FXML
    private void btnnextstep(MouseEvent event) {
        String code = txtsecorecode.getText();
        
        
        if(secure.equals(code)){
            passwordpane.setVisible(true);
            codepane.setVisible(false);
        }else{
            txtsecorecode.setText("");
        }
        
    }
    
    
    

    @FXML
    private void btnfinish(MouseEvent event) {
        String newpassword = txtnewpass.getText();
        String confirmpassword = txtconfirmpass.getText();
        
        if(!newpassword.equals(confirmpassword)){
            txtnewpass.setText("Not Match, try again!");
            txtconfirmpass.setText("");
        }else{
            
            String query5 = "UPDATE companyinfo SET Password = ? WHERE Email = ?";
            try {
                // create the java mysql update preparedstatement
                ps = connection.prepareStatement(query5);

                ps.setString(1,newpassword);
                ps.setString(2,txtrecoveryemail.getText());
                
                // execute the java preparedstatement
                ps.executeUpdate();
                //connection.close();
                JOptionPane.showMessageDialog(null, "Password set Successfull");
                
                //open login page
                btnrecoveryfinish.getScene().getWindow().hide();
                Stage stage = new Stage();
                try{
                    Parent root = FXMLLoader.load(getClass().getResource("/splashScreen/Login.fxml"));
                    
                    Scene scene = new Scene(root);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();
                }catch(IOException ex){
                    JOptionPane.showMessageDialog(null, "Loading page is Fail \n Error : "+ex);
                }
                recoverypane.setVisible(Boolean.FALSE);
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Password set Fail \n Error : "+e);
            }
            
            
            
            
            
        }
    }
    
    
    //Back Button
    
    @FXML
    private void btnbackemail(MouseEvent event) {
        
        emailpane.setVisible(false);
        Stage stage = new Stage();
        try{
                    Parent root = FXMLLoader.load(getClass().getResource("/splashScreen/Login.fxml"));
                    
                    Scene scene = new Scene(root);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();
                }catch(IOException ex){
                    JOptionPane.showMessageDialog(null, "Loading page is Fail \n Error : "+ex);
                }
                recoverypane.setVisible(Boolean.FALSE);
                
            
    }
        

    @FXML
    private void btnbackcode(MouseEvent event) {
        codepane.setVisible(false);
        emailpane.setVisible(true);
    }

    @FXML
    private void btnbackpassword(MouseEvent event) {
        passwordpane.setVisible(false);
        codepane.setVisible(true);  
    }
    
    public void emailmethod(){
        Random rand = new Random();
        
        
        this.secure = Integer.toString(rand.nextInt(9999)); 
        
        try{
            String host ="smtp.gmail.com" ;
            String user = "sarcorganization1@gmail.com";
            String pass = "sarc1234";
            String to = this.email;
            String from = "sarcorganization1@gmail.com";
            String messageText = "Your Secure Number :" + this.secure;
            String subject = "This is your secure code for your Warranty System Software. Please insert this code to activate your account.";
            
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject); msg.setSentDate(new Date());
            msg.setText(messageText);

            
            Transport transport=mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            System.out.println("message send successfully");
            JOptionPane.showMessageDialog(null, "Message send Successfully");
        }catch(Exception ex)
        {
            System.out.println("Error : " + ex);
            
        }
    }
    

    
}
