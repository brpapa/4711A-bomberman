import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
//IDEAL: CLIENTE NAO DEVERIA ACESSAR AS CONSTANTES, TUDO DEVERIA SER ENVIADO PELO SERVIDOR

public class Client {
   private Socket socket = null;
   static PrintStream out = null;
   static Scanner in = null;
   static int id;

   final static int rateStatusUpdate = 115;
   static Coordinate map[][];

   Client(String host, int porta) {
      try {
         this.socket = new Socket(host, porta);
         out = new PrintStream(socket.getOutputStream(), true);  //para enviar ao servidor
         in = new Scanner(socket.getInputStream()); //para receber do servidor
      } catch (UnknownHostException e) {
         System.out.println("UnknownHostException: " + e);
      } catch (IOException e) {
         System.out.println("IOException: " + e);
      }
      
      receiveInitialSettings();
      new Receiver().start();
   }

   void receiveInitialSettings() {
      id = in.nextInt();
      System.out.println("Bem vindo! Seu personagem é o " + Sprite.personColors[Client.id]);

      map = new Coordinate[Const.LIN][Const.COL];
      for (int i = 0; i < Const.LIN; i++)
         for (int j = 0; j < Const.COL; j++)
            map[i][j] = new Coordinate(Const.sizeGrid * j, Const.sizeGrid * i, in.next());
   }
   
   public static void main(String[] args) {
      new Client("127.0.0.1", 8080);
      new Window();
   }
}

class Window extends JFrame {
   Window() {
      Sprite.readAllImages();
      Sprite.setMaxLoopStatus();
      Sprite.setSpawnCoordinates();
      
      add(new Game(Const.COL*Const.sizeGrid, Const.LIN*Const.sizeGrid));
      setTitle("BomberMan");
      pack();
      setVisible(true);
      // setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      addKeyListener(new Sender());
   }
}