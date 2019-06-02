import java.awt.*;
import javax.swing.*;

public class Panel extends JPanel {
   static Player you, enemy1, enemy2, enemy3;

   Panel() {
      Const.readAllImages();
      Const.setGrid();
      Const.setMaxLoopStatus();
      Const.setSpawnCoordinates();
      setPreferredSize(new Dimension(Const.COL * Const.width, Const.LIN * Const.height));

      Map.init(this); //passa a intância de JPanel para poder usar repaint()
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

      //REPENSAR MAPA
      Map.drawMap(g);

      Panel.enemy1.drawPlayer(g);
      Panel.enemy2.drawPlayer(g);
      Panel.enemy3.drawPlayer(g);
      Panel.you.drawPlayer(g);
      
      System.out.format("%s: %s [%04d, %04d]\n", Panel.you.color, Panel.you.statusWithIndex, Panel.you.x, Panel.you.y);
      Toolkit.getDefaultToolkit().sync();
   }
}