import java.io.*;
import java.net.*;

public class FTCLIENT {
    public static void main(String args[]) throws IOException {
       
        Socket s = new Socket("localhost", 8081);

       
        PrintWriter put = new PrintWriter(s.getOutputStream(), true);

     
        BufferedReader serverResponse = new BufferedReader(new InputStreamReader(s.getInputStream()));

       
        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

       
        System.out.println("Enter the file name to transfer from the server:");
        String fileName = userInputReader.readLine(); 

        put.println(fileName);

    
        String response = serverResponse.readLine();

        
        if (response != null && response.startsWith("ERROR")) {
            System.out.println("Server error: " + response);
        } else {
           
            File receivedFile = new File("received_" + fileName); 
            FileOutputStream fileOut = new FileOutputStream(receivedFile);

            
            BufferedInputStream fileReader = new BufferedInputStream(s.getInputStream());

            byte[] buffer = new byte[4096]; 
            int bytesRead;

           
            while ((bytesRead = fileReader.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }

            fileOut.close();
            fileReader.close();

            
            System.out.println("File received successfully.");
        }

  
        s.close();
    }
}
