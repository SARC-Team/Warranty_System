package main.java.Home.HomePane;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.Home.ItemPane.InputItemController;
import main.java.Home.QRCodeReaderPath;
import main.java.SQLDatabase.SQLConnection;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static main.java.Home.CustomerPane.GoogleDrive.CreateFolder.createGoogleFolder;


public class HomeUIController implements Initializable {
    int numberRow = 0;
    int numberRowsold = 0;
    int clap,cphone,ckey,cmouse,cpen;
    int slap,sphone,skey,smouse,spen;
    float plap,pphone,pkey,pmouse,ppen;
    int sumlap,sumphone,sumkey,summouse,sumpen;
    private Connection connection = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    File file;
    String pathsInfo;
    ObservableList<Person> data = FXCollections.observableArrayList();
    
    ObservableList<String> listtype = FXCollections.observableArrayList("Laptop","Phone","Keybourd","Mouse","Pen_Drive");
    
    ObservableList<String> brandtype_lap = FXCollections.observableArrayList("HP","Acer","AGC","Microsoft","dell","Lenovo");
    ObservableList<String> brandtype_phone = FXCollections.observableArrayList("Lava","Apple","LES3","Nokia","Microsoft","Bird");
    ObservableList<String> brandtype_Keybourd = FXCollections.observableArrayList("Yamaha","Casio","Roland","Kawai","Korg");
    ObservableList<String> brandtype_Mouse = FXCollections.observableArrayList("Yamaha","Casio","Roland","Kawai","Korg");
    ObservableList<String> brandtype_Pen = FXCollections.observableArrayList("Kingston","SanDisk","Samsung","PNY","Patrot");
    
    ObservableList<String> listhardware = FXCollections.observableArrayList("3 Months","6 Months","1 Year","2 Year","3 Year","4 Year","5 Year");
    ObservableList<String> listsoftware = FXCollections.observableArrayList("3 Months","6 Months","1 Year","2 Year","3 Year","4 Year","5 Year");
    
    ObservableList<String> listfiled1 = FXCollections.observableArrayList("Serial Number", "Item Name", "Brand Name", "Warranty Hardware","Warranty Software","Customer Name","Date Buy","Phone Number","Pay Method","Exprire","Sold");
    
    ObservableList<PieChart.Data> pieChart_number = FXCollections.observableArrayList(
                new PieChart.Data("Laptops",clap),
                new PieChart.Data("Phone",cphone),
                new PieChart.Data("Keyboard",ckey),
                new PieChart.Data("Mouse",cmouse),
                new PieChart.Data("Pen Drive",cpen)
        );
    ObservableList<PieChart.Data> pieChart_soldnum = FXCollections.observableArrayList(
                new PieChart.Data("Laptops",slap),
                new PieChart.Data("Phone",sphone),
                new PieChart.Data("Keyboard",skey),
                new PieChart.Data("Mouse",smouse),
                new PieChart.Data("Pen Drive",spen)
        );
    
    @FXML
    private JFXButton btnnewcustomer;
    @FXML
    private HBox Homepane;
    @FXML
    private Pane pane;
    @FXML
    private Pane homepane;
    @FXML
    private Pane viewtablepane;
    @FXML
    private JFXButton btnnewitem;
    @FXML
    private Pane verifypane;
    @FXML
    private Label email;
    @FXML
    private TableView<Person> tableview;
    @FXML
    private TableColumn<Person, String> serialno;
    @FXML
    private TableColumn<Person, String> itemname;
    @FXML
    private TableColumn<Person, String> brandname;
    @FXML
    private TableColumn<Person, String> warrantyhard;
    @FXML
    private TableColumn<Person, String> warrantysoft;
    @FXML
    private TableColumn<Person, String> customer;
    @FXML
    private TableColumn<Person, String> dbuy;
    @FXML
    private TableColumn<Person, String> phone;
    @FXML
    private TableColumn<Person, String> paymethod;
    @FXML
    private TableColumn<Person, Float> dpayment;
    @FXML
    private TableColumn<Person, Float> mpayment;
    @FXML
    private TableColumn<Person, Float> payamount;
    private TableColumn<Person, String> expire;
    @FXML
    private TableColumn<Person, String> sold;
    @FXML
    private TableColumn<Person, Integer> repair;
    private JFXComboBox<String> listitem;
    private JFXComboBox<String> listbrand;
    private JFXTextField txtsearch;
    @FXML
    private JFXTextField allitem;
    @FXML
    private JFXTextField solditem;
    @FXML
    private JFXTextField notsolditem;
    private JFXComboBox<String> listfield;

