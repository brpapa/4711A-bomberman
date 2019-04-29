package src;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class PlayerListener extends KeyAdapter {
   // tecla é apertada
   public void keyPressed(KeyEvent e) {
      // inicia a thread que anima o boneco e faz ele ficar andado no sentido acionado
      int keyCode = e.getKeyCode();

      // if (keyCode == KeyEvent.VK_D)

      // else if (keyCode == KeyEvent.VK_A)

      // else if (keyCode == KeyEvent.VK_W)

      // else if (keyCode == KeyEvent.VK_S)
   }

   // tecla é solta
   public void keyReleased(KeyEvent e) {
      // para a execução da thread

   }
}

// precisa descender de Janela para poder usar repaint()
// class Anima extends Janela implements Runnable {

// }
