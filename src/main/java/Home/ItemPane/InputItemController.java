package main.java.Home.ItemPane;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import main.java.Home.HomePane.HomeUIController;
import main.java.Home.QRCode;
import main.java.Home.Set_type_brand;
import main.java.SQLDatabase.SQLConnection;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static java.lang.Float.parseFloat;


public class InputItemController implements Initializable {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    ObservableList<String> listtype = FXCollections.observableArrayList("Laptop","Phone","Keybourd","Mouse","Pen_Drive");
    
    ObservableList<String> brandtype_lap = FXCollections.observableArrayList("HP","Dell","Abanse","Acer","AGC","Microsoft","Lenovo");
    ObservableList<String> brandtype_phone = FXCollections.observableArrayList("Lava","Apple","LES3","Nokia","Microsoft","Bird");
    ObservableList<String> brandtype_Keybourd = FXCollections.observableArrayList("Yamaha","Casio","Roland","Kawai","Korg");
    ObservableList<String> brandtype_Mouse = FXCollections.observableArrayList("Yamaha","Casio","Roland","Kawai","Korg");
    ObservableList<String> brandtype_Pen = FXCollections.observableArrayList("Kingston","SanDisk","Samsung","PNY","Patrot");
    
    ObservableList<String> listhardware = FXCollections.observableArrayList("3 Months","6 Months","1 Year","2 Year","3 Year","4 Year","5 Year");
    ObservableList<String> listsoftware = FXCollections.observableArrayList("3 Months","6 Months","1 Year","2 Year","3 Year","4 Year","5 Year");
    
    @FXML
    private JFXComboBox<String> servicewar;
    @FXML
    private JFXComboBox<String> hardwarewar;
    @FXML
    private Pane namebox;
    @FXML
    private Pane warrantybox;
    @FXML
    private Pane amountbox;
    @FXML
    private Pane serialbox;
    @FXML
    private JFXButton btnWarranty;
    @FXML
    private AnchorPane newItemPane;
    @FXML
    private JFXComboBox<String> brandtype;
    @FXML
    private JFXComboBox<String> typename;
    @FXML
    private Pane brandbox;
    @FXML
    private JFXTextField serialid;
    @FXML
    private JFXTextField amount;

    public String email1; 
    @FXML
    private JFXTextField buyamount;
   
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        typename.setItems(listtype);
        hardwarewar.setItems(listhardware);
        servicewar.setItems(listsoftware);

        SQLConnection conclass1 = new SQLConnection();
        connection= SQLConnection.createConnection();
       
        
     
    }    
        
      
    
    
    @FXML
    private void btnwarranty(ActionEvent event) throws WriterException, IOException, NotFoundException {
        int branchid = 0;
        String branchname = "";
        String email = this.email1;
        String checkemail;
        
        
        String query1 = "select * from companyinfo";
      
        try{
            ps = connection.prepareStatement(query1);   
            rs = ps.executeQuery(query1);
            while(rs.next()){
                checkemail = rs.getString("Email");
                branchid = rs.getInt("Branch_ID");
                branchname = rs.getString("Branch_Name");
                
                if(checkemail.equals(email)){
                    break;
                }
                //ps.close();
                
            }
            //JOptionPane.showMessageDialog(null, "Branch Found Successfully" + email);
        }
        
        catch (Exception ex) {
            System.out.print("Do not connect to DB - Error:" + ex);
            JOptionPane.showMessageDialog(null, "Branch Found Fail" + ex + email);
        }
        
        
        
        
        Set_type_brand settb = new Set_type_brand();
        
        String typeid = settb.set_type_ID(typename.getValue());
        String brandid = settb.set_Brand_ID(brandtype.getValue());
        
        String setitem = "INSERT INTO singer_items(Serial_Number, Branch_ID, Branch_Name, ItemType_ID, ItemType_Name, ItemBrand_ID, ItemBrand_Name, Amount, Warranty_Hardware, Warranty_Software,QRCode,Buy_Amount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ";
        
        int lengthid = Integer.toString(branchid).length();
        String serial_number;
        
        if(lengthid == 1){
            serial_number = "00" + branchid + typeid + brandid + serialid.getText();
        }
        else{
            serial_number = "0" + branchid + typeid + brandid + serialid.getText();
        }
        
        QRCode qrCode = new QRCode(serial_number);
        
        try {
            ps = connection.prepareStatement(setitem);

        ps.setString(1, serial_number);
        ps.setInt(2, branchid);
        ps.setString(3, branchname);      
        ps.setString(4, typeid);
        ps.setString(5, typename.getValue());
        ps.setString(6, brandid);
        ps.setString(7, brandtype.getValue());
        ps.setFloat(8, parseFloat(amount.getText()));
        ps.setString(9, hardwarewar.getValue());
        ps.setString(10, servicewar.getValue());
        
        FileInputStream fin=new FileInputStream("C:\\WarrantySystem\\QRCodes\\" + serial_number + ".png");  
        ps.setBinaryStream(11,fin,fin.available());          
        ps.setFloat(12,parseFloat(buyamount.getText()));
        
        
        ps.executeUpdate();
        //ps.close();
        JOptionPane.showMessageDialog(null, "Item Installing Successfully");
        
        
        
        } catch (Exception ex) {
            System.out.print("Do not connect to DB - Error:"+ex);
        }
        
        
        
        
        
        btnWarranty.getScene().getWindow().hide();
            
            FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/main/java/Home/HomePane/homeUI.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ex){
                        Logger.getLogger("Error");
                    }
                    
                    HomeUIController display = loader.getController();
                    display.setTexr(email1);
                    Parent root1 = loader.getRoot();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root1);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.showAndWait();
                    
           
            
            newItemPane.setVisible(false);
        
        
    }
    
 
    

    @FXML
    private void btnbackModel(MouseEvent event) {
        brandbox.setVisible(false);
        namebox.setVisible(true);
    }
    @FXML
    private void btnModel(MouseEvent event) {
        brandbox.setVisible(false);
        serialbox.setVisible(true);
    }
     @FXML
    private void btnbackserial(MouseEvent event) {
        brandbox.setVisible(true);
        serialbox.setVisible(false);
    }
    
    
    @FXML
    private void btnserial(MouseEvent event) {
        serialbox.setVisible(false);
        amountbox.setVisible(true);
    }
    @FXML
    private void btnbackamount(MouseEvent event) {
        amountbox.setVisible(false);
        serialbox.setVisible(true);
    }

    @FXML
    private void btnamount(MouseEvent event) {
        amountbox.setVisible(false);
        warrantybox.setVisible(true);
    }
    

    

    

    @FXML
    private void btnname(ActionEvent event) {
        String type = null;
        try{
            type = typename.getValue();
        
        switch (type) {
            case "Laptop":
                brandtype.setItems(brandtype_lap);
                break;
            case "Phone":
                brandtype.setItems(brandtype_phone);
                break;
            case "Keybourd":
                brandtype.setItems(brandtype_Keybourd);
                break;
            case "Mouse":
                brandtype.setItems(brandtype_Mouse);
                break;
            case "Pen_Drive":
                brandtype.setItems(brandtype_Pen);
                break;
            default:
                break;
        }
        }catch(Exception e){
            System.out.println("Error : " + e);
        }
        
    
            

        namebox.setVisible(false);
        brandbox.setVisible(true);
    }

    private void sendBranch(int branchid, String branchname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setTexr(String Email1) {
        this.email1 = Email1;
    }

    
    
    
    
}
