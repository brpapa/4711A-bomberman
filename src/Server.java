import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

class Server {
   static boolean logged[] = new boolean[Const.qtePlayers];
   public static void main(String[] args) {
      Const.initGrid();
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

   CoordinatesThrower thrower;

   ClientManager(Socket clientSocket, int id) {
      this.id = id;
      this.clientSocket = clientSocket;
      (thrower = new CoordinatesThrower(this.id)).start();

      try {
         this.in = new Scanner(clientSocket.getInputStream()); // para receber do cliente
         this.out = new PrintStream(clientSocket.getOutputStream(), true); // para enviar ao cliente
      } catch (IOException e) {}
   }

   public void run() {
      String outputLine;
      clientConnected();
      while (in.hasNextLine()) { //conexão estabelecida com o cliente this.id
         outputLine = convertClientInput(in.nextLine());

         for (PrintStream outClient : listOutClients)
            outClient.println(this.id + " " + outputLine);
      }
      clientDesconnected();
   }

   String convertClientInput(String inputLine) {
      String str[] = inputLine.split(" ");

      if (str[0].equals("keyCodePressed")) {
         thrower.x = Integer.parseInt(str[2]);
         thrower.y = Integer.parseInt(str[3]);

         switch (Integer.parseInt(str[1])) {
            case KeyEvent.VK_W: thrower.setUp();    return "newStatus up";
            case KeyEvent.VK_S: thrower.setDown();  return "newStatus down";
            case KeyEvent.VK_D: thrower.setRight(); return "newStatus right";
            case KeyEvent.VK_A: thrower.setLeft();  return "newStatus left";
         }
      }
      else if (str[0].equals("keyCodeReleased")) {
         switch (Integer.parseInt(str[1])) {
            case KeyEvent.VK_W: thrower.up = false;      break;
            case KeyEvent.VK_S: thrower.down = false;    break;
            case KeyEvent.VK_D: thrower.right = false;   break;
            case KeyEvent.VK_A: thrower.left = false;    break;
         }
         return "stopStatusUpdate";
      }
      else if (str[0].equals("pressedB")) {
         //SEMPRE QUE ALTERAR O MAPA, ATUALIZAR O GRID DO SERVIDOR
         return "gridMap bomb-planted-loop-1 " + 3 + " " + 3;
      }
      return "void";
   }

   void clientConnected() {
      System.out.println("Jogador " + id + " se conectou.");
      listOutClients.add(out);
      Server.logged[id] = true;

      out.println(id); //informa qual o id ao cliente
      // for (int i = 0; i < Const.LIN; i++)
      //    for (int j = 0; j < Const.COL; j++)
      //       out.println(Const.grid[i][j].img);
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

//thread que dispara as coordenadas seguintes aos clientes enquanto a tecla não é solta
class CoordinatesThrower extends Thread {
   boolean up, right, left, down; //true se pressionado, false caso contrário
   int id, x, y;

   CoordinatesThrower(int id) {
      this.id = id;
      this.x = Const.spawn[id].getX() - Const.varX;
      this.y = Const.spawn[id].getY() - Const.varY;
      up = false; down = false; right = false; left = false;
   }
   public void run() {
      int newX = x, newY = y;
      while (true) {
         if (up || down || right || left) {
            if (up)          newY = y - Const.RESIZE;
            else if (down)   newY = y + Const.RESIZE;
            else if (right)  newX = x + Const.RESIZE;
            else if (left)   newX = x - Const.RESIZE;

            if (coordinateIsValid(newX, newY)) {
               for (PrintStream outClient : ClientManager.listOutClients) //para cada cliente logado
                  outClient.println(this.id + " " + "newCoordinate" + " " + newX + " " + newY);
               x = newX; 
               y = newY;
            } else {
               newX = x; 
               newY = y;
            }
            try {sleep(Const.rateCoordinatesUpdate);} catch (InterruptedException e) {}
         }
         try {sleep(0);} catch (InterruptedException e) {}
      }
   }

   //encontra sobre qual Const.grid o jogador está, retorna true apenas se todos forem "floor-1"
   boolean coordinateIsValid(int newX, int newY) {
      int x[] = new int[4], y[] = new int[4];
      //0: ponto do canto superior esquerdo
      x[0] = Const.varX + newX + Const.RESIZE;
      y[0] = Const.varY + newY + Const.RESIZE;
      //1: ponto do canto superior direito
      x[1] = Const.varX + newX + Const.sizeGrid - 2*Const.RESIZE;
      y[1] = Const.varY + newY + Const.RESIZE;
      //2: ponto do canto inferior esquerdo
      x[2] = Const.varX + newX + Const.RESIZE;
      y[2] = Const.varY + newY + Const.sizeGrid - 2*Const.RESIZE;
      //3: ponto do canto inferior direito
      x[3] = Const.varX + newX + Const.sizeGrid - 2*Const.RESIZE;
      y[3] = Const.varY + newY + Const.sizeGrid - 2*Const.RESIZE;
      // System.out.format("[%03d, %03d] [%03d, %03d] [%03d, %03d] [%03d, %03d]\n", x0, y0, x1, y1, x2, y2, x3, y3);

      int l[] = new int[4], c[] = new int[4];
      for (int i = 0; i < 4; i++) { //converte todos os pontos para indicies de Const.grid
         c[i] = x[i]/Const.sizeGrid;
         l[i] = y[i]/Const.sizeGrid;
      }
      // System.out.format("%s %s %s %s\n", Const.grid[l[0]][c[0]].img, Const.grid[l[1]][c[1]].img, Const.grid[l[2]][c[2]].img, Const.grid[l[3]][c[3]].img);

      if (Const.grid[l[0]][c[0]].img.equals("floor-1") && Const.grid[l[1]][c[1]].img.equals("floor-1") 
            && Const.grid[l[2]][c[2]].img.equals("floor-1") && Const.grid[l[3]][c[3]].img.equals("floor-1"))
         return true;
      return false;
   }
   void setUp() {
      up = true; down = false; right = false; left = false;
   }
   void setDown() {
      up = false; down = true; right = false; left = false;
   }
   void setRight() { 
      up = false; down = false; right = true; left = false;
   }
   void setLeft() { 
      up = false; down = false; right = false; left = true;
   }
}