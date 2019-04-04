package main.java.Home.CustomerPane;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.jfoenix.controls.*;
import com.twilio.sdk.TwilioRestException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.Home.HomePane.HomeUIController;
import main.java.Home.QRCodeReaderPath;
import main.java.SQLDatabase.SQLConnection;
import net.sf.jasperreports.engine.JRException;

import javax.mail.MessagingException;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static main.java.Home.CustomerPane.GoogleDrive.CreateGoogleFile.createGoogleFile;
import static main.java.Home.CustomerPane.GoogleDrive.GetSubFoldersByName.getGoogleRootFoldersByName;

public class CustomerController implements Initializable {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    private Pane emailbox;
    private Pane addressbox;
    private Pane phonebox;
    private Pane idbox;
    private Pane namebox;
    
    
    @FXML
    private AnchorPane inputPane;
    @FXML
    private Pane sendpane;
    private AnchorPane inputboxframe;
    private AnchorPane sendboxframe;
    @FXML
    private Pane panename;
    @FXML
    private JFXTextField cusname;
    @FXML
    private Pane paneid;
    @FXML
    private JFXTextField cusid;
    @FXML
    private Pane paneaddress;
    @FXML
    private JFXTextField cusaddress;
    @FXML
    private Pane panephone;
    @FXML
    private JFXTextField cusphone;
    @FXML
    private Pane panebuy;
    @FXML
    private JFXDatePicker cusbuy;
    @FXML
    private Pane panepaymethod;
    @FXML
    private JFXComboBox<String> cuspaymethod;
    @FXML
    private Pane panemonthly;
    @FXML
    private JFXTextField cusmonthly;
    @FXML
    private Pane panepayamount;
    @FXML
    private JFXTextField payamount;
    @FXML
    private Pane panecheckitem;
    @FXML
    private Label lblitemytpe;
    @FXML
    private Label lblbrand;
    @FXML
    private Label lblserial;
    @FXML
    private Label lblamount;
    @FXML
    private Label lblwarrantyhard;
    @FXML
    private Label lblwarrantysoft;
    @FXML
    private JFXTextField cusdownpay;
    @FXML
    private Pane panedownpay;
    @FXML
    private JFXButton btnsend;
    @FXML
    private JFXButton nextstep;
    @FXML
    private ImageView imgqr;
    @FXML
    private JFXTextField txtpath;
    @FXML
    private JFXTextField txturl;
    
    File file;
    String serial,email,branchid,varcusemail,varcusphone,varcusimageurl, varcusname;
    ObservableList<String> selectmethod = FXCollections.observableArrayList("Cash","Check");
    String folderid, sharablelink;
    
    
    
    private static final long serialVersionUID = 1L;
    String itemtype,itembrand,itemamount,warhard,warsoft,itemserial,iaddress,web,branch,iphone;
    String invoice = this.serial;
    String date;
    @FXML
    private Pane paneemail;
    @FXML
    private JFXTextField cusemail;
    @FXML
    private JFXButton btncreate;
    @FXML
    private JFXCheckBox checkemail;
    @FXML
    private JFXCheckBox checksms;
    @FXML
    private ImageView invoiceimageview;
    @FXML
    private JFXButton btncreate1;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SQLConnection conclass = new SQLConnection();
        connection= SQLConnection.createConnection();
        
