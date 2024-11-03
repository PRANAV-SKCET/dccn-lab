import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.io.*;
import java.util.Base64;

public class SENDER {
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final int SMTP_PORT = 465;
    private static final String USER = "pranavarulprakash@gmail.com";
    private static final String PASSWORD = "dcmt vute hvkp qbal"; // Use App Password if using 2FA

    public static void main(String[] args) {
        try {
            // Create an SSL socket to connect to the SMTP server
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) ssf.createSocket(SMTP_SERVER, SMTP_PORT);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            System.out.println("Response: " + reader.readLine());

            writer.write("HELO " + SMTP_SERVER + "\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            writer.write("AUTH LOGIN\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // Send username (Base64 encoded)
            writer.write(Base64.getEncoder().encodeToString(USER.getBytes()) + "\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            // Send password (Base64 encoded)
            writer.write(Base64.getEncoder().encodeToString(PASSWORD.getBytes()) + "\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            writer.write("MAIL FROM:<" + USER + ">\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            writer.write("RCPT TO:<pranavarulprakash@gmail.com>\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            writer.write("DATA\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            writer.write("Subject: Test Email\r\n");
            writer.write("From: " + USER + "\r\n");
            writer.write("To: pranavarulprakash@gmail.com\r\n");
            writer.write("\r\n");
            writer.write("HI THIS IS A TEST MAIL\r\n");
            writer.write(".\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            writer.write("QUIT\r\n");
            writer.flush();
            System.out.println("Response: " + reader.readLine());

            writer.close();
            reader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}