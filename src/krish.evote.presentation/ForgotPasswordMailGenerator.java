package krish.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krish.evote.logic.LogicLayer;
import krish.evote.session.Session;
import krish.evote.session.SessionManager;

public class ForgotPasswordMailGenerator extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static Properties mailServerProperties;

	static javax.mail.Session getMailSession;

	static MimeMessage generateMailMessage;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Forgot password request received");

		String username = null;
		Session session = null;
		LogicLayer logicLayer = null;

		if (session == null) {
			try {
				session = SessionManager.createSession();
			} catch (Exception e) {
				return;
			}
		}
		logicLayer = session.getLogicLayer();
		username = req.getParameter("username");

		if (username == null) {
			genResponseBody(resp,"Error","Username is a required field");
			return;
		}

		try {
			String userEmail = logicLayer.getUserEmail(session, username);
			System.out.println("Email is : " + userEmail);
			
			 SecureRandom random = new SecureRandom();
			 String newPassword = (new BigInteger(130, random).toString(32)).substring(0, 10);
			
			logicLayer.updateUserPassword(session, username, newPassword);
			sendFromGMail(userEmail,newPassword);
			genResponseBody(resp,"Success","A new password has been generated and sent to the email address associated with the account."
					+ "<br>Click <a href=\"\\\">here</a> to go back to the login page.");

		} catch (Exception e) {
			System.out.println(e);
			genResponseBody(resp,"Error", e.getMessage());
			return;
		}

	}

	void genResponseBody(HttpServletResponse resp, String title, String msg) throws IOException {
		PrintWriter out = resp.getWriter().append("<html><title> Stocks Result </title><body></body></html>");
		out.println("<html>");
		out.println("<head><title>"+title+"</title></head>");
		out.println("<body>");
		out.println(msg);
		out.println("</body></html>");
		out.close();
	}

	public void sendFromGMail(String emailAddr, String newPassword) throws AddressException, MessagingException {

		if (emailAddr != null && emailAddr.length() != 0) {
			System.out.println("\n 1st ===> setup Mail Server Properties..");

			mailServerProperties = System.getProperties();

			mailServerProperties.put("mail.smtp.port", "587");

			mailServerProperties.put("mail.smtp.auth", "true");

			mailServerProperties.put("mail.smtp.starttls.enable", "true");

			System.out.println("Mail Server Properties have been setup successfully..");

			System.out.println("\n\n 2nd ===> get Mail Session..");

			getMailSession = javax.mail.Session.getDefaultInstance(mailServerProperties, null);

			generateMailMessage = new MimeMessage(getMailSession);

			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddr));

			generateMailMessage.setSubject("Evoter password recovery");

			String emailBody = "Your new password is : "+newPassword;

			generateMailMessage.setContent(emailBody, "text/html");

			System.out.println("Mail Session has been created successfully..");

			System.out.println("\n\n 3rd ===> Get Session and Send mail");

			Transport transport = getMailSession.getTransport("smtp");

			transport.connect("smtp.gmail.com", "evote.team8@gmail.com", "team8.evote");

			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());

			transport.close();

		}

	}
}
