package main.java.Home.CustomerPane;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import java.util.Date;
import java.util.Properties;


public class send_email {
    
    public void emailmethod(String email,String imagename){
        
        try{
            
            String host ="smtp.gmail.com" ;
            String user = "sarcorganization1@gmail.com";
            String pass = "sarc1234";
            String to = email;
            String from = "sarcorganization1@gmail.com";
            //String messageText = "";
            String subject = "This is your QR Code & Invoice \nSecure it your Memory, that will help to verify your identity :";
            
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.EnableSSL.enable","true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject); msg.setSentDate(new Date());
            //msg.setText(messageText);

            
            MimeMultipart multipart = new MimeMultipart("related");
            
             // first part (the image)
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("C:\\WarrantySystem\\Invoice_Image\\"+imagename+".png");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);

            // second part (the image)
            messageBodyPart = new MimeBodyPart();
            DataSource fds1 = new FileDataSource("C:\\WarrantySystem\\QRCodes\\" + imagename + ".png");
            messageBodyPart.setDataHandler(new DataHandler(fds1));
            messageBodyPart.setHeader("Content-ID", "<image>");

            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);
            
            
            // put everything together
            msg.setContent(multipart);
            
            
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
