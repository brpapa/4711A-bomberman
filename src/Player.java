package src;

import java.awt.Graphics;

class Person {
   final int width = 22 * Images.RESIZE, height = 33 * Images.RESIZE;

   /*
    * // retorna sobre qual imagem o person está no momento public String
    * estaSobre() { // procurar pela grid valida em que o personagem está dentro //
    * colocar margem de erro de 1 para mais ou para menos, tanto no x, quanto no y
    * // (pois tem uma folga)
    * 
    * return "floor-1"; // for (int i = 0; i < m.LIN; i++) // for (int j = 0; j <
    * m.COL; j++)
    * 
    * }
    * 
    * public boolean podeAndar() { if (estaSobre() == "floor-1") return true;
    * return false; }
    */
}

public class Player extends Person {
   public int xGrid, yGrid, x, y;
   public String estado;

   Player(int x0, int y0) {
      x = x0;
      y = y0;
      estado = "wait-0";
   }

   public void draw(Graphics g) {
      // System.out.printf("%3d, %3d\n", this.x, this.y);

      this.x -= 3 * Images.RESIZE;
      this.y -= 17 * Images.RESIZE;
      g.drawImage(Images.ht.get(this.estado), this.x, this.y, width, height, null);
   }
}