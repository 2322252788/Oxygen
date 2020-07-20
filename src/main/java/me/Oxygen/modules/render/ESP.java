package me.Oxygen.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventPriority;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventRender3D;
import me.Oxygen.event.events.EventRenderEntity;
import me.Oxygen.event.events.EventRenderGui;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.injection.interfaces.IFontRenderer;
import me.Oxygen.injection.interfaces.IMinecraft;
import me.Oxygen.injection.interfaces.IRenderManager;
import me.Oxygen.manager.FriendManager;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.combat.Antibot;
import me.Oxygen.modules.combat.KillAura;
import me.Oxygen.utils.fontRenderer.UnicodeFontRenderer;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import me.Oxygen.value.Value;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

@ModuleRegister(name = "ESP", category = Category.RENDER)
public class ESP extends Module {

	public static float h = 0;

	public final Value<String> MODE = new Value<String>("ESP", "Mode", 0);
	public final Value<String> COLORMODE = new Value<String>("ESP", "Color", 0);
	
    private final Value<Boolean> ITEMS = new Value<Boolean>("ESP_Items", true);
    private final Value<Boolean> ARMOR = new Value<Boolean>("ESP_Armor", false);
    private final Value<Boolean> NAME = new Value<Boolean>("ESP_Name", false);
    
    
    private static Value<Boolean> PLAYERS = new Value<Boolean>("ESP_Player", true);
	private static Value<Boolean> ANIMALS = new Value<Boolean>("ESP_Animals", true);
	private static Value<Boolean> INVISIBLES  = new Value<Boolean>("ESP_Invisibles", false);
    
	private double gradualFOVModifier;
    public static Map<EntityLivingBase, double[]> entityPositionstop = new HashMap<EntityLivingBase, double[]>();
    public static Map<EntityLivingBase, double[]> entityPositionsbottom = new HashMap<EntityLivingBase, double[]>();
    
    public ESP() {
		this.MODE.addValue("2D");
		this.MODE.addValue("Box");
		this.MODE.addValue("Box2");
		this.MODE.addValue("Fill");
		this.MODE.addValue("CSGO");
		this.COLORMODE.addValue("Rainbow");
		this.COLORMODE.addValue("Team");
		this.COLORMODE.addValue("Health");
		this.COLORMODE.addValue("Custom");
	}

    /*
     * NOCLIP VIEW : entityrenderer
     * NO BLIND IN BLOCKS : EntityPlayerSP
     */
    
