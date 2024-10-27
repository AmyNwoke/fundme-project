package com.bptn.fundmeproject.manager;

import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailManager {

	private String fromEmail = "fundmesavings@gmail.com";
	private String password = "zaxa uohd rbtl tgtw";
	private String smtpHost = "smtp.gmail.com";
	private int smtpPort = 587;

	// Method to send an email
	public void sendEmail(String toEmail, String subject, String messageBody) {
		// properties for the mail session
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true"); // enables smth authentication
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

	// Email template to send mail after funding your savings
	public void sendFundSavingsEmail(String userName, String toEmail, double individualContribution) {
		String subject = "Savings Funded Successfully!";
		String messageBody = String.format("Hello %s,\n\nYou have successfully funded your savings with $%.2f.\n"
				+ "Happy saving!\nFundMeApp Team", userName, individualContribution);
		sendEmail(toEmail, subject, messageBody);
	}
}
