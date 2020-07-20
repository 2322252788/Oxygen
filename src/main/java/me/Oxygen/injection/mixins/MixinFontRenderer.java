package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.injection.interfaces.IFontRenderer;
import net.minecraft.client.gui.FontRenderer;

@Mixin(FontRenderer.class)
public class MixinFontRenderer implements IFontRenderer{
	
	@Shadow
	private int[] colorCode;

	@Override
	public int[] getColorCode() {
		return colorCode;
	}

	@Override
	public int setColorCode(int is) {
		colorCode = new int[is];
		return Integer.parseInt(String.valueOf(colorCode));
	}

	/*@Override
	public int setColorCode(int i) {
		colorCode = new int[] {i};
	}*/

}
