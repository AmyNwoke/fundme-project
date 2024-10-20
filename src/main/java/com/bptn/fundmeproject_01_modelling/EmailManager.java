package com.bptn.fundmeproject_01_modelling;

import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailManager {

    private String fromEmail = "fundmesavings@gmail.com"; // Your email
    private String password = "@Lilboo25"; // Your email password
    private String smtpHost = "smtp.gmail.com"; // SMTP server address
    private int smtpPort = 587; // SMTP port

    // Method to send an email
    public void sendEmail(String toEmail, String subject, String messageBody) {
        // Set up the properties for the mail session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        // Get the mail session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Email template for group creation
    public void sendGroupCreationEmail(String userName, String toEmail, String groupCode) {
        String subject = "Your Group Has Been Created!";
        String messageBody = String.format(
            "Hello %s,\n\nWelcome to FundMeApp!\n" +
            "Your group has been created. Your group code is %s.\n" +
            "Share this code with your group members to join.\n\n" +
            "Happy saving!\nFundMeApp Team",
            userName, groupCode
        );
        sendEmail(toEmail, subject, messageBody);
    }

    // Email template for joining a group
    public void sendJoinGroupEmail(String userName, String toEmail, String groupName, double savingsTarget) {
        String subject = "You Have Successfully Joined a Group!";
        String messageBody = String.format(
            "Hello %s,\n\nWelcome to FundMeApp!\n" +
            "You have successfully joined the group \"%s\".\n" +
            "You are one step closer to achieving your savings target of $%.2f.\n\n" +
            "Happy saving!\nFundMeApp Team",
            userName, groupName, savingsTarget
        );
        sendEmail(toEmail, subject, messageBody);
    }

    // Email template for funding savings
    public void sendFundSavingsEmail(String userName, String toEmail, double individualContribution) {
        String subject = "Savings Funded Successfully!";
        String messageBody = String.format(
            "Hello %s,\n\nYou have successfully funded your savings with $%.2f.\n" +
            "Keep funding every month to achieve your savings target.\n\n" +
            "Happy saving!\nFundMeApp Team",
            userName, individualContribution
        );
        sendEmail(toEmail, subject, messageBody);
    }
}
