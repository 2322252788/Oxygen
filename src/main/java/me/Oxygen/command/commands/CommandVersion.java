package me.Oxygen.command.commands;

import me.Oxygen.command.Command;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;

public class CommandVersion extends Command {

	public CommandVersion(String[] commands) {
		super(commands);
	}

	@Override
	public void onCmd(String[] args) {
		ClientUtil.sendClientMessage("OxygenBase By Rainbow", Type.INFO);
	}
	
}
