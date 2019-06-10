import java.awt.*;
import javax.swing.*;

public class Game extends JPanel {
   static Player you, enemy1, enemy2, enemy3;

   Game(int width, int height) {
      setPreferredSize(new Dimension(width, height));
      try {
         you = new Player(Client.id, this);
         enemy1 = new Player((Client.id+1)%Const.qtePlayers, this);
         enemy2 = new Player((Client.id+2)%Const.qtePlayers, this);
         enemy3 = new Player((Client.id+3)%Const.qtePlayers, this);
      } catch (InterruptedException e) {}
   }

   //desenha os componentes, chamada por paint() e repaint()
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      drawFullMap(g);

      enemy1.draw(g);
      enemy2.draw(g);
      enemy3.draw(g);
      you.draw(g);
      
      // System.out.format("%s: %s [%04d, %04d]\n", Game.you.color, Game.you.statusWithIndex, Game.you.x, Game.you.y);;
      Toolkit.getDefaultToolkit().sync();
   }
      
   void drawFullMap(Graphics g) {
      for (int i = 0; i < Const.LIN; i++)
         for (int j = 0; j < Const.COL; j++)
            g.drawImage(
               Sprite.ht.get(Client.map[i][j].img), 
               Client.map[i][j].getX(), Client.map[i][j].getY(), 
               Const.sizeGrid, Const.sizeGrid, null
            );
   }

   static void changeMap(String keyWord, int l, int c) {
      Client.map[l][c].img = keyWord;
   }
}