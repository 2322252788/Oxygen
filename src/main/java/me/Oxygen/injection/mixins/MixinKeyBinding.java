package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.injection.interfaces.IKeyBinding;
import net.minecraft.client.settings.KeyBinding;

@Mixin({KeyBinding.class})
public class MixinKeyBinding implements IKeyBinding
{
    @Shadow
    private boolean pressed;
    
    @Override
    public boolean getPress() {
        return this.pressed;
    }
    
    @Override
    public void setPress(final Boolean b) {
        this.pressed = b;
    }
}
