import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

class Design extends JPanel {
   private static final long serialVersionUID = 1L;
   Map m;
   Player p;
   
   Design() {
      m = new Map();
      p = new Player();
      setPreferredSize(new Dimension(Sprite.COL * Sprite.width, Sprite.LIN * Sprite.height));
      Sprite.readAllImages();
   }
   
   // função invocada com repaint()
   public void paintComponent(Graphics g) {
      // DÚVIDA: pq no logo após o inicio do programa ele executa paintComponet 2 vezes?
      // System.out.println("teste");
      super.paintComponent(g);
      m.drawBlocks(g);
      m.drawBase(g);
      p.draw(g);
      
      Toolkit.getDefaultToolkit().sync();
   }
}

public class Window extends JFrame {
   private static final long serialVersionUID = 1L;
   Design d;

   Window() {
      add(d = new Design());
      setTitle("Bomber Man");
      pack();
      setVisible(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      // escuta enquanto o JFrame estiver em foco
      addKeyListener(new WindowListener());
   }
}

class WindowListener implements KeyListener {
   int keyCodePressed, keyCodeReleased;
   int newKeyCodePressed, newKeyCodeReleased;

   // tecla é apertada
   public void keyPressed(KeyEvent e) {
      newKeyCodePressed = e.getKeyCode();
      Client.out.println(e.getKeyChar()); // envia ao servidor

   }

   // tecla é solta
   public void keyReleased(KeyEvent e) {
      newKeyCodeReleased = e.getKeyCode();
      Client.out.println(e.getKeyChar());
   }

   // tecla é digitada (confirmar se é isso mesmo)
   public void keyTyped(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_B)
         Client.out.println(e.getKeyChar());
   }
}