package cn.rainbow.oxygen.module.modules.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.Render2DEvent;
import cn.rainbow.oxygen.event.events.Render3DEvent;
import cn.rainbow.oxygen.gui.font.CFontRenderer;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.modules.combat.AntiBot;
import cn.rainbow.oxygen.module.modules.player.Teams;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.ModeValue;
import cn.rainbow.oxygen.utils.EntityUtils;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;

import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Nametags extends Module {
    private final Map<EntityLivingBase, double[]> entityPositions = new HashMap<>();
	
    private final ModeValue mode = new ModeValue("Mode" , "New");
    private final BooleanValue armor = new BooleanValue("Armor", true);
    
    public Nametags() {
    	super("NameTags", Category.Render);
    	this.mode.addValue("Normal");
    	this.mode.addValue("New");
    }
    
    @EventTarget(events = {Render3DEvent.class, Render2DEvent.class})
    private void onEvent(final Event event) {
        if (event instanceof Render2DEvent) {
            if(this.mode.isCurrentMode("Normal")) {
                renderNormal();
            }
            if(this.mode.isCurrentMode("New")) {
                renderNew();
            }
        }
        if (event instanceof Render3DEvent) {
            try {
                updatePositions();
            } catch (Exception ignored) {}
        }
    }

    private void renderNew() {
        ScaledResolution scaledRes = new ScaledResolution(mc);

        try {
            for (EntityLivingBase ent : entityPositions.keySet()) {

                if (EntityUtils.isSelected(ent, false)) {
                    GlStateManager.pushMatrix();
                    double[] renderPositions = entityPositions.get(ent);
                    if ((renderPositions[3] < 0.0D) || (renderPositions[3] >= 1.0D)) {
                        GlStateManager.popMatrix();
                        continue;
                    }

                    CFontRenderer font = Oxygen.INSTANCE.fontmanager.wqy16;

                    GlStateManager.translate(renderPositions[0] / scaledRes.getScaleFactor(), renderPositions[1] / scaledRes.getScaleFactor(), 0.0D);

                    GlStateManager.scale(1, 1, 1);
                    GlStateManager.translate(0.0D, -2.5D, 0.0D);

                    String tag = (AntiBot.isBot(ent) ? "\2479[BOT]" : "") + (Teams.isOnSameTeam(ent) ? "\247b[TEAM]" : "") + "\247f";
                    String str = ent.getName();

                    float allWidth = font.getStringWidth(tag.replaceAll("\247.", "") + str.replaceAll("\247.", "")) + 14;

                    RenderUtil.drawRect(-allWidth / 2, -14.0f, allWidth / 2, 0, ColorUtils.getColor(0, 150));

                    font.drawStringWithShadow(tag + str.replaceAll("\247.", ""), -allWidth / 2 + 5.5f, -13F, ColorUtils.WHITE.c);

                    float nowhealth = (float) Math.ceil(ent.getHealth() + ent.getAbsorptionAmount());
                    float maxHealth = ent.getMaxHealth() + ent.getAbsorptionAmount();
                    float healthP = nowhealth / maxHealth;

                    int color = ColorUtils.WHITE.c;
                    String text = ent.getDisplayName().getFormattedText();

                    //Megawalls
                    text = text.replaceAll((text.contains("[") && text.contains("]")) ? "\2477":  "", "");
                    for (int i = 0; i < text.length(); i++) {
                        if ((text.charAt(i) == '\247') && (i + 1 < text.length())) {
                            char oneMore = Character.toLowerCase(text.charAt(i + 1));
                            int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                            if (colorCode < 16) {
                                try {
                                    color = RenderUtil.reAlpha(mc.fontRendererObj.getColorCode(oneMore), 1f);
                                } catch (ArrayIndexOutOfBoundsException ignored) {}
                            }
                        }
                    }

                    RenderUtil.drawRect(-allWidth / 2, -2f, allWidth / 2 - ((allWidth / 2) * (1 - healthP)) * 2, 0, RenderUtil.reAlpha(color, 0.8f));

                    boolean armors = this.armor.getCurrentValue();

                    if (armors) {
                        List<ItemStack> itemsToRender = new ArrayList<>();

                        for (int i = 0; i < 5; i++) {
                            ItemStack stack = ent.getEquipmentInSlot(i);
                            if (stack != null) {
                                itemsToRender.add(stack);
                            }
                        }

                        int x = -(itemsToRender.size() * 9) - 3;

                        for (ItemStack stack : itemsToRender) {

                            GlStateManager.pushMatrix();
                            RenderHelper.enableGUIStandardItemLighting();
                            GlStateManager.disableAlpha();
                            GlStateManager.clear(256);
                            mc.getRenderItem().zLevel = -150.0F;
                            this.fixGlintShit();
                            mc.getRenderItem().renderItemIntoGUI(stack, x + 6, -32);
                            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x + 6, -32);
                            mc.getRenderItem().zLevel = 0.0F;
                            x += 6;
                            GlStateManager.enableAlpha();
                            RenderHelper.disableStandardItemLighting();
                            GlStateManager.popMatrix();

                            if (stack != null) {
                                int y = 0;
                                int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId,
                                        stack);
                                int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,
                                        stack);
                                int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId,
                                        stack);
                                CFontRenderer c10 = Oxygen.INSTANCE.fontmanager.comfortaa10;
                                if (sLevel > 0) {
                                    drawEnchantTag("Sh" + getColor(sLevel) + sLevel, x, y);
                                    y += c10.getHeight() - 2;
                                }
                                if (fLevel > 0) {
                                    drawEnchantTag("Fir" + getColor(fLevel) + fLevel, x, y);
                                    y += c10.getHeight() - 2;
                                }
                                if (kLevel > 0) {
                                    drawEnchantTag("Kb" + getColor(kLevel) + kLevel, x, y);
                                } else if ((stack.getItem() instanceof ItemArmor)) {
                                    int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId,
                                            stack);
                                    int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId,
                                            stack);
                                    int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId,
                                            stack);
                                    if (pLevel > 0) {
                                        drawEnchantTag("P" + getColor(pLevel) + pLevel, x, y);
                                        y += c10.getHeight() - 2;
                                    }
                                    if (tLevel > 0) {
                                        drawEnchantTag("Th" + getColor(tLevel) + tLevel, x, y);
                                        y += c10.getHeight() - 2;
                                    }
                                    if (uLevel > 0) {
                                        drawEnchantTag("Unb" + getColor(uLevel) + uLevel, x, y);
                                    }
                                } else if ((stack.getItem() instanceof ItemBow)) {
                                    int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
                                    int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
                                    int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);

                                    if (powLevel > 0) {
                                        drawEnchantTag("Pow" + getColor(powLevel) + powLevel, x, y);
                                        y += c10.getHeight() - 2;
                                    }

                                    if (punLevel > 0) {
                                        drawEnchantTag("Pun" + getColor(punLevel) + punLevel, x, y);
                                        y += c10.getHeight() - 2;
                                    }

                                    if (fireLevel > 0) {
                                        drawEnchantTag("Fir" + getColor(fireLevel) + fireLevel, x, y);
                                    }
                                } else if (stack.getRarity() == EnumRarity.EPIC) {
                                    drawEnchantTag("\2476\247lGod", (int) (x - 0.5f), y + 12);
                                }
                                x += 12;
                            }
                        }
                    }
                    GlStateManager.popMatrix();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void renderNormal() {
        GlStateManager.pushMatrix();
        ScaledResolution scaledRes = new ScaledResolution(mc);

        for (EntityLivingBase ent : entityPositions.keySet()) {
            if (EntityUtils.isSelected(ent, false)) {

                GlStateManager.pushMatrix();
                double[] renderPositions = entityPositions.get(ent);
                if ((renderPositions[3] < 0.0D) || (renderPositions[3] >= 1.0D)) {
                    GlStateManager.popMatrix();
                    continue;
                }

                CFontRenderer font = Oxygen.INSTANCE.fontmanager.wqy18;

                GlStateManager.translate(renderPositions[0] / scaledRes.getScaleFactor(), renderPositions[1] / scaledRes.getScaleFactor(), 0.0D);
                scale();
                GlStateManager.translate(0.0D, -2.5D, 0.0D);

                String health = "Health: " + Math.round(ent.getHealth() * 10) / 10;
                String str = (AntiBot.isBot(ent) ? "\2479[BOT]" : "") + (Teams.isOnSameTeam(ent) ? "\247b[TEAM]" : "") + "\247f" + ent.getDisplayName().getUnformattedText();
                String suff = "";

                str = str + suff;

                float strWidth = font.getStringWidth(str.replaceAll("\247.", ""));
                float strWidth2 = Oxygen.INSTANCE.fontmanager.comfortaa12.getStringWidth(health);
                float allWidth = (strWidth > strWidth2 ? strWidth : strWidth2) + 8;
                RenderUtil.drawRect(-allWidth / 2, -25.0f, allWidth / 2, 0, ColorUtils.getColor(0, 130));
                int x3 = ((int) (renderPositions[0] + -allWidth / 2 - 3) / 2) - 26;
                int x4 = ((int) (renderPositions[0] + allWidth / 2 + 3) / 2) + 20;
                int y1 = ((int) (renderPositions[1] + -30) / 2);
                int y2 = ((int) (renderPositions[1] + 11) / 2);
                int mouseY = (scaledRes.getScaledHeight() / 2);
                int mouseX = (scaledRes.getScaledWidth() / 2);
                font.drawStringWithShadow(str, -allWidth / 2 + 4, -22.0F, ColorUtils.WHITE.c);
                Oxygen.INSTANCE.fontmanager.comfortaa12.drawString(health, -allWidth / 2 + 4, -10.0F, ColorUtils.WHITE.c);

                EntityLivingBase entity = ent;
                float nowhealth = (float) Math.ceil(entity.getHealth() + entity.getAbsorptionAmount());
                float maxHealth = entity.getMaxHealth() + entity.getAbsorptionAmount();
                float healthP = nowhealth / maxHealth;

                RenderUtil.drawRect(-allWidth / 2, -1, allWidth / 2 - ((allWidth / 2) * (1 - healthP)) * 2, 0, ColorUtils.WHITE.c);

                if(armor.getCurrentValue()) {
                    List<ItemStack> itemsToRender = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        ItemStack stack = ent.getEquipmentInSlot(i);
                        if (stack != null) {
                            itemsToRender.add(stack);
                        }
                    }
                    int x = -(itemsToRender.size() * 9);
                    for (ItemStack stack : itemsToRender) {
                        RenderHelper.enableGUIStandardItemLighting();
                        mc.getRenderItem().renderItemIntoGUI(stack, x + 6, -42);
                        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, -42);
                        x += 3;
                        RenderHelper.disableStandardItemLighting();
                        if (stack != null) {
                            int y = 21;
                            int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId,
                                    stack);
                            int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,
                                    stack);
                            int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId,
                                    stack);
                            if (sLevel > 0) {
                                drawEnchantTag("Sh" + getColor(sLevel) + sLevel, x, y);
                                y += 6;
                            }
                            if (fLevel > 0) {
                                drawEnchantTag("Fir" + getColor(fLevel) + fLevel, x, y);
                                y += 6;
                            }
                            if (kLevel > 0) {
                                drawEnchantTag("Kb" + getColor(kLevel) + kLevel, x, y);
                            } else if ((stack.getItem() instanceof ItemArmor)) {
                                int pLevel = EnchantmentHelper
                                        .getEnchantmentLevel(Enchantment.protection.effectId, stack);
                                int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId,
                                        stack);
                                int uLevel = EnchantmentHelper
                                        .getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
                                if (pLevel > 0) {
                                    drawEnchantTag("P" + getColor(pLevel) + pLevel, x, y);
                                    y += 6;
                                }
                                if (tLevel > 0) {
                                    drawEnchantTag("Th" + getColor(tLevel) + tLevel, x, y);
                                    y += 6;
                                }
                                if (uLevel > 0) {
                                    drawEnchantTag("Unb" + getColor(uLevel) + uLevel, x, y);
                                }
                            } else if ((stack.getItem() instanceof ItemBow)) {
                                int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId,
                                        stack);
                                int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId,
                                        stack);
                                int fireLevel = EnchantmentHelper
                                        .getEnchantmentLevel(Enchantment.flame.effectId, stack);
                                if (powLevel > 0) {
                                    drawEnchantTag("Pow" + getColor(powLevel) + powLevel, x, y);
                                    y += 6;
                                }
                                if (punLevel > 0) {
                                    drawEnchantTag("Pun" + getColor(punLevel) + punLevel, x, y);
                                    y += 6;
                                }
                                if (fireLevel > 0) {
                                    drawEnchantTag("Fir" + getColor(fireLevel) + fireLevel, x, y);
                                }
                            } else if (stack.getRarity() == EnumRarity.EPIC) {
                                drawEnchantTag("\2476\247lGod", x - 2, y);
                            }
                            int var7 = (int) Math.round(255.0D - (double) stack.getItemDamage() * 255.0D / (double) stack.getMaxDamage());
                            int var10 = 255 - var7 << 16 | var7 << 8;
                            Color customColor = new Color(var10).brighter();

                            float x2 = (float) (x * 1.05D) - 2;
                            if ((stack.getMaxDamage() - stack.getItemDamage()) > 0) {
                                GlStateManager.pushMatrix();
                                GlStateManager.disableDepth();
                                //Hanabi.INSTANCE.fontManager.comfortaa12.drawString("" + (stack.getMaxDamage() - stack.getItemDamage()), x2 + 6, -32, customColor.getRGB());
                                GlStateManager.enableDepth();
                                GlStateManager.popMatrix();
                            }

                            x += 12;
                        }
                    }
                }
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }

    private void fixGlintShit() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    private void drawEnchantTag(String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x = (int) (x * 1.05D);
        y -= 6;
        Oxygen.INSTANCE.fontmanager.comfortaa10.drawStringWithShadow(text, x + 9, -30 - y, ColorUtils.getColor(255));
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private String getColor(int level) {
        if (level == 1) {

        } else if (level == 2) {
            return "\247a";
        } else if (level == 3) {
            return "\2473";
        } else if (level == 4) {
            return "\2474";
        } else if (level >= 5) {
            return "\2476";
        }
        return "\247f";
    }

    private void scale() {
        float scale = 1;
        scale *= mc.gameSettings.smoothCamera ? 2 : 1;
        GlStateManager.scale(scale, scale, scale);
    }

    private void updatePositions() {
        entityPositions.clear();
        float pTicks = mc.timer.renderPartialTicks;
        for (Entity ent : mc.theWorld.loadedEntityList) {
            if (EntityUtils.isSelected(ent, false)) {
                double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - mc.getRenderManager().viewerPosX;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
                double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - mc.getRenderManager().viewerPosZ;
                y += ent.height + 0.2D;
                if ((convertTo2D(x, y, z)[2] >= 0.0D) && (convertTo2D(x, y, z)[2] < 1.0D)) {
                    entityPositions.put((EntityLivingBase) ent,
                            new double[]{convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1],
                                    Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]),
                                    convertTo2D(x, y, z)[2]});
                }
            }
        }
    }

    private float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;
        double yDiff = y - mc.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[] { yaw, pitch };
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        float pTicks = mc.timer.renderPartialTicks;
        float prevYaw = mc.thePlayer.rotationYaw;
        float prevPrevYaw = mc.thePlayer.prevRotationYaw;
        float[] rotations = getRotationFromPosition(
                ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks,
                ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks,
                ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6D);
        mc.getRenderViewEntity().rotationYaw = (mc.getRenderViewEntity().prevRotationYaw = rotations[0]);
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        double[] convertedPoints = convertTo2D(x, y, z);
        mc.getRenderViewEntity().rotationYaw = prevYaw;
        mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[]{screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }

}
