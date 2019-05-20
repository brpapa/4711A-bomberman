import java.io.*;
import java.net.*;
import java.util.*;

class Client {
   public static void main(String[] args) {
      new Client("127.0.0.1", 8080).init();
      // new Window();
   }
   
   private String host;
   private int porta;
   public Client(String host, int porta) {
      this.host = host;
      this.porta = porta;
   }
   
   public void init() {
      try {
         Socket socket = new Socket(this.host, this.porta);
         new Receiver(socket).start();
         new Sender(socket).start();
      } catch (UnknownHostException e) {
         System.out.println("UnknownHostException: " + e);
      } catch (IOException e) {
         System.out.println("IOException: " + e);
      }
   }
}

class Sender extends Thread {
   private Socket socket;
   public Sender(Socket socket) {
      this.socket = socket;
   }
   public void run() {
      try {
         PrintStream out = new PrintStream(socket.getOutputStream());
         Scanner stdIn = new Scanner(System.in);
         
         String clientInput;
         while (stdIn.hasNextLine()) {
            clientInput = stdIn.nextLine();
            out.println(clientInput); //envia ao servidor
         }
         out.close();
         stdIn.close();
         socket.close();
      } catch(IOException e) {
         System.out.println("IOException: " + e);
      }
   }
}

class Receiver extends Thread {
   private Socket socket;
   public Receiver(Socket socket) {
      this.socket = socket;
   }
   public void run() {
      try {
         Scanner in = new Scanner(socket.getInputStream());
         
         String serverInput;
         while (in.hasNextLine()) {
            serverInput = in.nextLine(); //recebe do servidor
            System.out.println(serverInput);
         }
         in.close();
      } catch(UnknownHostException e) {
         System.out.println("UnknownHostException: " + e);
      } catch(IOException e) {
         System.out.println("IOException: " + e);
      }
   }
}