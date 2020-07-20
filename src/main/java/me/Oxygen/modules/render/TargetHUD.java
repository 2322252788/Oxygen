package me.Oxygen.modules.render;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import org.lwjgl.opengl.GL11;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventRender2D;
import me.Oxygen.injection.interfaces.IGuiPlayerTabOverlay;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.combat.KillAura;
import me.Oxygen.utils.render.AnimationUtils;
import me.Oxygen.utils.render.ColorUtils;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import me.Oxygen.utils.render.RenderUtils;
import me.Oxygen.value.Value;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@ModuleRegister(name = "TargetHUD", category = Category.RENDER)
public class TargetHUD extends Module {
	public static boolean shouldMove;
	public static boolean useFont;
	int Long = 0;
	Entity lasttarget;
	public static DecimalFormat format = new DecimalFormat("0.0");
	private static final Color COLOR = new Color(0, 0, 0, 180);
	private final Stopwatch animationStopwatch = new Stopwatch();
    private double healthBarWidth;
    private double hudHeight;
	public static Entity lastEnt;
	public static float lastHealth = -1.0f;
	public static float damageDelt = 0.0f;
	public static float lastPlayerHealth = -1.0f;
	public static float damageDeltToPlayer = 0.0f;
	public double animation = 0.0;
	private EntityOtherPlayerMP target;
	public Value<String> mode = new Value<String>("TargetHUD", "Mode", 0);
	private Value<Boolean> black = new Value<Boolean>("TargetHUD_Black", false);

	public TargetHUD() {
		this.mode.mode.add("Normal");
		this.mode.mode.add("Oxygen");
		this.mode.mode.add("Flat");
		this.mode.mode.add("Debug");
		this.mode.mode.add("Autunm");
		this.mode.mode.add("Exhibition");
	}

	float width = 0;

