package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.injection.interfaces.IS27PacketExplosion;
import net.minecraft.network.play.server.S27PacketExplosion;

@Mixin(S27PacketExplosion.class)
public class MixinS27PacketExplosion implements IS27PacketExplosion{
	
	@Shadow
	private float field_149152_f;
	
	@Shadow
    private float field_149153_g;
	
	@Shadow
    private float field_149159_h;

	@Override
	public float getField_149152_f() {
		return field_149152_f;
	}

	@Override
	public float getField_149153_g() {
		return field_149153_g;
	}

	@Override
	public float getField_149159_h() {
		return field_149159_h;
	}

	@Override
	public void setField_149152_f(float a) {
		this.field_149152_f = a;
	}

	@Override
	public void setField_149153_g(float b) {
		this.field_149153_g = b;
	}

	@Override
	public void setField_149159_h(float c) {
		this.field_149159_h = c;
	}

}
