package me.Oxygen.command.commands;

import me.Oxygen.Oxygen;
import me.Oxygen.command.Command;
import me.Oxygen.modules.player.FastPlay;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;

public class CommandFastPlay extends Command{

	public CommandFastPlay(String[] commands) {
		super(commands);
		this.setArgs("-fastplayer");
	}
	
	@Override
    public void onCmd(final String[] args) {
        Oxygen.INSTANCE.ModMgr.getModule(FastPlay.class).set(true);
        ClientUtil.sendClientMessage("Succeed!", Type.SUCCESS);
    }

}