	@EventTarget(events = EventRender2D.class)
	private final void onEvent(Event er) {
		if(er instanceof EventRender2D) {
		if(this.mode.isCurrentMode("Normal")) {
			 ScaledResolution sr = new ScaledResolution(mc);
				final FontRenderer font2 = mc.fontRendererObj;
				if (KillAura.target != null && Oxygen.INSTANCE.ModMgr.getModule(TargetHUD.class).isEnabled()
						& Oxygen.INSTANCE.ModMgr.getModule(KillAura.class).isEnabled()) {
					final String name = KillAura.target.getName();
					font2.drawStringWithShadow(name, (float) (sr.getScaledWidth() / 2) - (font2.getStringWidth(name) / 2),
							(float) (sr.getScaledHeight() / 2 - 30), -1);
					mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
					int i = 0;
					while ((float) i < KillAura.target.getMaxHealth() / 2.0f) {
						mc.ingameGUI.drawTexturedModalRect((float) (sr.getScaledWidth() / 2)
								- KillAura.target.getMaxHealth() / 2.0f * 10.0f / 2.0f + (float) (i * 10),
								(float) (sr.getScaledHeight() / 2 - 16), 16, 0, 9, 9);
						++i;
					}
					i = 0;
					while ((float) i < KillAura.target.getHealth() / 2.0f) {
						mc.ingameGUI.drawTexturedModalRect((float) (sr.getScaledWidth() / 2)
								- KillAura.target.getMaxHealth() / 2.0f * 10.0f / 2.0f + (float) (i * 10),
								(float) (sr.getScaledHeight() / 2 - 16), 52, 0, 9, 9);
						++i;
					}
				}
		}
		if(this.mode.isCurrentMode("Oxygen")) {
			ScaledResolution res = new ScaledResolution(this.mc);	 
    		int x = res.getScaledWidth() /2;
    		int y = res.getScaledHeight() /2;
    		final EntityLivingBase player = (EntityLivingBase) KillAura.target;
    		if (player != null) {
    			final float health = player.getHealth();
                final float[] fractions = { 0.0f, 0.5f, 1.0f };
                final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
                final float progress = health / player.getMaxHealth();
                BigDecimal bigDecimal = new BigDecimal((double)player.getHealth());
	            bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
	    		double HEALTH = bigDecimal.doubleValue();
	    		double width = (double)Oxygen.INSTANCE.font.wqy18.getStringWidth(player.getName());
                width = getIncremental(width, 10.0);
                if (width < 50.0) {
                    width = 50.0;
                }
                final String string =  String.valueOf(format.format(((EntityLivingBase) KillAura.target).getHealth()));
                final Color customColor = (health >= 0.0f) ? blendColors(fractions, colors, progress).brighter() : Color.RED;
                final double healthLocation = width * progress;
                RenderUtils.R2DUtils.drawRoundedRect(x+20, (float)y +20, x+134, y+60, new Color(255, 255, 255, 130).getRGB(),
					new Color(255, 255, 255, 130).getRGB());
			RenderUtil.drawRect(x+ 22.0, y+ 57.0, x+68.0 + healthLocation + 13.0, y+52.0, customColor.getRGB());
			RenderUtil.rectangleBordered(x+22.0, y + 57.0, x+81.0 + width, y+52.0, 0.5, Colors.getColor(0, 0), Colors.getColor(0));
			Oxygen.INSTANCE.font.wqy18.drawCenteredString("Name: ",x+38.0f, y+24.0f, new Color(0, 0, 0,150).getRGB());
			Oxygen.INSTANCE.font.wqy18.drawCenteredString(player.getName(),x+25f + Oxygen.INSTANCE.font.wqy18.getStringWidth(player.getName() + "%s%s"), y+24.0f, new Color(0, 0, 0,150).getRGB());
			final String str = "HP: "+ HEALTH /*+ " Dist: " + Dis*/;
			Oxygen.INSTANCE.font.wqy18.drawCenteredString(str, x+ 40.0f, y+38.0f, new Color(0, 0, 0,150).getRGB());
			
			/*if(player instanceof EntityPlayer) {
                final List var5 = ((IGuiPlayerTabOverlay) new GuiPlayerTabOverlay(mc, mc.ingameGUI)).getField_175252_a().sortedCopy((Iterable)mc.thePlayer.sendQueue.getPlayerInfoMap());
                for (final Object aVar5 : var5) {
                    final NetworkPlayerInfo var6 = (NetworkPlayerInfo)aVar5;
                    if (mc.theWorld.getPlayerEntityByUUID(var6.getGameProfile().getId()) == player) {
                        mc.getTextureManager().bindTexture(var6.getLocationSkin());
                        Gui.drawScaledCustomSizeModalRect(x+2, y+2, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
                        if (((EntityPlayer)player).isWearing(EnumPlayerModelParts.HAT)) {
                            Gui.drawScaledCustomSizeModalRect(x+2, y+2, 40.0f, 8.0f, 8, 8, 35, 32, 74.0f, 74.0f);
                        }
                        GlStateManager.bindTexture(0);
                        break;
                    }
                }
                
                }*/
			
    		}
		}
		if(this.mode.isCurrentMode("Flat")) {
    		ScaledResolution res1 = new ScaledResolution(this.mc);	 
    		int x = res1.getScaledWidth() /2 + 10;
    		int y = res1.getScaledHeight() - 90;
            final EntityLivingBase player = (EntityLivingBase) KillAura.target;
             if (player != null) {
                GlStateManager.pushMatrix();
                //BackGround
                

                RenderUtil.drawRect(x+1.0, y+1.0, x+123.0, y+35.0,this.black.getValueState().booleanValue() ? Integer.MIN_VALUE : new Color(240,238,225,150).getRGB() );
                
                RenderUtil.drawBorderedRect(x, (float)y, x+124, y+36, 2,this.black.getValueState().booleanValue() ? new Color(240,238,225).getRGB() : new Color(0,0,0).getRGB(), 1);
                
                RenderUtil.drawRect(x+35.0, y+1.0, x+123.0, y+35.0,this.black.getValueState().booleanValue() ? Integer.MIN_VALUE : new Color(240,238,225,150).getRGB() );

                mc.fontRendererObj.drawString(player.getName(), x+38.0f, y+4.0f,this.black.getValueState().booleanValue() ? -1 : 1,false);
                BigDecimal bigDecimal = new BigDecimal((double)player.getHealth());
        		bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
        		double HEALTH = bigDecimal.doubleValue();
                BigDecimal DT = new BigDecimal((double)mc.thePlayer.getDistanceToEntity(player));
        		DT = DT.setScale(1, RoundingMode.HALF_UP);
        		double Dis = DT.doubleValue();
                final float health = player.getHealth();
                final float[] fractions = { 0.0f, 0.5f, 1.0f };
                final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
                final float progress = health / player.getMaxHealth();
                final Color customColor = (health >= 0.0f) ? blendColors(fractions, colors, progress).brighter() : Color.RED;
                double width = (double)mc.fontRendererObj.getStringWidth(player.getName());
                width = getIncremental(width, 10.0);
                if (width < 50.0) {
                    width = 50.0;
                }
                final double healthLocation = width * progress;
                //health bar
                RenderUtil.drawRect(x+ 37.5, y+ 22.5, x+48.0 + healthLocation + 0.5, y+14.5, customColor.getRGB());
                RenderUtil.rectangleBordered(x+37.0, y + 22.0, x+49.0 + width, y+15.0, 0.5, Colors.getColor(0, 0), Colors.getColor(0));
               
                String COLOR1;
                if (health > 20.0D) {
                   COLOR1 = " \2479";
                } else if (health >= 10.0D) {
                   COLOR1 = " \247a";
                } else if (health >= 3.0D) {
                   COLOR1 = " \247e";
                } else {
                   COLOR1 = " \2474";
                }
                
                GlStateManager.scale(0.5, 0.5, 0.5);
                final String str3 = String.format("HP: %s HURT: %s", Math.round(player.getHealth()), player.hurtTime);
                Oxygen.INSTANCE.font.comfortaa34.drawStringWithShadow(str3, x*2+76.0f, y*2+49.0f, this.black.getValueState().booleanValue() ? -1 : 1);
//              GuiInventory.drawEntityOnScreen(x*2-500s , 32, 25 , 2.0f, 2.0f, Aura.target);

                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                
                if(player instanceof EntityPlayer) {
                	final List var5 = ((IGuiPlayerTabOverlay) new GuiPlayerTabOverlay(mc, mc.ingameGUI)).getField_175252_a().sortedCopy((Iterable)mc.thePlayer.sendQueue.getPlayerInfoMap());
                for (final Object aVar5 : var5) {
                    final NetworkPlayerInfo var6 = (NetworkPlayerInfo)aVar5;
                    if (mc.theWorld.getPlayerEntityByUUID(var6.getGameProfile().getId()) == player) {
                        mc.getTextureManager().bindTexture(var6.getLocationSkin());
                        Gui.drawScaledCustomSizeModalRect(x+2, y+2, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
                        if (((EntityPlayer)player).isWearing(EnumPlayerModelParts.HAT)) {
                            Gui.drawScaledCustomSizeModalRect(x+2, y+2, 40.0f, 8.0f, 8, 8, 35, 32, 74.0f, 74.0f);
                        }
                        GlStateManager.bindTexture(0);
                        break;
                    }
                }
                
                }
                
                GlStateManager.popMatrix();
            }
    	}
		if(mode.isCurrentMode("Autunm")) {
			ScaledResolution res = new ScaledResolution(this.mc);
		
			float scaledWidth = res.getScaledWidth();
	        float scaledHeight = res.getScaledHeight();
	        if (KillAura.target != null ) {
	            if (KillAura.target instanceof EntityOtherPlayerMP) {
	                this.target = (EntityOtherPlayerMP)KillAura.target;
	                float width = 140.0f;
	                float height = 40.0f;
	                float xOffset = 40.0f;
	                float x = scaledWidth / 2.0f - 70.0f;
	                float y = scaledHeight / 2.0f + 80.0f;
	                float health = this.target.getHealth();
	                double hpPercentage = health / this.target.getMaxHealth();
	                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
	                double hpWidth = 92.0 * hpPercentage;
	                int healthColor = ColorUtils.getHealthColor(this.target.getHealth(), this.target.getMaxHealth()).getRGB();
	                String healthStr = String.valueOf((float)((int)this.target.getHealth()) / 2.0f);
	                if (this.animationStopwatch.elapsed(15L)) {
	                    this.healthBarWidth = AnimationUtils.animate(hpWidth, this.healthBarWidth, 0.353f);
	                    this.hudHeight = AnimationUtils.animate(40.0, this.hudHeight, 0.1f);
	                    this.animationStopwatch.reset();
	                }
	                GL11.glEnable((int)3089);
	                RenderUtil.prepareScissorBox(x, y, x + 140.0f, (float)((double)y + this.hudHeight));
	                RenderUtil.drawRect(x, y, x + 140.0f, y + 40.0f, COLOR.getRGB());
	                RenderUtil.drawRect(x + 40.0f, y + 15.0f, (double)(x + 40.0f) + this.healthBarWidth, y + 25.0f, healthColor);
	                mc.fontRendererObj.drawStringWithShadow(healthStr, x + 40.0f + 46.0f - (float)mc.fontRendererObj.getStringWidth(healthStr) / 2.0f, y + 16.0f, -1);
	                mc.fontRendererObj.drawStringWithShadow(this.target.getName(), x + 40.0f, y + 2.0f, -1);
	                GuiInventory.drawEntityOnScreen((int)(x + 13.333333f), (int)(y + 40.0f), 20, this.target.rotationYaw, this.target.rotationPitch, this.target);
	                GL11.glDisable((int)3089);
	            }
	        }
		}else {
	            this.healthBarWidth = 92.0;
	            this.hudHeight = 0.0;
	            this.target = null;
	        }
		if(this.mode.isCurrentMode("Debug")) {
			if (KillAura.target != null) {
				final ScaledResolution scaledResolution = new ScaledResolution(this.mc);
	                final Entity target = KillAura.target;
	                if (target != lastEnt) {
	                	lastEnt = (EntityLivingBase) target;
	                	lastHealth = KillAura.target.getHealth();
	                	damageDelt = 0.0f;
	                	damageDeltToPlayer = 0.0f;
	                }
	                if (lastHealth != KillAura.target.getHealth() && KillAura.target.getHealth() - lastHealth < 1.0f) {
	                	damageDelt = KillAura.target.getHealth() - lastHealth;
	                    lastHealth = KillAura.target.getHealth();
	                }
	                if (!this.mc.thePlayer.isEntityAlive()) {
	                	lastPlayerHealth = -1.0f;
	                }
	                if (lastPlayerHealth == -1.0f && this.mc.thePlayer.isEntityAlive()) {
	                	lastPlayerHealth = this.mc.thePlayer.getHealth();
	                }
	                if (lastPlayerHealth != this.mc.thePlayer.getHealth()) {
	                	damageDeltToPlayer = this.mc.thePlayer.getHealth() - lastPlayerHealth;
	                    lastPlayerHealth = this.mc.thePlayer.getHealth();
	                }
	                final String replaceAll = target.getName().replaceAll("ยง.", "");
	                final String string = "HP: " + String.valueOf(format.format(((EntityLivingBase) target).getHealth()));
	                final String string2 = "In: " + format.format(damageDeltToPlayer).replace("-", "") + "/Out: " + format.format(damageDelt).replace("-", "");
	                GL11.glPushMatrix();
	                GL11.glTranslatef((float)(scaledResolution.getScaledWidth() / 2), (float)(scaledResolution.getScaledHeight() / 2), 0.0f);
	                if (!target.isDead) {
	                    final float n = KillAura.target.getHealth() / KillAura.target.getMaxHealth() * 100.0f;
	                    this.animation = (float) RenderUtil.getAnimationState(this.animation, n, Math.max(10.0, Math.abs(this.animation - n) * 30.0) * 0.3);
	                    RenderUtil.drawArc(1.0f, 1.0f, 15.0, Colors.RED.c, 180, 180.0 + 3.5999999046325684 * this.animation, 5);
	                    RenderUtil.drawArc(1.0f, 1.0f, 16.0, Colors.BLUE.c, 180, 180.0f + 3.6f * (KillAura.target.getTotalArmorValue() * 4), 3);
	                    Oxygen.INSTANCE.font.wqy14.drawStringWithColor(replaceAll, -2, -30, Colors.WHITE.c);
	                    Oxygen.INSTANCE.font.wqy14.drawCenteredString(string2, 0.0f, 20.0f, Colors.WHITE.c);
	                    Oxygen.INSTANCE.font.wqy14.drawCenteredString(string, 0.0f, 30.0f, Colors.WHITE.c);
	                }
	                GL11.glPopMatrix();
	            
			}
		}
		if(this.mode.isCurrentMode("Exhibition")) {
			ScaledResolution res = new ScaledResolution(this.mc);	 
			int x = res.getScaledWidth() /2 + 10;
			int y = res.getScaledHeight() - 90;
	        final EntityLivingBase player = (EntityLivingBase) KillAura.target;
	         if (player != null) {

	            GlStateManager.pushMatrix();
	            RenderUtil.drawRect(x+0.0, y+0.0, x+113.0, y+36.0, Colors.getColor(0, 150));
	            
	            mc.fontRendererObj.drawStringWithShadow(player.getName(), x+38.0f, y+2.0f, -1);
	       
	            BigDecimal bigDecimal = new BigDecimal((double)player.getHealth());
	            bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
	    		double HEALTH = bigDecimal.doubleValue();
	    		
	            BigDecimal DT = new BigDecimal((double)mc.thePlayer.getDistanceToEntity(player));
	    		DT = DT.setScale(1, RoundingMode.HALF_UP);
	    		double Dis = DT.doubleValue();
	    		
	            final float health = player.getHealth();
	            final float[] fractions = { 0.0f, 0.5f, 1.0f };
	            final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
	            final float progress = health / player.getMaxHealth();
	            final Color customColor = (health >= 0.0f) ? blendColors(fractions, colors, progress).brighter() : Color.RED;
	            double width = (double)mc.fontRendererObj.getStringWidth(player.getName());
	            width = getIncremental(width, 10.0);
	            if (width < 50.0) {
	                width = 50.0;
	            }
	            final double healthLocation = width * progress;
	            RenderUtil.drawRect(x+37.5, y+11.5, x+38.0 + healthLocation + 0.5, y+14.5, customColor.getRGB());
	            RenderUtil.rectangleBordered(x+37.0, y+11.0, x+39.0 + width, y+15.0, 0.5, Colors.getColor(0, 0), Colors.getColor(0));
	            for (int i = 1; i < 10; ++i) {
	                final double dThing = width / 10.0 * i;
	                RenderUtil.drawRect(x+38.0 + dThing, y+11.0, x+38.0 + dThing + 0.5, y+15.0, Colors.getColor(0));
	            }
	            String COLOR1;
	            if (health > 20.0D) {
	               COLOR1 = " \2479";
	            } else if (health >= 10.0D) {
	               COLOR1 = " \247a";
	            } else if (health >= 3.0D) {
	               COLOR1 = " \247e";
	            } else {
	               COLOR1 = " \2474";
	            }
	            RenderUtil.rectangleBordered(x+1.0, y+1.0, x+35.0, y+35.0, 0.5, Colors.getColor(0, 0), Colors.getColor(255));
	            
	            GlStateManager.scale(0.5, 0.5, 0.5);
	            final String str = "HP: "+ HEALTH + " Dist: " + Dis;
	            mc.fontRendererObj.drawStringWithShadow(str, x*2+76.0f, y*2+35.0f, -1);
	            GlStateManager.scale(2.0f, 2.0f, 2.0f);
	            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	            GlStateManager.enableAlpha();
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	            GlStateManager.popMatrix();
	        }
		}
	}
}
	
	private final double getIncremental(final double val, final double inc) {
        final double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }

	private final static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
		Color color = null;
		if (fractions == null) {
			throw new IllegalArgumentException("Fractions can't be null");
		}
		if (colors == null) {
			throw new IllegalArgumentException("Colours can't be null");
		}
		if (fractions.length == colors.length) {
			final int[] indicies = getFractionIndicies(fractions, progress);
			final float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
			final Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
			final float max = range[1] - range[0];
			final float value = progress - range[0];
			final float weight = value / max;
			color = blend(colorRange[0], colorRange[1], 1.0f - weight);
			return color;
		}
		throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
	}

