package me.Oxygen.modules.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventPriority;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventRender2D;
import me.Oxygen.event.events.EventRender3D;
import me.Oxygen.injection.interfaces.IEntityRenderer;
import me.Oxygen.injection.interfaces.IMinecraft;
import me.Oxygen.injection.interfaces.IRenderManager;
import me.Oxygen.manager.FriendManager;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.combat.Antibot;
import me.Oxygen.modules.player.Teams;
import me.Oxygen.utils.fontRenderer.UnicodeFontRenderer;
import me.Oxygen.utils.render.ChatColors;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import me.Oxygen.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MathHelper;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@ModuleRegister(name = "Nametags", category = Category.RENDER)
public class Nametags extends Module
{
	private Map<EntityLivingBase, double[]> entityPositions = new HashMap<EntityLivingBase, double[]>();
	private Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();
	public boolean formatting = true;
	private boolean armors = true;
	
    private final Value<String> mode = new Value<String>("NameTags" , "Mode",0);
    private final Value<Boolean> clearname = new Value<Boolean>("Nametags_ClearName", false);
    private final Value<Boolean> invis = new Value<Boolean>("Nametags_Invisibles", false);
    private final Value<Boolean> armor = new Value<Boolean>("Nametags_Armor", true);
    
    public Nametags() {
    	this.mode.mode.add("Old");
    	this.mode.mode.add("New");
    }
    
