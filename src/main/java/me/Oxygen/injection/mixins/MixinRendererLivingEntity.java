package me.Oxygen.injection.mixins;

import java.awt.Color;

import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.Oxygen.Oxygen;
import me.Oxygen.event.events.EventRenderEntity;
import me.Oxygen.event.events.EventRenderEntity.RenderType;
import me.Oxygen.injection.interfaces.IFontRenderer;
import me.Oxygen.manager.FriendManager;
import me.Oxygen.modules.combat.Antibot;
import me.Oxygen.modules.render.ESP;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render{
	
	@Shadow
    protected boolean renderOutlines;
    @Shadow
    protected ModelBase mainModel;
    @Shadow
    @Final
    private static Logger logger;
    
    protected MixinRendererLivingEntity(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
    	super(renderManagerIn);
    	this.mainModel = modelBaseIn;
        this.shadowSize = shadowSizeIn;
        this.renderOutlines = false;
    }
    
    @Inject(method = { "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V" }, at = { @At("HEAD") }, cancellable = true)
    public void onChat(final EntityLivingBase entity, final double x, final double y, final double z, final CallbackInfo ci) {
        if (Oxygen.INSTANCE.ModMgr.getModuleByName("Nametags").isEnabled() && entity instanceof EntityPlayer) {
            ci.cancel();
        }
    }
    
    @Shadow
    protected abstract float interpolateRotation(final float p0, final float p1, final float p2);
    
    @Shadow
    protected abstract float getSwingProgress(final T p0, final float p1);
    
    @Shadow
    protected abstract void renderLivingAt(final T p0, final double p1, final double p2, final double p3);
    
    @Shadow
    protected abstract void rotateCorpse(final T p0, final float p1, final float p2, final float p3);
    
    @Shadow
    protected abstract float handleRotationFloat(final T p0, final float p1);
    
    @Shadow
    protected abstract void preRenderCallback(final T p0, final float p1);
    
    @Shadow
    protected abstract boolean setScoreTeamColor(final EntityLivingBase p0);
    
    @Shadow
    protected abstract void unsetScoreTeamColor();
    
    @Shadow
    protected abstract void renderModel(final T p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    @Shadow
    protected abstract void renderLayers(final T p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    @Shadow
    protected abstract boolean setDoRenderBrightness(final T p0, final float p1);
    
    @Shadow
    protected abstract void unsetBrightness();
	
	@Overwrite
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		EventRenderEntity em = new EventRenderEntity((EntityLivingBase) entity, RenderType.PRE);
		em.call();
		if (em.isCancelled()) return;
		GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        this.mainModel.isRiding = entity.isRiding();
        this.mainModel.isChild = entity.isChild();
        try {
            float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            final float f2 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float f3 = f2 - f;
            if (this.mainModel.isRiding && entity.ridingEntity instanceof EntityLivingBase) {
                final EntityLivingBase entitylivingbase = (EntityLivingBase)entity.ridingEntity;
                f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                f3 = f2 - f;
                float f4 = MathHelper.wrapAngleTo180_float(f3);
                if (f4 < -85.0f) {
                    f4 = -85.0f;
                }
                if (f4 >= 85.0f) {
                    f4 = 85.0f;
                }
                f = f2 - f4;
                if (f4 * f4 > 2500.0f) {
                    f += f4 * 0.2f;
                }
            }
            final float f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            this.renderLivingAt(entity, x, y, z);
            final float f6 = this.handleRotationFloat(entity, partialTicks);
            this.rotateCorpse(entity, f6, f, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0f, -1.0f, 1.0f);
            this.preRenderCallback(entity, partialTicks);
            final float f7 = 0.0625f;
            GlStateManager.translate(0.0f, -1.5078125f, 0.0f);
            float f8 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
            float f9 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
            if (entity.isChild()) {
                f9 *= 3.0f;
            }
            if (f8 > 1.0f) {
                f8 = 1.0f;
            }
            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations((EntityLivingBase)entity, f9, f8, partialTicks);
            this.mainModel.setRotationAngles(f9, f8, f6, f3, f5, 0.0625f, (Entity)entity);
            if (this.renderOutlines) {
                final boolean flag1 = this.setScoreTeamColor(entity);
                this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                if (flag1) {
                    this.unsetScoreTeamColor();
                }
            }
            else {
                final boolean flag2 = this.setDoRenderBrightness(entity, partialTicks);
                this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                boolean valid = entity instanceof EntityMob || entity instanceof EntityIronGolem ||
						entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityPlayer;
                
                if(valid){
                    ESP espMod = (ESP) Oxygen.INSTANCE.ModMgr.getModule(ESP.class);
                	boolean esp = espMod.isEnabled();
            		String mode = espMod.MODE.getModeAt(espMod.MODE.getCurrentMode());
            	   	if(esp && (mode.equalsIgnoreCase("CSGO") || mode.equalsIgnoreCase("Fill"))){
                    	if(ESP.isValid(entity)){
                       		Minecraft mc = Minecraft.getMinecraft();
                    		String colorMode =  espMod.COLORMODE.getModeAt(espMod.COLORMODE.getCurrentMode());
                    		int renderColor = new Color(255, 255, 255).getRGB();
                    		switch(colorMode){
     	                    case"Rainbow":{
     	                    	final Color color = Color.getHSBColor(ESP.h / 255.0f, 0.8f, 1.0f);
     	            			final int c = color.getRGB();
     	                    	renderColor = c;
     	                    }
     	                    break;
     	                    case"Team":{
     	                    String text = entity.getDisplayName().getFormattedText();
     	                	if(Character.toLowerCase(text.charAt(0)) == '搂'){
     	                		
     	                    	char oneMore = Character.toLowerCase(text.charAt(1));
     	                    	int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
     	                    	
     	                    	if (colorCode < 16) {
     	                            try {
     	                                int newColor = ((IFontRenderer)mc.fontRendererObj).setColorCode(colorCode);   
     	                                 renderColor = Colors.getColor((newColor >> 16), (newColor >> 8 & 0xFF), (newColor & 0xFF), 255);
     	                            } catch (ArrayIndexOutOfBoundsException ignored) {
     	                            }
     	                        }
     	                	}else{
     	                		renderColor = Colors.getColor(255, 255, 255, 255);
     	                	}
     	                    }
     	                    break;
     	                    case"Health":{
     	                    	float health = entity.getHealth();
     	                        if (health > 20) {
     	                            health = 20;
     	                        }
     	                        float[] fractions = new float[]{0f, 0.5f, 1f};
     	                        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
     	                        float progress = (health * 5) * 0.01f;
     	                        Color customColor = ESP.blendColors(fractions, colors, progress).brighter();
     	                        renderColor = customColor.getRGB();
     	                    }
     	                    break;
     	                    case"Custom":
     	                    	renderColor = new Color(255,255,255).getRGB();
     	                    	break;
     	                    }        
     	                    if(entity.hurtResistantTime > 15 && colorMode.equalsIgnoreCase("Fill")){
     	                    	renderColor = Colors.getColor(1, 0, 0, 1);
     	                    }
     	                    if(Antibot.invalid.contains(entity)){
     	                    	renderColor = Colors.getColor(100,100,100,255);
     	                    }
     	                    if(FriendManager.isFriend(entity.getName()) && !(entity instanceof EntityPlayerSP)){
     	                    	renderColor = Colors.getColor(0,195,255,255);
     	                    }                     
                        	mc.entityRenderer.disableLightmap();
                        	RenderUtil.glColor(renderColor);
                    		GL11.glPushMatrix();
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                            if(!mode.equalsIgnoreCase("CSGO")){
                           	 	RenderHelper.disableStandardItemLighting();
                            }
                            
                        	GL11.glEnable(32823);
                            GL11.glPolygonOffset(1.0f, -3900000.0f);
                            this.renderModel(entity, f6, f5, f7, f2, f5, 0.0625F);
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glEnable(GL11.GL_LIGHTING);
                            GL11.glEnable(GL11.GL_DEPTH_TEST);
                            if(!mode.equalsIgnoreCase("CSGO")){
                                GlStateManager.enableLighting();
                                GlStateManager.enableLight(0);
                                GlStateManager.disableLight(1);
                                GlStateManager.enableColorMaterial();
                            }
                            GL11.glPopMatrix();
                            mc.entityRenderer.disableLightmap();
                            RenderUtil.glColor(-1);
                          
                    	}
                	}
            	}
                
                if (flag2) {
                    this.unsetBrightness();
                }
                GlStateManager.depthMask(true);
                if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                    this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, 0.0625f);
                }
            }
            GlStateManager.disableRescaleNormal();
        }
        catch (Exception exception) {
            MixinRendererLivingEntity.logger.error("Couldn't render entity", (Throwable)exception);
        }
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        if (!this.renderOutlines) {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
        EventRenderEntity emm = new EventRenderEntity((EntityLivingBase) entity, RenderType.POST);
		emm.call();
	}

}