	private final static int[] getFractionIndicies(final float[] fractions, final float progress) {
		final int[] range = new int[2];
		int startPoint;
		for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
		}
		if (startPoint >= fractions.length) {
			startPoint = fractions.length - 1;
		}
		range[0] = startPoint - 1;
		range[1] = startPoint;
		return range;
	}

	private final static Color blend(final Color color1, final Color color2, final double ratio) {
		final float r = (float) ratio;
		final float ir = 1.0f - r;
		final float[] rgb1 = new float[3];
		final float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		float red = rgb1[0] * r + rgb2[0] * ir;
		float green = rgb1[1] * r + rgb2[1] * ir;
		float blue = rgb1[2] * r + rgb2[2] * ir;
		if (red < 0.0f) {
			red = 0.0f;
		} else if (red > 255.0f) {
			red = 255.0f;
		}
		if (green < 0.0f) {
			green = 0.0f;
		} else if (green > 255.0f) {
			green = 255.0f;
		}
		if (blue < 0.0f) {
			blue = 0.0f;
		} else if (blue > 255.0f) {
			blue = 255.0f;
		}
		Color color3 = null;
		try {
			color3 = new Color(red, green, blue);
		} catch (IllegalArgumentException exp) {
			final NumberFormat nf = NumberFormat.getNumberInstance();
			System.out.println(
					String.valueOf(String.valueOf(nf.format(red))) + "; " + nf.format(green) + "; " + nf.format(blue));
			exp.printStackTrace();
		}
		return color3;
	}

	public static int getColor(final int red,final  int green,final  int blue) {
		return getColor(red, green, blue, 255);
	}

	public static int getColor(final int red,final  int green,final  int blue,final  int alpha) {
		int color = 0;
		color |= alpha << 24;
		color |= red << 16;
		color |= green << 8;
		color |= blue;
		return color;
	}
	
	public class Stopwatch {
	    private long ms = this.getCurrentMS();

	    private long getCurrentMS() {
	        return System.currentTimeMillis();
	    }

	    public final long getElapsedTime() {
	        return this.getCurrentMS() - this.ms;
	    }

	    public final boolean elapsed(long milliseconds) {
	        return this.getCurrentMS() - this.ms > milliseconds;
	    }

	    public final void reset() {
	        this.ms = this.getCurrentMS();
	    }
	}
}
