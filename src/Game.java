import java.awt.*;
import javax.swing.*;

public class Game extends JPanel {
   static Player you, enemy;

   Game() {
      Sprite.readAllImages();
      Sprite.setGrid();
      Sprite.setMaxLoopStatus();
      Sprite.setSpawnCoordinates();
      setPreferredSize(new Dimension(Sprite.COL * Sprite.width, Sprite.LIN * Sprite.height));

      // passa a intância de JPanel pelo construtor para poder usar repaint()
      Map.init(this);
      try {
         initPlayers();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
   
   //inicia os jogadores com as suas respectivas coordenadas iniciais e cores
   private void initPlayers() throws InterruptedException {
      you = new Player(
         Sprite.spawn[Client.id].getX(), 
         Sprite.spawn[Client.id].getY(), 
         Sprite.personColors[Client.id], this
      );
      enemy = new Player(
         Sprite.spawn[(Client.id + 1) % Sprite.qtePlayers].getX(),
         Sprite.spawn[(Client.id + 1) % Sprite.qtePlayers].getY(),
         Sprite.personColors[(Client.id + 1) % Sprite.qtePlayers], this
      );
   }

   //desenha os componentes, chamada por paint() e repaint()
   public void paintComponent(Graphics g) {
      super.paintComponent(g);

      //ESTÁ DESENHANDO A BASE DO MAPA TODA VEZ, PARA ARRUMAR ISSO CARREGAR UM PANEL EM BAIXO DESSE, O QUAL NUNCA SERÁ ATUALIZADO
      Map.drawFloor(g);
      Map.drawWalls(g);
      Game.enemy.drawPlayer(g);
      Game.you.drawPlayer(g);
      
      System.out.format("%s: %s [%04d, %04d]\t", Game.you.color, Game.you.statusWithIndex, Game.you.x, Game.you.y);
      System.out.format("%s: %s [%04d, %04d]\n", Game.enemy.color, Game.enemy.statusWithIndex, Game.enemy.x, Game.enemy.y);
      Toolkit.getDefaultToolkit().sync();
   }
}