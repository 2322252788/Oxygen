package me.Oxygen.command.commands;

import org.lwjgl.input.Keyboard;

import me.Oxygen.Oxygen;
import me.Oxygen.command.Command;
import me.Oxygen.modules.Module;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;

public class CommandBind extends Command {
	public CommandBind(String[] command) {
        super(command);
        this.setArgs(".bind <mod> <key>");
    }
	
	@Override
    public void onCmd(String[] args) {
		if(args.length < 3) {
			ClientUtil.sendClientMessage(this.getArgs(), Type.WARNING);
		} else {
			String mod = args[1];
			int key = Keyboard.getKeyIndex((String)args[2].toUpperCase());
			for (Module m : Oxygen.INSTANCE.ModMgr.getModules()) {
				if (!m.getName().equalsIgnoreCase(mod)) continue;
				m.setKeyCode(key);
                ClientUtil.sendClientMessage(String.valueOf(m.getName()) + " was bound to " + Keyboard.getKeyName((int)key), Keyboard.getKeyName((int)key).equals("NONE") ? Type.ERROR : Type.SUCCESS);
                Oxygen.INSTANCE.FileMgr.saveKeys();
                return;
			}
			ClientUtil.sendClientMessage("Cannot find Module : " + mod, Type.ERROR);
		}
	}
}
