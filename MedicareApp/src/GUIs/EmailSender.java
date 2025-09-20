package GUIs;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    public static void main(String[] args) {
        // SMTP Server Information
        String host = "smtp.gmail.com";  // Change if using another SMTP provider
        String port = "587"; // Use "465" for SSL
        final String username = "sithijasulank789@gmail.com";
        final String password = "qcom resh mjyp rhgv"; // Use an App Password if using Gmail

        // SMTP Server Properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS security
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Authenticate and start a session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recipient@example.com"));
            message.setSubject("Test Email from Java");
            message.setText("Hello, this is a test email sent using JavaMail API 1.6.2!");

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
 }
}
}
