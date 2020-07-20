package me.Oxygen.modules.player;

import me.Oxygen.Oxygen;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

@ModuleRegister(name = "Teams", category = Category.PLAYER)
public class Teams extends Module {
	
	public final static boolean isOnSameTeam(final Entity entity) {
		if(Oxygen.INSTANCE.ModMgr.getModule(Teams.class).isEnabled()) {
			if(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("\247")) {
				if(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2
	                    || entity.getDisplayName().getUnformattedText().length() <= 2) {
					return false;
				}
				if(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
	                return true;
	            }
			}
		}
		return false;
	}
	
	/*public static boolean isInYourTeam(EntityLivingBase entity) {
        //mc.thePlayer ?: return false;

        if (scoreboardValue.get() &&(Minecraft.getMinecraft().thePlayer.team != null && entity.team != null &&
        		(Minecraft.getMinecraft().thePlayer.team.isSameTeam(entity.team));
            return true;

        if(armorColorValue.get()){
            val entityPlayer = entity as EntityPlayer
            if(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3] != null && entityPlayer.inventory.armorInventory[3] != null){
                val myHead = Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3]
                val myItemArmor = myHead.item as ItemArmor


                val entityHead = entityPlayer.inventory.armorInventory[3]
                var entityItemArmor = myHead.item as ItemArmor

                if(myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead)){
                    return true;
                }
            }
        }

        if (colorValue.get() && Minecraft.getMinecraft().thePlayer.displayName != null && entity.displayName != null) {
            val targetName = entity.displayName.formattedText.replace("§r", "");
            val clientName = Minecraft.getMinecraft().thePlayer.displayName.formattedText.replace("§r", "");
            return targetName.startsWith("§${clientName[1]}");
        }



        return false;
    }*/

}
