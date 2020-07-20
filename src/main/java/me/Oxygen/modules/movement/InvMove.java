package me.Oxygen.modules.movement;


import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventTick;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@ModuleRegister(name = "InvMove", category = Category.MOVEMENT)
public class InvMove extends Module{
	
	boolean inInventory = false;
	
	@EventTarget(events = {EventTick.class})
	   private void onEvent(Event event) {
		if(event instanceof EventTick) {
			if(!(mc.currentScreen instanceof GuiChat)) {
		         if(mc.currentScreen != null) {
		            KeyBinding[] moveKeys = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump};
		            KeyBinding[] var6 = moveKeys;
		            int var5 = moveKeys.length;

		            for(int var4 = 0; var4 < var5; ++var4) {
		               KeyBinding bind = var6[var4];
		               KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
		            }

		            if(!this.inInventory) {
		               this.inInventory = !this.inInventory;
		            }
		         } else if(this.inInventory) {
		            this.inInventory = !this.inInventory;
		         }

		      }
		}
	   }

}
