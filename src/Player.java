import java.awt.Graphics;

class Person {
   final int width = 22 * Sprite.RESIZE;
   final int height = 33 * Sprite.RESIZE;
   // diferença de pixel entre spriter do mapa e do jogador
   final int difWidth = 3 * Sprite.RESIZE;
   final int difHeight = 16 * Sprite.RESIZE;
}

public class Player extends Person {
   Coordinate p;
   String estado, color;
   
   Player() {
      this.p.x = Sprite.spawn[Client.id].getX();
      this.p.y = Sprite.spawn[Client.id].getY();
      this.estado = "wait-0";
      this.color = Sprite.personColors[Client.id];
   }

   void draw(Graphics g) {
      // System.out.printf("%3d, %3d\n", x, y);
      g.drawImage(
         Sprite.ht.get(color + "/" + estado),
         p.x - difWidth, p.y - difHeight, 
         width, height, null
      );
   }
}