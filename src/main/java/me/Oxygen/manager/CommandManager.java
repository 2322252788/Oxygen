package me.Oxygen.manager;

import java.util.ArrayList;

import me.Oxygen.Oxygen;
import me.Oxygen.command.Command;
import me.Oxygen.command.commands.CommandBind;
import me.Oxygen.command.commands.CommandClear;
import me.Oxygen.command.commands.CommandConfig;
import me.Oxygen.command.commands.CommandFastPlay;
import me.Oxygen.command.commands.CommandHelp;
import me.Oxygen.command.commands.CommandSay;
import me.Oxygen.command.commands.CommandSetting;
import me.Oxygen.command.commands.CommandTitle;
import me.Oxygen.command.commands.CommandToggle;
import me.Oxygen.command.commands.CommandVersion;

public class CommandManager {
	private static ArrayList<Command> commands = new ArrayList<Command>();
	
	public CommandManager() {
		Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " " + "Command Loading...");
		add(new CommandBind(new String[] {"bind"}));
		add(new CommandToggle(new String[] {"toggle", "t"}));
		add(new CommandVersion(new String[] {"version", "v"}));
		add(new CommandHelp(new String[] {"help"}));
		add(new CommandSetting(new String[] {"setting"}));
		add(new CommandClear(new String[] {"clearchat", "cc"}));
		add(new CommandConfig(new String[] {"config"}));
		add(new CommandFastPlay(new String[] {"fastplay", "fp"}));
		add(new CommandTitle(new String[] {"title"}));
		add(new CommandSay(new String[] {"say"}));
	}
	
	public void add(Command c) {
		commands.add(c);
	}
	
	public static ArrayList<Command> getCommands() {
        return commands;
    }
	
	public static String removeSpaces(String message) {
        String space = " ";
        String doubleSpace = "  ";
        while (message.contains(doubleSpace)) {
            message = message.replace(doubleSpace, space);
        }
        return message;
    }
}
