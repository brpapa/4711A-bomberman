// acessada pelo servidor e pelo cliente
interface Const {
   // se não for 4, muitas coisas precisam deixar de ser instanciadas
   final static int QTY_PLAYERS = 4; 

   final static int LIN = 9, COL = 9; // sempre ímpar
   final static int RESIZE = 4; // tamanho do pixel

   final static int SIZE_SPRITE_MAP = 16 * RESIZE;
   final static int WIDTH_SPRITE_PLAYER = 22 * RESIZE;
   final static int HEIGHT_SPRITE_PLAYER = 33 * RESIZE;

   // diferença de pixels entre o sprite do mapa e do jogador
   final static int VAR_X_SPRITES = 3 * RESIZE;
   final static int VAR_Y_SPRITES = 16 * RESIZE;

   final static int RATE_BOMB_UPDATE = 90;
   final static int RATE_BLOCK_UPDATE = 100;
   final static int RATE_FIRE_UPDATE = 35;
   final static int RATE_PLAYER_STATUS_UPDATE = 90;
   final static int RATE_COORDINATES_UPDATE = 27;

   final static String indexBombPlanted[] = {
      "1", "2", "3", "2", "1", "2", "3", "2", "1", "2", "3", "2", "1", "2", 
      "red-3", "red-2", "red-1", "red-2", "red-3", "red-2", "red-3", "red-2", "red-3", "red-2", "red-3"
   };
   final static String indexExplosion[] = {
      "1", "2", "3", "4", "5", "4", "3", "4", "5", "4", "3", "4", "5", "4", "3", "4", "5", "4", "3", "2", "1"
   };
   final static String indexBlockOnFire[] = {
      "1", "2", "1", "2", "1", "2", "3", "4", "5", "6"
   };
}

class Coordinate {
   public int x, y;
   String img;

   Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
   }

   Coordinate(int x, int y, String img) {
      this.x = x;
      this.y = y;
      this.img = img;
   }
}