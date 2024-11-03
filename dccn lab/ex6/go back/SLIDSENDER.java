import java.net.*;
import java.io.*;

public class SLIDSENDER {
    public static void main(String[] args) throws Exception {
       
        ServerSocket ser = new ServerSocket(10);
        Socket s = ser.accept();

        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in1 = new BufferedReader(new InputStreamReader(s.getInputStream()));

        String sbuff[] = new String[100]; 
        PrintStream p; 
        int sws, nf, ano, i, base = 0, nextFrame = 0;
        boolean ackReceived = false;

        System.out.print("Enter the window size: ");
        sws = Integer.parseInt(in.readLine());

       
        System.out.print("Enter the total number of frames to send: ");
        nf = Integer.parseInt(in.readLine());

        p = new PrintStream(s.getOutputStream());
        p.println(nf);

        
        System.out.println("Enter " + nf + " Messages to send:");
        for (i = 0; i < nf; i++) {
            sbuff[i] = in.readLine();
        }

       
        do {
           
            while (nextFrame < nf && (nextFrame - base) < sws) {
                p = new PrintStream(s.getOutputStream());
                System.out.println("Sending Frame " + nextFrame + ": " + sbuff[nextFrame]);
                p.println(nextFrame + ":" + sbuff[nextFrame]); 
                nextFrame++;
            }

            System.out.println("Waiting for acknowledgment...");
            long startTime = System.currentTimeMillis();
            int timeout = 5000;

            while (!ackReceived && (System.currentTimeMillis() - startTime) < timeout) {
              
                if (in1.ready()) {
                    ano = Integer.parseInt(in1.readLine());
                    System.out.println("Acknowledgment received for Frame " + ano);
                   
                    base = ano + 1;
                    ackReceived = true;

                    if (base >= nf) {
                        System.out.println("All frames acknowledged.");
                        break;
                    }
                }
            }

    
            if (!ackReceived) {
                System.out.println("Timeout! No acknowledgment received for Frame " + base);
                System.out.println("Retransmitting all frames in the window starting from Frame " + base);
                for (i = base; i < nextFrame; i++) {
                    p = new PrintStream(s.getOutputStream());
                    System.out.println("Resending Frame " + i + ": " + sbuff[i]);
                    p.println(i + ":" + sbuff[i]); 
                }
            } else {
                ackReceived = false;
            }

        } while (base < nf); 

       
        s.close();
    }
}
