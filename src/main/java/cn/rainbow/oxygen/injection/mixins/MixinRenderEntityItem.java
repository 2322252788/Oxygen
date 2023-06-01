package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.module.modules.render.ItemPhysic;
import cn.rainbow.oxygen.utils.ClientPhysic;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEntityItem.class )
public class MixinRenderEntityItem {

	@Inject(method = "doRender*", at = {@At("HEAD")}, cancellable = true)
	public void doRender(final EntityItem entity,final double x,final double y,final double z,final float entityYaw,final float partialTicks, CallbackInfo ci)
    {
    	if(Oxygen.INSTANCE.moduleManager.getModule(ItemPhysic.class).getEnabled()) {
    		ci.cancel();
    		ClientPhysic.doRenderItemPhysic(entity, x, y, z, entityYaw, partialTicks);
    	}
    }
}
