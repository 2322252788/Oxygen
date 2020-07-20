package me.Oxygen.command.commands;

import me.Oxygen.command.Command;
import me.Oxygen.utils.other.ChatUtils;

public class CommandHelp extends Command{

	public CommandHelp(String[] commands) {
		super(commands);
		this.setArgs(".help");
	}
	
	 @Override
	    public void onCmd(String[] args) {
	        if (args.length != 0) {
	        	ChatUtils.printChat("\u00a77\u00a7m\u00a7l----------------------------------");
	        	ChatUtils.printChat("                    \u00a7c\u00a7l\tOxygen");
	        	ChatUtils.printChat("\u00a7a.help >\u00a77 查看所有命令");
	        	ChatUtils.printChat("\u00a7a.v >\u00a77 查看版本信息");
	        	ChatUtils.printChat("\u00a7a.t >\u00a77 开关功能");
	        	ChatUtils.printChat("\u00a7a.say >\u00a77 发送会被Command系统拦截的消息");
	        	ChatUtils.printChat("\u00a7a.clear >\u00a77 清除聊天栏");
	        	ChatUtils.printChat("\u00a7a.title >\u00a77 改标题和端名");
	        	ChatUtils.printChat("\u00a77\u00a7m\u00a7l----------------------------------");
	        } else {
	        	 ChatUtils.printChat("未知命令，请输入 .help");
	        }
	        
	    }

}
