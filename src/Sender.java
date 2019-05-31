import java.awt.event.*;
//ENVIAR A TECLA, SERVIDOR TRATAR E ENVIAR DE VOLTA A COORDENADA

//escuta enquanto a janela (JFrame) estiver em foco
public class Sender extends KeyAdapter {
   int lastKeyCodePressed;
   
   public void keyPressed(KeyEvent e) {
      if (isNewKeyCode(e.getKeyCode()))
         Client.out.println("pressed " + e.getKeyCode() + " " + Game.you.x + " " + Game.you.y);
   }
      
   public void keyReleased(KeyEvent e) {
      Client.out.println("released " + e.getKeyCode());
      lastKeyCodePressed = -1; //a próxima tecla sempre será nova
   }
   
   boolean isNewKeyCode(int keyCode) {
      boolean ok = (keyCode != lastKeyCodePressed) ? true : false;
      lastKeyCodePressed = keyCode;
      return ok;
   }
}