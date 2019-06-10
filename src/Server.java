import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

class Server {
   public static void main(String[] args) {
      Const.initMap();
      Const.setPlayerCoordinates();
      new Server(8080);
   }
   static boolean logged[] = new boolean[Const.qtePlayers];
   void initLogged() {
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
         ServerSocket ss = new ServerSocket(portNumber); // socket escuta a porta
         System.out.println("Porta " + portNumber + " aberta.");

         //PROBLEMA COM SERVIDOR LOTADO!
         for (int i = 0; !loggedIsFull(); i = (++i)%logged.length)
            if (!logged[i]) {
               Socket clientSocket = ss.accept();
               new ClientManager(clientSocket, i).start(); //trata o cliente em uma nova instância da classe/thread
            }
         //nao encerra o servidor enquanto a thread dos clientes continuam executando

      } catch (IOException e) {
         System.out.println("IOException: " + e);
         System.exit(1);
      }
   }
}

class ClientManager extends Thread {
   static List<PrintStream> listOutClients = new ArrayList<PrintStream>();

   private Socket clientSocket = null;
   private Scanner in = null;
   private PrintStream out = null;
   private int id;

   CoordinatesThrower ct;
   MapThrower mt;

   ClientManager(Socket clientSocket, int id) {
      this.id = id;
      this.clientSocket = clientSocket;
      (ct = new CoordinatesThrower(this.id)).start();
      (mt = new MapThrower(this.id)).start();

      try {
         this.in = new Scanner(clientSocket.getInputStream()); // para receber do cliente
         this.out = new PrintStream(clientSocket.getOutputStream(), true); // para enviar ao cliente
      } catch (IOException e) {}
   }

   public void run() {
      String outputLine;
      //envia uma única string com as configurações iniciais do cliente
      sendInitialSettings(); 
      clientConnected();
      while (in.hasNextLine()) { 
         //conexão estabelecida com o cliente this.id
         outputLine = convertClientInput(in.nextLine());

         for (PrintStream outClient : listOutClients)
            outClient.println(this.id + " " + outputLine);
      }
      clientDesconnected();
   }

   String convertClientInput(String inputLine) {
      String str[] = inputLine.split(" ");

      if (str[0].equals("keyCodePressed")) {
         ct.x = Integer.parseInt(str[2]);
         ct.y = Integer.parseInt(str[3]);

         switch (Integer.parseInt(str[1])) {
            case KeyEvent.VK_W: ct.setUp();    return "newStatus up";
            case KeyEvent.VK_S: ct.setDown();  return "newStatus down";
            case KeyEvent.VK_D: ct.setRight(); return "newStatus right";
            case KeyEvent.VK_A: ct.setLeft();  return "newStatus left";
         }
      }
      else if (str[0].equals("keyCodeReleased")) {
         switch (Integer.parseInt(str[1])) {
            case KeyEvent.VK_W: ct.up = false;      return "stopStatusUpdate";
            case KeyEvent.VK_S: ct.down = false;    return "stopStatusUpdate";
            case KeyEvent.VK_D: ct.right = false;   return "stopStatusUpdate";
            case KeyEvent.VK_A: ct.left = false;    return "stopStatusUpdate";
         }
         return "void";
      }
      else if (str[0].equals("pressedB")) {
         mt.setBombPlanted(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
         return "void";
      }
      return "void";
   }

   void clientConnected() {
      System.out.println("Jogador " + id + " se conectou.");
      listOutClients.add(out);
      Server.logged[id] = true;
   }

   void sendInitialSettings() {
      out.print(id);

      for (int i = 0; i < Const.LIN; i++)
         for (int j = 0; j < Const.COL; j++)
            out.print(" " + Const.map[i][j].img);

      for (int i = 0; i < Const.qtePlayers; i++)
         out.print(" " + Const.playerCoordinate[i].getX() + " " + Const.playerCoordinate[i].getY());
         
      out.print("\n");
   }

   void clientDesconnected() {
      System.out.println("Jogador " + id + " se desconectou.");
      listOutClients.remove(out);
      Server.logged[id] = false;
      try {
         in.close();
         out.close();
         clientSocket.close();
      } catch (IOException e) {}
   }
}