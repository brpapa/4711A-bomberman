import java.awt.Graphics;
import javax.swing.JPanel;

class Map {
   static JPanel p;

   static void init(JPanel p) {
      Map.p = p;
   }

   static void draw(Graphics g, String keyWord, int l, int c) {
      Sprite.grid[l][c].img = keyWord;
      g.drawImage(
         Sprite.ht.get(keyWord), 
         Sprite.grid[l][c].getX(), Sprite.grid[l][c].getY(), 
         Sprite.width, Sprite.height, null
      );
      p.repaint();
   }
   
   static void drawBlocks(Graphics g) {
      for (int i = 1; i < Sprite.LIN - 1; i++)
         for (int j = 1; j < Sprite.COL - 1; j++)
            draw(g, "block", i, j);
   }

   static void drawBase(Graphics g) {
      draw(g, "floor-1", 1, 1);
      draw(g, "floor-1", 2, 1);
      draw(g, "floor-1", 1, 2);
      
      draw(g, "floor-1", Sprite.LIN - 2, Sprite.COL - 2);
      draw(g, "floor-1", Sprite.LIN - 3, Sprite.COL - 2);
      draw(g, "floor-1", Sprite.LIN - 2, Sprite.COL - 3);

      // paredes laterais
      for (int j = 1; j < Sprite.COL - 1; j++)
         draw(g, "wall-center", 0, j);
      for (int j = 1; j < Sprite.COL - 1; j++)
         draw(g, "wall-center", Sprite.LIN - 1, j);

      for (int i = 1; i < Sprite.LIN - 1; i++)
         draw(g, "wall-center", i, 0);
      for (int i = 1; i < Sprite.LIN - 1; i++)
         draw(g, "wall-center", i, Sprite.COL - 1);

      draw(g, "wall-up-left", 0, 0);
      draw(g, "wall-up-right", 0, Sprite.COL - 1);
      draw(g, "wall-down-left", Sprite.LIN - 1, 0);
      draw(g, "wall-down-right", Sprite.LIN - 1, Sprite.COL - 1);

      // paredes centrais
      for (int i = 2; i < Sprite.LIN - 2; i++)
         for (int j = 2; j < Sprite.COL - 2; j++)
            if (i % 2 == 0 && j % 2 == 0)
               draw(g, "wall-center", i, j);
   }
}