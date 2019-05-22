import java.io.*;
import java.net.*;
import java.util.*;

//recebe e trata as informações de cada cliente!!!
//depois, distribui para todos os clientes conectados ao servidor

class Server {
   public static void main(String[] args) {
      new Server(8080);
   }
   
   static boolean logged[]; 
   void initLogged() {
      logged = new boolean[Sprite.qteMaxPlayers];
      for (int i = 0; i < logged.length; i++)
         logged[i] = false;
   }
   boolean loggedIsFull() {
      for (int i = 0; i < logged.length; i++)
         if (logged[i] == false)
            return false;
      return true;
   }
   
   Server(int portNumber) {
      initLogged();
      try {
         System.out.println("Iniciando servidor...");
         ServerSocket serverSocket = new ServerSocket(portNumber); // socket escuta a porta
         System.out.println("Porta " + portNumber + " aberta.");

         //PROBLEMA COM SERVIDOR LOTADO!
         for (int i = 0; !loggedIsFull(); i = (++i)%logged.length)
            if (!logged[i]) {
               Socket clientSocket = serverSocket.accept();
               new ClientManager(clientSocket, i).start();
            }
         //nao encerra o servidor enquanto a thread dos clientes continuam executando
      } catch (IOException e) {
         System.out.println("IOException: " + e);
         System.exit(1);
      }
   }
}

//cada cliente tem sua thread
class ClientManager extends Thread {
   private Socket clientSocket = null;
   private Scanner in = null;
   private PrintStream out = null;
   private int id;
   private String inputLine, outputLine;

   //de todos as classes (clientes)
   static List<PrintStream> listOutClients = new ArrayList<PrintStream>(); 

   ClientManager(Socket clientSocket, int id) {
      this.id = id;
      this.clientSocket = clientSocket;
      try {
         this.in = new Scanner(clientSocket.getInputStream()); // para receber do cliente
         this.out = new PrintStream(clientSocket.getOutputStream(), true); // para enviar ao cliente
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void run() {
      clientConnected();
      while (in.hasNextLine()) { //cliente está no servidor
         inputLine = in.nextLine();

         // trata/valida a entrada do cliente antes de enviar para todos
         outputLine = inputLine;

         for (PrintStream outClient : listOutClients)
            outClient.println(outputLine); //envia para todos os clientes

         // if (outputLine.equals(""))
         //    break;
      }
      clientDesconnected();
   }
   void clientConnected() {
      System.out.println("Jogador " + id + " se conectou.");
      listOutClients.add(out);
      Server.logged[id] = true;
      out.println(id);
   }
   void clientDesconnected() {
      System.out.println("Jogador " + id + " se desconectou.");
      listOutClients.remove(out);
      Server.logged[id] = false;
      try {
         in.close();
         out.close();
         clientSocket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}