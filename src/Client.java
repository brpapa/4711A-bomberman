import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
// Scanner sempre le um InputStream
// PrintStream sempre escreve num OutputStream

class Client {
   public static void main(String[] args) {
      // new Window();
      try {
         //dispara cliente
         new Client("127.0.0.1", 8080).set();
      }
      catch(UnknownHostException e) {
         System.out.println("UnknownHostException: " + e);
      } 
      catch(IOException e) {
         System.out.println("IOException: " + e);
      }
   }

   private String host;
   private int porta;

   public Client(String host, int porta) {
      this.host = host;
      this.porta = porta;
   }

   public void set() throws UnknownHostException, IOException {
      Socket client = new Socket(this.host, this.porta);
      System.out.println("Conectado ao servidor.");

      //thread que recebe mensagens do servidor
      new Recebedor(client.getInputStream()).start();


      //DUVIDA DO QUE ENVIAR (OBJETO? COMO? NAO SEI MEU)
      Scanner is = new Scanner(System.in); //lê do teclado

      PrintStream os = new PrintStream(client.getOutputStream());
      while (is.hasNextLine())
         os.println(is.nextLine()); //envia para o servidor
      is.close();   
      os.close();   
      client.close();
   }
}

class Recebedor extends Thread {
   private InputStream server;
   public Recebedor(InputStream server) {
      this.server = server;
   }
   public void run() {
      Scanner s = new Scanner(this.server); //le do servidor
      while (s.hasNextLine())
         System.out.println(s.nextLine()); //imprime na tela
      s.close();
   }
}

