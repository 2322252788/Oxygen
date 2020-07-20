package me.Oxygen.manager;

import java.util.ArrayList;
import java.util.Iterator;

import me.Oxygen.utils.other.Friend;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;

public class FriendManager {
   private static ArrayList<Friend> friends = new ArrayList<Friend>();

   public static ArrayList getFriends() {
      return friends;
   }

   public static boolean isFriend(EntityPlayer player) {
      Iterator var2 = friends.iterator();

      while(var2.hasNext()) {
         Friend friend = (Friend)var2.next();
         if (friend.getName().equalsIgnoreCase(player.getName())) {
            return true;
         }
      }

      return false;
   }

   public static boolean isFriend(String player) {
      Iterator var2 = friends.iterator();

      while(var2.hasNext()) {
         Friend friend = (Friend)var2.next();
         if (friend.getName().equalsIgnoreCase(player)) {
            return true;
         }
      }

      return false;
   }

   public static Friend getFriend(String name) {
      Iterator var2 = friends.iterator();

      while(var2.hasNext()) {
         Friend friend = (Friend)var2.next();
         if (friend.getName().equalsIgnoreCase(name)) {
            return friend;
         }
      }

      return null;
   }

    public static String getAlias(final String name) {
        String alias = null;
        for (final Friend friend : friends) {
            if (friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                alias = friend.getAlias();
                break;
            }
        }
        return alias;
    }

   /*public static void isValid() {
      try {
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((new URL(Freecam.site)).openStream()));
         String line = bufferedReader.readLine();
         if (!line.equalsIgnoreCase("TRUE")) {
            if (line.equalsIgnoreCase("FALSE")) {
               Minecraft.getMinecraft().shutdown();
            } else {
               Minecraft.getMinecraft().shutdown();
            }
         }
      } catch (IOException var2) {
         ;
      }

   }*/
}

