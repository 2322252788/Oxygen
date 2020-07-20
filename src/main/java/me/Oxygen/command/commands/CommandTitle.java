package me.Oxygen.command.commands;

import org.lwjgl.opengl.Display;

import me.Oxygen.Oxygen;
import me.Oxygen.command.Command;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;



public class CommandTitle extends Command{

	public CommandTitle(String[] commands) {
		super(commands);
	}
	public void onCmd(String[] args) {
	    if (args.length == 1) {
	      ClientUtil.sendClientMessage(getArgs(), Type.WARNING);
	    } else {
      String msg = "";
	    int i = 1;
	    while (i < args.length) {
	         msg = String.valueOf(String.valueOf(String.valueOf(msg))) + args[i] + " ";
       i++;
	    } 
	      msg = msg.substring(0, msg.length() - 1);
	      Oxygen.INSTANCE.CLIENT_NAME = msg;
	      Display.setTitle(msg);
	     ClientUtil.sendClientMessage("Changed to " + msg, Type.SUCCESS);
	      super.onCmd(args);
	   } 
	   }
}
