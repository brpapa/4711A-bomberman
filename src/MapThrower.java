import java.io.PrintStream;

//thread que lança mudanças graduais no mapa que ocorrem logo após a bomba ser plantada
class MapThrower extends Thread {
   boolean bombPlanted;
   int id, l, c;

   MapThrower(int id) {
      this.id = id;
      this.bombPlanted = false;
   }

   void setBombPlanted(int x, int y) {
      x += Const.widthPlayer / 2; // x + 1/2 da largura do personagem
      y += 2 * Const.heightPlayer / 3; // y + 2/3 da altura do personagem

      this.c = x / Const.sizeGrid;
      this.l = y / Const.sizeGrid;
      this.bombPlanted = true;
   }

   void changeMap(String keyWord, int l, int c) {
      Const.map[l][c].img = keyWord; //atualiza mapa do servidor
      for (PrintStream outClient : ClientManager.listOutClients) //notifica todos os clientes
         outClient.println(this.id + " mapUpdate " + keyWord + " " + l + " " + c);
   }

   public void run() {
      String indexBombPlanted[] = {
         "1", "2", "3", "2", "1", "2", "3", "2", "1", "2", "3", "2", "1", "2", 
         "red-3", "red-2", "red-1", "red-2", "red-3", "red-2", "red-3", "red-2", "red-3"
      };

      while (true) {
         if (bombPlanted) {
            for (String index: indexBombPlanted) {
               changeMap("bomb-planted-" + index, l, c);
               try {sleep(Const.rateMapUpdate);} catch (InterruptedException e) {}
            }

            //efeitos da explosão
            for (int i = 1; i <= 5; i++) {
               changeMap("center-explosion-" + i, l, c);
               
               if (Const.map[l+1][c].img.equals("floor-1") || Const.map[l+1][c].img.contains("down-explosion")) {
                  changeMap("down-explosion-" + i, l+1, c);

                  //verifica se o fogo matou alguém
                  for (int id = 0; id < Const.qtePlayers; id++)
                     if (Const.map[l+1][c].playerOn[id] == true)
                        for (PrintStream outClient : ClientManager.listOutClients)
                           outClient.println(this.id + " playerDead");
               }
               else if (Const.map[l+1][c].img.contains("block"))
                  changeMap("block-on-fire-" + i, l+1, c);
                  
               if (Const.map[l][c+1].img.equals("floor-1") || Const.map[l][c+1].img.contains("right-explosion")) {
                  changeMap("right-explosion-" + i, l, c+1);

                  // verifica se o fogo matou alguém
                  for (int id = 0; id < Const.qtePlayers; id++)
                     if (Const.map[l + 1][c].playerOn[id] == true)
                        for (PrintStream outClient : ClientManager.listOutClients)
                           outClient.println(this.id + " playerDead");
               }
               else if (Const.map[l][c+1].img.contains("block"))
                  changeMap("block-on-fire-" + i, l, c+1);
               
               if (Const.map[l-1][c].img.equals("floor-1") || Const.map[l-1][c].img.contains("up-explosion")) {
                  changeMap("up-explosion-" + i, l-1, c);

                  // verifica se o fogo matou alguém
                  for (int id = 0; id < Const.qtePlayers; id++)
                     if (Const.map[l + 1][c].playerOn[id] == true)
                        for (PrintStream outClient : ClientManager.listOutClients)
                           outClient.println(this.id + " playerDead");
               }
               else if (Const.map[l-1][c].img.contains("block"))
                  changeMap("block-on-fire-" + i, l-1, c);
                     
               if (Const.map[l][c-1].img.equals("floor-1") || Const.map[l][c-1].img.contains("left-explosion")) {
                  changeMap("left-explosion-" + i, l, c-1);

                  // verifica se o fogo matou alguém
                  for (int id = 0; id < Const.qtePlayers; id++)
                     if (Const.map[l + 1][c].playerOn[id] == true)
                        for (PrintStream outClient : ClientManager.listOutClients)
                           outClient.println(this.id + " playerDead");
               }
               else if (Const.map[l][c-1].img.contains("block"))
                  changeMap("block-on-fire-" + i, l, c-1);

               try {sleep(Const.rateMapUpdate);} catch (InterruptedException e) {}
            }

            //libera bomba
            bombPlanted = false;
            for (PrintStream outClient : ClientManager.listOutClients)
               outClient.println(this.id + " " + "oneMoreBombAvailable");

            //situação após explosão, abre caminho se parede não é fixa
            if (!Const.map[l][c].img.contains("wall"))
               changeMap("floor-1", l, c);
            if (!Const.map[l+1][c].img.contains("wall"))
               changeMap("floor-1", l+1, c);
            if (!Const.map[l][c+1].img.contains("wall"))
               changeMap("floor-1", l, c+1);
            if (!Const.map[l-1][c].img.contains("wall"))
               changeMap("floor-1", l-1, c);
            if (!Const.map[l][c-1].img.contains("wall"))
               changeMap("floor-1", l, c-1);
         }
         try {sleep(0);} catch (InterruptedException e) {}
      }
   }
}