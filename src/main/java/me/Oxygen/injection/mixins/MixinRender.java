package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

@Mixin({ Render.class })
public class MixinRender<T extends Entity>
{
    @Shadow
    protected RenderManager renderManager;
    
    /*@Inject(method = "renderLivingLabel", at = @At("HEAD"))
	private void eventNametags(T entityIn, String str, double x, double y, double z, int maxDistance, CallbackInfo callbackinfo) {
		if (Client.INSTANCE.ModMgr.getModule(Nametags.class).isEnabled()) {
			return;
		}
	}*/
    
    @Shadow
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
    }
}
