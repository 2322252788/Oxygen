package cn.rainbow.oxygen.module.modules.render;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.Render2DEvent;
import cn.rainbow.oxygen.event.events.Render3DEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.ModuleInfo;
import cn.rainbow.oxygen.module.modules.client.TargetEntity;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.ModeValue;
import cn.rainbow.oxygen.utils.EntityUtils;
import cn.rainbow.oxygen.utils.render.ESP2D;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "ESP", category = Category.Render)
public class ESP extends Module {
	
	private final ModeValue mode = new ModeValue("Mode", "Box", new String[]{"Box", "2D"});
	public final ModeValue healthColor = new ModeValue("HPColor", "Health", new String[]{"Health", "Static"});
    public final BooleanValue health = new BooleanValue("HPBar", true);
    public final BooleanValue armor = new BooleanValue("Armor", true);
    public final BooleanValue box = new BooleanValue("Box", true);
    public final BooleanValue tags = new BooleanValue("Tags", true);

    public static Color espColours = Color.GRAY;
    public static Color healthColors = Color.GREEN;
	
	@EventTarget(events = {Render3DEvent.class, Render2DEvent.class})
	public void onRender(Event event) {
		if(event instanceof Render3DEvent) {
			Render3DEvent er3 = (Render3DEvent) event;
            this.setDisplayName(this.mode.getCurrentValue());
			if(this.mode.isCurrentMode("Box")) {//Box
				this.doBoxESP(er3);
			}
			if (this.mode.isCurrentMode("2D")) {
				ESP2D.INSTANCE.updatePositions(mc);
			}
		}
		if (event instanceof Render2DEvent) {
			if(this.mode.isCurrentMode("2D")) {//2D
				ESP2D.INSTANCE.renderBox(mc);
			}
		}
	}
	
	private void doBoxESP(Render3DEvent event) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
		TargetEntity target = (TargetEntity) Oxygen.INSTANCE.moduleManager.getModule(TargetEntity.class);
        for (Entity o : this.mc.theWorld.loadedEntityList) {
            if(EntityUtils.isSelected(o, false)) {
            	 if(o instanceof EntityLivingBase && ((EntityLivingBase) o).hurtTime > 0) {
            		RenderUtil.entityESPBox(o, new Color(255,0,0), event);
            	} else if(o.isInvisible() && target.getInvis().getCurrentValue()) {
            		RenderUtil.entityESPBox(o, new Color(255,255,0), event);
            	} else {
            		RenderUtil.entityESPBox(o, new Color(255,255,255), event);
            	}
            }
        }
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
	
	public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
	      drawRect(x, y, x2, y2, col2);
	      float f = (float)(col1 >> 24 & 255) / 255.0F;
	      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
	      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
	      float f3 = (float)(col1 & 255) / 255.0F;
	      GL11.glEnable(3042);
	      GL11.glDisable(3553);
	      GL11.glBlendFunc(770, 771);
	      GL11.glEnable(2848);
	      GL11.glPushMatrix();
	      GL11.glColor4f(f1, f2, f3, f);
	      GL11.glLineWidth(l1);
	      GL11.glBegin(1);
	      GL11.glVertex2d((double)x, (double)y);
	      GL11.glVertex2d((double)x, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y);
	      GL11.glVertex2d((double)x, (double)y);
	      GL11.glVertex2d((double)x2, (double)y);
	      GL11.glVertex2d((double)x, (double)y2);
	      GL11.glVertex2d((double)x2, (double)y2);
	      GL11.glEnd();
	      GL11.glPopMatrix();
	      GL11.glEnable(3553);
	      GL11.glDisable(3042);
	      GL11.glDisable(2848);
	   }
	
	public static void drawRect(float g, float h, float i, float j, int col1) {
	      float f = (float)(col1 >> 24 & 255) / 255.0F;
	      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
	      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
	      float f3 = (float)(col1 & 255) / 255.0F;
	      GL11.glEnable(3042);
	      GL11.glDisable(3553);
	      GL11.glBlendFunc(770, 771);
	      GL11.glEnable(2848);
	      GL11.glPushMatrix();
	      GL11.glColor4f(f1, f2, f3, f);
	      GL11.glBegin(7);
	      GL11.glVertex2d((double)i, (double)h);
	      GL11.glVertex2d((double)g, (double)h);
	      GL11.glVertex2d((double)g, (double)j);
	      GL11.glVertex2d((double)i, (double)j);
	      GL11.glEnd();
	      GL11.glPopMatrix();
	      GL11.glEnable(3553);
	      GL11.glDisable(3042);
	      GL11.glDisable(2848);
	}
	
	public void pre() {
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public void post() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
    }
}
