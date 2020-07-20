package me.Oxygen.command.commands;

import me.Oxygen.command.Command;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import net.minecraft.client.Minecraft;

public class CommandClear extends Command{

	public CommandClear(String[] commands) {
		super(commands);
		this.setArgs("-clearchat");
	}

	
	@Override
    public void onCmd(final String[] args) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
        ClientUtil.sendClientMessage("Succeed!", Type.SUCCESS);
    }
    
    
    
}
