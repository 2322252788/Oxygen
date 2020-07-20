package me.Oxygen.modules.player;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventTick;
import me.Oxygen.manager.FriendManager;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.handler.MouseInputHandler;
import me.Oxygen.utils.other.Friend;
import net.minecraft.entity.player.EntityPlayer;

@ModuleRegister(name = "MCF", category = Category.PLAYER)
public class MCF extends Module{
	
	private final MouseInputHandler handler = new MouseInputHandler(2);

	   @EventTarget(events = EventTick.class)
	   private final void onEvent(Event event) {
		   if(event instanceof EventTick) {
	      if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null && this.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
	         String name = this.mc.objectMouseOver.entityHit.getName();
	         if (this.handler.canExcecute()) {
	            if (FriendManager.isFriend(name)) {
	               for(int i = 0; i < FriendManager.getFriends().size(); ++i) {
	                  Friend f = (Friend)FriendManager.getFriends().get(i);
	                  if (f.getName().equalsIgnoreCase(name)) {
	                     FriendManager.getFriends().remove(i);
	                     this.tellPlayer("[MCF]Remove friend" + name, "Info");
	                  }
	               }
	            } else {
	               FriendManager.getFriends().add(new Friend(name, name));
	               this.tellPlayer("[MCF]Added friend" + name, "Info");
	            }

	            Oxygen.INSTANCE.FileMgr.saveFriends();
	         }
	      }
		   }
	   }
	}
