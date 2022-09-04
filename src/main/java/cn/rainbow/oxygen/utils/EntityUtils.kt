package cn.rainbow.oxygen.utils

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.module.modules.combat.AntiBot
import cn.rainbow.oxygen.module.modules.client.TargetEntity
import cn.rainbow.oxygen.module.modules.player.Teams
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.monster.EntityGhast
import net.minecraft.entity.monster.EntityGolem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.monster.EntitySlime
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityBat
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer

object EntityUtils {

    @JvmStatic
    fun isSelected(entity: Entity, canAttackCheck: Boolean): Boolean {
        val ent = Oxygen.INSTANCE.moduleManager.getModule(TargetEntity::class.java) as TargetEntity
        val mc = Minecraft.getMinecraft()
        if (entity is EntityLivingBase && (ent.dead.currentValue || entity.isEntityAlive()) && entity !== mc.thePlayer) {
            if (ent.invis.currentValue || !entity.isInvisible()) {
                if (ent.player.currentValue && entity is EntityPlayer) {
                    if (canAttackCheck) {
                        if (AntiBot.isBot(entity)) {
                            return false
                        }

                        if (entity.isSpectator) {
                            return false
                        }

                        if (entity.isPlayerSleeping) {
                            return false
                        }

                        return !Teams.isOnSameTeam(entity)
                    }
                    return true
                }
                return ent.mobs.currentValue && isMob(entity) || ent.animals.currentValue && isAnimal(entity)
            }
        }
        return false
    }

    fun isAnimal(entity: Entity): Boolean {
        return entity is EntityAnimal || entity is EntitySquid || entity is EntityGolem || entity is EntityVillager || entity is EntityBat
    }

    fun isMob(entity: Entity): Boolean {
        return entity is EntityMob || entity is EntitySlime || entity is EntityGhast || entity is EntityDragon
    }
}