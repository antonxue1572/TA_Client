import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
 
 
public class Client {
    
    // Reference vars
    public static Client client = new Client();
    public static ClientGUI cgui = new ClientGUI();
    
    // Constants
    private final int PORT = 31415;
    private final String HOST_NAME = "localhost";
    
    // Sockets
    Socket clientSocket;
    
    // Constructor
    public Client() {
        
    }
   
    // Main method
    public static void main(String ... args) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                cgui.initGUI();
            }
        }).start();
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                client.initClient();
            }
        }).start();
    }
    
    public void initClient() {
        try {
            clientSocket = new Socket(InetAddress.getByName(HOST_NAME), PORT);
            System.out.println("Conncetion established to " + clientSocket.toString());
            
            new Thread(new ClientOutput(clientSocket)).start();
            new Thread(new ClientInput(clientSocket)).start();
            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private class ClientOutput implements Runnable {

        private Socket socket;
        
        public ClientOutput(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            PrintWriter pw;
            try {
                pw = new PrintWriter(socket.getOutputStream(), true);
                while(true) {
                    /*
                     * Print blank string to deter "bug" within Java output
                     * streams. 
                     */
                    System.out.print("");
                    if(cgui.getUpdateReady()) {
                        pw.println(cgui.getChatMessage());
                        cgui.setUpdateReady(false);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    private class ClientInput implements Runnable {

        private Socket socket;
        
        public ClientInput(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String serverText;
                while((serverText = br.readLine()) != null) {
                    cgui.updateChat(serverText);
                }
            } catch (IOException ex) {
                cgui.updateChat("- Server@" + socket.getLocalSocketAddress() + " disconnected -");
            }
        }
        
    }
   
    public Socket getClientSocket() {
        return clientSocket;
    }
    
}