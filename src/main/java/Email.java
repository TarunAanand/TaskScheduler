import java.util.regex.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;

public class Email {
    String fromAddress;
    String toAddress;
    String subject;
    String contents;

    Email(String fromAddress, String ToAddress, String subject, String contents) {
        this.fromAddress = fromAddress;
        this.toAddress = ToAddress;
        this.subject = subject;
        this.contents = contents;
    }

//    public static String getFromAddress() {    return fromAddress; }
//    public void setFromAddress(String fromAddress) {    Email.fromAddress = fromAddress; }
//
//    public static String getToAddress() {    return toAddress; }
//    public void setToAddress(String ToAddress) {    Email.toAddress = ToAddress; }

    void sendEmail() {
        if(!isValidEmailAddress(toAddress)) {
            System.out.println("INVALID EMAIL");
            return;
        }

        String host = "smtp.gmail.com";
        String username = "aegs.colab@gmail.com";
        String password = "vzfy vhob nmha rblk";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject(subject);
            message.setText(contents);
            Transport.send(message);
            System.out.println("EMAIL SENT SUCCESSFULLY");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public static boolean isValidEmailAddress(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
