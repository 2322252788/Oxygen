package me.Oxygen.injection.interfaces;

import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface IMinecraft {
	
	Timer getTimer();

	Session getSession();

	void setSession(Session session);

	LanguageManager getLanguageManager();
	
    void setRightClickDelayTimer(final int i);
	
	void setClickCounter(final int i);
	
	void runCrinkMouse();

}