    @EventTarget(
    		priority = EventPriority.LOW,
    		events = {EventUpdate.class, EventRender3D.class, 
    				EventRenderEntity.class,  EventRenderGui.class}
    )
    public void onEvent(Event event) {
    	String mode = MODE.getModeAt(MODE.getCurrentMode());
    	String colormode = COLORMODE.getModeAt(COLORMODE.getCurrentMode());
    	this.setDisplayName(MODE.getModeAt(MODE.getCurrentMode()));
        if (event instanceof EventRender3D) {
        	
        	for (Entity next : mc.theWorld.loadedEntityList) {
                if (next instanceof EntityPlayer && MODE.isCurrentMode("Box2")) {
                	EntityPlayer entityPlayer = (EntityPlayer)next;
                    if (entityPlayer == mc.thePlayer || entityPlayer.isDead) {
                        continue;
                    }
                    this.renderBox(entityPlayer, 0.0, 1.0, 0.0);
                }
            }
        	
        	
        	if (h > 255) {
				h = 0;
			}
    
			h+= 0.1;
			int renderColor = 0;
			int list = GL11.glGenLists(1);
			

			GL11.glPushMatrix();
		
			for(Object obj : mc.theWorld.loadedEntityList){
				Entity entity = (Entity)obj;
				 if (isValid(entity)) {

					mc.entityRenderer.disableLightmap();
					 switch(colormode){
	                    case"Rainbow":{
	                    	final Color color = Color.getHSBColor(h / 255.0f, 0.6f, 1.0f);
	            			final int c = color.getRGB();
	                    	renderColor = c;
	                    }
	                    break;
	                    case"Team":{
	                    String text = entity.getDisplayName().getFormattedText();
	                	if(Character.toLowerCase(text.charAt(0)) == '§'){
	                		
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
	                    	float health = ((EntityLivingBase)entity).getHealth();
	                    

	                        if (health > 20) {
	                            health = 20;
	                        }
	                        float[] fractions = new float[]{0f, 0.5f, 1f};
	                        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
	                        float progress = (health * 5) * 0.01f;
	                        Color customColor = blendColors(fractions, colors, progress).brighter();
	                        renderColor = customColor.getRGB();
	                    }
	                    break;
	                    case"Custom":
	                    	renderColor = new Color(255,255,255).getRGB();
	                    	break;
	                    }        
	                    if(entity.hurtResistantTime > 15 && colormode.equalsIgnoreCase("Fill")){
	                    	renderColor = Colors.getColor(1, 0, 0, 1);
	                    }
	                    if(Antibot.invalid.contains(entity)){
	                    	renderColor = Colors.getColor(100,100,100,255);
	                    }
	                    if(FriendManager.isFriend(entity.getName()) && !(entity instanceof EntityPlayerSP)){
	                    	renderColor = Colors.getColor(0,195,255,255);
	                    }
	                    

                double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ((IMinecraft)mc).getTimer().renderPartialTicks;
                double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ((IMinecraft)mc).getTimer().renderPartialTicks;
                double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ((IMinecraft)mc).getTimer().renderPartialTicks;
                Render<Entity> entityRender = mc.getRenderManager().getEntityRenderObject(entity);
				switch(mode){
				case"Box":

            		double x = entity.lastTickPosX
            				+ (entity.posX - entity.lastTickPosX) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();

            		double y = entity.lastTickPosY
            				+ (entity.posY - entity.lastTickPosY) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();

            		double z = entity.lastTickPosZ
            				+ (entity.posZ - entity.lastTickPosZ) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();   
            		EntityLivingBase base = (EntityLivingBase)entity;
            		double widthX = (entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX) / 2 + 0.05;
            		double widthZ = (entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ) / 2 + 0.05;
                	double height = (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY);
                	if(entity instanceof EntityPlayer)
                		height *= 1.1;
                	RenderUtil.pre3D();
                	RenderUtil.glColor(renderColor);
                    for(int i = 0; i < 2; i++){
                    	if(i == 1)
                    	RenderUtil.glColor(Colors.getColor(Color.black));
                        GL11.glLineWidth(3 - i*2);
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        GL11.glVertex3d(x-widthX, y,z-widthZ);
                        GL11.glVertex3d(x-widthX, y,z-widthZ);
                        GL11.glVertex3d(x-widthX, y + height,z-widthZ);
                        GL11.glVertex3d(x+widthX, y + height,z-widthZ);
                        GL11.glVertex3d(x+widthX, y,z-widthZ);
                        GL11.glVertex3d(x-widthX, y,z-widthZ);
                        GL11.glVertex3d(x-widthX, y,z+widthZ);
                        GL11.glEnd();
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        GL11.glVertex3d(x+widthX, y,z+widthZ);
                        GL11.glVertex3d(x+widthX, y + height,z+widthZ);
                        GL11.glVertex3d(x-widthX, y + height,z+widthZ);
                        GL11.glVertex3d(x-widthX, y,z+widthZ);
                        GL11.glVertex3d(x+widthX, y,z+widthZ);
                        GL11.glVertex3d(x+widthX, y,z-widthZ);
                        GL11.glEnd();
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        GL11.glVertex3d(x+widthX, y + height,z+widthZ);
                        GL11.glVertex3d(x+widthX, y + height,z-widthZ);
                        GL11.glEnd();
                        GL11.glBegin(GL11.GL_LINE_STRIP);
                        GL11.glVertex3d(x-widthX, y + height,z+widthZ);
                        GL11.glVertex3d(x-widthX, y + height,z-widthZ);
                        GL11.glEnd();
                    }
                    
                    RenderUtil.post3D();
					 
					break;
					default:
					break;
					}
				}
			}
			RenderUtil.post3D();
        	if(mode.equalsIgnoreCase("2D")){
        		try {
        			updatePositions();
        		} catch (Exception e) {
        		}        
        	}
        	mc.entityRenderer.disableLightmap();
        	RenderHelper.disableStandardItemLighting(); 
        } else if (event instanceof EventRenderEntity) {
        	
        }else if (event instanceof EventRenderGui){
        	if(mode.equalsIgnoreCase("2D")){

                EventRenderGui er = (EventRenderGui) event;
                GlStateManager.pushMatrix();
                ScaledResolution scaledRes = new ScaledResolution(mc);
                double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0D);
                GlStateManager.scale(twoDscale, twoDscale, twoDscale);
                for (Entity ent : entityPositionstop.keySet()) {
                    double[] renderPositions = entityPositionstop.get(ent);
                    double[] renderPositionsBottom = entityPositionsbottom.get(ent);
                    if ((renderPositions[3] > 0.0D) || (renderPositions[3] <= 1.0D)) {
                        GlStateManager.pushMatrix();
                        if (isValid(ent)) {
                            scale(ent);
                            try {
                                float y = (float) renderPositions[1];
                                float endy = (float) renderPositionsBottom[1];
                                float meme = endy - y;
                                float x = (float) renderPositions[0] - (meme / 4f);
                                float endx = (float) renderPositionsBottom[0] + (meme / 4f);
                                if (x > endx) {
                                    endx = x;
                                    x = (float) renderPositionsBottom[0] + (meme / 4f);
                                }
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(2, 2, 2);
                                GlStateManager.popMatrix();
                                GL11.glEnable(GL11.GL_BLEND);
                                GL11.glDisable(GL11.GL_TEXTURE_2D);
                                int color = 0;
                                switch(colormode){
        	                    case"Rainbow":{
        	                    	final Color colorz = Color.getHSBColor(h / 255.0f, 0.6f, 1.0f);
        	            			final int c = colorz.getRGB();
        	                    	color = c;
        	                    }
        	                    break;
        	                    case"Team":{
        	                    String text = ent.getDisplayName().getFormattedText();
        	                	if(Character.toLowerCase(text.charAt(0)) == '§'){
        	                		
        	                    	char oneMore = Character.toLowerCase(text.charAt(1));
        	                    	int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
        	                    	
        	                    	if (colorCode < 16) {
        	                            try {
        	                                int newColor = ((IFontRenderer)mc.fontRendererObj).setColorCode(colorCode);   
        	                                 color = Colors.getColor((newColor >> 16), (newColor >> 8 & 0xFF), (newColor & 0xFF), 255);
        	                            } catch (ArrayIndexOutOfBoundsException ignored) {
        	                            }
        	                        }else{
        	                        }
        	                	}else{
        	                		color = Colors.getColor(255, 255, 255, 255);
        	                	}
        	                    }
        	                    break;
        	                    case"Health":{
        	                    	float health = ((EntityLivingBase)ent).getHealth();
        	                        if (health > 20) {
        	                            health = 20;
        	                        }
        	                        float[] fractions = new float[]{0f, 0.5f, 1f};
        	                        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
        	                        float progress = (health * 5) * 0.01f;
        	                        Color customColor = blendColors(fractions, colors, progress).brighter();
        	                        color = customColor.getRGB();
        	                    }
        	                    break;
        	                    case"Custom":
        	                    	color = new Color(255,255,255).getRGB();
        	                    	break;
        	                    }
        	                    if(Antibot.invalid.contains(ent)){
        	                    	color = Colors.getColor(100,100,100,255);
        	                    }
        	                    
                                RenderUtil.rectangleBordered(x, y, endx, endy, 2.25, Colors.getColor(0, 0, 0, 0), color);
                                RenderUtil.rectangleBordered(x - 0.5, y - 0.5, endx + 0.5, endy + 0.5, 0.9, Colors.getColor(0, 0), Colors.getColor(0));
                                RenderUtil.rectangleBordered(x + 2.5, y + 2.5, endx - 2.5, endy - 2.5, 0.9, Colors.getColor(0, 0), Colors.getColor(0));
                                RenderUtil.rectangleBordered(x - 5, y - 1, x - 1, endy, 1, Colors.getColor(0, 100), Colors.getColor(0, 255));
                                if(ent instanceof EntityPlayer){
                                	 if (!Oxygen.INSTANCE.ModMgr.getModule(Nametags.class).isEnabled() && NAME.getValueState()) {
                                         GlStateManager.pushMatrix();
                                         String renderName = FriendManager.isFriend(ent.getName()) ? FriendManager.getAlias(ent.getName()) : ent.getName();
                                         UnicodeFontRenderer font = Oxygen.INSTANCE.font.wqy18;
                                         float meme2 = ((endx - x) / 2 - (font.getStringWidth(renderName) / 2f));
                                         font.drawStringWithShadow(renderName + " " + (int) mc.thePlayer.getDistanceToEntity(ent) + "m", (x + meme2), (y - font.getStringHeight(renderName) - 5), FriendManager.isFriend(ent.getName()) ? Colors.getColor(192, 80, 64) : -1);
                                         GlStateManager.popMatrix();
                                     }
                                     if (((EntityPlayer) ent).getCurrentEquippedItem() != null && ITEMS.getValueState()) {
                                         GlStateManager.pushMatrix();
                                         GlStateManager.scale(2, 2, 2);
                                         ItemStack stack = ((EntityPlayer) ent).getCurrentEquippedItem();
                                         String customName =((EntityPlayer) ent).getCurrentEquippedItem().getItem().getItemStackDisplayName(stack);
                                         UnicodeFontRenderer font = Oxygen.INSTANCE.font.tahoma12;
                                         float meme2 = ((endx - x) / 2 - (font.getStringWidth(customName) / 1f));
                                         font.drawStringWithShadow(customName, (x + meme2) / 2f, (endy + font.getStringHeight(customName) / 2 * 2f) / 2f, -1);
                                         GlStateManager.popMatrix();
                                     }
                                     if (ARMOR.getValueState()) {
                                         float var1 = (endy - y) / 4;
                                         ItemStack stack = ((EntityPlayer) ent).getEquipmentInSlot(4);
                                         if (stack != null) {
                                             RenderUtil.rectangleBordered(endx + 1, y + 1, endx + 6, y + var1, 1, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                             float diff1 = (y + var1 - 1) - (y + 2);
                                             double percent = 1 - (double) stack.getItemDamage() / (double) stack.getMaxDamage();
                                             RenderUtil.rectangle(endx + 2, y + var1 - 1, endx + 5, y + var1 - 1 - (diff1 * percent), Colors.getColor(78, 206, 229));
                                             mc.fontRendererObj.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "", endx + 7, (y + var1 - 1 - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
                                         }
                                         ItemStack stack2 = ((EntityPlayer) ent).getEquipmentInSlot(3);
                                         if (stack2 != null) {
                                             RenderUtil.rectangleBordered(endx + 1, y + var1, endx + 6, y + var1 * 2, 1, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                             float diff1 = (y + var1 * 2) - (y + var1 + 2);
                                             double percent = 1 - (double) stack2.getItemDamage() * 1 / (double) stack2.getMaxDamage();
                                             RenderUtil.rectangle(endx + 2, (y + var1 * 2), endx + 5, (y + var1 * 2) - (diff1 * percent), Colors.getColor(78, 206, 229));
                                             mc.fontRendererObj.drawStringWithShadow(stack2.getMaxDamage() - stack2.getItemDamage() + "", endx + 7, ((y + var1 * 2) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
                                         }
                                         ItemStack stack3 = ((EntityPlayer) ent).getEquipmentInSlot(2);
                                         if (stack3 != null) {
                                             RenderUtil.rectangleBordered(endx + 1, y + var1 * 2, endx + 6, y + var1 * 3, 1, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                             float diff1 = (y + var1 * 3) - (y + var1 * 2 + 2);
                                             double percent = 1 - (double) stack3.getItemDamage() * 1 / (double) stack3.getMaxDamage();
                                             RenderUtil.rectangle(endx + 2, (y + var1 * 3), endx + 5, (y + var1 * 3) - (diff1 * percent), Colors.getColor(78, 206, 229));
                                             mc.fontRendererObj.drawStringWithShadow(stack3.getMaxDamage() - stack3.getItemDamage() + "", endx + 7, ((y + var1 * 3) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
                                         }
                                         ItemStack stack4 = ((EntityPlayer) ent).getEquipmentInSlot(1);
                                         if (stack4 != null) {
                                             RenderUtil.rectangleBordered(endx + 1, y + var1 * 3, endx + 6, y + var1 * 4, 1, Colors.getColor(28, 156, 179, 100), Colors.getColor(0, 255));
                                             float diff1 = (y + var1 * 4) - (y + var1 * 3 + 2);
                                             double percent = 1 - (double) stack4.getItemDamage() * 1 / (double) stack4.getMaxDamage();
                                             RenderUtil.rectangle(endx + 2, (y + var1 * 4) - 1, endx + 5, (y + var1 * 4) - (diff1 * percent), Colors.getColor(78, 206, 229));
                                             mc.fontRendererObj.drawStringWithShadow(stack4.getMaxDamage() - stack4.getItemDamage() + "", endx + 7, ((y + var1 * 4) - (diff1 / 2)) - (mc.fontRendererObj.FONT_HEIGHT / 2), -1);
                                         }
                                     }
                                }
                               
                                float health = ((EntityLivingBase) ent).getHealth();
                                if(health > 20)
                                	health = 20;
                                float[] fractions = new float[]{0f, 0.5f, 1f};
                                Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                                float progress = (health * 5) * 0.01f;
                                Color customColor = blendColors(fractions, colors, progress).brighter();
                                double healthLocation = endy + (y - endy) * ((health * 5) * 0.01f);
                                RenderUtil.rectangle(x - 4, endy - 1, x - 2, healthLocation, customColor.getRGB());
                            } catch (Exception e) {
                            	//ChatUtil.printChat("§c" + ent);
                            }
                        }
                        GlStateManager.popMatrix();
                        GL11.glColor4f(1, 1, 1, 1);
                    }
                }
                GL11.glScalef(1, 1, 1);
                GL11.glColor4f(1, 1, 1, 1);
                GlStateManager.popMatrix();
            }
          }
        }
    
    public static boolean isValid(Entity entity){
        boolean players = PLAYERS.getValueState();
        boolean invis = INVISIBLES.getValueState();
    	boolean others = ANIMALS.getValueState();
    	boolean valid = entity instanceof EntityMob || entity instanceof EntityIronGolem ||
				entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityPlayer;
    	if(entity.isInvisible() && !invis){

    		return false;
    	}
    	if((players && entity instanceof EntityPlayer) || (others && (entity instanceof EntityMob || entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityIronGolem))){
    		if(entity instanceof EntityPlayerSP){
    			
    			return  Minecraft.getMinecraft().gameSettings.thirdPersonView != 0;
    		}else{
    		
    			return true;
    		}
    	
    	}else{
    		return false;
    	}
    }
    
    private void renderBox(final Entity entity, final double n, final double n2, final double n3) {
        if ((entity.isInvisible() && !this.INVISIBLES.getValueState()) || (KillAura.targets.contains(entity) && ((KillAura)Oxygen.INSTANCE.ModMgr.getModuleByName("KillAura")).esp.getValueState())) {
            return;
        }
        mc.getRenderManager();
        final double n4 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
        mc.getRenderManager();
        final double n5 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
        mc.getRenderManager();
        RenderUtil.drawEntityESP(n4, n5, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ(), entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX - 0.1, entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25, 1.0f, 1.0f, 1.0f, 0.2f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    }
    

public static Color blendColors(float[] fractions, Color[] colors, float progress) {
    Color color = null;
    if (fractions != null) {
        if (colors != null) {
            if (fractions.length == colors.length) {
                int[] indicies = getFractionIndicies(fractions, progress);

                if (indicies[0] < 0 || indicies[0] >= fractions.length || indicies[1] < 0 || indicies[1] >= fractions.length) {
                    return colors[0];
                }
                float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
                Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};

                float max = range[1] - range[0];
                float value = progress - range[0];
                float weight = value / max;

                color = blend(colorRange[0], colorRange[1], 1f - weight);
            } else {
                throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
            }
        } else {
            throw new IllegalArgumentException("Colours can't be null");
        }
    } else {
        throw new IllegalArgumentException("Fractions can't be null");
    }
    return color;
}

public static int[] getFractionIndicies(float[] fractions, float progress) {
    int[] range = new int[2];

    int startPoint = 0;
    while (startPoint < fractions.length && fractions[startPoint] <= progress) {
        startPoint++;
    }

    if (startPoint >= fractions.length) {
        startPoint = fractions.length - 1;
    }

    range[0] = startPoint - 1;
    range[1] = startPoint;

    return range;
}

public static Color blend(Color color1, Color color2, double ratio) {
    float r = (float) ratio;
    float ir = (float) 1.0 - r;

    float rgb1[] = new float[3];
    float rgb2[] = new float[3];

    color1.getColorComponents(rgb1);
    color2.getColorComponents(rgb2);

    float red = rgb1[0] * r + rgb2[0] * ir;
    float green = rgb1[1] * r + rgb2[1] * ir;
    float blue = rgb1[2] * r + rgb2[2] * ir;

    if (red < 0) {
        red = 0;
    } else if (red > 255) {
        red = 255;
    }
    if (green < 0) {
        green = 0;
    } else if (green > 255) {
        green = 255;
    }
    if (blue < 0) {
        blue = 0;
    } else if (blue > 255) {
        blue = 255;
    }

    Color color = null;
    try {
        color = new Color(red, green, blue);
    } catch (IllegalArgumentException exp) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
        exp.printStackTrace();
    }
    return color;
}

private void updatePositions() {
    entityPositionstop.clear();
    entityPositionsbottom.clear();
    float pTicks = ((IMinecraft)mc).getTimer().renderPartialTicks;
    for (Object o : mc.theWorld.getLoadedEntityList()) {
  
        if (o instanceof EntityLivingBase && o != mc.thePlayer) {
          	EntityLivingBase ent = (EntityLivingBase)o;
            double x;
            double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
            double z;
            x = ent.lastTickPosX + ((ent.posX + 10) - (ent.lastTickPosX + 10)) * pTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
            z = ent.lastTickPosZ + ((ent.posZ + 10) - (ent.lastTickPosZ + 10)) * pTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
            y += ent.height + 0.5D;
            double[] convertedPoints = convertTo2D(x, y, z);
            double xd = Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]);
            assert convertedPoints != null;
            if ((convertedPoints[2] >= 0.0D) && (convertedPoints[2] < 1.0D)) {
                entityPositionstop.put(ent, new double[]{convertedPoints[0], convertedPoints[1], xd, convertedPoints[2]});
                y = ent.lastTickPosY + ((ent.posY - 2.2) - (ent.lastTickPosY - 2.2)) * pTicks - mc.getRenderManager().viewerPosY;
                entityPositionsbottom.put(ent, new double[]{convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1], xd, convertTo2D(x, y, z)[2]});
            }
        }
    }
}

private double[] convertTo2D(double x, double y, double z, Entity ent) {
    return convertTo2D(x, y, z);
}

private void scale(Entity ent) {
    float scale = (float) 1;
    float target = scale * (mc.gameSettings.fovSetting
            / (mc.gameSettings.fovSetting/*
     * *
     * mc.thePlayer.getFovModifier()
     *//* .func_175156_o() */));
    if ((this.gradualFOVModifier == 0.0D) || (Double.isNaN(this.gradualFOVModifier))) {
        this.gradualFOVModifier = target;
    }
    this.gradualFOVModifier += (target - this.gradualFOVModifier) / (Minecraft.getDebugFPS() * 0.7D);

    scale = (float) (scale * this.gradualFOVModifier);

    GlStateManager.scale(scale, scale, scale);
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
