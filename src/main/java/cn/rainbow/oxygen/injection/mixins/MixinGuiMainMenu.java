package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.gui.mainmenu.MainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

@Mixin({GuiMainMenu.class })
public class MixinGuiMainMenu {

    @Inject(method = { "initGui" }, at = { @At("HEAD") }, cancellable = true)
    public void onInit(final CallbackInfo ci) {
        Minecraft.getMinecraft().displayGuiScreen(new MainMenu());
        ci.cancel();
    }

}