        cuspaymethod.setItems(selectmethod);
        serial = lblserial.getText();
    }    

    
    
    @FXML
    private void btnsentdata(MouseEvent event) throws JRException, ClassNotFoundException, SQLException, MessagingException, IOException, InterruptedException , TwilioRestException {
       
        getEmail();

        this.varcusemail = cusemail.getText();
        this.varcusphone = cusphone.getText();
        //this.varcusimageurl = txturl.getText();
        this.varcusname = cusname.getText();
        System.out.println("serial : " + this.serial);
        System.out.println(this.varcusemail + this.serial);
        System.out.println(this.sharablelink+this.varcusphone+this.varcusname);
        TimeUnit.SECONDS.sleep(5);

        if(this.checkemail.isSelected() && this.checksms.isSelected()){
            new sendimage().send(this.varcusemail, this.serial);
            new Send_sms().send(this.sharablelink,this.varcusphone, this.varcusname);
        } else if(this.checkemail.isSelected() && !this.checksms.isSelected()) {
            new sendimage().send(this.varcusemail, this.serial);
        }else if(!this.checkemail.isSelected() && this.checksms.isSelected()) {
            new Send_sms().send(this.sharablelink,this.varcusphone, this.varcusname);
        }else{
            //JOptionPane.showMessageDialog(null, "No Send Any SMS or Email");
        }

        //JOptionPane.showMessageDialog(null, "Success Transaction");


        
        //setInvoice();
        setInvoice();

        btnsend.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/main/java/Home/HomePane/homeUI.fxml"));
                    try{
                        loader.load();
                    }catch(IOException ex){
                        Logger.getLogger("Error");
                    }
                    
                    HomeUIController display = loader.getController();
                    display.setTexr(this.email);
                    Parent root1 = loader.getRoot();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root1);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(scene);
                    stage.show();
                    
           inputPane.setVisible(Boolean.FALSE); 
           
        
    }
    
    
    
    private void sendBranch(int branchid, String branchname) {
        
    }
    


    private void btnclose(MouseEvent event) {
        
        System.exit(0);
    }

    private void btnmini(MouseEvent event) {
         Stage stage1 = (Stage)inputPane.getScene().getWindow();
         stage1.setIconified(true);
         
    }
    
    
    @FXML
    private void btnscanqr(MouseEvent event) throws WriterException, IOException, NotFoundException {
        
        String filepath = txtpath.getText();
       
        
        QRCodeReaderPath qrCodeReaderPath = new QRCodeReaderPath(filepath);
        this.serial = qrCodeReaderPath.getSerial();
        lblserial.setText(this.serial);
        
        File file = new File("C:\\WarrantySystem\\QRCodes\\" + this.serial + ".png");
        Image image = new Image(file.toURI().toString());
        imgqr.setImage(image);
        
        
        String query1 = "select * from singer_items";
      
        try{
            ps = connection.prepareStatement(query1);   
            rs = ps.executeQuery(query1);
            while(rs.next()){
                this.branchid = rs.getString("Branch_ID");
                itemtype = rs.getString("ItemType_Name");
                itembrand = rs.getString("ItemBrand_Name");
                itemamount = rs.getString("Amount");
                warhard = rs.getString("Warranty_Hardware");
                warsoft = rs.getString("Warranty_Software");
                itemserial = rs.getString("Serial_Number");
                this.varcusemail = rs.getString("Cus_Email");
                if(itemserial.equals(lblserial.getText())){
                    lblitemytpe.setText(itemtype);
                    lblbrand.setText(itembrand);
                    lblamount.setText(itemamount);
                    lblwarrantyhard.setText(warhard);
                    lblwarrantysoft.setText(warsoft); 
                    break;
                }
                //ps.close();
            }
            //JOptionPane.showMessageDialog(null, "Branch Found Successfully");
        }
        
        catch (Exception ex) {
            System.out.print("Do not connect to DB - Error:" + ex);
            JOptionPane.showMessageDialog(null, "Branch Found Fail" + ex);
        }
        
        
        
    }
    
     @FXML
    private void btnnextstep(MouseEvent event) {
        panecheckitem.setVisible(false);
        panename.setVisible(true);
    }
    
    @FXML
    private void btnbackcname(MouseEvent event) {
        panecheckitem.setVisible(true);
        panename.setVisible(false);
    }
    
    
    @FXML
    private void btnname(MouseEvent event) {
        panename.setVisible(false);
        paneid.setVisible(true);
    }
    
     @FXML
    private void btnbackcid(MouseEvent event) {
        panename.setVisible(true);
        paneid.setVisible(false);
    }
    
    @FXML
    private void btnid(MouseEvent event) {
        paneid.setVisible(false);
        paneaddress.setVisible(true);
    }
    
    @FXML
    private void btnbackaddress(MouseEvent event) {
        paneid.setVisible(true);
        paneaddress.setVisible(false);
        
    }
    
    @FXML
    private void btnaddress(MouseEvent event) {
        paneaddress.setVisible(false);
        panephone.setVisible(true);
    }
    
    @FXML
    private void btnbackphone(MouseEvent event) {
        
        paneaddress.setVisible(true);
        panephone.setVisible(false);
    }

    @FXML
    private void btnphone(MouseEvent event) {
        panephone.setVisible(false);
        paneemail.setVisible(true);
    }
    
    @FXML
    private void btnbackemail(MouseEvent event) {
        panephone.setVisible(true);
        paneemail.setVisible(false);
    }
    
    @FXML
    private void btnemail(MouseEvent event) {

        paneemail.setVisible(false);
        panebuy.setVisible(true);

    }
    
    @FXML
    private void btnbackbuy(MouseEvent event) {
        paneemail.setVisible(true);
        panebuy.setVisible(false);
    }
    
    
    @FXML
    private void btnbuy(MouseEvent event) {
        panebuy.setVisible(false);
        panepaymethod.setVisible(true);
    }

    @FXML
    private void btnbackpaymethod(MouseEvent event) {
        panebuy.setVisible(true);
        panepaymethod.setVisible(false);
    }

    
    @FXML
    private void btnpaymethod(MouseEvent event) {
        panepaymethod.setVisible(false);
        panedownpay.setVisible(true);
    }

    @FXML
    private void btnbackdownpay(MouseEvent event) {
         panepaymethod.setVisible(true);
         panedownpay.setVisible(false);
    }
    
    @FXML
    private void btndownpay(MouseEvent event) {
        panedownpay.setVisible(false);
        panemonthly.setVisible(true);
    }
    
    @FXML
    private void btnbackmonthly(MouseEvent event) {
        panedownpay.setVisible(true);
        panemonthly.setVisible(false);
    }
    
    @FXML
    private void btnmonthly(MouseEvent event) {
        panemonthly.setVisible(false);
        panepayamount.setVisible(true);
    }

    @FXML
    private void btnbackpayamount(MouseEvent event) {
        panemonthly.setVisible(true);
        panepayamount.setVisible(false);
    }
    


    
    
    @FXML
    private void btnpayamount(MouseEvent event) throws JRException, ClassNotFoundException, SQLException, FileNotFoundException {
        panepayamount.setVisible(false);
        sendpane.setVisible(true);
        try {                                                                           
                            String queryitem = "UPDATE singer_items SET Customer_Name = ?, Customer_ID = ?, Customer_Address = ?, Phone_Number = ?, Date_Buy = ?, Pay_Method = ?, Down_Payment = ?, Monthly_Payment = ?, Payable_Amount = ?, Sold = ?,Cus_Email = ?,Invoice_No = ? WHERE Serial_Number = ?";
                            
                            ps = connection.prepareStatement(queryitem);
                            
                            ps.setString(1, cusname.getText());
                            ps.setString(2, cusid.getText());
                            ps.setString(3, cusaddress.getText());
                            ps.setString(4, cusphone.getText());      
                            ps.setDate(5, Date.valueOf(cusbuy.getValue()));
                            ps.setString(6, cuspaymethod.getValue());
                            ps.setFloat(7, Float.parseFloat(cusdownpay.getText()));
                            ps.setFloat(8, Float.parseFloat(cusmonthly.getText()));
                            ps.setFloat(9, Float.parseFloat(payamount.getText()));
                            ps.setString(10, "yes");
                            ps.setString(11, cusemail.getText());
                            ps.setString(12, this.serial);
                            ps.setString(13, this.serial);
                            ps.executeUpdate();
                            //ps.close();
                            
                            JOptionPane.showMessageDialog(null, "Entered Successfully");
                            //JOptionPane.showMessageDialog(null, "Please Open WarrantyPrint Application\n Then Please Enter this Serial Number : " + this.serial);
                            JTextArea ta = new JTextArea(15, 10);
                            ta.setText(this.serial);
                            //ta.setWrapStyleWord(true);
                            //ta.setLineWrap(true);
                            ta.setCaretPosition(0);
                            ta.setEditable(false);

                            JOptionPane.showMessageDialog(null, new JScrollPane(ta), "Please Open WarrantyPrint Application\n Then Please Enter this Serial Number : ", JOptionPane.INFORMATION_MESSAGE);
                            
                            /*
                            JasperPrint print = new PrintReport().showReport(this.serial);  
                            String pdfpath = "src\\pdf\\"+ this.serial + ".pdf";
                            String imgpath = "src\\pdf\\"+ this.serial + ".png";
                            JasperExportManager.exportReportToPdfFile( print, pdfpath );
                            
                            JRExporter exporter = new JRPdfExporter();
                            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);



                            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream(pdfpath)); // your output goes here
                            exporter.exportReport();

                            new PrintReport().extractPrintImage(imgpath,print);*/
        
        //String outputFilename = "FancyPants-" + dateString + ".pdf";
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Do not connect to DB - Error:"+ex);
                            System.out.print("Do not connect to DB - Error:"+ex);
                        }
        

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
 
                String pathsInfo = null;
                try {
                    pathsInfo = file.getPath();
                }catch (Exception e){
                    System.out.println(e);
                }

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
                txtpath.setText(pathsInfo);
    }

    

    public void getEmail(){
        String query5 = "select * from companyinfo";
        String branchide;
        try{
            ps = connection.prepareStatement(query5);   
            rs = ps.executeQuery(query5);
            while(rs.next()){
                
                branchide = rs.getString("Branch_ID");              
                this.email = rs.getString("Email");
               
                
                if(branchide.equals(this.branchid)){
                    break;
                }
                //ps.close();
            }
            //JOptionPane.showMessageDialog(null, "get Email Successfully");
        }
        
        catch (Exception ex) {
            System.out.print("Do not connect to DB - Error:" + ex);
            JOptionPane.showMessageDialog(null, "get Email Fail" + ex);
        }
    }

    @FXML
    private void btnrefresh(MouseEvent event) throws IOException {
        
         File file1 = new File("C:\\WarrantySystem\\Invoice_Image\\" + this.serial + ".png");
         Image invoiceimg = new Image(file1.toURI().toString());
         invoiceimageview.setImage(invoiceimg);


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a Google Root Folder
        List<com.google.api.services.drive.model.File> rootGoogleFolders = getGoogleRootFoldersByName("WarrantySystem");
        for (com.google.api.services.drive.model.File folder : rootGoogleFolders) {

            System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
            this.folderid = folder.getId();
        }

        System.out.println(this.folderid);
        System.out.println("Done!");


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        java.io.File uploadFile = new java.io.File("C://WarrantySystem/QRCodes/" + this.serial + ".png");
        // Create Google File:

        com.google.api.services.drive.model.File googleFile = createGoogleFile(this.folderid, "image/png", this.serial + ".png", uploadFile);

        System.out.println("Upload QR Code file!");

        this.sharablelink = googleFile.getWebViewLink();
        System.out.println("WebContentLink: " + googleFile.getWebContentLink());
        System.out.println("WebViewLink: " + this.sharablelink);

        System.out.println("Done!");
                        
    }
    


    private void setInvoice() throws IOException
    {           
        File pdfFile = new File("C:\\WarrantySystem\\Invoice_PDF\\" + this.serial + ".pdf");
        byte[] pdfData = new byte[(int) pdfFile.length()];
        try (DataInputStream dis2 = new DataInputStream(new FileInputStream(pdfFile))) {
            dis2.readFully(pdfData);  // read from file into byte[] array
        } // read from file into byte[] array

         File file4 = new File("C:\\WarrantySystem\\Invoice_Image\\" + this.serial + ".png");
         FileInputStream fis4 = new FileInputStream(file4);
        String setinvoiceq = "UPDATE singer_items SET Invoice_Image = ? WHERE Serial_Number = ?";
      

        try {
            ps = connection.prepareStatement(setinvoiceq);
            
           // FileInputStream fin1=new FileInputStream("C:\\WarrantySystem\\Invoice_Image\\" + this.serial + ".png");
           //ps.setBinaryStream(1,fin1,fin1.available());
            
            ps.setBytes(2, pdfData);  // byte[] array
            
            ps.setBinaryStream(1, fis4, (int) file4.length());
            
            ps.setString(2, this.serial);

        
            ps.executeUpdate();
            fis4.close();
        //ps.close();
        
    }catch(Exception e)  {
        JOptionPane.showMessageDialog(null, "Invoice Store Unsccessfully");
    }  
    }
    

    
    
 
    
}

