import java.io.*;
import java.net.*;
import java.util.*;

//recebe e trata as informações de cada cliente!!!
//depois, distribui para todos os clientes conectados ao servidor
class Server {
   public static void main(String[] args) {
      new Server(8080).init();
   }
   private int port;
   public Server(int port) {
      this.port = port;
   }

   public void init() {
      try {
         System.out.println("Iniciando servidor...");
         ServerSocket serverSocket = new ServerSocket(this.port); //socket escuta a porta
         System.out.println("Porta " + this.port + " aberta.");
         
         //escuta clientes
         while (true) {
            System.out.println("Aguardando conexão...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente " + clientSocket.getInetAddress().getHostName() + " se conectou.");
            new ThreadClient(clientSocket).start();
         }
         // System.out.println("Servidor encerrado.");
      } catch(IOException e) {
         System.out.println("IOException: " + e);
         System.exit(1);
      }
   }
}

class ThreadClient extends Thread {
   private Socket clientSocket = null;
   static List<PrintStream> listClients = new ArrayList<PrintStream>(); //da classe!
   
   public ThreadClient(Socket clientSocket) {
      this.clientSocket = clientSocket;
   }
   public void run() {
      try {
         //leitor p o fluxo de entrada do cliente
         Scanner in = new Scanner(clientSocket.getInputStream());
         //gravador p o fluxo de saída do cliente
         PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
         listClients.add(out);
         
         String inputLine, outputLine;
         while (in.hasNextLine()) { 
            //FICA NO LOOPING ENQUANTO O CLIENTE TA NO SERVIDOR
            inputLine = in.nextLine();

            //trata a entrada do cliente 
            outputLine = inputLine;

            //distribui para todos os clientes
            for (PrintStream outC : listClients)
               outC.println(outputLine);

            // if (outputLine.equals(""))
            //    break;
         }
         System.out.println("Cliente " + clientSocket.getInetAddress().getHostName() + " se desconectou.");
         listClients.remove(out);
         in.close();
         out.close();
         clientSocket.close();
      } catch(IOException e) {
         e.printStackTrace();
      }
   }
}