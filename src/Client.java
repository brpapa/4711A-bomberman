import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Client {
   private Socket socket = null;
   static PrintStream out = null;
   static Scanner in = null;
   static int id;

   public static void main(String[] args) {
      new Client("127.0.0.1", 8080);
      new Window();
   }

   Client(String host, int porta) {
      try {
         this.socket = new Socket(host, porta);
         out = new PrintStream(socket.getOutputStream(), true);  //para enviar ao servidor
         in = new Scanner(socket.getInputStream()); //para receber do servidor

         //recebe configurações iniciais do servidor
         id = in.nextInt();
         System.out.println("Bem vindo! Seu personagem é o " + Const.personColors[Client.id]);

         Const.initGrid();
         // for (int i = 0; i < Const.LIN; i++)
         //    for (int j = 0; j < Const.COL; j++)
         //       Const.grid[i][j].img = in.nextLine();

         new Receiver().start();
      } catch (UnknownHostException e) {
         System.out.println("UnknownHostException: " + e);
      } catch (IOException e) {
         System.out.println("IOException: " + e);
      }
   }
}

//IDEAL: CLIENTE NAO DEVERIA ACESSAR AS CONSTANTES, TUDO DEVE SER ENVIADO PELO SERVIDOR
//RECEBER ASSIM QUE O CLIENTE LOGA TODAS AS SUAS CONFIGURACÕES INICIAIS

class Window extends JFrame {
   Window() {
      Const.readAllImages();
      Const.setMaxLoopStatus();
      Const.setSpawnCoordinates();

      add(new Game());
      setTitle("Bomber-Man");
      pack();
      setVisible(true);
      // setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      addKeyListener(new Sender());
   }
}