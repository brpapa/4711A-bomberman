import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Client {
   private Socket socket = null;
   static PrintStream out = null;
   static Scanner in = null;
   static int id;

   final static int rateStatusUpdate = 115;
   static Coordinate map[][] = new Coordinate[Const.LIN][Const.COL];

   static Coordinate spawn[] = new Coordinate[Const.QTY_PLAYERS];
   static boolean alive[] = new boolean[Const.QTY_PLAYERS];

   Client(String host, int porta) {
      try {
         System.out.print("Estabelecendo conexão com o servidor...");
         this.socket = new Socket(host, porta);
         out = new PrintStream(socket.getOutputStream(), true);  //para enviar ao servidor
         in = new Scanner(socket.getInputStream()); //para receber do servidor
      } 
      catch (UnknownHostException e) {
         System.out.println(" erro: " + e + "\n");
         System.exit(1);
      } 
      catch (IOException e) {
         System.out.println(" erro: " + e + "\n");
         System.exit(1);
      }
      System.out.print(" ok\n");
      
      receiveInitialSettings();
      new Receiver().start();
   }

   void receiveInitialSettings() {
      id = in.nextInt();

      //mapa
      for (int i = 0; i < Const.LIN; i++)
         for (int j = 0; j < Const.COL; j++)
            map[i][j] = new Coordinate(Const.SIZE_SPRITE_MAP * j, Const.SIZE_SPRITE_MAP * i, in.next());
      
      //situação (vivo ou morto) inicial de todos os jogadores
      for (int i = 0; i < Const.QTY_PLAYERS; i++)
         Client.alive[i] = in.nextBoolean();

      //coordenadas inicias de todos os jogadores
      for (int i = 0; i < Const.QTY_PLAYERS; i++)
         Client.spawn[i] = new Coordinate(in.nextInt(), in.nextInt());
   }
   
   public static void main(String[] args) {
      new Client("127.0.0.1", 8383);
      new Window();
   }
}

class Window extends JFrame {
   private static final long serialVersionUID = 1L;

   Window() {
      Sprite.loadImages();
      Sprite.setMaxLoopStatus();
      
      add(new Game(Const.COL*Const.SIZE_SPRITE_MAP, Const.LIN*Const.SIZE_SPRITE_MAP));
      setTitle("bomberman");
      pack();
      setVisible(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      addKeyListener(new Sender());
   }
}