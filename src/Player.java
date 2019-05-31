import java.awt.*;
import javax.swing.*;

//tanto para you quanto para enemy
public class Player {
   int x, y;
   String statusWithIndex, color;
   JPanel panel;

   public StatusAnimer thStatus;

   Player(int x0, int y0, String color, JPanel panel) throws InterruptedException {
      this.x = x0;
      this.y = y0;
      this.color = color;
      this.panel = panel;

      thStatus = new StatusAnimer(this, "wait");
      thStatus.start();
   }

   public void drawPlayer(Graphics g) {
      g.drawImage(
         Sprite.ht.get(color + "/" + statusWithIndex),
         x - Sprite.difWidth, y - Sprite.difHeight, 
         Sprite.widthPlayer, Sprite.heightPlayer, null
      );
   }
}

class StatusAnimer extends Thread {
   Player p;
   String status;
   int index;

   StatusAnimer(Player p, String status) {
      this.p = p;
      this.status = status;
      index = 0;
   }
   public void run() {
      while (true) {
         p.statusWithIndex = status + "-" + index;
         index = (++index) % Sprite.maxLoopStatus.get(status);

         p.panel.repaint();
         try {
            Thread.sleep(120);
         } catch (InterruptedException e) {}
      }
   }
   void setStatus(String status) {
      this.status = status;
      index = 0;
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