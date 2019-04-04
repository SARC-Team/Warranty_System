package main.java.Home.CustomerPane;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Send_sms {
    // Find your Account Sid and Token at twilio.com/console
    // DANGER! This is insecure. See http://twil.io/secure
    private String IMAGE_URL = null;
    private String USER_NUMBER = null;
    private String USER_NAME = null;
    private static final String ACCOUNT_SID = "ACd00ff207a62cd55ae4bc44b11312861a";
    private static final String AUTH_TOKEN = "be64b5a9c3b22424aa69bba2f6e4740a";
    private static final String TWILIO_NUMBER = "+12512542392";

    public void send(String image_url, String number, String cusname) throws TwilioRestException {
        this.IMAGE_URL = image_url;
        number = number.substring(1);

        this.USER_NUMBER = "+94" +  number;
        this.USER_NAME = cusname;

        System.out.println(this.USER_NUMBER + this.USER_NAME + this.IMAGE_URL);

        try {
            TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("Body", "Hello " + this.USER_NAME + "!!! Thank you for visiting us and making your first purchase!, If you have any Problem with our Product(Apply condition) Please, Come and Show this QR Code. Please click url here that see attached QR Code.    " + this.IMAGE_URL + "    We look forward to seeing you again. Have a great day!    Best Regards,Singer(Sri Lanka)PLC,SARC Organization."));
            params.add(new BasicNameValuePair("To", this.USER_NUMBER)); //Add real number here
            params.add(new BasicNameValuePair("From", TWILIO_NUMBER));

            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            //System.out.println(message.getSid());
            //System.out.println("Message :"+message.getSid());
        }finally {
            System.out.println("Target Complete");
        }
    }
}