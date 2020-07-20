package me.Oxygen.injection.mixins;

import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.IChatComponent;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen {
	
	@Shadow
	private GuiTextField inputField;
	
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {    	
   	    drawRect(1, this.height - 14, mc.fontRendererObj.getStringWidth(inputField.getText()) + 12, this.height - 1, RenderUtil.reAlpha(Colors.BLACK.c, 0.5F));
        RenderUtil.drawRect(1.0F, (float)(this.height - 14), 2.5F, (float)(this.height - 1), RenderUtil.rainbow(50));
        this.inputField.drawTextBox();
        IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null)
        {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
