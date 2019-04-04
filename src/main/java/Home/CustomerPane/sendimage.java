/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.Home.CustomerPane;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author M.S.Prasad
 */
public class sendimage {
    
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage msg;
    String image;
    public void send(String email, String image) throws MessagingException {
        this.image = image;
        System.out.println("\n1st ===> setup Mail Server Properties..");
 
        final String sourceEmail = "sarcorganization1@gmail.com"; // requires valid Gmail id
        final String password = "sarc1234"; // correct password for Gmail id
        final String toEmail = email; // any destination email id
 
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.EnableSSL.enable","true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        System.out
                .println("\n2nd ===> create Authenticator object to pass in Session.getInstance argument..");
 
        Authenticator authentication = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sourceEmail, password);
            }
        };
        Session session = Session.getInstance(props, authentication);
        generateAndSendEmail(
                session,
                toEmail,
                "Warranty System in Sarc Oraganization",
                "Greetings, <br><br>This is your Invoice and QR Code. Please find here attached Image."
                        + "<br><br> Regards, <br>Sarc Organization");
 
    }
 
    public void generateAndSendEmail(Session session, String toEmail, String subject,
                                     String body) {
        try {
            System.out.println("\n3rd ===> generateAndSendEmail() starts..");
 
            MimeMessage crunchifyMessage = new MimeMessage(session);
            crunchifyMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
            crunchifyMessage.addHeader("format", "flowed");
            crunchifyMessage.addHeader("Content-Transfer-Encoding", "8bit");
 
            crunchifyMessage.setFrom(new InternetAddress("noreply@crunchify.com",
                    "Sarc Oraganization"));
            crunchifyMessage.setReplyTo(InternetAddress.parse("noreply@crunchify.com", false));
            crunchifyMessage.setSubject(subject, "UTF-8");
            crunchifyMessage.setSentDate(new Date());
            crunchifyMessage.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail, false));
 
            // Create the message body part
            //BodyPart messageBodyPart = new MimeBodyPart();
            //messageBodyPart.setContent(body, "text/html");
 
            // Create a multipart message for attachment
            //Multipart multipart = new MimeMultipart();
 
            // Set text message part
           // multipart.addBodyPart(messageBodyPart);
 
            //messageBodyPart = new MimeBodyPart();
 
            // Valid file location
            /*
            String filename = "C:\\WarrantySystem\\Invoice_Image\\" + this.image+".png";
            DataSource source1 = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source1));
            messageBodyPart.setFileName(filename);
            
            String filename2 = "C:\\WarrantySystem\\QRCodes\\" + this.image + ".png";
            DataSource source2 = new FileDataSource(filename2);
            messageBodyPart.setDataHandler(new DataHandler(source2));
            messageBodyPart.setFileName(filename2);
            */
            
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            
            messageBodyPart.setContent(body, "text/html");
            
            //messageBodyPart.setText("fdgfd"); 
            multipart.addBodyPart(messageBodyPart); 

            //attachment... 
            messageBodyPart = new MimeBodyPart();

            String fileAttachment = "C:\\WarrantySystem\\Invoice_Image\\" + this.image+ ".png"; 
            DataSource source = new FileDataSource(fileAttachment); 
            messageBodyPart.setDataHandler( new DataHandler(source)); 
            messageBodyPart.setFileName("C:\\WarrantySystem\\Invoice_Image\\" + this.image+ ".png"); 
            multipart.addBodyPart(messageBodyPart); 

            //second attachment 
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            DataSource source2 = new FileDataSource("C:\\WarrantySystem\\QRCodes\\" + this.image + ".png"); 
            messageBodyPart2.setDataHandler( new DataHandler(source2)); 
            messageBodyPart2.setFileName("C:\\WarrantySystem\\QRCodes\\" + this.image + ".png"); 
            multipart.addBodyPart(messageBodyPart2); 

            
            // Trick is to add the content-id header here
            messageBodyPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(messageBodyPart);
 
            System.out.println("\n4th ===> third part for displaying image in the email body..");
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<br><h3>Find below attached image</h3>"
                    + "<img src='cid:image_id'>", "text/html");
            multipart.addBodyPart(messageBodyPart);
            crunchifyMessage.setContent(multipart);
 
            System.out.println("\n5th ===> Finally Send message..");
 
            // Finally Send message
            Transport.send(crunchifyMessage);
 
            System.out.println("\n6th ===> Email Sent Successfully With Image Attachment. Check your email now..");
            System.out.println("\n7th ===> generateAndSendEmail() ends..");
 
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
}
