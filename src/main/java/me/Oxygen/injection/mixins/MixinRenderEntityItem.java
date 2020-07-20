package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.Oxygen.Oxygen;
import me.Oxygen.modules.render.ItemPhysic;
import me.Oxygen.utils.render.ClientPhysic;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;

@Mixin(RenderEntityItem.class )
public class MixinRenderEntityItem {
	
	public MixinRenderEntityItem() {
	}

	@Inject(method = {"doRender"}, at = {@At("HEAD")}, cancellable = true)
	public void doRender(final EntityItem entity,final double x,final double y,final double z,final float entityYaw,final float partialTicks, CallbackInfo ci)
    {
    	if(Oxygen.INSTANCE.ModMgr.getModule(ItemPhysic.class).isEnabled() & entity instanceof EntityItem) {
    		ci.cancel();
    		ClientPhysic.doRenderItemPhysic(entity, x, y, z, entityYaw, partialTicks);
    		
    	}
        
    }
	
}
