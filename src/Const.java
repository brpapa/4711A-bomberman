//constantes usadas no lado do servidor
class Const {
   final static int qtePlayers = 4;

   final static int LIN = 9, COL = 11; // SEMPRE IMPAR

   final static int RESIZE = 4;
   final static int sizeGrid = 16 * RESIZE;
   final static int widthPlayer = 22 * RESIZE;
   final static int heightPlayer = 33 * RESIZE;
   // diferença entre img do mapa e do personagem
   final static int varX = 3 * RESIZE;
   final static int varY = 16 * RESIZE;

   final static int rateMapUpdate = 125;
   final static int ratePlayerStatusUpdate = 115;
   final static int rateCoordinatesUpdate = 27;

   final static Coordinate spawn[] = new Coordinate[4];
   static void setSpawnCoordinates() {
      spawn[0] = map[1][1];
      spawn[1] = map[LIN-2][COL-2];
      spawn[2] = map[LIN-2][1];
      spawn[3] = map[1][COL-2];
   }

   static Coordinate map[][] = new Coordinate[LIN][COL];
   static void initMap() {
      for (int i = 0; i < LIN; i++)
         for (int j = 0; j < COL; j++)
            map[i][j] = new Coordinate(sizeGrid * j, sizeGrid * i, "block");
            // map[i][j] = new Coordinate(sizeGrid * j, sizeGrid * i, "floor-1");
      
      //paredes fixas das bordas
      for (int j = 1; j < COL-1; j++) {
         map[0][j].img = "wall-center";
         map[LIN-1][j].img = "wall-center";
      }
      for (int i = 1; i < LIN-1; i++) {
         map[i][0].img = "wall-center";
         map[i][COL-1].img = "wall-center";
      }
      map[0][0].img = "wall-up-left";
      map[0][COL-1].img = "wall-up-right";
      map[LIN-1][0].img = "wall-down-left";
      map[LIN-1][COL-1].img = "wall-down-right";

      //paredes fixas centrais
      for (int i = 2; i < LIN-2; i++)
         for (int j = 2; j < COL-2; j++)
            if (i % 2 == 0 && j % 2 == 0)
               map[i][j].img = "wall-center";

      //arredores do spawn
      map[1][1].img = "floor-1";
      map[1][2].img = "floor-1";
      map[2][1].img = "floor-1";
      map[LIN-2][COL-2].img = "floor-1";
      map[LIN-3][COL-2].img = "floor-1";
      map[LIN-2][COL-3].img = "floor-1";
      map[LIN-2][1].img = "floor-1";
      map[LIN-3][1].img = "floor-1";
      map[LIN-2][2].img = "floor-1";
      map[1][COL-2].img = "floor-1";
      map[2][COL-2].img = "floor-1";
      map[1][COL-3].img = "floor-1";
   }
}