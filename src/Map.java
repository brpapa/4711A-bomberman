import java.awt.Graphics;

class Map {
   int lin, col;
   
   Map() {
      Sprite.setGrid();
      this.lin = Sprite.LIN;
      this.col = Sprite.COL;
   }

   void draw(Graphics g, String keyWord, int l, int c) {
      Sprite.g[l][c].img = keyWord;
      g.drawImage(
         Sprite.ht.get(keyWord), 
         Sprite.g[l][c].getX(), Sprite.g[l][c].getY(), 
         Sprite.width, Sprite.height, null
      );
   }
   
   void drawBlocks(Graphics g) {
      for (int i = 1; i < lin - 1; i++)
         for (int j = 1; j < col - 1; j++)
            draw(g, "block", i, j);
      
      // buracos aleatórios entre as paredes quebráveis
      // for (Coordinate c : rand.coord) {
      //    System.out.printf("%d, %d\n", c.getX(), c.getY());
      //    draw(g, "floor-1", c.getX(), c.getY());
      // }
   }

   void drawBase(Graphics g) {
      // onde player 1 nasce
      draw(g, "floor-1", 1, 1);
      draw(g, "floor-1", 2, 1);
      draw(g, "floor-1", 1, 2);
      // draw(g, "floor-1", 3, 1);
      // draw(g, "floor-1", 1, 3);
      
      // onde player 2 nasce
      draw(g, "floor-1", lin - 2, col - 2);
      draw(g, "floor-1", lin - 3, col - 2);
      draw(g, "floor-1", lin - 2, col - 3);
      // draw(g, "floor-1", lin - 4, col - 2);
      // draw(g, "floor-1", lin - 2, col - 4);

      // paredes das bordas
      for (int j = 1; j < col - 1; j++)
         draw(g, "wall-center", 0, j);
      for (int j = 1; j < col - 1; j++)
         draw(g, "wall-center", lin - 1, j);

      for (int i = 1; i < lin - 1; i++)
         draw(g, "wall-center", i, 0);
      for (int i = 1; i < lin - 1; i++)
         draw(g, "wall-center", i, col - 1);

      draw(g, "wall-up-left", 0, 0);
      draw(g, "wall-up-right", 0, col - 1);
      draw(g, "wall-down-left", lin - 1, 0);
      draw(g, "wall-down-right", lin - 1, col - 1);

      // paredes centrais
      for (int i = 2; i < lin - 2; i++)
         for (int j = 2; j < col - 2; j++)
            if (i % 2 == 0 && j % 2 == 0)
               draw(g, "wall-center", i, j);
   }

}

class Coordinate {
   int x, y;

   void setX(int x) {
      this.x = x;
   }
   void setY(int y) {
      this.y = y;
   }
   int getX() {
      return this.x;
   }
   int getY() {
      return this.y;
   }
}
class Grid extends Coordinate {
   String img;
   
   Grid(int x, int y) {
      this.x = x;
      this.y = y;
   }
}