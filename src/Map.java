package src;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class Map {
   public final int LIN = 13, COL = 19;
   public final int gridWidth = 16 * Images.RESIZE, gridHeight = 16 * Images.RESIZE;
   public Coordinate grid[][] = new Coordinate[LIN][COL];

   public Map() {
      // define as coordenadas válidas das imagens do mapa
      for (int i = 0; i < LIN; i++)
         for (int j = 0; j < COL; j++)
            grid[i][j] = new Coordinate(gridWidth * j, gridHeight * i);
   }

   public void draw(Graphics g, String keyWord, int l, int c) {
      grid[l][c].img = keyWord;
      g.drawImage(Images.ht.get(keyWord), grid[l][c].getX(), grid[l][c].getY(), gridWidth, gridHeight, null);
   }
   
   public void drawBlocks(Graphics g) {
      for (int i = 1; i < LIN - 1; i++)
         for (int j = 1; j < COL - 1; j++)
            draw(g, "block", i, j);
   }
   public void drawBase(Graphics g) {
      // onde player 1 nasce
      draw(g, "floor-1", 1, 1);
      draw(g, "floor-1", 2, 1);
      draw(g, "floor-1", 3, 1);
      draw(g, "floor-1", 1, 2);
      draw(g, "floor-1", 1, 3);
      // onde player 2 nasce
      draw(g, "floor-1", LIN - 2, COL - 2);
      draw(g, "floor-1", LIN - 3, COL - 2);
      draw(g, "floor-1", LIN - 4, COL - 2);
      draw(g, "floor-1", LIN - 2, COL - 3);
      draw(g, "floor-1", LIN - 2, COL - 4);

      // paredes das bordas
      for (int j = 1; j < COL - 1; j++)
         draw(g, "wall-center", 0, j);
      for (int j = 1; j < COL - 1; j++)
         draw(g, "wall-center", LIN - 1, j);

      for (int i = 1; i < LIN - 1; i++)
         draw(g, "wall-center", i, 0);
      for (int i = 1; i < LIN - 1; i++)
         draw(g, "wall-center", i, COL - 1);

      draw(g, "wall-up-left", 0, 0);
      draw(g, "wall-up-right", 0, COL - 1);
      draw(g, "wall-down-left", LIN - 1, 0);
      draw(g, "wall-down-right", LIN - 1, COL - 1);

      // paredes centrais
      for (int i = 2; i < LIN - 2; i++)
         for (int j = 2; j < COL - 2; j++)
            if (i % 2 == 0 && j % 2 == 0)
               draw(g, "wall-center", i, j);
   }
   
}

class Coordinate {
   public int x, y;
   public String img;
   public boolean temItemEspecial;

   public Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
      temItemEspecial = false;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }
}