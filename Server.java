import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.util.Hashtable; //para evitar ambiguidade com a classe Map
import javax.imageio.*;
import src.*;

class Design extends JPanel {
   Map m;
   Player p1, p2;

   Design() {
      m = new Map();
      p1 = new Player(0, 0);

      setPreferredSize(new Dimension(m.COL * m.gridWidth, m.LIN * m.gridHeight));
      Images.read();
   }

   //função invocada com repaint()
   //DÚVIDA: pq no inicio do programa ele executa essa funcao 2 vezes?
   public void paintComponent(Graphics g) {
      // System.out.println("teste");
      super.paintComponent(g);
      m.drawBlocks(g);
      m.drawBase(g);
      p1.draw(g);

      Toolkit.getDefaultToolkit().sync();
   }

}


class Window extends JFrame {
   Design d = new Design();

   public Window() {
      config();

      // threads

      // adiciona evento ao JFrame (escuta enquanto a janela estiver em foco)
      // this.addKeyListener(new PlayerListener());
   }

   public void config() {
      add(d);
      setTitle("Bomber Man");
      pack();
      setVisible(true);
      // setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   }
}

public class Server {
   static public void main(String args[]) {
      try {
         Window w = new Window();
      } catch (NullPointerException e) {}
   }
}
