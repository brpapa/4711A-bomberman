import java.awt.Graphics;
import javax.swing.JPanel;

class Map {
   static JPanel p;

   static void init(JPanel p) {
      Map.p = p;
   }

   static void drawMap(Graphics g) {
      drawFloor(g);
      drawWalls(g);
   }

   static void draw(Graphics g, String keyWord, int l, int c) {
      Const.grid[l][c].img = keyWord;
      g.drawImage(
         Const.ht.get(keyWord), 
         Const.grid[l][c].getX(), Const.grid[l][c].getY(), 
         Const.width, Const.height, null
      );
   }
   
   static void drawFloor(Graphics g) {
      for (int i = 1; i < Const.LIN - 1; i++)
         for (int j = 1; j < Const.COL - 1; j++)
            draw(g, "floor-1", i, j);
   }

   static void drawWalls(Graphics g) {
      // paredes laterais
      for (int j = 1; j < Const.COL - 1; j++)
         draw(g, "wall-center", 0, j);
      for (int j = 1; j < Const.COL - 1; j++)
         draw(g, "wall-center", Const.LIN - 1, j);

      for (int i = 1; i < Const.LIN - 1; i++)
         draw(g, "wall-center", i, 0);
      for (int i = 1; i < Const.LIN - 1; i++)
         draw(g, "wall-center", i, Const.COL - 1);

      draw(g, "wall-up-left", 0, 0);
      draw(g, "wall-up-right", 0, Const.COL - 1);
      draw(g, "wall-down-left", Const.LIN - 1, 0);
      draw(g, "wall-down-right", Const.LIN - 1, Const.COL - 1);

      // paredes centrais
      for (int i = 2; i < Const.LIN - 2; i++)
         for (int j = 2; j < Const.COL - 2; j++)
            if (i % 2 == 0 && j % 2 == 0)
               draw(g, "wall-center", i, j);
   }
}