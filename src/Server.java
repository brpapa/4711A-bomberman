import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

class Server {
   static boolean logged[] = new boolean[Const.qtePlayers];
   public static void main(String[] args) {
      Const.setGrid();
      Const.setSpawnCoordinates();
      new Server(8080);
   }
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
   static List<PrintStream> listOutClients = new ArrayList<PrintStream>(); //de todos as classes (clientes)

   private Socket clientSocket = null;
   private Scanner in = null;
   private PrintStream out = null;
   private int id;
   private String outputLine, inputLine;

   DisparadorCoordenadas thDisp;

   ClientManager(Socket clientSocket, int id) {
      this.id = id;
      this.clientSocket = clientSocket;
      (thDisp = new DisparadorCoordenadas(this.id)).start();

      try {
         this.in = new Scanner(clientSocket.getInputStream()); // para receber do cliente
         this.out = new PrintStream(clientSocket.getOutputStream(), true); // para enviar ao cliente
      } catch (IOException e) {}
   }

   public void run() {
      clientConnected();
      while (in.hasNextLine()) { // conexão com o cliente this.id
         inputLine = in.nextLine();
         System.out.format("%s: %s\n", Const.personColors[id], inputLine);
         
         outputLine = trataEntrada(inputLine);
         for (PrintStream outClient : listOutClients)
            outClient.println(this.id + " " + outputLine);
      }
      clientDesconnected();
   }

   String trataEntrada(String inputLine) {
      String str[] = inputLine.split(" ");

      if (str[0].equals("keyCodePressed")) {
         thDisp.setCoordinates(Integer.parseInt(str[2]), Integer.parseInt(str[3]));

         //MELHORAR CRIANDO UMA TABELA HASH, tipo KeyEvent.VK_W -> "up"
         switch (Integer.parseInt(str[1])) {
            case KeyEvent.VK_W: 
               thDisp.setUp();
               return "newStatus up";
            case KeyEvent.VK_S: 
               thDisp.setDown();
               return "newStatus down";
            case KeyEvent.VK_D: 
               thDisp.setRight();
               return "newStatus right";
            case KeyEvent.VK_A: 
               thDisp.setLeft();
               return "newStatus left";
         }
      }
      else if (str[0].equals("keyCodeReleased")) {
         switch (Integer.parseInt(str[1])) {
            case KeyEvent.VK_W: 
               thDisp.upPressed = false; 
               break;
            case KeyEvent.VK_S: 
               thDisp.downPressed = false; 
               break;
            case KeyEvent.VK_D: 
               thDisp.rightPressed = false; 
               break;
            case KeyEvent.VK_A: 
               thDisp.leftPressed = false; 
               break;
         }
         return "stopStatusUpdate";
      }
      return "void";
   }

   void clientConnected() {
      System.out.println("Jogador " + id + " se conectou.");
      listOutClients.add(out);
      Server.logged[id] = true;
      out.println(id); //informa qual o id ao cliente
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

//thread que dispara as novas coordenadas aos clientes enquanto a tecla não é solta
class DisparadorCoordenadas extends Thread {
   boolean upPressed, rightPressed, leftPressed, downPressed;
   int id, x, y;

   DisparadorCoordenadas(int id) {
      this.id = id;
      this.x = Const.spawn[id].getX() - Const.varX;
      this.y = Const.spawn[id].getY() - Const.varY;
      upPressed = false;
      downPressed = false;
      rightPressed = false;
      leftPressed = false;
   }
   public void run() {
      int newX = x, newY = y;
      while (true) {
         if (upPressed || downPressed || rightPressed || leftPressed) {
            if (upPressed)
               newY = y - Const.RESIZE;
            else if (downPressed)
               newY = y + Const.RESIZE;
            else if (rightPressed)
               newX = x + Const.RESIZE;
            else if (leftPressed)
               newX = x - Const.RESIZE;

            if (newCoordinateIsValid(newX, newY)) {
               for (PrintStream outClient : ClientManager.listOutClients) // para cada cliente logado
                  outClient.println(this.id + " " + "newCoordinate" + " " + newX + " " + newY);
               x = newX;
               y = newY;
            }
            else {
               newX = x;
               newY = y;
            }

            try {
               sleep(Const.rateCoordinatesUpdate);
            } catch (InterruptedException e) {}
         }
         try {
            sleep(0);
         } catch (InterruptedException e) {}
      }
   }
   boolean newCoordinateIsValid(int newX, int newY) {
      newX += Const.varX; //para facilitar a comparação com as coordenadas das images do mapa
      newY += Const.varY;
      if (newX < Const.grid[1][1].getX()-Const.RESIZE || newX > Const.grid[Const.LIN-2][Const.COL-2].getX()+Const.RESIZE)
         return false;
      if (newY < Const.grid[1][1].getY()-Const.RESIZE || newY > Const.grid[Const.LIN-2][Const.COL-2].getY()+Const.RESIZE)
         return false;

      return true;
   }
   void setCoordinates(int x, int y) {
      this.x = x;
      this.y = y;
   }
   void setUp() {
      upPressed = true;
      downPressed = false;
      rightPressed = false;
      leftPressed = false;
   }
   void setDown() {
      upPressed = false;
      downPressed = true;
      rightPressed = false;
      leftPressed = false;
   }
   void setRight() {
      upPressed = false;
      downPressed = false;
      rightPressed = true;
      leftPressed = false;
   }
   void setLeft() {
      upPressed = false;
      downPressed = false;
      rightPressed = false;
      leftPressed = true;
   }
}