import java.awt.*;
import javax.swing.*;

//tanto para you quanto para enemy
public class Player {
   int x, y;
   String statusWithIndex, color;
   JPanel panel;
   StatusAnimer thStatus;

   Player(int id, JPanel panel) throws InterruptedException {
      this.x = Const.spawn[id].getX() - Const.varX;
      this.y = Const.spawn[id].getY() - Const.varY;
      this.color = Const.personColors[id];
      this.panel = panel;
      (thStatus = new StatusAnimer(this, "wait")).start();
   }

   public void draw(Graphics g) {
      g.drawImage(Const.ht.get(color + "/" + statusWithIndex), x, y, Const.widthPlayer, Const.heightPlayer, null);
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
            index = (++index) % Const.maxLoopStatus.get(status);

         p.panel.repaint();
         try {
            Thread.sleep(Const.rateStatusUpdate);
         } catch (InterruptedException e) {}
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
