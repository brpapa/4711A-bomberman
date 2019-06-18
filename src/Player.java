import java.awt.Graphics;
import javax.swing.JPanel;

//tanto para you quanto para enemy
public class Player {
   int x, y;
   String status, color;
   JPanel panel;
   boolean alive;

   StatusChanger sc;

   Player(int id, JPanel panel) throws InterruptedException {
      this.x = Client.spawn[id].x;
      this.y = Client.spawn[id].y;
      this.color = Sprite.personColors[id];
      this.panel = panel;
      this.alive = Client.alive[id];

      (sc = new StatusChanger(this, "wait")).start();
   }

   public void draw(Graphics g) {
      if (alive)
         g.drawImage(Sprite.ht.get(color + "/" + status), x, y, Const.WIDTH_SPRITE_PLAYER, Const.HEIGHT_SPRITE_PLAYER, null);
   }
}

class StatusChanger extends Thread {
   Player p;
   String status;
   int index;
   boolean playerInMotion;

   StatusChanger(Player p, String initialStatus) {
      this.p = p;
      this.status = initialStatus;
      index = 0;
      playerInMotion = true;
   }
   public void run() {
      while (true) {
         p.status = status + "-" + index;
         if (playerInMotion) {
            index = (++index) % Sprite.maxLoopStatus.get(status);
            p.panel.repaint();
         }

         try {
            Thread.sleep(Const.RATE_PLAYER_STATUS_UPDATE);
         } catch (InterruptedException e) {}

         if (p.status.equals("dead-4")) {
            p.alive = false;
            if (Game.you == p)
               System.exit(1);
         }
      }
   }
   void setLoopStatus(String status) {
      this.status = status;
      index = 1;
      playerInMotion = true;
   }
   void stopLoopStatus() {
      playerInMotion = false;
      index = 0;
   }
}