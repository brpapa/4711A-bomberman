
//recebe informações de todos os clientes
public class Receiver extends Thread {
   Player p;

   Player fromWhichPlayerIs(int id) {
      if (id == Client.id)
         return Panel.you;
      else if (id == (Client.id+1)%Const.qtePlayers)
         return Panel.enemy1;
      else if (id == (Client.id+2)%Const.qtePlayers)
         return Panel.enemy2;
      return Panel.enemy3;
   }

   public void run() {
      String str;
      while (Client.in.hasNextLine()) {
         this.p = fromWhichPlayerIs(Client.in.nextInt()); // id do cliente
         
         str = Client.in.next();
         if (str.equals("newCoordinate")) {
            p.x = Client.in.nextInt();
            p.y = Client.in.nextInt();
            p.panel.repaint();
         }
         else if (str.equals("newStatus")) {
            p.thStatus.setStatus(Client.in.next());
         }
         else if (str.equals("stopStatusUpdate")) {
            p.thStatus.stopStatusUpdate();
         }
         else if (str.equals("newBomb")) {
            
            p.panel.repaint();
         }
      }
      Client.in.close();
   }
}