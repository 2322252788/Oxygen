package me.Oxygen.modules.player;

import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.ui.music.MusicWindow;

@ModuleRegister(name = "MusicPlayer", category = Category.PLAYER)
public class MusicPlayer extends Module{
	
	public void onEnable() {
        if (mc.currentScreen instanceof MusicWindow) {
            this.set(false);
            return;
        }
        MusicWindow class148 = new MusicWindow();
        mc.displayGuiScreen(class148);
        this.set(false);
    }

}
