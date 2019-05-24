import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
   private Socket socket = null;
   static PrintStream out = null;
   static Scanner in = null;
   static int id;
   public static void main(String[] args) {
      new Client("127.0.0.1", 8080);
      new Window();
   }

   Client(String host, int porta) {
      try {
         this.socket = new Socket(host, porta);
         Client.out = new PrintStream(socket.getOutputStream(), true);  //para enviar ao servidor
         Client.in = new Scanner(socket.getInputStream()); //para receber do servidor

         //PRIMEIRA LINHA QUE O O SERVIDOR envia é o ID DO PLAYER
         Client.id = Integer.parseInt(in.nextLine());
         System.out.println("Bem vindo! Seu id é " + Client.id);

         new Receiver().start();
      } catch (UnknownHostException e) {
         System.out.println("UnknownHostException: " + e);
      } catch (IOException e) {
         System.out.println("IOException: " + e);
      }
   }
}