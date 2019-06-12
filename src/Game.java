import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Game extends JPanel {
   private static final long serialVersionUID = 1L;
   static Player you, enemy1, enemy2, enemy3;

   Game(int width, int height) {
      setPreferredSize(new Dimension(width, height));
      try {
         System.out.print("Inicializando jogadores...");
         you = new Player(Client.id, this);
         enemy1 = new Player((Client.id+1)%Const.QTY_PLAYERS, this);
         enemy2 = new Player((Client.id+2)%Const.QTY_PLAYERS, this);
         enemy3 = new Player((Client.id+3)%Const.QTY_PLAYERS, this);
      } catch (InterruptedException e) {
         System.out.println(" erro: " + e + "\n");
         System.exit(1);
      }
      System.out.print(" ok\n");

      System.out.println("Meu jogador: " + Sprite.personColors[Client.id]);
   }

   //desenha os componentes, chamada por paint() e repaint()
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      drawMap(g);
      enemy1.draw(g);
      enemy2.draw(g);
      enemy3.draw(g);
      you.draw(g);
      
      // System.out.format("%s: %s [%04d, %04d]\n", Game.you.color, Game.you.statusWithIndex, Game.you.x, Game.you.y);;
      Toolkit.getDefaultToolkit().sync();
   }
   
   void drawMap(Graphics g) {
      for (int i = 0; i < Const.LIN; i++)
         for (int j = 0; j < Const.COL; j++)
            g.drawImage(
               Sprite.ht.get(Client.map[i][j].img), 
               Client.map[i][j].x, Client.map[i][j].y, 
               Const.SIZE_SPRITE_MAP, Const.SIZE_SPRITE_MAP, null
            );
   }

   static void setSpriteMap(String keyWord, int l, int c) {
      Client.map[l][c].img = keyWord;
   }
}