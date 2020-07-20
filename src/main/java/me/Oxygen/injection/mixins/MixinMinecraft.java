package me.Oxygen.injection.mixins;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.Oxygen.Oxygen;
import me.Oxygen.event.events.EventClickMouse;
import me.Oxygen.event.events.EventKey;
import me.Oxygen.event.events.EventTick;
import me.Oxygen.injection.interfaces.IMinecraft;
import me.Oxygen.manager.ModuleManager;
import me.Oxygen.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IMinecraft {
	
	@Shadow
	private int rightClickDelayTimer;
	
	@Shadow
    private int leftClickCounter;
	
	@Shadow
    private Session session;
	
	@Shadow
	private Timer timer;
	
	@Shadow
    private LanguageManager mcLanguageManager;
	
	@Shadow
	private GuiScreen currentScreen;
	
	@Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
	private void startGame(CallbackInfo callbackInfo) {
		Oxygen.INSTANCE.Start();
	}
	
	@Inject(method = "shutdownMinecraftApplet", at = @At("HEAD"))
	private void stopClient(CallbackInfo callbackInfo) {
		Oxygen.INSTANCE.Stop();
	}
	
	@Inject(method = "clickMouse", at = @At("HEAD"))
	private void clickMouse(final CallbackInfo ci) {
	    new EventClickMouse().call();
	}
	
	@Inject(method = "runTick", at = @At("HEAD"))
	private void eventTick(CallbackInfo callbackInfo) {
		new EventTick().call();
	}
	
	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
	private void onKey(CallbackInfo callbackInfo) {
		
		int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
		if (Keyboard.getEventKeyState() && this.currentScreen == null) {
			new EventKey(k).call();
			for(Module m : ModuleManager.modules) {
        		if(k == m.getKeyCode()) {
        			m.set(!m.isEnabled());
        		}
        	}
		}		
	}
	
	@Shadow
    protected abstract void clickMouse();

	@Override
	public Timer getTimer() {
		return this.timer;
	}

	@Override
	public Session getSession() {
		return this.session;
	}

	@Override
	public void setSession(final Session session) {
		this.session = session;
		
	}

	@Override
	public LanguageManager getLanguageManager() {
		return this.mcLanguageManager;
	}

	@Override
	public void setRightClickDelayTimer(int i) {
		this.rightClickDelayTimer = i;
	}

	@Override
	public void setClickCounter(final int i) {
		this.leftClickCounter = i;
	}

	@Override
	public void runCrinkMouse() {
		this.clickMouse();
	}

}
