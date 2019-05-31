import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

class Server {
   static boolean logged[] = new boolean[Sprite.qtePlayers];
   public static void main(String[] args) {
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

//thread que dispara as coordenadas subsequentes aos clientes enquanto a tecla não é solta
class DisparadorCoordenadas extends Thread {
   boolean upPressed, rightPressed, leftPressed, downPressed;
   int id, x, y, newX, newY;

   DisparadorCoordenadas(int id) {
      this.id = id;
      upPressed = false;
      downPressed = false;
      rightPressed = false;
      leftPressed = false;
   }
   public void run() {
      while (true) {
         if (upPressed || downPressed || rightPressed || leftPressed) {
            if (upPressed)
               newY = y - Sprite.RESIZE;
            else if (downPressed)
               newY = y + Sprite.RESIZE;
            else if (rightPressed)
               newX = x + Sprite.RESIZE;
            else if (leftPressed)
               newX = x - Sprite.RESIZE;
            
            for (PrintStream outClient : ClientManager.listOutClients) //para cada cliente logado
               outClient.println(this.id + " " + "newCoordinate" + " " + newX + " " + newY);
            x = newX; 
            y = newY;
            try {
               sleep(Sprite.personFrameRate);
            } catch(InterruptedException e) {}
         }
         try {
            sleep(1);
         } catch(InterruptedException e) {}
      }
   }
}

//cada cliente tem sua thread
class ClientManager extends Thread {
   private Socket clientSocket = null;
   private Scanner in = null;
   private PrintStream out = null;
   private int id;
   private String outputLine, inputLine;

   //de todos as classes (clientes)
   static List<PrintStream> listOutClients = new ArrayList<PrintStream>(); 

   DisparadorCoordenadas thDisp;

   ClientManager(Socket clientSocket, int id) {
      this.id = id;
      this.clientSocket = clientSocket;
      (thDisp = new DisparadorCoordenadas(this.id)).start();

      try {
         this.in = new Scanner(clientSocket.getInputStream()); // para receber do cliente
         this.out = new PrintStream(clientSocket.getOutputStream(), true); // para enviar ao cliente
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void run() {
      clientConnected();
      while (in.hasNextLine()) { //conexão com o cliente this.id
         inputLine = in.nextLine();
         System.out.format("%s: %s\n", Sprite.personColors[id], inputLine);
         
         if (!ehCoordenada(inputLine)) {
            outputLine = inputLine;
            for (PrintStream outClient : listOutClients)
               outClient.println(this.id + " " + outputLine); //envia para todos os clientes
         }
      }
      clientDesconnected();
   }

   boolean ehCoordenada(String inputLine) {
      String aux[] = inputLine.split(" ");

      if (aux[0].equals("pressed")) {
         switch (Integer.parseInt(aux[1])) {
            case KeyEvent.VK_W:
               thDisp.upPressed = true;
               break;
            case KeyEvent.VK_S:
               thDisp.downPressed = true;
               break;
            case KeyEvent.VK_D:
               thDisp.rightPressed = true;
               break;
            case KeyEvent.VK_A:
               thDisp.leftPressed = true;
               break;
         }
         thDisp.x = Integer.parseInt(aux[2]);
         thDisp.y = Integer.parseInt(aux[3]);
         return true;
      }
      if (aux[0].equals("released")) {
         switch (Integer.parseInt(aux[1])) {
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
         return true;
      }
      return false;
   }

   void clientConnected() {
      System.out.println("Jogador " + id + " se conectou.");
      listOutClients.add(out);
      Server.logged[id] = true;
      out.println(id); //informa qual o id do cliente
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