    String serial_no,item_name,brand_name,war_hard,war_soft,cust_name,date_buy,phone_no,pay_method,expr,sol,serial_no1;
    float down_pay,month_pay,payable_amount;
    int rep;
    
    int branch;
    @FXML
    private Label lblnamev;
    @FXML
    private Label lblidv;
    @FXML
    private Label lbladdressv;
    @FXML
    private Label lblmobile;
    @FXML
    private Label lblbuyv;
    @FXML
    private Label lbltypev;
    @FXML
    private Label lblbrandv;
    @FXML
    private Label lblwarhardv;
    @FXML
    private Label lblwarsoftv;
    @FXML
    private Label lblserialv;
    @FXML
    private Label lblusev;
    @FXML
    private Label lblamountv;
    @FXML
    private Label lblpayamount;
    @FXML
    private ImageView imgqrcode;
    @FXML
    private JFXTextField txtpath;
    @FXML
    private PieChart piechart_itemno;
    @FXML
    private PieChart piechart_sold;
    @FXML
    private NumberAxis profit_y;
    @FXML
    private CategoryAxis time_x;
    @FXML
    private BarChart<?, ?> barchart_profit;
    
    
    Date v_buy;
    String v_name,v_id,v_address,v_mobile,v_item,v_brand,v_hard,v_soft,v_expire,v_serial;
    float v_amount,v_buyamount,v_payamount;
    int v_repair;
    String folderid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SQLConnection conclass = new SQLConnection();
        connection= SQLConnection.createConnection();
        
       
        getRowNumber();
        getrowsold();
        tableview.setEditable(true);
        
        get_branch();
        getItemNumbers();
        getSoldNumbers();
        getProfite();
        
        ObservableList<PieChart.Data> pieChart_number1 = FXCollections.observableArrayList(
                new PieChart.Data("Laptops",this.clap),
                new PieChart.Data("Phone",this.cphone),
                new PieChart.Data("Keyboard",this.ckey),
                new PieChart.Data("Mouse",this.cmouse),
                new PieChart.Data("Pen Drive",this.cpen)
        );
    ObservableList<PieChart.Data> pieChart_soldnum1 = FXCollections.observableArrayList(
                new PieChart.Data("Laptops",this.slap),
                new PieChart.Data("Phone",this.sphone),
                new PieChart.Data("Keyboard",this.skey),
                new PieChart.Data("Mouse",this.smouse),
                new PieChart.Data("Pen Drive",this.spen)
        );
        
        piechart_itemno.setData(pieChart_number1);
        piechart_itemno.setStartAngle(90);
        
        piechart_sold.setData(pieChart_soldnum1);
        piechart_sold.setStartAngle(90);
        
        XYChart.Series series1 = new XYChart.Series(); 
        series1.getData().add(new XYChart.Data("Laptops",this.plap));
        series1.getData().add(new XYChart.Data("Phone", this.pphone));
        series1.getData().add(new XYChart.Data("Keyboard", this.pkey));
        series1.getData().add(new XYChart.Data("Mouse", this.pmouse));
        series1.getData().add(new XYChart.Data("Pen Drive", this.ppen));  
        
