package me.Oxygen.command.commands;

import me.Oxygen.command.Command;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class CommandSay extends Command{

	public CommandSay(String[] commands) {
		super(commands);
		this.setArgs(".Say <Massage>");
	}

	
	public void onCmd(String[] args) {
		     if (args.length == 1) {
		       ClientUtil.sendClientMessage(".say <Text>", Type.WARNING);
		     } else {
		       String msg = "";
		       int i = 1;
		       while (i < args.length) {
		         msg = String.valueOf(String.valueOf(String.valueOf(msg))) + args[i] + " ";
		         i++;
		       } 
		       msg = msg.substring(0, msg.length() - 1);
		       
		       Minecraft.getMinecraft();
		       Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C01PacketChatMessage(msg));
		     } 
		 
		     
		     super.onCmd(args);
		   }
}
