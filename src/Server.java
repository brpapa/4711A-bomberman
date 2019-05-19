import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

//recebe e trata as informações de cada cliente!!!
//depois, distribui para todos os clientes conectados ao servidor
class Server {
   public static void main(String[] args) {
      try {
         //inicia servidor
         new Server(8080).set();
      } catch (IOException e) {
         System.out.println("IOException: " + e);
         System.exit(1);
      }
   }

   private int port;
   private List<PrintStream> listClients;

   public Server(int port) {
      this.port = port;
      this.listClients = new ArrayList<PrintStream>();
   }

   public void set() throws IOException {
      ServerSocket server = new ServerSocket(this.port);
      System.out.println("Porta " + this.port + " aberta.");

      while (true) {
         //aceita um cliente
         Socket client = server.accept();
         System.out.println("Cliente " + client.getInetAddress().getHostAddress() + " se conectou.");

         //adiciona saída do cliente na lista
         this.listClients.add(new PrintStream(client.getOutputStream()));

         //trata o cliente em uma nova thread
         new ThreadClient(client.getInputStream(), this).start();
      }
   }

   public void distributeMessages(String msg) {
      for (PrintStream client : this.listClients)
         client.println(msg); //imprime na tela do cliente
   }
}

class ThreadClient extends Thread {
   private InputStream client;
   private Server server;

   public ThreadClient(InputStream client, Server server) {
      this.client = client;
      this.server = server;
   }
   public void run() {
      Scanner s = new Scanner(this.client); //le do cliente, quando chega algo
      while (s.hasNextLine())
         server.distributeMessages(s.nextLine()); //envia para todos os cliente
      s.close();
   }
}
