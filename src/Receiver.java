//recebe informações de todos os clientes
public class Receiver extends Thread {
   Player p;
   
   Player fromWhichPlayerIs(int id) {
      if (id == Client.id)
         return Game.you;
      else if (id == (Client.id+1)%Const.QTY_PLAYERS)
         return Game.enemy1;
      else if (id == (Client.id+2)%Const.QTY_PLAYERS)
         return Game.enemy2;
      else if (id == (Client.id+3)%Const.QTY_PLAYERS)
         return Game.enemy3;
      return null;
   }

   public void run() {
      String str;
      while (Client.in.hasNextLine()) {
         this.p = fromWhichPlayerIs(Client.in.nextInt()); //id do cliente
         str = Client.in.next();

         if (str.equals("mapUpdate")) { //p null
            Game.setSpriteMap(Client.in.next(), Client.in.nextInt(), Client.in.nextInt());
            Game.you.panel.repaint();
         }
         else if (str.equals("newCoordinate")) {
            p.x = Client.in.nextInt();
            p.y = Client.in.nextInt();
            Game.you.panel.repaint();
         }
         else if (str.equals("newStatus")) {
            p.sc.setLoopStatus(Client.in.next());
         }
         else if (str.equals("stopStatusUpdate")) {
            p.sc.stopLoopStatus();
         }
         else if (str.equals("playerJoined")) {
            p.alive = true;
         }
      }
      Client.in.close();
   }
}