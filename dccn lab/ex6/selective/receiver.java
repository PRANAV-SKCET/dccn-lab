import java.net.*;
import java.io.*;

class SLIDSELRECEIVER {
    public static void main(String[] args) throws Exception {
        
        Socket s = new Socket(InetAddress.getLocalHost(), 10);
        
        
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
     
        PrintStream p = new PrintStream(s.getOutputStream());
        
        int expectedFrame = 0, totalFrames;
        String rbuf[] = new String[100]; 
        boolean received[] = new boolean[100]; 
        int damagedFrame = 2; 
        boolean damagedFrameReceived = false; 

        System.out.println("Receiver is ready...");

        totalFrames = Integer.parseInt(in.readLine());
        System.out.println("Total number of frames to receive: " + totalFrames);

      
        while (expectedFrame < totalFrames) {
           
            String frameMessage = in.readLine();
            
           
            String[] parts = frameMessage.split(":");
            int frameNumber = Integer.parseInt(parts[0]);
            String frameContent = parts[1];

            
            if (frameNumber == damagedFrame && !damagedFrameReceived) {
                System.out.println("Frame " + frameNumber + " is damaged. No acknowledgment sent.");
                damagedFrameReceived = true; 
                continue; 
            }

            
            if (frameNumber >= expectedFrame && !received[frameNumber]) {
                System.out.println("Received Frame " + frameNumber + ": " + frameContent);
             
                rbuf[frameNumber] = frameContent;
                received[frameNumber] = true;

              
                System.out.println("Sending acknowledgment for Frame " + frameNumber);
                p.println(frameNumber); 

               
                while (received[expectedFrame]) {
                    expectedFrame++;
                }
            }
        }

     
        System.out.println("All frames received and acknowledged.");
      
        s.close();
    }
}
