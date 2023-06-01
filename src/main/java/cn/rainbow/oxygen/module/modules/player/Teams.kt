package cn.rainbow.oxygen.module.modules.player

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import net.minecraft.client.Minecraft

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor

@ModuleInfo(name = "Teams", category = Category.Player)
class Teams : Module() {

    companion object {
        private val armor = BooleanValue("Armor", false)

        @JvmStatic
        fun isOnSameTeam(entity: Entity): Boolean {
            val mc = Minecraft.getMinecraft()

            if (mc.thePlayer != null && !Oxygen.INSTANCE.moduleManager.getModule(Teams::class.java)!!.enabled)
                return false

            if (armor.currentValue) {
                if (entity is EntityPlayer && mc.thePlayer.inventory.armorInventory[3] != null &&
                    entity.inventory.armorInventory[3] != null) {
                    val myHead = mc.thePlayer.inventory.armorInventory[3]
                    val myItemArmor = myHead.item as ItemArmor
                    val entityHead = entity.inventory.armorInventory[3]
                    val entityItemArmor = myHead.item as ItemArmor
                    return myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead)
                }
            }
            if (mc.thePlayer.displayName.unformattedText.startsWith("\u00a7")) {
                if (mc.thePlayer.displayName.unformattedText.length <= 2 ||
                    entity.displayName.unformattedText.length <= 2) {
                    return false
                }
                if (mc.thePlayer.displayName.unformattedText.substring(0, 2) ==
                    entity.displayName.unformattedText.substring(0, 2)) {
                    return true
                }
            }
            return false
        }
    }

}