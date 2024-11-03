import java.io.*;
import java.net.*;

public class FTSERVER {
    public static void main(String args[]) throws IOException {
        
        ServerSocket ss = new ServerSocket(8081);
        System.out.println("Server started, waiting for connection...");

        Socket cs = ss.accept();
        System.out.println("Client connected.");

    
        BufferedReader st = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        String requestedFile = st.readLine(); 
        System.out.println("The requested file is: " + requestedFile);

       
        File file = new File(requestedFile);

        PrintWriter put = new PrintWriter(cs.getOutputStream(), true);

        if (file.exists()) {
           
            BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream outputStream = new BufferedOutputStream(cs.getOutputStream());

            byte[] buffer = new byte[4096]; 
            int bytesRead;

          
            while ((bytesRead = fileReader.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

           
            fileReader.close();
            outputStream.flush();
            outputStream.close();

            System.out.println("File transferred successfully.");
        } else {
            
            put.println("ERROR: File not found");
            System.out.println("File does not exist.");
        }

     
        put.close();
        cs.close();  
        ss.close();  
    }
}