    @EventTarget(
    		priority = EventPriority.MEDIUM,
    		events = {EventRender3D.class, EventRender2D.class}
    )
    private final void onEvent(Event event) {
    	if(event instanceof EventRender3D){
    	if(this.mode.isCurrentMode("New")) {
        try {
            this.updatePositions();
        }
        catch (Exception ex) {}
    	} else if(this.mode.isCurrentMode("Old")) {
    		Iterator<?> var3 = this.mc.theWorld.playerEntities.iterator();
    	      while(var3.hasNext()) {
    	         Object o = var3.next();
    	         EntityPlayer p = (EntityPlayer)o;
    	         if (p != this.mc.thePlayer) {
    	            double var10000 = p.lastTickPosX + (p.posX - p.lastTickPosX) * (double)((IMinecraft)this.mc).getTimer().renderPartialTicks;
    	            this.mc.getRenderManager();
    	            double pX = var10000 - ((IRenderManager)this.mc.getRenderManager()).getRenderPosX();
    	            var10000 = p.lastTickPosY + (p.posY - p.lastTickPosY) * (double)((IMinecraft)this.mc).getTimer().renderPartialTicks;
    	            this.mc.getRenderManager();
    	            double pY = var10000 - ((IRenderManager)this.mc.getRenderManager()).getRenderPosY();
    	            var10000 = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * (double)((IMinecraft)this.mc).getTimer().renderPartialTicks;
    	            this.mc.getRenderManager();
    	            double pZ = var10000 - ((IRenderManager)this.mc.getRenderManager()).getRenderPosZ();
    	            this.renderNameTag(p, String.valueOf(p.getDisplayName()), pX, pY, pZ);
    	         }
    	      }
    	}
    	}
    	
    	if(event instanceof EventRender2D) {
    		if(this.mode.isCurrentMode("New")) {
    	        GlStateManager.pushMatrix();
    	        final ScaledResolution scaledResolution = new ScaledResolution(mc);
    	        for (final Entity entity : entityPositions.keySet()) {
    	            if (entity != mc.thePlayer && (this.invis.getValueState() || !entity.isInvisible())) {
    	                GlStateManager.pushMatrix();
    	                if (entity instanceof EntityPlayer) {
    	                    final double[] array = entityPositions.get(entity);
    	                    if (array[3] < 0.0 || array[3] >= 1.0) {
    	                        GlStateManager.popMatrix();
    	                        continue;
    	                    }
    	                    final UnicodeFontRenderer wqy18 = Oxygen.INSTANCE.font.wqy18;
    	                    GlStateManager.translate(array[0] / scaledResolution.getScaleFactor(), array[1] / scaledResolution.getScaleFactor(), 0.0);
    	                    this.scale();
    	                    GlStateManager.translate(0.0, -2.5, 0.0);
    	                    final String string = "Health: " + String.valueOf(Math.round(((EntityLivingBase)entity).getHealth() * 10.0f) / 10);
    	                    final String string2 = (FriendManager.isFriend(entity.getName()) ? "\u00a7e[FRIEND]" : "") +  (Antibot.isBot(entity) ? "§9[BOT]" : "") + (Teams.isOnSameTeam(entity) ? "§b[TEAM]" : "") + "§r" + entity.getDisplayName().getUnformattedText();
    	                    final String string4 = string2;
    	                    final float n = wqy18.getStringWidth(string4.replaceAll("§.", ""));
    	                    final float n2 = Oxygen.INSTANCE.font.comfortaa12.getStringWidth(string);
    	                    final float n3 = ((n > n2) ? n : n2) + 8.0f;
    	                    RenderUtil.drawRect(-n3 / 2.0f, -25.0f, n3 / 2.0f, 0.0f, Colors.getColor(0, 130));
    	                    scaledResolution.getScaledHeight();
    	                    scaledResolution.getScaledWidth();
    	                    wqy18.drawStringWithColor(string4, -n3 / 2.0f + 4.0f, -22.0f, Colors.WHITE.c);
    	                    Oxygen.INSTANCE.font.comfortaa12.drawString(string, -n3 / 2.0f + 4.0f, -10.0f, Colors.WHITE.c);
    	                    final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
    	                    RenderUtil.drawRect(-n3 / 2.0f, -1.0f, n3 / 2.0f - n3 / 2.0f * (1.0f - (float)Math.ceil(entityLivingBase.getHealth() + entityLivingBase.getAbsorptionAmount()) / (entityLivingBase.getMaxHealth() + entityLivingBase.getAbsorptionAmount())) * 2.0f, 0.0f, Colors.RED.c);
    	                    if (this.armor.getValueState()) {
    	                        final ArrayList<ItemStack> list = new ArrayList<ItemStack>();
    	                        for (int i = 0; i < 5; ++i) {
    	                            final ItemStack getEquipmentInSlot = ((EntityPlayer)entity).getEquipmentInSlot(i);
    	                            if (getEquipmentInSlot != null) {
    	                                list.add(getEquipmentInSlot);
    	                            }
    	                        }
    	                        int n10 = -(list.size() * 9);
    	                        for (final ItemStack itemStack : list) {
    	                            RenderHelper.enableGUIStandardItemLighting();
    	                            mc.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -42);
    	                            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, n10, -42);
    	                            n10 += 3;
    	                            RenderHelper.disableStandardItemLighting();
    	                            if (itemStack != null) {
    	                                int n11 = 21;
    	                                final int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
    	                                final int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack);
    	                                final int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, itemStack);
    	                                if (getEnchantmentLevel > 0) {
    	                                    this.drawEnchantTag("Sh" + this.getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
    	                                    n11 += 6;
    	                                }
    	                                if (getEnchantmentLevel2 > 0) {
    	                                    this.drawEnchantTag("Fir" + this.getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
    	                                    n11 += 6;
    	                                }
    	                                if (getEnchantmentLevel3 > 0) {
    	                                    this.drawEnchantTag("Kb" + this.getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
    	                                }
    	                                else if (itemStack.getItem() instanceof ItemArmor) {
    	                                    final int getEnchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);
    	                                    final int getEnchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack);
    	                                    final int getEnchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack);
    	                                    if (getEnchantmentLevel4 > 0) {
    	                                        this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel4) + getEnchantmentLevel4, n10, n11);
    	                                        n11 += 6;
    	                                    }
    	                                    if (getEnchantmentLevel5 > 0) {
    	                                        this.drawEnchantTag("Th" + this.getColor(getEnchantmentLevel5) + getEnchantmentLevel5, n10, n11);
    	                                        n11 += 6;
    	                                    }
    	                                    if (getEnchantmentLevel6 > 0) {
    	                                        this.drawEnchantTag("Unb" + this.getColor(getEnchantmentLevel6) + getEnchantmentLevel6, n10, n11);
    	                                    }
    	                                }
    	                                else if (itemStack.getItem() instanceof ItemBow) {
    	                                    final int getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack);
    	                                    final int getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack);
    	                                    final int getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack);
    	                                    if (getEnchantmentLevel7 > 0) {
    	                                        this.drawEnchantTag("Pow" + this.getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
    	                                        n11 += 6;
    	                                    }
    	                                    if (getEnchantmentLevel8 > 0) {
    	                                        this.drawEnchantTag("Pun" + this.getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
    	                                        n11 += 6;
    	                                    }
    	                                    if (getEnchantmentLevel9 > 0) {
    	                                        this.drawEnchantTag("Fir" + this.getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
    	                                    }
    	                                }
    	                                else if (itemStack.getRarity() == EnumRarity.EPIC) {
    	                                    this.drawEnchantTag("§6§lGod", n10 - 2, n11);
    	                                }
    	                                final int n12 = (int)Math.round(255.0 - itemStack.getItemDamage() * 255.0 / itemStack.getMaxDamage());
    	                                new Color(255 - n12 << 16 | n12 << 8).brighter();
    	                                if (itemStack.getMaxDamage() - itemStack.getItemDamage() > 0) {
    	                                    GlStateManager.pushMatrix();
    	                                    GlStateManager.disableDepth();
    	                                    GlStateManager.enableDepth();
    	                                    GlStateManager.popMatrix();
    	                                }
    	                                n10 += 12;
    	                            }
    	                        }
    	                    }
    	                }
    	                GlStateManager.popMatrix();
    	            }
    	        }
    	        GlStateManager.popMatrix();
    	    	}
    	}
    }
    
    private final void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
        FontRenderer var12 = this.mc.fontRendererObj;
        pY += entity.isSneaking() ? 0.5D : 0.7D;
        float var13 = this.mc.thePlayer.getDistanceToEntity(entity) / 6.0F;
        if (var13 < 1.2F) {
           var13 = 1.2F;
        }

        int colour = 16777215;
        if (entity.isInvisible()) {
           colour = 16756480;
        } else if (entity.isSneaking()) {
           colour = 11468800;
        }

        if (!this.formatting) {
           tag = ChatColors.stripColor(tag);
        }

        if (!this.clearname.getValueState()) {
           tag = entity.getDisplayName().getFormattedText();
        } else {
           tag = entity.getName();
        }

        double health = Math.ceil((double)(entity.getHealth() + entity.getAbsorptionAmount()));
        ChatColors healthCol;
        if (health < 10.0D) {
           healthCol = ChatColors.DARK_RED;
        } else if (health > 10.0D && health < 13.0D) {
           healthCol = ChatColors.GOLD;
        } else {
           healthCol = ChatColors.DARK_GREEN;
        }

        String teams = null;
        if (Teams.isOnSameTeam(entity)) {
           teams = " \u00a7r[\u00a7aTeam\u00a7r] ";
        } else {
           teams = " \u00a7r[\u00a7cEnemy\u00a7r] ";
        }
        
        String bot = null;
        if (Antibot.isBot(entity)) {
      	  bot = " \u00a7r[\u00a79Bot\u00a7r] ";
        }else {
      	  bot = "";
        }

        String distance = null;
        if (this.mc.thePlayer.getDistanceToEntity(entity) > 50.0F) {
           distance = " \u00a7a" + (int)this.mc.thePlayer.getDistanceToEntity(entity) + "m \u00a7r";
        } else if (this.mc.thePlayer.getDistanceToEntity(entity) < 50.0F && this.mc.thePlayer.getDistanceToEntity(entity) > 20.0F) {
           distance = " \u00a76" + (int)this.mc.thePlayer.getDistanceToEntity(entity) + "m \u00a7r";
        } else if (this.mc.thePlayer.getDistanceToEntity(entity) < 20.0F) {
           distance = " \u00a7c" + (int)this.mc.thePlayer.getDistanceToEntity(entity) + "m \u00a7r";
        }

        String ping = null;
        if (getPing(entity) < 150.0D) {
           ping = "\u00a7a\u00a7l" + (int)getPing(entity) + "ms\u00a7r";
        } else if (getPing(entity) > 150.0D && getPing(entity) < 250.0D) {
           ping = "\u00a76\u00a7l" + (int)getPing(entity) + "ms\u00a7r";
        } else if (getPing(entity) > 250.0D) {
           ping = "\u00a7c\u00a7l" + (int)getPing(entity) + "ms\u00a7r";
        }

        String linehealth = null;
        if (health >= 20.0D) {
           linehealth = ChatColors.GREEN + "||||||||||";
        } else if (health > 18.0D) {
           linehealth = ChatColors.GREEN + "|||||||||" + ChatColors.RED + "|";
        } else if (health > 16.0D) {
           linehealth = ChatColors.GREEN + "||||||||" + ChatColors.RED + "||";
        } else if (health > 14.0D) {
           linehealth = ChatColors.GREEN + "|||||||" + ChatColors.RED + "|||";
        } else if (health > 12.0D) {
           linehealth = ChatColors.GREEN + "||||||" + ChatColors.RED + "||||";
        } else if (health > 10.0D) {
           linehealth = ChatColors.GREEN + "|||||" + ChatColors.RED + "|||||";
        } else if (health > 8.0D) {
           linehealth = ChatColors.GREEN + "||||" + ChatColors.RED + "||||||";
        } else if (health > 6.0D) {
           linehealth = ChatColors.GREEN + "|||" + ChatColors.RED + "|||||||";
        } else if (health > 4.0D) {
           linehealth = ChatColors.GREEN + "||" + ChatColors.RED + "||||||||";
        } else if (health > 2.0D) {
           linehealth = ChatColors.GREEN + "|" + ChatColors.RED + "|||||||||";
        } else if (health > 0.0D) {
           linehealth = ChatColors.RED + "||||||||||";
        }

        if (Math.floor(health) == health) {
           tag = ping + distance + tag + teams + bot + healthCol + linehealth;
        } else {
           tag = ping + distance + tag + teams + bot + healthCol + linehealth;
        }

        RenderManager renderManager = this.mc.getRenderManager();
        float scale = var13 * 2.0F;
        scale /= 100.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)pX, (float)pY + 1.4F, (float)pZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        setGLCap(2896, false);
        setGLCap(2929, false);
        Tessellator var14 = Tessellator.getInstance();
        var14.getWorldRenderer();
        int width = this.mc.fontRendererObj.getStringWidth(tag) / 2;
        setGLCap(3042, true);
        GL11.glBlendFunc(770, 771);
        RenderUtil.drawBorderedRect((float)(-width - 2), (float)(-(this.mc.fontRendererObj.FONT_HEIGHT + 1)), (float)(width + 15), 2.0F, 1.0F, -16777216, Integer.MIN_VALUE);
        var12.drawString(tag, (float)(-width), (float)(-(this.mc.fontRendererObj.FONT_HEIGHT - 1)), colour, false);
        GL11.glPushMatrix();
        if (this.armors) {
           int xOffset = 0;
           ItemStack[] var30 = entity.inventory.armorInventory;
           int var29 = entity.inventory.armorInventory.length;

           ItemStack renderStack;
           for(int var28 = 0; var28 < var29; ++var28) {
              renderStack = var30[var28];
              if (renderStack != null) {
                 xOffset -= 8;
              }
           }

           if (entity.getHeldItem() != null) {
              xOffset -= 8;
              renderStack = entity.getHeldItem().copy();
              if (((ItemStack)renderStack).hasEffect() && (((ItemStack)renderStack).getItem() instanceof ItemTool || ((ItemStack)renderStack).getItem() instanceof ItemArmor)) {
                 ((ItemStack)renderStack).stackSize = 1;
              }

              RenderUtil.renderItemStack((ItemStack)renderStack, xOffset, -26);
              xOffset += 16;
           }

           ItemStack[] var31 = entity.inventory.armorInventory;
           int var34 = entity.inventory.armorInventory.length;

           for(var29 = 0; var29 < var34; ++var29) {
              ItemStack armourStack = var31[var29];
              if (armourStack != null) {
                 ItemStack renderStack1 = armourStack.copy();
                 if (renderStack1.hasEffect() && (renderStack1.getItem() instanceof ItemTool || renderStack1.getItem() instanceof ItemArmor)) {
                    renderStack1.stackSize = 1;
                 }

                 RenderUtil.renderItemStack(renderStack1, xOffset, -26);
                 xOffset += 16;
              }
           }
        }

        GL11.glPopMatrix();
        revertAllCaps();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
     }
    
    public void revertAllCaps() {
        Iterator<Integer> localIterator = glCapMap.keySet().iterator();

        while(localIterator.hasNext()) {
           int cap = ((Integer)localIterator.next()).intValue();
           revertGLCap(cap);
        }

     }
    
    public static double getPing(EntityPlayer player) {
      NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfo(player.getName());
      int playerPing = playerInfo != null ? playerInfo.getResponseTime() : -1;
      return (double)playerPing;
   }
    
    public void setGLCap(int cap, boolean flag) {
        glCapMap.put(cap, GL11.glGetBoolean(cap));
        if (flag) {
           GL11.glEnable(cap);
        } else {
           GL11.glDisable(cap);
        }

     }
   
    public void revertGLCap(int cap) {
        Boolean origCap = (Boolean)glCapMap.get(cap);
        if (origCap != null) {
           if (origCap.booleanValue()) {
              GL11.glEnable(cap);
           } else {
              GL11.glDisable(cap);
           }
        }

     }

	private void drawEnchantTag(final String text, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n *= (int)1.05;
        n2 -= 6;
        Oxygen.INSTANCE.font.comfortaa10.drawStringWithColor(text, n + 9, -30 - n2, Colors.getColor(255));
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    private String getColor(final int n) {
        if (n != 1) {
            if (n == 2) {
                return "§a";
            }
            if (n == 3) {
                return "§3";
            }
            if (n == 4) {
                return "§4";
            }
            if (n >= 5) {
                return "§6";
            }
        }
        return "§f";
    }
    
    private void scale() {
        final float n = 1.0f * (mc.gameSettings.smoothCamera ? 2.0f : 1.0f);
        GlStateManager.scale(n, n, n);
    }
    
    private void updatePositions() {
        entityPositions.clear();
        final float renderPartialTicks = ((IMinecraft)mc).getTimer().renderPartialTicks;
        for (final Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer && entity instanceof EntityPlayer && (!entity.isInvisible() || !this.invis.getValueState())) {
                final double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * renderPartialTicks - mc.getRenderManager().viewerPosX;
                final double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * renderPartialTicks - mc.getRenderManager().viewerPosY;
                final double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * renderPartialTicks - mc.getRenderManager().viewerPosZ;
                final double n4 = n2 + (entity.height + 0.2);
                if (this.convertTo2D(n, n4, n3)[2] < 0.0 || this.convertTo2D(n, n4, n3)[2] >= 1.0) {
                    continue;
                }
                entityPositions.put((EntityLivingBase)entity, new double[] { this.convertTo2D(n, n4, n3)[0], this.convertTo2D(n, n4, n3)[1], Math.abs(this.convertTo2D(n, n4 + 1.0, n3, entity)[1] - this.convertTo2D(n, n4, n3, entity)[1]), this.convertTo2D(n, n4, n3)[2] });
            }
        }
    }
    
    private double[] convertTo2D(final double n, final double n2, final double n3, final Entity entity) {
        final float renderPartialTicks = ((IMinecraft)mc).getTimer().renderPartialTicks;
        final float rotationYaw = mc.thePlayer.rotationYaw;
        final float prevRotationYaw = mc.thePlayer.prevRotationYaw;
        final float[] array = getRotationFromPosition(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * renderPartialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * renderPartialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * renderPartialTicks - 1.6);
        final Entity getRenderViewEntity = mc.getRenderViewEntity();
        final Entity getRenderViewEntity2 = mc.getRenderViewEntity();
        final float n4 = array[0];
        getRenderViewEntity2.prevRotationYaw = n4;
        getRenderViewEntity.rotationYaw = n4;

        ((IEntityRenderer)Minecraft.getMinecraft().entityRenderer).runSetupCameraTransform(renderPartialTicks, 0);
        final double[] array2 = this.convertTo2D(n, n2, n3);
        mc.getRenderViewEntity().rotationYaw = rotationYaw;
        mc.getRenderViewEntity().prevRotationYaw = prevRotationYaw;
        ((IEntityRenderer)Minecraft.getMinecraft().entityRenderer).runSetupCameraTransform(renderPartialTicks, 0);
        return array2;
    }
    
    private double[] convertTo2D(final double n, final double n2, final double n3) {
        final FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(3);
        final IntBuffer intBuffer = BufferUtils.createIntBuffer(16);
        final FloatBuffer floatBuffer2 = BufferUtils.createFloatBuffer(16);
        final FloatBuffer floatBuffer3 = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, floatBuffer2);
        GL11.glGetFloat(2983, floatBuffer3);
        GL11.glGetInteger(2978, intBuffer);
        if (GLU.gluProject((float)n, (float)n2, (float)n3, floatBuffer2, floatBuffer3, intBuffer, floatBuffer)) {
            return new double[] { floatBuffer.get(0), Display.getHeight() - floatBuffer.get(1), floatBuffer.get(2) };
        }
        return null;
    }
    
    public static float[] getRotationFromPosition(final double n, final double n2, final double n3) {
        final double n4 = n - Minecraft.getMinecraft().thePlayer.posX;
        final double n5 = n2 - Minecraft.getMinecraft().thePlayer.posZ;
        return new float[] { (float)(Math.atan2(n5, n4) * 180.0 / 3.141592653589793) - 90.0f, (float)(-Math.atan2(n3 - Minecraft.getMinecraft().thePlayer.posY - 1.2, MathHelper.sqrt_double(n4 * n4 + n5 * n5)) * 180.0 / 3.141592653589793) };
    }
    
}
