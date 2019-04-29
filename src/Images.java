package src;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class Images {
   public final static int RESIZE = 3;
   public static Hashtable<String, Image> ht = new Hashtable<String, Image>();

   static final String mapKeyWords[] = { "background", "block-on-fire-1", "block-on-fire-2", "block-on-fire-3",
         "block-on-fire-4", "block-on-fire-5", "block-on-fire-6", "block", "bomb-1", "bomb-2", "bomb-planted-loop-1",
         "bomb-planted-loop-2", "bomb-planted-loop-3", "bomb-planted-loop-4", "bomb-red-1", "bomb-red-2",
         "bomb-red-loop-1", "bomb-red-loop-2", "bomb-red-loop-3", "bomb-red-loop-4", "center-explosion-1",
         "center-explosion-2", "center-explosion-3", "center-explosion-4", "center-explosion-5", "down-explosion-1",
         "down-explosion-2", "down-explosion-3", "down-explosion-4", "down-explosion-5", "floor-1", "floor-2",
         "item-destruction-1", "item-destruction-2", "item-destruction-3", "item-destruction-4", "item-destruction-5",
         "item-destruction-6", "item-destruction-7", "left-explosion-1", "left-explosion-2", "left-explosion-3",
         "left-explosion-4", "left-explosion-5", "loop-center-explosion-4", "loop-center-explosion-5",
         "mid-hori-explosion-1", "mid-hori-explosion-2", "mid-hori-explosion-3", "mid-hori-explosion-4",
         "mid-hori-explosion-5", "mid-vert-explosion-1", "mid-vert-explosion-2", "mid-vert-explosion-3",
         "mid-vert-explosion-4", "mid-vert-explosion-5", "right-explosion1", "right-explosion2", "right-explosion3",
         "right-explosion4", "right-explosion5", "up-explosion-1", "up-explosion-2", "up-explosion-3", "up-explosion-4",
         "up-explosion-5", "wall-center", "wall-down-left", "wall-down-right", "wall-up-left", "wall-up-right" };

   static final String personKeyWords[] = { "dead-0", "dead-1", "dead-2", "dead-3", "dead-4", "down-0", "down-1",
      "down-2", "down-3", "down-4", "down-5", "down-6", "down-7", "right-0", "right-1", "right-2", "right-3",
      "right-4", "right-5", "right-6", "right-7", "left-0", "left-1", "left-2", "left-3", "left-4", "left-5",
      "left-6", "left-7", "up-0", "up-1", "up-2", "up-3", "up-4", "up-5", "up-6", "up-7", "uhu-0", "uhu-1", "uhu-2",
      "uhu-3", "wait-0", "wait-1", "wait-2", "wait-3", "wait-4", "win-0", "win-1", "win-2", "win-3", "win-4" };
   
   public static void read() {
      try {
         for (String keyWord : mapKeyWords)
            ht.put(keyWord, ImageIO.read(new File("images/map/basic/" + keyWord + ".png")));

         for (String keyWord : personKeyWords)
            ht.put(keyWord, ImageIO.read(new File("images/person/white/" + keyWord + ".png")));
      } catch (IOException e) {
         System.out.println("Erro ao carregar as imagens!");
         System.exit(1);
      }
   }
}