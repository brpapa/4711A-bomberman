import java.awt.event.KeyEvent;

//thread que dispara as coordenadas seguintes aos clientes enquanto W/A/S/D não é solto
class CoordinatesThrower extends Thread {
   boolean up, right, left, down;
   int id;

   CoordinatesThrower(int id) {
      this.id = id;
      up = down = right = left = false;
   }

   public void run() {
      int newX = Server.player[id].x;
      int newY = Server.player[id].y;
      
      while (true) {
         if (up || down || right || left) {
            if (up)           newY = Server.player[id].y - Const.RESIZE;
            else if (down)    newY = Server.player[id].y + Const.RESIZE;
            else if (right)   newX = Server.player[id].x + Const.RESIZE;
            else if (left)    newX = Server.player[id].x - Const.RESIZE;

            if (coordinateIsValid(newX, newY)) {
               ClientManager.sendToAllClients(id + " newCoordinate " + newX + " " + newY);

               Server.player[id].x = newX;
               Server.player[id].y = newY;
            } else {
               newX = Server.player[id].x;
               newY = Server.player[id].y;
            }
            try {
               sleep(Const.RATE_COORDINATES_UPDATE);
            } catch (InterruptedException e) {}
         }
         try {sleep(0);} catch (InterruptedException e) {}
      }
   }

   int getColumnOfMap(int x) {
      return x/Const.SIZE_SPRITE_MAP;
   }
   int getLineOfMap(int y) {
      return y/Const.SIZE_SPRITE_MAP;
   }

   // encontra sobre quais sprites do mapa o jogador está e verifica se são válidos
   boolean coordinateIsValid(int newX, int newY) {
      if (!Server.player[id].alive)
         return false;

      //verifica se o jogador foi para o fogo (coordenada do centro do corpo)
      int xBody = newX + Const.WIDTH_SPRITE_PLAYER/2;
      int yBody = newY + 2*Const.HEIGHT_SPRITE_PLAYER/3;

      if (Server.map[getLineOfMap(yBody)][getColumnOfMap(xBody)].img.contains("explosion")) {
         Server.player[id].alive = false;
         ClientManager.sendToAllClients(id + " newStatus dead");
         return true;
      }

      
      int x[] = new int[4], y[] = new int[4];
      // 0: ponto do canto superior esquerdo
      x[0] = Const.VAR_X_SPRITES + newX + Const.RESIZE;
      y[0] = Const.VAR_Y_SPRITES + newY + Const.RESIZE;
      // 1: ponto do canto superior direito
      x[1] = Const.VAR_X_SPRITES + newX + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
      y[1] = Const.VAR_Y_SPRITES + newY + Const.RESIZE;
      // 2: ponto do canto inferior esquerdo
      x[2] = Const.VAR_X_SPRITES + newX + Const.RESIZE;
      y[2] = Const.VAR_Y_SPRITES + newY + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
      // 3: ponto do canto inferior direito
      x[3] = Const.VAR_X_SPRITES + newX + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
      y[3] = Const.VAR_Y_SPRITES + newY + Const.SIZE_SPRITE_MAP - 2 * Const.RESIZE;
      
      int l[] = new int[4], c[] = new int[4];
      for (int i = 0; i < 4; i++) { 
         c[i] = getColumnOfMap(x[i]);
         l[i] = getLineOfMap(y[i]);
      }
      
      //PRECISA PERMITIR ANDAR CASO A BOMBA TENHA ACABADO DE SER PLANTADA, MAS BLOQUEAR AO TENTAR VOLTAR

      if (
         (Server.map[l[0]][c[0]].img.equals("floor-1") || Server.map[l[0]][c[0]].img.contains("bomb-planted") || Server.map[l[0]][c[0]].img.contains("explosion")) && 
         (Server.map[l[1]][c[1]].img.equals("floor-1") || Server.map[l[1]][c[1]].img.contains("bomb-planted") || Server.map[l[1]][c[1]].img.contains("explosion")) &&
         (Server.map[l[2]][c[2]].img.equals("floor-1") || Server.map[l[2]][c[2]].img.contains("bomb-planted") || Server.map[l[2]][c[2]].img.contains("explosion")) && 
         (Server.map[l[3]][c[3]].img.equals("floor-1") || Server.map[l[3]][c[3]].img.contains("bomb-planted") || Server.map[l[3]][c[3]].img.contains("explosion"))
      ) 
         return true;

      return false;
   }

   void keyCodePressed(int keyCode) {
      switch (keyCode) {
         case KeyEvent.VK_W: 
            up = true; down = right = left = false;
            ClientManager.sendToAllClients(this.id + " newStatus up");
            break;
         case KeyEvent.VK_S: 
            down = true; up = right = left = false;
            ClientManager.sendToAllClients(this.id + " newStatus down");
            break;
         case KeyEvent.VK_D: 
            right = true; up = down = left = false;
            ClientManager.sendToAllClients(this.id + " newStatus right");
            break;
         case KeyEvent.VK_A: 
            left = true; up = down = right = false;
            ClientManager.sendToAllClients(this.id + " newStatus left");
            break;
      }
   }

   void keyCodeReleased(int keyCode) {
      if (keyCode != KeyEvent.VK_W && keyCode != KeyEvent.VK_S && keyCode != KeyEvent.VK_D && keyCode != KeyEvent.VK_A)
         return;

      ClientManager.sendToAllClients(this.id + " stopStatusUpdate");
      switch (keyCode) {
         case KeyEvent.VK_W: up = false; break;
         case KeyEvent.VK_S: down = false; break;
         case KeyEvent.VK_D: right = false; break;
         case KeyEvent.VK_A: left = false; break;
      }
   }
}