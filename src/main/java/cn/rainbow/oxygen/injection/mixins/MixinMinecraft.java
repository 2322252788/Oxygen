package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.Helper;
import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.events.ClickMouseEvent;
import cn.rainbow.oxygen.event.events.KeyEvent;
import cn.rainbow.oxygen.event.events.TickEvent;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.modules.client.Rotations;
import cn.rainbow.oxygen.utils.rotation.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.Session;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    public Session session;

    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    private void runGameLoop(CallbackInfo ci) {
        Helper.onGameLoop();
    }

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    private void startGame(CallbackInfo callbackInfo) {
        new Oxygen();
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At("HEAD"))
    private void stopClient(CallbackInfo callbackInfo) {
        Oxygen.INSTANCE.shutdown();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void eventTick(CallbackInfo callbackInfo) {
        new TickEvent().call();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    private void onKey(CallbackInfo callbackInfo) {
        int k = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        if (Keyboard.getEventKeyState() && this.currentScreen == null) {
            new KeyEvent(k).call();
            for(Module m : Oxygen.INSTANCE.moduleManager.getModules()) {
                if(k == m.getKeyCode()) {
                    m.setEnabled(!m.getEnabled());
                }
            }
        }
    }

    @Inject(method = "clickMouse", at = @At("HEAD"))
    private void clickMouse(CallbackInfo ci) {
        new ClickMouseEvent(0).call();
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"))
    private void rightClickMouse(CallbackInfo ci) {
        new ClickMouseEvent(1).call();
    }

    @Inject(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void createDisplay(CallbackInfo callbackInfo) {
        Display.setTitle(Oxygen.name + " " + Oxygen.version + (Oxygen.DEV_MODE ? " | DevMode" : ""));
    }

    @Inject(method = "getRenderViewEntity", at = @At("HEAD"))
    public void getRenderViewEntity(CallbackInfoReturnable<Entity> cir) {
        if (RotationUtils.targetRotation != null && thePlayer != null) {
            final Rotations rotations = (Rotations) Oxygen.INSTANCE.moduleManager.getModule(Rotations.class);
            final float yaw = RotationUtils.targetRotation.getYaw();
            if (rotations.getHeadValue().getCurrentValue()) {
                thePlayer.rotationYawHead = yaw;
            }
            if (rotations.getBodyValue().getCurrentValue()) {
                thePlayer.renderYawOffset = yaw;
            }
        }
    }
}
