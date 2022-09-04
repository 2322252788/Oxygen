package cn.rainbow.oxygen.injection.mixins;

import java.util.List;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.events.ChatEvent;
import cn.rainbow.oxygen.gui.font.fluxfont.FontUtils;
import cn.rainbow.oxygen.module.modules.render.HUD;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat extends Gui {
	
	@Shadow
	@Final
	private Minecraft mc;
	
	@Shadow
	@Final
	private List<ChatLine> drawnChatLines;
	
	@Shadow
	private int scrollPos;
	
	@Shadow
    private boolean isScrolled;

    private String lastMessage;
    private int sameMessageAmount;
    private int line;

    @Shadow
    public abstract void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId);

    /**
     * @author Liuli
     */
    @Overwrite
    public void printChatMessage(IChatComponent chatComponent) {
        final HUD hud = (HUD) Oxygen.INSTANCE.moduleManager.getModule("HUD");
        if (!hud.getChatCombine().getCurrentValue()) {
            this.printChatMessageWithOptionalDeletion(chatComponent, 0);
            return;
        }

        String text = fixString(chatComponent.getFormattedText());

        if (text.equals(this.lastMessage)) {
            (Minecraft.getMinecraft()).ingameGUI.getChatGUI().deleteChatLine(this.line);
            this.sameMessageAmount++;
            chatComponent.appendText(ChatFormatting.WHITE + " [" + "x" + this.sameMessageAmount + "]");
        } else {
            this.sameMessageAmount = 1;
        }

        this.lastMessage = text;
        this.line++;
        if (this.line > 256)
            this.line = 0;

        printChatMessageWithOptionalDeletion(chatComponent, this.line);
    }

    private String fixString(String str) {
        str = str.replaceAll("\uF8FF", "");//remove air chars

        StringBuilder sb=new StringBuilder();
        for (char c : str.toCharArray()) {
            if ((int) c > (33 + 65248) && (int) c < (128 + 65248)) {
                sb.append(Character.toChars((int) c - 65248));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
	
	/**
     * @author Rainbow
     */
    @Overwrite
	public void drawChat(int p_146230_1_)
    {
        final HUD hud = (HUD) Oxygen.INSTANCE.moduleManager.getModule("HUD");

        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            int i = this.getLineCount();
            boolean flag = false;
            int j = 0;
            int k = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (k > 0)
            {
                if (this.getChatOpen())
                {
                    flag = true;
                }

                float f1 = this.getChatScale();
                int l = MathHelper.ceiling_float_int((float)this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);

                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1)
                {
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);

                    if (chatline != null)
                    {
                        int j1 = p_146230_1_ - chatline.getUpdatedCounter();
                        if (j1 < 200 || flag)
                        {
                            double d0 = (double)j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            int l1 = (int)(255.0D * d0);

                            if (flag)
                            {
                                l1 = 255;
                            }

                            l1 = (int)((float)l1 * f);
                            ++j;

                            if (l1 > 3)
                            {
                                int i2 = 0;
                                int j2 = -i1 * 9;

                                if (hud.getChatAnim().getCurrentValue() && !flag) {
                                    if (j1 <= 20) {
                                        GL11.glTranslatef((float) (-(l + 4) * easeInQuart(1 - ((j1 + mc.timer.renderPartialTicks) / 20.0))), 0F, 0F);
                                    }
                                    if (j1 >= 180) {
                                        GL11.glTranslatef((float) (-(l + 4) * easeInQuart(((j1 + mc.timer.renderPartialTicks) - 180) / 20.0)), 0F, 0F);
                                    }
                                }

                                if (hud.getChatRect().getCurrentValue()) {
                                    Gui.drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
                                }

                                String s = chatline.getChatComponent().getFormattedText();

                                GlStateManager.enableBlend();
                                if (hud.getChatFont().getCurrentValue()) {
                                	FontUtils font = Oxygen.INSTANCE.fontmanager.owqy18;
                                	font.drawStringWithShadowForChat(s, i2, j2 - 7.5f, 16777215 + (l1 << 24));
                                } else {
                                    this.mc.fontRendererObj.drawStringWithShadow(s, (float)i2, (float)(j2 - 8), 16777215 + (l1 << 24));
                                }
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (flag)
                {
                    int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = k * k2 + k;
                    int i3 = j * k2 + j;
                    int j3 = this.scrollPos * i3 / k;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3)
                    {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 13382451 : 3355562;
                        Gui.drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        Gui.drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

    private double easeInQuart(double x) {
        return x * x * x * x;
    }
	
	@Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"), cancellable = true)
    public void eventChat(IChatComponent chatComponent, int chatLineId, CallbackInfo callbackInfo) {
    	ChatEvent ec = new ChatEvent(chatComponent.getUnformattedText());
        ec.call();
        
        if (ec.isCancelled()) {
        	callbackInfo.cancel();
        }
    }
	
	@Shadow
	public int getLineCount() {
		return 0;
	}
	
	@Shadow
	public boolean getChatOpen() {
		return false;		
	}
	
	@Shadow
	public int getChatWidth() {
		return 0;		
	}
	
	@Shadow
	public float getChatScale() {
		return 0;
		
	}

}
