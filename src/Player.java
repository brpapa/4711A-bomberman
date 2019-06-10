import java.awt.*;
import javax.swing.*;

//tanto para you quanto para enemy
public class Player {
   int x, y;
   String statusWithIndex, color;
   JPanel panel;
   StatusAnimer thStatus;
   int numberOfBombs;
   boolean alive;

   Player(int id, JPanel panel) throws InterruptedException {
      this.x = Client.spawn[id].getX();
      this.y = Client.spawn[id].getY();
      this.color = Sprite.personColors[id];
      this.alive = false;
      this.panel = panel;
      this.numberOfBombs = 1; //para 2 bombas, é preciso tratar cada bomba em uma thread diferente

      (thStatus = new StatusAnimer(this, "wait")).start();
   }

   public void draw(Graphics g) {
      g.drawImage(Sprite.ht.get(color + "/" + statusWithIndex), x, y, Const.widthPlayer, Const.heightPlayer, null);
   }
}

class StatusAnimer extends Thread {
   Player p;
   String status;
   int index;
   boolean loop;

   StatusAnimer(Player p, String initialStatus) {
      this.p = p;
      this.status = initialStatus;
      index = 0;
      loop = true;
   }
   public void run() {
      while (true) {
         p.statusWithIndex = status + "-" + index;
         if (loop)
            index = (++index) % Sprite.maxLoopStatus.get(status);

         p.panel.repaint();
         try {Thread.sleep(Const.ratePlayerStatusUpdate);} catch (InterruptedException e) {}
      }
   }
   void setStatus(String status) {
      this.status = status;
      index = 1;
      loop = true;
   }
   void stopStatusUpdate() {
      loop = false;
      index = 0;
   }
}

class Coordinate {
   int x, y;
   String img;
   boolean playerOn[] = new boolean[Const.qtePlayers];

   Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
   }
   Coordinate(int x, int y, String img) {
      this.x = x;
      this.y = y;
      this.img = img;
      for (int i = 0; i < Const.qtePlayers; i++)
         this.playerOn[i] = false; // i: id do player
   }

   boolean containsAnyPlayer() {
      for (boolean i : playerOn)
         if (i == true)
            return true;
      return false;
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