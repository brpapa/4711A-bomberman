import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public class Const {
   final static int RESIZE = 4;
   final static int LIN = 5, COL = 9; //SEMPRE IMPAR
   final static int width = 16 * RESIZE;
   final static int height = 16 * RESIZE;

   final static int rateCoordinatesUpdate = 35;
   final static int rateStatusUpdate = 115;

   final static int widthPlayer = 22 * RESIZE;
   final static int heightPlayer = 33 * RESIZE;
   //diferença entre img do mapa e do personagem
   final static int varX = 3 * RESIZE; 
   final static int varY = 16 * RESIZE;

   final static int qtePlayers = 4;

   final static Coordinate grid[][] = new Coordinate[LIN][COL];
   static void setGrid() {
      for (int i = 0; i < LIN; i++)
         for (int j = 0; j < COL; j++)
            grid[i][j] = new Coordinate(width * j, height * i);
      /*
             0         1         2         3         4         5         6         7         8
      0  [000,000] [064,000] [128,000] [192,000] [256,000] [320,000] [384,000] [448,000] [512,000] 
      1  [000,064] [064,064] [128,064] [192,064] [256,064] [320,064] [384,064] [448,064] [512,064]
      2  [000,128] [064,128] [128,128] [192,128] [256,128] [320,128] [384,128] [448,128] [512,128] 
      3  [000,192] [064,192] [128,192] [192,192] [256,192] [320,192] [384,192] [448,192] [512,192]
      4  [000,256] [064,256] [128,256] [192,256] [256,256] [320,256] [384,256] [448,256] [512,256]
      */
   }

   final static String personColors[] = {
      "white", 
      "black", 
      "red", 
      "yellow"
   };
   final static Coordinate spawn[] = new Coordinate[4];
   static void setSpawnCoordinates() {
      spawn[0] = grid[1][1];
      spawn[1] = grid[LIN-2][COL-2];
      spawn[2] = grid[LIN-2][1];
      spawn[3] = grid[1][COL-2];
   }

   final static Hashtable<String, Image> ht = new Hashtable<String, Image>();
   //COLOCAR NA ORDEM DO SPRITESHEET
   final static String mapKeyWords[] = { 
      "background", 
      "block", 
      "block-on-fire-1", "block-on-fire-2", "block-on-fire-3", "block-on-fire-4", "block-on-fire-5", "block-on-fire-6", 
      "bomb-1", "bomb-2", 
      "bomb-planted-loop-1", "bomb-planted-loop-2", "bomb-planted-loop-3", "bomb-planted-loop-4", 
      "bomb-red-1", "bomb-red-2", 
      "bomb-red-loop-1", "bomb-red-loop-2", "bomb-red-loop-3", "bomb-red-loop-4", 
      "center-explosion-1", "center-explosion-2", "center-explosion-3", "center-explosion-4", "center-explosion-5", 
      "down-explosion-1", "down-explosion-2", "down-explosion-3", "down-explosion-4", "down-explosion-5", 
      "floor-1", "floor-2",
      "item-destruction-1", "item-destruction-2", "item-destruction-3", "item-destruction-4", "item-destruction-5", "item-destruction-6", "item-destruction-7", 
      "left-explosion-1", "left-explosion-2", "left-explosion-3", "left-explosion-4", "left-explosion-5", "loop-center-explosion-4", "loop-center-explosion-5",
      "mid-hori-explosion-1", "mid-hori-explosion-2", "mid-hori-explosion-3", "mid-hori-explosion-4", "mid-hori-explosion-5", 
      "mid-vert-explosion-1", "mid-vert-explosion-2", "mid-vert-explosion-3", "mid-vert-explosion-4", "mid-vert-explosion-5", 
      "right-explosion-1", "right-explosion-2", "right-explosion-3", "right-explosion-4", "right-explosion-5", 
      "up-explosion-1", "up-explosion-2", "up-explosion-3", "up-explosion-4", "up-explosion-5", 
      "wall-center", "wall-down-left", "wall-down-right", "wall-up-left", "wall-up-right" 
   };
   //já está na ordem do spritesheet para usar autoCropAndRename.cpp
   static final String personKeyWords[] = {
      "dead-0", "dead-1", "dead-2", "dead-3", "dead-4", 
      "down-0", "down-1", "down-2", "down-3", "down-4", "down-5", "down-6", "down-7", 
      "left-0", "left-1", "left-2", "left-3", "left-4", "left-5", "left-6", "left-7", 
      "right-0", "right-1", "right-2", "right-3", "right-4", "right-5", "right-6", "right-7", 
      "uhu-0", "uhu-1", "uhu-2", "uhu-3", 
      "up-0", "up-1", "up-2", "up-3", "up-4", "up-5", "up-6", "up-7", 
      "wait-0", "wait-1", "wait-2", "wait-3", "wait-4", 
      "win-0", "win-1", "win-2", "win-3", "win-4"
   };
   
   final static Hashtable<String, Integer> maxLoopStatus = new Hashtable<String, Integer>();
   static void setMaxLoopStatus() {
      maxLoopStatus.put("dead", 5);
      maxLoopStatus.put("down", 8);
      maxLoopStatus.put("left", 8);
      maxLoopStatus.put("right", 8);
      maxLoopStatus.put("uhu", 4);
      maxLoopStatus.put("up", 8);
      maxLoopStatus.put("wait", 5);
      maxLoopStatus.put("win", 5);
   }

   static void readAllImages() {
      try {
         for (String keyWord : mapKeyWords)
            ht.put(keyWord, ImageIO.read(new File("../images/map/basic/"+keyWord+".png")));

         for (String color : personColors)
            for (String keyWord : personKeyWords)
               ht.put(color+"/"+keyWord, ImageIO.read(new File("../images/person/"+color+"/"+keyWord+".png")));
      } catch (IOException e) {
         System.out.println("Erro ao carregar as imagens!");
         System.exit(1);
      }
   }
}

class Coordinate {
   String img;
   public int x, y;

   Coordinate(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public void setX(int x) {
      this.x = x;
   }
   public void setY(int y) {
      this.y = y;
   }
   public int getX() {
      return this.x;
   }
   public int getY() {
      return this.y;
   }
}