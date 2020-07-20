package me.Oxygen.modules;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.Oxygen.Oxygen;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.render.HUD;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import me.Oxygen.utils.other.ChatUtils;
import me.Oxygen.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Module {
	
	//Name
	public String name;
	
	//Category
	private Category category;
	
	//Keycode
    public int keyCode;
    
    //Enabled
    private boolean enabled;
    
    //DisplayName
  	public String displayName = "";
  	
  	//Value
  	public boolean openValues;
	public double arrowAnlge;
  	public double hoverOpacity;
	
	//Minecraft instance
	protected Minecraft mc = Minecraft.getMinecraft();

	public int valueSize;
	
	public Module() {
		this.name = ((ModuleRegister)this.getClass().getAnnotation(ModuleRegister.class)).name();
		this.category = ((ModuleRegister)this.getClass().getAnnotation(ModuleRegister.class)).category();
		this.keyCode = ((ModuleRegister)this.getClass().getAnnotation(ModuleRegister.class)).keybind();
		this.enabled = ((ModuleRegister)this.getClass().getAnnotation(ModuleRegister.class)).enabled();
	}
	
	public Module(String name, Category category) {
		this.name = name;
		this.category = category;
		this.keyCode = 0;
		this.enabled = false;
	}
	
    public void onEnable() {}
	
	public void onDisable() {}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public boolean isCategory(Category category) {
		return this.category == category;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void toggle() {
		if(this.enabled) {
			this.set(false);
		}else if(!this.enabled) {
			this.set(true);
		}
	}
	
    public void set(boolean state) {
    	
		if(name.equalsIgnoreCase("Animation")) {
			return;
		}
		
		this.enabled = state;
		
		if(state) {
			if(mc.theWorld != null) {
				mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
				if(HUD.toogleinfo.getValueState()) {
        		ClientUtil.sendClientMessage(getName() + " onEnable",Type.SUCCESS);
				}
				this.onEnable();
			}
			Oxygen.INSTANCE.EventMgr.register(this);
		} else {
			if(mc.theWorld != null) {				
        		mc.thePlayer.playSound("random.click", 1.0F,0.8F);
        		if(HUD.toogleinfo.getValueState()) {
        		ClientUtil.sendClientMessage(getName() + " onDisable",Type.ERROR);
        		}
				this.onDisable();
			}
			Oxygen.INSTANCE.EventMgr.unregister(this);
		}
	}
    
    public List<Value> getValues() {
		return Value.list;
	}
    
    public boolean hasValues() {
        for (Value value : Value.list) {
            String name = value.getValueName().split("_")[0];
            if (!name.equalsIgnoreCase(this.getName())) continue;
            return true;
        }
        return false;
    }
    
    public boolean isServer(String server) {
		if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains(server)) {
			return true;
		}
		return false;
	}
    
    public void tellPlayer(String text, String title) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                ChatFormatting.WHITE + "[" + ChatFormatting.RED + Oxygen.INSTANCE.CLIENT_NAME + ChatFormatting.WHITE + "]" + "\247a[" + title + "] " + "\247e" + text));
    }
    
    public static void sendMessage(String message) {
		new ChatUtils.ChatMessageBuilder(true, true).appendText(message).setColor(EnumChatFormatting.WHITE).build()
				.displayClientSided();
	}

}
