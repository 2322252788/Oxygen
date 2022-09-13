package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.events.AttackEvent;
import cn.rainbow.oxygen.module.modules.combat.Reach;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Shadow
    private WorldSettings.GameType currentGameType;

    @Inject(method = "attackEntity",at = @At("HEAD"))
    private void attackEntity(EntityPlayer p_attackEntity_1_, Entity p_attackEntity_2_, CallbackInfo ci) {
        new AttackEvent(p_attackEntity_2_).call();
    }

    @Inject(method = "getBlockReachDistance", at = @At("HEAD"), cancellable = true)
    private void getBlockReachDistance(CallbackInfoReturnable<Float> cir) {
        if (Oxygen.INSTANCE.moduleManager.getModule("Reach").getEnabled()) {
            cir.setReturnValue(Reach.getReach() + 1.5f);
        }
    }

}
