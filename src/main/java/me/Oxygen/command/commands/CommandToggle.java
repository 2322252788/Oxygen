package me.Oxygen.command.commands;

import me.Oxygen.Oxygen;
import me.Oxygen.command.Command;
import me.Oxygen.modules.Module;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;

public class CommandToggle
extends Command {
    public CommandToggle(String[] commands) {
        super(commands);
        this.setArgs("-toggle <mod>");
    }

    public void onCmd(String[] args) {
        if (args.length != 2) {
            ClientUtil.sendClientMessage(this.getArgs(), Type.INFO);
            return;
        }
        boolean found = false;
        for (Module mod : Oxygen.INSTANCE.ModMgr.getModules()) {
            if (!args[1].equalsIgnoreCase(mod.getName())) continue;
            try {
                mod.set(!mod.isEnabled());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            found = true;
            ClientUtil.sendClientMessage(String.valueOf(mod.getName()) + " was toggled", Type.SUCCESS);
            break;
        }
        if (!found) {
            ClientUtil.sendClientMessage("Cannot find Module : " + args[1], Type.WARNING);
        }
    }
}
