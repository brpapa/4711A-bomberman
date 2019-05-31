
//recebe informações de todos os clientes
public class Receiver extends Thread {
   Player p;

   Player fromWhichPlayerIs(int id) {
      if (id == Client.id)
         return Game.you;
      return Game.enemy;
   }

   public void run() {
      while (Client.in.hasNextLine()) {
         // System.out.println(Client.in.nextLine());
         // /*
         this.p = fromWhichPlayerIs(Client.in.nextInt()); // id do cliente
         
         if (Client.in.next().equals("newCoordinate")) {
            p.x = Client.in.nextInt();
            p.y = Client.in.nextInt();
            p.panel.repaint();
         }
         // */
      }
      Client.in.close();
   }
}