        barchart_profit.getData().add(series1);
    }    

        
    public void setTexr(String Email1) {
        this.email.setText(Email1);
        
        
    }
    
    @FXML
    private void btnnewcus(MouseEvent event) {
            btnnewcustomer.getScene().getWindow().hide();
            
            Stage stage = new Stage();
            Parent root1;
        try {
            root1 = FXMLLoader.load(getClass().getResource("/main/java/Home/CustomerPane/Customer.fxml"));
             //stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root1);
            //stage.initStyle(StageStyle.UNDECORATED);
            Image image1 = new Image("/main/resources/images/warranty2.PNG");
            stage.getIcons().add(image1);
            stage.setTitle("Warranty System");
            stage.setScene(scene);
            stage.show();
            
            Homepane.setVisible(Boolean.FALSE);
        } catch (Exception ex) {
            System.out.println("Can't Load Window");
        }
           
    } 

    @FXML
    private void btnnewitem(MouseEvent event) {
        
                    btnnewitem.getScene().getWindow().hide();
            
        
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/main/java/Home/ItemPane/InputItem.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ex){
                        Logger.getLogger("Error");
                    }
                   
                    InputItemController display = loader.getController();
                    display.setTexr(this.email.getText());
                    Parent root1 = loader.getRoot();
                    Stage stage = new Stage();
                    Image image = new Image("/main/resources/images/warranty2.PNG");
                    stage.getIcons().add(image);
                    stage.setTitle("Warranty System");
                    Scene scene = new Scene(root1);
                    stage.setResizable(false);
                    stage.setMaximized(false);
                    stage.setScene(scene);
                    stage.show();
                    
     
    }
    private void get_branch(){
        String cquery = "SELECT * FROM companyinfo";
        try {
            ps = connection.prepareStatement(cquery);   
            rs = ps.executeQuery(cquery);
            
             while(rs.next()){
                String check_email = rs.getString("Email");
                int check_id = rs.getInt("Branch_ID");
                
                if(check_email.equals(this.email.getText())){
                    this.branch = check_id;
                    break;
                }
            }
        }catch(Exception e){
            System.out.print("Do not connect to DB - Error:"+e);
            
        }          
    }
    
    @FXML
    private void btnviewtable(MouseEvent event) {
        tableview.getItems().clear();
        allitem.setText(Integer.toString(this.numberRow));
        solditem.setText(Integer.toString(this.numberRowsold));
        int notsold = this.numberRow - this.numberRowsold;
        notsolditem.setText(Integer.toString(notsold));
        
        homepane.setVisible(false);
        verifypane.setVisible(false);
        viewtablepane.setVisible(true);
        get_branch();
        
        
        String query = "SELECT * FROM singer_items";
        try {
            ps = connection.prepareStatement(query);   
            rs = ps.executeQuery(query);
           
           
            while(rs.next()){ 
                    
                     serial_no = rs.getString("Serial_Number");
                     item_name = rs.getString("ItemType_Name");
                     brand_name = rs.getString("ItemBrand_Name");
                     war_hard = rs.getString("Warranty_Hardware");
                     war_soft = rs.getString("Warranty_Software");
                     cust_name = rs.getString("Customer_Name");
                     date_buy = rs.getString("Date_buy");
                     phone_no = rs.getString("Phone_Number");
                     pay_method = rs.getString("Pay_Method");
                     down_pay = rs.getFloat("Down_Payment");
                     month_pay = rs.getFloat("Monthly_Payment");
                     payable_amount = rs.getFloat("Payable_Amount");
                     expr = rs.getString("Expire");
                     sol = rs.getString("Sold");
                     rep = rs.getInt("Repair");
                    
                     int check = rs.getInt("Branch_ID");
                     
                     if(check == this.branch){
                         initiatecols();
                         load_data();
                     }
            }
            
            //JOptionPane.showMessageDialog(null, "Table Link Successfully");
        }catch(Exception e){
            System.out.print("Do not connect to DB - Error:"+e);
            JOptionPane.showMessageDialog(null, "Table Link Fail");
        }
        
    }

    private void load_data(){
        
        data.removeAll(data);
        data.addAll(new Person(serial_no, item_name, brand_name, war_hard,war_soft,cust_name,date_buy,phone_no,pay_method,down_pay,month_pay,payable_amount,sol,rep));
        tableview.getItems().addAll(data);
    }
    
    private void initiatecols(){
        serialno.setCellValueFactory(new PropertyValueFactory<>("serial_no"));
        itemname.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        brandname.setCellValueFactory(new PropertyValueFactory<>("brand_name"));
        warrantyhard.setCellValueFactory(new PropertyValueFactory<>("war_hard"));
        warrantysoft.setCellValueFactory(new PropertyValueFactory<>("war_soft"));
        customer.setCellValueFactory(new PropertyValueFactory<>("cust_name"));
        dbuy.setCellValueFactory(new PropertyValueFactory<>("date_buy"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone_no"));
        paymethod.setCellValueFactory(new PropertyValueFactory<>("pay_method"));
        dpayment.setCellValueFactory(new PropertyValueFactory<>("down_pay"));
        mpayment.setCellValueFactory(new PropertyValueFactory<>("month_pay"));
        payamount.setCellValueFactory(new PropertyValueFactory<>("payable_amount"));
        sold.setCellValueFactory(new PropertyValueFactory<>("sol"));
        repair.setCellValueFactory(new PropertyValueFactory<>("rep"));
        
        
    }
    @FXML
    private void btnsummerry(MouseEvent event) {
        homepane.setVisible(true);
        verifypane.setVisible(false);
        viewtablepane.setVisible(false);
    }
    
    @FXML
    private void btncheckvalidity(MouseEvent event) {
        homepane.setVisible(false);
        verifypane.setVisible(true);
        viewtablepane.setVisible(false);
    }

    @FXML
    private void btnclose(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void btnmini(MouseEvent event) {
         Stage stage1 = (Stage)Homepane.getScene().getWindow();
         stage1.setIconified(true);
         
    }

    private void selectbrand(ActionEvent event) {
        String type = null;
        try{
            type = listitem.getValue();
        
        switch (type) {
            case "Laptop":
                listbrand.setItems(brandtype_lap);
                break;
            case "Phone":
                listbrand.setItems(brandtype_phone);
                break;
            case "Keybourd":
                listbrand.setItems(brandtype_Keybourd);
                break;
            case "Mouse":
                listbrand.setItems(brandtype_Mouse);
                break;
            case "Pen_Drive":
                listbrand.setItems(brandtype_Pen);
                break;
            default:
                break;
        }
        }catch(Exception e){
            System.out.println("Error : " + e);
        }
    }
    
    
    private void txtserchfilter(KeyEvent event) {
        
        FilteredList<Person> flPerson = new FilteredList(data, p -> true);//Pass the data to a filtered list
        tableview.setItems(flPerson);//Set the table's items using the filtered list
        
        
        switch (listfield.getValue())//Switch on choiceBox value
            {
                case "Serial Number":
                    flPerson.setPredicate(p -> p.getSerial_no().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Item Name":
                    flPerson.setPredicate(p -> p.getItem_name().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Brand Name":
                    flPerson.setPredicate(p -> p.getBrand_name().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Warranty Hardware":
                    flPerson.setPredicate(p -> p.getWar_hard().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Warranty Software":
                    flPerson.setPredicate(p -> p.getWar_soft().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Customer Name":
                    flPerson.setPredicate(p -> p.getCust_name().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Date Buy":
                    flPerson.setPredicate(p -> p.getDate_buy().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Phone Number":
                    flPerson.setPredicate(p -> p.getPhone_no().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Pay Method":
                    flPerson.setPredicate(p -> p.getPay_method().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                case "Sold":
                    flPerson.setPredicate(p -> p.getSol().toLowerCase().contains(txtsearch.getText().toLowerCase().trim()));
                    break;
                  
            }
        
        listfield.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
        {//reset table and textfield when new choice is selected
            if (newVal != null)
            {
                txtsearch.setText("");
                flPerson.setPredicate(null);//This is same as saying flPerson.setPredicate(p->true);
            }
        });
        
        load_data();
    }

    @FXML
    private void btnscanqrv(MouseEvent event) throws WriterException, IOException, NotFoundException {
        String filepath = txtpath.getText();
         
        
        QRCodeReaderPath qrCodeReaderPath = new QRCodeReaderPath(filepath);
        String v_qrserial = qrCodeReaderPath.serialno;
        lblserialv.setText(v_qrserial);
        //FileInputStream fin=new FileInputStream("C:\\QRCodes\\" + v_qrserial + ".png");  
        File file2 = new File(this.pathsInfo);
        Image image = new Image(file2.toURI().toString());
        imgqrcode.setImage(image);
        
        
        String query3 = "SELECT * FROM singer_items";
        try {
            ps = connection.prepareStatement(query3);   
            rs = ps.executeQuery(query3);
           
            while(rs.next()){
                
                     this.v_name = rs.getString("Customer_Name");
                     this.v_id = rs.getString("Customer_ID");
                     this.v_address = rs.getString("Customer_Address");
                     this.v_mobile = rs.getString("Phone_Number");
                     this.v_buy = rs.getDate("Date_buy");
                     this.v_item = rs.getString("ItemType_Name");
                     this.v_brand = rs.getString("ItemBrand_Name");
                     this.v_amount = rs.getFloat("Amount");
                     this.v_payamount = rs.getFloat("Payable_Amount");
                     this.v_hard = rs.getString("Warranty_Hardware");
                     this.v_soft = rs.getString("Warranty_Software");
                     //v_expire = rs.getString("Expire");
                     this.v_repair = rs.getInt("Repair"); 
                     this.v_serial = rs.getString("Serial_Number");
                     
                     
                     
                     
                     if(v_qrserial.equals(v_serial)){
                         
                         //LocalDate now = LocalDate.now();
                    
                        //Duration duration = Duration.between(now,v_buy);
                       // long diff = Math.abs(duration.toDays());
                    
                    
                        lblnamev.setText(this.v_name);
                        lblidv.setText(this.v_id);
                        lbladdressv.setText(this.v_address);
                        lblmobile.setText(this.v_mobile);
                        lblbuyv.setText(this.v_buy.toString());
                        lbltypev.setText(this.v_item);
                        lblbrandv.setText(this.v_brand);
                        lblamountv.setText(Float.toString(this.v_amount));
                        lblpayamount.setText(Float.toString(this.v_payamount));
                        lblwarhardv.setText(this.v_hard);
                        lblwarsoftv.setText(this.v_soft);
                        //lbltimev.setText(Long.toString(diff));
                        lblusev.setText(Integer.toString(this.v_repair));
                        
                        break;
                     }

            }
           
            JOptionPane.showMessageDialog(null, "QR Scan Successfully");
        }catch(Exception e){
            System.out.print("Do not connect to DB - Error:"+e);
            JOptionPane.showMessageDialog(null, "Table Link Fail" + e);
        }
        
        
    }

    @FXML
    private void btnusev(MouseEvent event) {
        
        int usetimes = Integer.parseInt(lblusev.getText()) + 1;
        String checkserialv = lblserialv.getText();
        
         String setrepaireq = "UPDATE singer_items SET Repair = ? WHERE Serial_Number = ?";
        try {
            ps = connection.prepareStatement(setrepaireq);
            

                     ps.setInt(1,usetimes);
                     ps.setString(2,checkserialv);
                     ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Update Item Used Times");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Table Link Fail \n Error : "+e);
        }
    }

    @FXML
    private void btncancelv(MouseEvent event) {
        homepane.setVisible(true);
        verifypane.setVisible(false);
    }

    @FXML
    private void btnpath(MouseEvent event) {
        
        FileChooser fileChooser = new FileChooser();
 
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new
                        FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
                fileChooser.getExtensionFilters().add(extFilter);
 
        //Show open file dialog
                file = fileChooser.showOpenDialog(null);
 
                this.pathsInfo = null;
                this.pathsInfo = file.getPath();
                //pathsInfo += "getAbsolutePath(): " + file.getAbsolutePath() + "\n";
 
                //pathsInfo += (new File(file.getPath())).isAbsolute();
                /*
                try {
                    pathsInfo += "getCanonicalPath(): " + 
                            file.getCanonicalPath() + "\n";
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Choose File is Fail \n Error : "+e);
                }
                */
                txtpath.setText(this.pathsInfo);
            
    }

    @FXML
    private void btndownloadqr(MouseEvent event) throws IOException {
        new File("C:\\WarrantySystem").mkdirs();
        new File("C:\\WarrantySystem\\Invoice_PDF").mkdirs();
        new File("C:\\WarrantySystem\\Invoice_Image").mkdirs();
        new File("C:\\WarrantySystem\\QRCodes").mkdirs();
        
        String querydown = "SELECT * FROM singer_items";
        try {
            ps = connection.prepareStatement(querydown);   
            rs = ps.executeQuery(querydown);

            byte[] b;
            byte[] b1;
            byte[] b2;
            Blob blob,blob1,blob2;
            
            
             while(rs.next()){
                 
                this.serial_no1 = rs.getString("Serial_Number");
                blob=rs.getBlob("QRCode");
                
                    if(blob != null){
                        File file1=new File("C:\\WarrantySystem\\QRCodes\\" + serial_no1 + ".png");
                        FileOutputStream fos=new FileOutputStream(file1);
                        b=blob.getBytes(1,(int)blob.length());
                        fos.write(b);
                    }
                    
                blob1=rs.getBlob("Invoice_Image");
                
                    if(blob1 != null){
                        File file2=new File("C:\\WarrantySystem\\Invoice_Image\\" + serial_no1 + ".png");
                        FileOutputStream fos1=new FileOutputStream(file2);
                        b1=blob1.getBytes(1,(int)blob1.length());
                        fos1.write(b1);
                    }   
                blob2=rs.getBlob("Invoice_Pdf");
                
                    if(blob2 != null){
                        File file2=new File("C:\\WarrantySystem\\Invoice_PDF\\" + serial_no1 + ".pdf");
                        FileOutputStream fos2=new FileOutputStream(file2);
                        b2=blob2.getBytes(1,(int)blob2.length());
                        fos2.write(b2);
                    } 
                 
                
                
            }
             JOptionPane.showMessageDialog(null, "All QRCode Image get Successfully \n Please check your 'C drive' and open WarrantySystem/QRCodes folders");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Do not connect to DB - Error:"+e);
            System.out.print("Do not connect to DB - Error:"+e);
            
        }

////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a Google Root Folder

        com.google.api.services.drive.model.File folder1 = createGoogleFolder(null, "WarrantySystem");

        this.folderid = folder1.getId();
        System.out.println("Created folder with id= "+ this.folderid);
        System.out.println("                    name= "+ folder1.getName());

        System.out.println("Done!");
////////////////////////////////////////////////////////////////////////////////////////////////////////
    }


//    public String getFolderid(){
//        return this.folderid;
//    }
    public static class Person
    
    {
 
        private final SimpleStringProperty serial_no;
        private final SimpleStringProperty item_name;
        private final SimpleStringProperty brand_name;
        private final SimpleStringProperty war_hard;
        private final SimpleStringProperty war_soft;
        private final SimpleStringProperty cust_name;
        private final SimpleStringProperty date_buy;
        private final SimpleStringProperty phone_no;
        private final SimpleStringProperty pay_method;
        private final SimpleFloatProperty down_pay;
        private final SimpleFloatProperty month_pay;
        private final SimpleFloatProperty payable_amount;
        private final SimpleStringProperty sol;
        private final SimpleIntegerProperty rep;
        
       

    Person(String serial_no, String item_name, String brand_name, String war_hard, String war_soft, String cust_name, String date_buy, String phone_no, String pay_method, float down_pay,float month_pay, float payable_amount, String sol, int rep) {
            this.serial_no = new SimpleStringProperty(serial_no);
            this.item_name = new SimpleStringProperty(item_name);
            this.brand_name = new SimpleStringProperty(brand_name);
            this.war_hard = new SimpleStringProperty(war_hard);
            this.war_soft = new SimpleStringProperty(war_soft);
            this.cust_name = new SimpleStringProperty(cust_name);
            this.date_buy = new SimpleStringProperty(date_buy);
            this.phone_no = new SimpleStringProperty(phone_no);
            this.pay_method = new SimpleStringProperty(pay_method);
            this.down_pay = new SimpleFloatProperty(down_pay);
            this.month_pay = new SimpleFloatProperty(month_pay);
            this.payable_amount = new SimpleFloatProperty(payable_amount);
            this.sol = new SimpleStringProperty(sol);
            this.rep = new SimpleIntegerProperty(rep);
    }
        

        public String getSerial_no() {
            return serial_no.get();
        }

        public String getItem_name() {
            return item_name.get();
        }
        
        public String getBrand_name() {
            return brand_name.get();
        }

        public String getWar_hard() {
            return war_hard.get();
        }

        public String getWar_soft() {
            return war_soft.get();
        }

        public String getCust_name() {
            return cust_name.get();
        }

        public String getDate_buy() {
            return date_buy.get();
        }

        public String getPhone_no() {
            return phone_no.get();
        }

        public String getPay_method() {
            return pay_method.get();
        }

        public float getDown_pay() {
            return down_pay.get();
        }

        public float getMonth_pay() {
            return month_pay.floatValue();
        }

        public float getPayable_amount() {
            return payable_amount.floatValue();
        }

        
        public String getSol() {
            return sol.get();
        }

        public int getRep() {
            return rep.get();
        }
 
       
    
    
    
    }
    //String lap_count = "SELECT * FROM singer_items WHERE ItemType_Name = 'Laptop' AND Branch_ID = " + this.branch;
    public void getItemNumbers(){
         
        String lap_count = "SELECT * FROM singer_items WHERE ItemType_Name = 'Laptop'";
        String phone_count = "SELECT * FROM singer_items WHERE ItemType_Name = 'Phone' ";
        String keyboard_count = "SELECT * FROM singer_items WHERE ItemType_Name = 'Keybourd'";
        String mouse_count = "SELECT * FROM singer_items WHERE ItemType_Name = 'Mouse'";
        String pen_count = "SELECT * FROM singer_items WHERE ItemType_Name = 'Pen_Drive'";
        
        
        try {
            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
              
            rs = stmt.executeQuery(lap_count);
            rs.last();
            this.clap = rs.getRow();
            rs.beforeFirst();
            
            rs = stmt.executeQuery(phone_count);
            rs.last();
            this.cphone = rs.getRow();
            rs.beforeFirst();
            
            rs = stmt.executeQuery(keyboard_count);
            rs.last();
            this.ckey = rs.getRow();
            rs.beforeFirst();
            
            rs = stmt.executeQuery(mouse_count);
            rs.last();
            this.cmouse = rs.getRow();
            rs.beforeFirst();
            
            rs = stmt.executeQuery(pen_count);
            rs.last();
            this.cpen = rs.getRow();
            rs.beforeFirst();
            
            System.out.println("number :" + clap);

        }catch(Exception e){
           
        }

        
    }
    
    
    
    public void getSoldNumbers(){
         
        String lap_sold = "SELECT * FROM singer_items WHERE ItemType_Name = 'Laptop' AND Sold = 'yes'";
        String phone_sold = "SELECT * FROM singer_items WHERE ItemType_Name = 'Phone' AND Sold = 'yes' ";
        String keyboard_sold = "SELECT * FROM singer_items WHERE ItemType_Name = 'Keybourd' AND Sold = 'yes'";
        String mouse_sold = "SELECT * FROM singer_items WHERE ItemType_Name = 'Mouse' AND Sold = 'yes'";
        String pen_sold = "SELECT * FROM singer_items WHERE ItemType_Name = 'Pen_Drive' AND Sold = 'yes'";
        
        
        try {
            Statement stmt1 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
              
            rs = stmt1.executeQuery(lap_sold);
            rs.last();
            this.slap = rs.getRow();
            rs.beforeFirst();
            
            rs = stmt1.executeQuery(phone_sold);
            rs.last();
            this.sphone = rs.getRow();
            rs.beforeFirst();
            
            rs = stmt1.executeQuery(keyboard_sold);
            rs.last();
            this.skey = rs.getRow();
            rs.beforeFirst();
            
            rs = stmt1.executeQuery(mouse_sold);
            rs.last();
            this.smouse = rs.getRow();
            rs.beforeFirst();
            
            rs = stmt1.executeQuery(pen_sold);
            rs.last();
            this.spen = rs.getRow();
            rs.beforeFirst();

            System.out.println("Sold :" + slap + this.branch);
        }catch(Exception e){
           
        }
    }
    
    public void getProfite(){
         
        String lap_q = "SELECT sum(Amount)-sum(Buy_Amount) FROM singer_items WHERE ItemType_Name = 'Laptop'";
        String phone_q = "SELECT sum(Amount)-sum(Buy_Amount) FROM singer_items WHERE ItemType_Name = 'Phone'";
        String keyboard_q = "SELECT sum(Amount)-sum(Buy_Amount) FROM singer_items WHERE ItemType_Name = 'Keybourd'";
        String mouse_q = "SELECT sum(Amount)-sum(Buy_Amount) FROM singer_items WHERE ItemType_Name = 'Mouse'";
        String pen_q = "SELECT sum(Amount)-sum(Buy_Amount) FROM singer_items WHERE ItemType_Name = 'Pen_Drive'";

       
      
        try {
            ps = connection.prepareStatement(lap_q);   
            rs = ps.executeQuery();
                if (rs.next())
                 {  
                    float getlap = rs.getFloat(1);
                    this.plap = (int) (this.plap + getlap);


                 }
                 System.out.println("profit : " + this.plap);
            }
        catch(Exception e){
            System.out.print("Do not connect to DB - Error:"+e);
            //JOptionPane.showMessageDialog(null, "Table Link Fail");
        }
       
        try {
            ps = connection.prepareStatement(phone_q);   
            rs = ps.executeQuery();
                    if (rs.next())
                         {  
                            float getphone = rs.getFloat(1);
                            this.pphone = (int) (this.pphone + getphone);


                         }
                         System.out.println("profit : " + this.pphone);
            } 
        catch(Exception e){
            System.out.print("Do not connect to DB - Error:"+e);
            //JOptionPane.showMessageDialog(null, "Table Link Fail");
        }
        
        try {
            ps = connection.prepareStatement(keyboard_q);   
            rs = ps.executeQuery();
            if (rs.next())
                         {  
                            float getkey = rs.getFloat(1);
                            this.pkey = (int) (this.pkey + getkey);


                         }
                         System.out.println("profit : " + this.pkey);
            }
        catch(Exception e){
            System.out.print("Do not connect to DB - Error:"+e);
            //JOptionPane.showMessageDialog(null, "Table Link Fail");
        }
        
        try {
            ps = connection.prepareStatement(mouse_q);   
            rs = ps.executeQuery();
            if (rs.next())
            {  
               float getmouse = rs.getFloat(1);
               this.pmouse = (int) (this.pmouse + getmouse);
              
               
            }
            System.out.println("profit : " + this.pmouse);
        }
        catch(Exception e){
            System.out.print("Do not connect to DB - Error:"+e);
            //JOptionPane.showMessageDialog(null, "Table Link Fail");
         }
        
        
        try {
            ps = connection.prepareStatement(pen_q);   
            rs = ps.executeQuery();
            if (rs.next())
            {  
               float getpen = rs.getFloat(1);
               this.ppen = (int) (this.ppen + getpen);
              
               
            }
            System.out.println("profit : " + this.ppen);
           // System.out.println("sum : " + this.ppen);
                            
            }
        catch(Exception e){
            System.out.print("Do not connect to DB - Error:"+e);
        }
        
        
    }
    
       public void getRowNumber(){
           int numrow = 0;
             try{
                
                 String qgetrow = "select count(*) from singer_items";
                 ps = connection.prepareStatement(qgetrow);   
                 rs = ps.executeQuery();
                 while(rs.next()){
                     numrow = rs.getInt("count(*)");
                 }
             }catch (Exception ex){
                 System.out.println(ex.getMessage());
             }
            this.numberRow = numrow;
             
       }
       
       public void getrowsold(){
           int numrows = 0;
             try{
                
                 String qgetrows = "select count(*) from singer_items where Sold = 'yes'";
                 ps = connection.prepareStatement(qgetrows);   
                 rs = ps.executeQuery();
                 while(rs.next()){
                     numrows = rs.getInt("count(*)");
                 }
             }catch (Exception ex){
                 System.out.println(ex.getMessage());
             }
            this.numberRowsold = numrows;
             
       }
       
       
       
    
}
