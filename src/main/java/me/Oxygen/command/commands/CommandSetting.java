package me.Oxygen.command.commands;

import me.Oxygen.Oxygen;
import me.Oxygen.command.Command;
import me.Oxygen.modules.Module;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import me.Oxygen.value.Value;

public class CommandSetting extends Command{

	public CommandSetting(String[] commands) {
		super(commands);
		this.setArgs(".setting <string/number/boolean> <ValueName> <Value>");
	}
	
	public void onCmd(String[] args) {
		if(args.length < 5) {
			ClientUtil.sendClientMessage(".setting <string/number/boolean> <ValueName> <Value>", Type.ERROR);
		}
		 if(args.length == 5) {
	        	Module m = Oxygen.INSTANCE.ModMgr.getModuleByName(args[1]);

	            if (m ==null)
	                //return false;
	            
	        	if(args[2].contains("number")) {
	    			Value.getDoubleValueByName(args[3]).setValueState(Double.parseDouble(args[4]));
	    			ClientUtil.sendClientMessage("Succeed!", Type.SUCCESS);
	    			//return true;
	    		}else if(args[2].contains("boolean")) {
	    			Value.getDoubleValueByName(args[1] + "_" + args[3]).setValueState(Boolean.parseBoolean(args[4]));
	    			ClientUtil.sendClientMessage("Succeed!", Type.SUCCESS);
	    			//return true;
	    		}else if(args[2].contains("string")) {
	    			Value.getDoubleValueByName(args[1] + "_" + args[3]).setValueState(args[4]);
	    			ClientUtil.sendClientMessage("Succeed!", Type.SUCCESS);
	    			//return true;
	    		}else {
	    			ClientUtil.sendClientMessage("-help", Type.ERROR);
	    			//return false;
	    		}
	        }
	        //return false;
	}

}
