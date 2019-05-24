import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

class Receiver extends Thread {
   public void run() {
      String str;

      while (Client.in.hasNextLine()) {
         // recebe uma linha inteira
         str = Client.in.next();

         if (str.equals("keyCodePressed"))
            processKeyCodePressed(Client.in.nextInt(), Client.in.nextInt());

      }
      Client.in.close();
   }

   void processKeyCodePressed(int id, int keyCode) {
      if (id == Client.id) {
         Game.you.threadMove.setDirection(keyCode);
         // Game.you.threadMove.notify(); // acorda a thread
      } else if (id == (Client.id + 1) % Sprite.qtePlayers) {
         Game.enemy.threadMove.setDirection(keyCode);
         // Game.enemy.threadMove.notify(); // acorda a thread
      }
   }
   // void processKeyCodeReleased(int id, int keyCode) {
   // Game.you.threadMove.wait(); //adormece a thread

   // }
}

// escuta enquanto a janela (JFrame) estiver em foco
class Sender extends KeyAdapter {
   public void keyPressed(KeyEvent e) {
      Client.out.println("keyCodePressed " + Client.id + " " + e.getKeyCode());
   }

   public void keyReleased(KeyEvent e) {
      Client.out.println("keyCodeReleased " + Client.id + " " + e.getKeyCode());
   }
}

class Game extends JPanel {
   static Player you, enemy;

   Game() {
      Sprite.readAllImages();
      Sprite.setGrid();
      Sprite.setMaxLoopStatus();
      Sprite.setSpawnCoordinates();
      setPreferredSize(new Dimension(Sprite.COL * Sprite.width, Sprite.LIN * Sprite.height));

      // passa a intância de JPanel pelo construtor para poder usar repaint()
      Map.init(this);
      try {
         initPlayers();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
   
   //desenha os componentes, chamada por paint() e repaint()
   public void paintComponent(Graphics g) {
      // DÚVIDA: pq no logo após o inicio do programa ele executa paintComponet 2 vezes?
      super.paintComponent(g);
      // Map.drawBlocks(g); //REDESENHA TODA HORA, TA ERRADO
      Map.drawBase(g);
      Game.enemy.drawPlayer(g);
      Game.you.drawPlayer(g);
      Toolkit.getDefaultToolkit().sync();
   }

   //inicia os jogadores com as suas respectivas coordenadas iniciais e cores
   private void initPlayers() throws InterruptedException {
      you = new Player(
         Sprite.spawn[Client.id].getX(), 
         Sprite.spawn[Client.id].getY(),
         Sprite.personColors[Client.id],
         this
      );
      enemy = new Player(
         Sprite.spawn[(Client.id+1)%Sprite.qtePlayers].getX(), 
         Sprite.spawn[(Client.id+1)%Sprite.qtePlayers].getY(),
         Sprite.personColors[(Client.id+1)%Sprite.qtePlayers],
         this
      );
   }
}

public class Window extends JFrame {
   Game panel;

   Window() {
      add(panel = new Game());
      setTitle("Bomber Man");
      pack();
      setVisible(true);
      // setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      addKeyListener(new Sender());
   }
}