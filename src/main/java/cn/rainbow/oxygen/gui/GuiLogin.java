package cn.rainbow.oxygen.gui;

import by.radioegor146.nativeobfuscator.Native;
import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.buttons.GuiPasswordField;
import cn.rainbow.oxygen.gui.buttons.UIFlatButton;
import cn.rainbow.oxygen.gui.mainmenu.MainMenu;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

@Native
public class GuiLogin extends GuiScreen {

    private GuiPasswordField password;
    private GuiTextField username;

    ScaledResolution res;

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                Minecraft.getMinecraft().displayGuiScreen(new MainMenu());
                /*if (!this.username.getText().isEmpty() && !this.password.getText().isEmpty()) {
                    Oxygen.INSTANCE.getClient().login(this.username.getText(), this.password.getText());
                }*/
                break;
            case 3:
                Oxygen.INSTANCE.exit();
        }
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        int h = res.getScaledHeight();
        int w = res.getScaledWidth();
        RenderUtil.renderImage(MainMenu.Resource.getMainMenu(), 0, 0, w, h);
        username.drawTextBox();
        password.drawTextBox();
        Oxygen.INSTANCE.fontmanager.wqy15.drawStringWithShadow("Username:",this.width / 2 - 100,height/2 - 60,-1);
        Oxygen.INSTANCE.fontmanager.wqy15.drawStringWithShadow("Password:",this.width / 2 - 100,height/2 - 20,-1);
        Oxygen.INSTANCE.fontmanager.wqy35.drawCenteredString(Oxygen.name.toUpperCase(), (float) this.width / 2, (float)this.height / 2 - 88, new Color(128,189,254).getRGB());
        Oxygen.INSTANCE.fontmanager.wqy15.drawCenteredString("Login",(float)this.width/2,(float)this.height / 2 - 134 + Oxygen.INSTANCE.fontmanager.wqy35.getStringWidth(Oxygen.name.toUpperCase()) + 1 ,-1);
        super.drawScreen(var1, var2, var3);
    }

    @Override
    public void initGui() {
        this.res = new ScaledResolution(mc);
        FontRenderer var1 = this.mc.fontRendererObj;
        int var2 = this.height / 2;
        super.initGui();
        GuiButton loginButton = new UIFlatButton(1, this.width / 2 - 100, var2 + 48, 100, 20, "登录", new Color(47, 154, 241).getRGB());
        GuiButton freeButton = new UIFlatButton(3, this.width / 2 + 2, var2 + 48, 100, 20, "退出", ColorUtils.GREY.c);
        this.buttonList.add(loginButton);
        this.buttonList.add(freeButton);
        this.username = new GuiTextField(var2, var1, this.width / 2 - 100, this.height / 2 - 50, 200, 20);
        this.password = new GuiPasswordField(var1, this.width / 2 - 100, this.height / 2 - 10, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char var1, int var2) {

        if (var1 == 9) {
            if (!this.username.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.username.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }

        if (var1 == 13) {
            this.actionPerformed(this.buttonList.get(0));
        }

        if (var1 == 27) {

        }

        this.username.textboxKeyTyped(var1, var2);
        this.password.textboxKeyTyped(var1, var2);
    }

    @Override
    protected void mouseClicked(int var1, int var2, int var3) {
        try {
            super.mouseClicked(var1, var2, var3);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        this.username.mouseClicked(var1, var2, var3);
        this.password.mouseClicked(var1, var2, var3);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        if (this.username != null && this.password != null) {
            this.username.updateCursorCounter();
            this.password.updateCursorCounter();
        }
    }
}
