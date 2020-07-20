package me.Oxygen.ui.clickgui4;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import me.Oxygen.Oxygen;
import me.Oxygen.manager.ModuleManager;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.utils.fontRenderer.UnicodeFontRenderer;
import me.Oxygen.value.Value;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ResourceLocation;

public class WorstClickGui extends GuiScreen implements GuiYesNoCallback {
	public static Category currentModuleType = Category.COMBAT;
	public static Module currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
			? ModuleManager.getModulesInType(currentModuleType).get(0)
			: null;
	public static float startX = 100, startY = 100;
	public Opacity opacity = new Opacity(0);
	public int moduleStart = 0;
	public int valueStart = 0;

	boolean previousmouse = true;
	boolean mouse;
	public int opacityx = 255;
	public float moveX = 0, moveY = 0;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (isHovered(startX, startY - 25, startX + 400, startY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
			if (moveX == 0 && moveY == 0) {
				moveX = mouseX - startX;
				moveY = mouseY - startY;
			} else {
				startX = mouseX - moveX;
				startY = mouseY - moveY;
			}
			this.previousmouse = true;
		} else if (moveX != 0 || moveY != 0) {
			moveX = 0;
			moveY = 0;
		}
	
		RenderUtil.drawRoundedRect(startX+44, startY, startX + 380, startY + 260,3F,
				new Color(32, 34, 37, 255).getRGB());
		RenderUtil.drawRoundedRect(startX+46, startY+2, startX + 378, startY + 5,1F,
				new Color(11, 84, 150,255).getRGB());
		RenderUtil.drawRoundedRect(startX+85, startY+22, startX + 379, startY + 260,3F,
				new Color(47, 49, 54, 255).getRGB());
		RenderUtil.drawRoundedRect(startX+190, startY+24, startX + 194, startY + 258,1F,
				new Color(54, 57, 62, 255).getRGB());
		RenderUtil.drawImage(new ResourceLocation("Client/aw2.png"), (int)startX+48, (int)startY+20,35,35);
		Oxygen.INSTANCE.font.tahoma20.drawString("A", startX + 307, startY + 7,(new Color(11, 84, 150)).getRGB());     
		Oxygen.INSTANCE.font.tahoma20.drawString("zureWare", startX + 315, startY + 7,(new Color(255, 255, 255)).getRGB());      
		for (int i = 0; i < Category.values().length; i++) {
			Category[] iterator = Category.values();
			if (iterator[i] != currentModuleType) {
			//	RenderUtil.drawFilledCircle(startX + 65, startY + 70 + i * 35, 15,
			//			new Color(56, 56, 56, 255).getRGB(), 5);
			} else {
				//RenderUtil.drawFilledCircle(startX + 65, startY + 70 + i * 35, 15,
				//		new Color(101, 81, 255, 255).getRGB(), 5);
			}
			try {
				if (this.isCategoryHovered(startX +50, startY + 55 + i * 35, startX + 80, startY + 85 + i * 35, mouseX,
						mouseY) && Mouse.isButtonDown((int) 0)) {
					currentModuleType = iterator[i];
					currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
							? ModuleManager.getModulesInType(currentModuleType).get(0)
							: null;
					moduleStart = 0;
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		//System.out.println(currentModule.getValues().size());

		int m = Mouse.getDWheel();
		if (this.isCategoryHovered(startX + 60, startY, startX + 200, startY + 320, mouseX, mouseY)) {
			if (m < 0 && moduleStart < ModuleManager.getModulesInType(currentModuleType).size() - 1) {
				moduleStart++;
			}
			if (m > 0 && moduleStart > 0) {
				moduleStart--;
			}
		}
		if (this.isCategoryHovered(startX + 200, startY, startX + 420, startY + 320, mouseX, mouseY)) {
			if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
				valueStart++;
			}
			if (m > 0 && valueStart > 0) {
				valueStart--;
			}
		}
		Oxygen.INSTANCE.font.simpleton16.drawString(
				currentModule == null ? currentModuleType.toString()
						: currentModuleType.toString() + ":" + currentModule.getName(),
				startX + 90, startY + 25, new Color(248, 248, 248).getRGB());
		if (currentModule != null) {
			float mY = startY + 30;
			for (int i = 0; i < ModuleManager.getModulesInType(currentModuleType).size(); i++) {
				Module module = ModuleManager.getModulesInType(currentModuleType).get(i);
				if (mY > startY + 240)
					break;
				if (i < moduleStart) {
					continue;
				}

				
				RenderUtil.drawRect(startX +90, mY+24 , startX + 175, mY + 25,new Color(26, 26, 26).getRGB());
				Oxygen.INSTANCE.font.simpleton20.drawString(module.getName(), startX + 108, mY + 11,
						new Color(130,130,130, (int) opacity.getOpacity()).getRGB());
				RenderUtil.circle(startX +95, mY+16 , 2, new Color(152,152,152).getRGB());
				if (module.isEnabled()) {
				//	RenderUtil.drawRoundedRect(startX +88, mY+8 , startX + 175, mY + 25,8F, new Color(210, 210, 210).getRGB());
					Oxygen.INSTANCE.font.simpleton20.drawString(module.getName(), startX + 108, mY + 11,
							new Color(152,152,152, (int) opacity.getOpacity()).getRGB());
					RenderUtil.circle(startX +95, mY+16 , 2, new Color(0, 100, 237).getRGB());
				}
				if (isSettingsButtonHovered(startX + 90, mY,
						startX + 100 + (Oxygen.INSTANCE.font.simpleton20.getStringWidth(module.getName())),
						mY + 8 + Oxygen.INSTANCE.font.simpleton20.FONT_HEIGHT, mouseX, mouseY)) {
					if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
						if (module.isEnabled()) {
							module.set(false);
						} else {
							module.set(true);
						}
						previousmouse = true;
					}
					if (!this.previousmouse && Mouse.isButtonDown((int) 1)) {
						previousmouse = true;
					}
				}

				if (!Mouse.isButtonDown((int) 0)) {
					this.previousmouse = false;
				}
				if (isSettingsButtonHovered(startX + 90, mY,
						startX + 100 + (Oxygen.INSTANCE.font.simpleton20.getStringWidth(module.getName())),
						mY + 8 + Oxygen.INSTANCE.font.simpleton20.FONT_HEIGHT, mouseX, mouseY) && Mouse.isButtonDown((int) 1)) {
					currentModule = module;
					valueStart = 0;
				}
				mY += 18;
			}
			mY = startY + 30;
			for (int i = 0; i < currentModule.getValues().size(); i++) {
				if (mY > startY + 215)
					break;
				if (i < valueStart) {
					continue;
				}
				UnicodeFontRenderer font = Oxygen.INSTANCE.font.simpleton17;
				Value value = currentModule.getValues().get(i);
				if (value.isValueDouble||value.isValueByte||value.isValueFloat||value.isValueInteger||value.isValueLong) {
					float x = startX + 300;
					double state = (double) value.getValueState();
					double min = (double) value.getValueMin();
					double max = (double) value.getValueMax();
					double render =  (68.0F * ((state - min) / (max - min)));
					RenderUtil.drawRect((float) x - 6, mY + 2, (float) ((double) x + 75), mY + 3,
							(new Color(20, 20, 20, 255)).getRGB());
					RenderUtil.drawRect((float) x - 6, mY + 2, (float) ((double) x + render + 6.5D), mY + 3,
							(new Color(0, 100, 237, 255)).getRGB());
					RenderUtil.circle((float)(x + render + 4), (float)mY+2, 2F, new Color(0, 100, 237).getRGB());
					font.drawString(value.getDisplayTitle() + ": " + value.getValueState(), startX + 200, mY,new Color(152,152,152).getRGB());
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}

					if (this.isButtonHovered(x, mY - 2, x + 100, mY + 7, mouseX, mouseY)
							&& Mouse.isButtonDown((int) 0)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
							render = (double) value.getValueMin();
							max = (double) value.getValueMax();
							double inc = 0.1;
							double valAbs = (double) mouseX - ((double) x + 1.0D);
							double perc = valAbs / 68.0D;
							perc = Math.min(Math.max(0.0D, perc), 1.0D);
							double valRel = (max - render) * perc;
							double val = render + valRel;
							val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
							double num =Double.valueOf(val);
							value.setValueState((double)num);
						}
						if (!Mouse.isButtonDown((int) 0)) {
							this.previousmouse = false;
						}
					}
					mY += 20;
				}


				if (value.isValueBoolean) {
					float x = startX + 290;
					font.drawString(value.getDisplayTitle(), startX + 200, mY, new Color(152,152,152).getRGB());
					//RenderUtil.drawRect(x + 56, mY , x + 76, mY + 9,
						//	new Color(240, 240, 240, (int) opacity.getOpacity()).getRGB());
					
					if ((boolean) value.getValueState()) {
						RenderUtil.drawRoundedRect(x + 56, mY-1 , x + 76, mY + 9,4F, new Color(58, 58, 58).getRGB());
						//RenderUtil.drawRect(x + 56, mY-1 , x + 76, mY + 9,
							//	new Color(220, 220, 255, (int) opacity.getOpacity()).getRGB());
						RenderUtil.circle(x+72, mY+4, 4, new Color(0, 100, 237).getRGB());
					} else {
						RenderUtil.drawRoundedRect(x + 56, mY-1 , x + 76, mY + 9,4F, new Color(58, 58, 58).getRGB());
						RenderUtil.circle(x+60, mY+4, 4, new Color(152,152,152).getRGB());
					}
					if (this.isCheckBoxHovered(x + 56, mY, x + 76, mY + 9, mouseX, mouseY)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
							this.previousmouse = true;
							this.mouse = true;
						}

						if (this.mouse) {
							value.setValueState(!(boolean) value.getValueState());
							this.mouse = false;
						}
					}
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}
					mY += 20;
				}
				if (value.isValueMode) {
					float x = startX + 280;
					font.drawString(value.getDisplayTitle(), startX + 200, mY, new Color(152, 152, 152, (int) opacity.getOpacity()).getRGB() );
					RenderUtil.drawRect(x +10-1, mY-2-1, x + 75+1, mY + 12+1,
							new Color(11, 84, 150).getRGB() );
					RenderUtil.drawRect(x +10, mY-2, x + 75, mY + 12,
							new Color(60, 60, 60).getRGB() );
					
					
					font.drawStringWithShadow(value.getModeAt((value).getCurrentMode()),
							(float) (x + 40 - font.getStringWidth(value.getModeAt((value).getCurrentMode())) / 2), mY, -1);
					if (this.isStringHovered(x, mY - 5, x + 100, mY + 15, mouseX, mouseY)) {
						if (Mouse.isButtonDown((int) 0) && !this.previousmouse) {
							String current = value.getModeAt((value).getCurrentMode());
							int next = 0;
							if((value).getCurrentMode()+1<(value).listModes().size()){
								next = (value).getCurrentMode()+1;
							}
							value.setCurrentMode(next);
							this.previousmouse = true;
						}
						if (!Mouse.isButtonDown((int) 0)) {
							this.previousmouse = false;
						}

					}
					mY += 25;
				}
				 
				
			
			}
		RenderUtil.drawFilledCircle(startX + 62, startY + 70 , 15,
								new Color(56, 56, 56, 255).getRGB(), 5);
		RenderUtil.drawFilledCircle(startX + 62, startY + 102 , 15,
				new Color(56, 56, 56, 255).getRGB(), 5);
		RenderUtil.drawFilledCircle(startX + 62, startY + 137 , 15,
				new Color(56, 56, 56, 255).getRGB(), 5);
		RenderUtil.drawFilledCircle(startX + 62, startY + 172 , 15,
				new Color(56, 56, 56, 255).getRGB(), 5);
		RenderUtil.drawFilledCircle(startX + 62, startY + 205 , 15,
				new Color(56, 56, 56, 255).getRGB(), 5);
			switch(currentModuleType.name()) {
		  	case "COMBAT":
		  		RenderUtil.drawImage(new ResourceLocation("Client/aw.png"), (int)startX+20, (int)startY+60,20,20);
		  		RenderUtil.drawFilledCircle(startX + 62, startY + 70 , 15,
						new Color(0, 100, 237, 180).getRGB(), 5);
		   	RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/"+currentModuleType+2+".png"), (int)startX+51, (int)startY+60,22,22);
			RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Render.png"), (int)startX+51, (int)startY+90,22,22);
			RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Movement.png"), (int)startX+51, (int)startY+125,22,22);
			RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Player.png"), (int)startX+51, (int)startY+160,22,22);
			RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/World.png"), (int)startX+51, (int)startY+195,22,22);
		  		break;
		  	case "RENDER":
		  		RenderUtil.drawImage(new ResourceLocation("Client/aw.png"), (int)startX+20, (int)startY+90,20,20);
		  		RenderUtil.drawFilledCircle(startX + 62, startY + 102 , 15,
		  				new Color(0, 100, 237, 180).getRGB(), 5);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Combat.png"), (int)startX+51, (int)startY+60,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/"+currentModuleType+2+".png"), (int)startX+51, (int)startY+90,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Movement.png"), (int)startX+51, (int)startY+125,22,22);
RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Player.png"), (int)startX+51, (int)startY+160,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/World.png"), (int)startX+51, (int)startY+195,22,22);
		  		break;
		  	case "MOVEMENT":
		  		RenderUtil.drawImage(new ResourceLocation("Client/aw.png"), (int)startX+20, (int)startY+125,20,20);
		  		RenderUtil.drawFilledCircle(startX + 62, startY + 137 , 15,
		  				new Color(0, 100, 237, 180).getRGB(), 5);
		  		
		  		RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Combat.png"), (int)startX+51, (int)startY+60,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Render.png"), (int)startX+51, (int)startY+90,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/"+currentModuleType+2+".png"),  (int)startX+51, (int)startY+125,22,22);
RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Player.png"), (int)startX+51, (int)startY+160,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/World.png"), (int)startX+51, (int)startY+195,22,22);
		  		break;
		  	case "PLAYER":
		  		RenderUtil.drawImage(new ResourceLocation("Client/aw.png"), (int)startX+20, (int)startY+165,20,20);
		  		RenderUtil.drawFilledCircle(startX + 62, startY + 172 , 15,
		  				new Color(0, 100, 237, 180).getRGB(), 5);
		  		RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Combat.png"), (int)startX+51, (int)startY+60,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Render.png"), (int)startX+51, (int)startY+90,22,22);
				
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Movement.png"),  (int)startX+51, (int)startY+125,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/"+currentModuleType+2+".png"), (int)startX+51, (int)startY+160,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/World.png"), (int)startX+51, (int)startY+195,22,22);
		  		break;
		  	case "WORLD":
		  	
		  		RenderUtil.drawImage(new ResourceLocation("Client/aw.png"), (int)startX+20, (int)startY+195,20,20);
		  		RenderUtil.drawFilledCircle(startX + 62, startY + 205 , 15,
		  				new Color(0, 100, 237, 180).getRGB(), 5);
		  		RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Combat.png"), (int)startX+51, (int)startY+60,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Render.png"), (int)startX+51, (int)startY+90,22,22);
				
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Movement.png"),  (int)startX+51, (int)startY+125,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/Player.png"), (int)startX+51, (int)startY+160,22,22);
				RenderUtil.drawImage(new ResourceLocation("Oxygen/clickui/"+currentModuleType+2+".png"), (int)startX+51, (int)startY+195,22,22);
		  		break;
			}
		}

	}

	public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

}
