import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.lang.String;

import javax.swing.JPanel;


public class Player {
   int x, y;
   String status, color;
   JPanel panel;

   public StatusAnime threadStatus;
   public MovePlayer threadMove;

   Player(int x0, int y0, String color, JPanel panel) throws InterruptedException {
      this.x = x0;
      this.y = y0;
      this.color = color;
      this.panel = panel;

      //executa tanto para you quanto para enemy
      threadMove = new MovePlayer(this);
      threadMove.start();
      // threadMove.wait();
      threadStatus = new StatusAnime(this, "wait");
      threadStatus.start();
      // threadStatus.wait();
   }

   public void drawPlayer(Graphics g) {
      g.drawImage(
         Sprite.ht.get(color + "/" + status),
         x - Sprite.difWidth, y - Sprite.difHeight, 
         Sprite.widthPlayer, Sprite.heightPlayer, null
      );
      panel.repaint(); //desnecessário, talvez??
   }
}

class StatusAnime extends Thread {
   Player p;
   String status;

   StatusAnime(Player p, String status) {
      this.p = p;
      this.status = status;
   }
   public void run() {
      for (int i = 0; true; i = (++i) % Sprite.maxLoopStatus.get(status)) {
         p.status = status + "-" + i;
         p.panel.repaint();

         try {
            Thread.sleep(120);
         } catch (InterruptedException e) {}
      }
   }
   void setStatus(String status) {
      this.status = status;
   }
}

class MovePlayer extends Thread {
   Player p;
   int keyCode;
   
   MovePlayer(Player p) {
      this.p = p;
   }
   public void run() {
      while (true) {
         switch (keyCode) {
            case KeyEvent.VK_D: 
               p.x += Sprite.RESIZE; 
               p.threadStatus.setStatus("right");
               break;
            case KeyEvent.VK_A: 
               p.x -= Sprite.RESIZE; 
               p.threadStatus.setStatus("left");
               break;
            case KeyEvent.VK_W: 
               p.y -= Sprite.RESIZE; 
               p.threadStatus.setStatus("up");
               break;
            case KeyEvent.VK_S: 
               p.y += Sprite.RESIZE; 
               p.threadStatus.setStatus("down");
               break;
         }
         p.panel.repaint();

         try {
            Thread.sleep(30);
         } catch (InterruptedException e) {}
      }
   }

   void setDirection(int keyCode) {
      this.keyCode = keyCode;
   }
}

class Coordinate {
   String img;
   public int x, y;

   Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public void setX(int x) {
      this.x = x;
   }
   public void setY(int y) {
      this.y = y;
   }
   public int getX() {
      return this.x;
   }
   public int getY() {
      return this.y;
   }
}