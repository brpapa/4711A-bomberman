import java.io.PrintStream;

//thread que dispara as coordenadas seguintes aos clientes enquanto W/A/S/D não é solto
class CoordinatesThrower extends Thread {
   boolean up, right, left, down; // true se pressionado, false caso contrário
   int id, x, y;

   CoordinatesThrower(int id) {
      this.id = id;
      this.x = Const.playerCoordinate[id].getX();
      this.y = Const.playerCoordinate[id].getY();
      up = false;
      down = false;
      right = false;
      left = false;
   }

   public void run() {
      int newX = x, newY = y;
      while (true) {
         if (up || down || right || left) {
            if (up)           newY = y - Const.RESIZE;
            else if (down)    newY = y + Const.RESIZE;
            else if (right)   newX = x + Const.RESIZE;
            else if (left)    newX = x - Const.RESIZE;

            if (coordinateIsValid(newX, newY)) {
               for (PrintStream outClient : ClientManager.listOutClients) // para cada cliente logado
                  outClient.println(this.id + " " + "newCoordinate" + " " + newX + " " + newY);
               x = newX;
               y = newY;
               Const.playerCoordinate[this.id].setX(newX);
               Const.playerCoordinate[this.id].setY(newY);
            } else {
               newX = x;
               newY = y;
            }
            try {
               sleep(Const.rateCoordinatesUpdate);
            } catch (InterruptedException e) {}
         }
         try {
            sleep(0);
         } catch (InterruptedException e) {}
      }
   }

   // encontra sobre quais índices do mapa o jogador está e verifica se é válido
   boolean coordinateIsValid(int newX, int newY) {
      int bodyX, bodyY, bodyNewX, bodyNewY;
      // ponto central do corpo do personagem
      bodyX = x + Const.widthPlayer / 2;
      bodyY = y + 2 * Const.heightPlayer / 3;
      Const.map[bodyY/Const.sizeGrid][bodyX/Const.sizeGrid].playerOn[this.id] = false;
      bodyNewX = newX + Const.widthPlayer / 2;
      bodyNewY = newY + 2 * Const.heightPlayer / 3;
      Const.map[bodyNewY/Const.sizeGrid][bodyNewX/Const.sizeGrid].playerOn[this.id] = true;


      int x[] = new int[4], y[] = new int[4];
      // 0: ponto do canto superior esquerdo
      x[0] = Const.varX + newX + Const.RESIZE;
      y[0] = Const.varY + newY + Const.RESIZE;
      // 1: ponto do canto superior direito
      x[1] = Const.varX + newX + Const.sizeGrid - 2 * Const.RESIZE;
      y[1] = Const.varY + newY + Const.RESIZE;
      // 2: ponto do canto inferior esquerdo
      x[2] = Const.varX + newX + Const.RESIZE;
      y[2] = Const.varY + newY + Const.sizeGrid - 2 * Const.RESIZE;
      // 3: ponto do canto inferior direito
      x[3] = Const.varX + newX + Const.sizeGrid - 2 * Const.RESIZE;
      y[3] = Const.varY + newY + Const.sizeGrid - 2 * Const.RESIZE;

      int l[] = new int[4], c[] = new int[4];
      for (int i = 0; i < 4; i++) { // converte todos os pontos para índices do mapa
         c[i] = x[i] / Const.sizeGrid;
         l[i] = y[i] / Const.sizeGrid;
      }
      // System.out.format("%s %s %s %s\n", map[l[0]][c[0]].img, map[l[1]][c[1]].img, map[l[2]][c[2]].img, map[l[3]][c[3]].img);

      // retorna true apenas se todos forem "floor-1" ou a bomba acabou de ser plantada
      if (
         (Const.map[l[0]][c[0]].img.equals("floor-1") || Const.map[l[0]][c[0]].img.contains("bomb-planted")) && 
         (Const.map[l[1]][c[1]].img.equals("floor-1") || Const.map[l[1]][c[1]].img.contains("bomb-planted")) &&
         (Const.map[l[2]][c[2]].img.equals("floor-1") || Const.map[l[2]][c[2]].img.contains("bomb-planted")) && 
         (Const.map[l[3]][c[3]].img.equals("floor-1") || Const.map[l[3]][c[3]].img.contains("bomb-planted"))
      ) return true;

      return false;
   }

   void setUp() {
      up = true; down = false; right = false; left = false;
   }

   void setDown() {
      up = false; down = true; right = false; left = false;
   }

   void setRight() {
      up = false; down = false; right = true; left = false;
   }

   void setLeft() {
      up = false; down = false; right = false; left = true;
   }
}