import java.net.*;
import java.io.*;

public class SLIDSELSENDER {
    public static void main(String[] args) throws Exception {
       
        ServerSocket ser = new ServerSocket(10);
        Socket s = ser.accept(); 

        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
        
       
        String sbuff[] = new String[100];
        boolean ack[] = new boolean[100]; 
        PrintStream p;

        int sws, nf, ano, i, base = 0, nextFrame = 0;

       
        System.out.print("Enter the window size: ");
        sws = Integer.parseInt(in.readLine());

       
        System.out.print("Enter the total number of frames to send: ");
        nf = Integer.parseInt(in.readLine());

        
        p = new PrintStream(s.getOutputStream());
        p.println(nf);

        
        System.out.println("Enter " + nf + " messages to send:");
        for (i = 0; i < nf; i++) {
            sbuff[i] = in.readLine();
            ack[i] = false; 
        }

       
        do {
           
            while (nextFrame < nf && (nextFrame - base) < sws) {
                if (!ack[nextFrame]) { 
                    p = new PrintStream(s.getOutputStream());
                    System.out.println("Sending Frame " + nextFrame + ": " + sbuff[nextFrame]);
                    p.println(nextFrame + ":" + sbuff[nextFrame]); 
                }
                nextFrame++;
            }

            
            System.out.println("Waiting for acknowledgment...");

            long startTime = System.currentTimeMillis();
            int timeout = 5000; 

            
            while ((System.currentTimeMillis() - startTime) < timeout) {
                
                if (in1.ready()) {
                    ano = Integer.parseInt(in1.readLine());
                    System.out.println("Acknowledgment received for Frame " + ano);

                    ack[ano] = true;

                 
                    while (ack[base]) {
                        base++;
                    }

                  
                    if (base >= nf) {
                        System.out.println("All frames acknowledged.");
                        break;
                    }
                }
            }

          
            for (i = base; i < nextFrame; i++) {
                if (!ack[i]) {
                    System.out.println("Retransmitting Frame " + i + ": " + sbuff[i]);
                    p.println(i + ":" + sbuff[i]); 
                }
            }

        } while (base < nf); 

        s.close();
    }
}
