package me.Oxygen.modules.world;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;
import net.minecraft.network.play.client.C01PacketChatMessage;

@ModuleRegister(name = "AntiSpammer", category = Category.WORLD)
public class AntiSpammer extends Module{
	
	private final Value<Boolean> ad = new Value<Boolean>("AntiSpammer_AD", true);
	private final Value<Boolean> abuse = new Value<Boolean>("AntiSpammer_Abuse", true);
	
	@EventTarget(events = EventPacket.class)
	private final void onEvent(Event e) {
		if(e instanceof EventPacket) {
			EventPacket ep = (EventPacket)e;
			if(ep.getPacket() instanceof C01PacketChatMessage) {
				C01PacketChatMessage c01 = (C01PacketChatMessage)ep.getPacket();
				
				if(ad.getValueState() && getAd(c01)) {
					ep.setCancelled(true);
				}
				
				if(abuse.getValueState() && getAbuse(c01)) {
					ep.setCancelled(true);
				}
				
			}
		}
	}
	
	private final boolean getAd(C01PacketChatMessage c01) {
		
		if(c01.getMessage().contains("内部") || c01.getMessage().contains("加群") || c01.getMessage().contains("maikama") || c01.getMessage().contains("购买")
				|| c01.getMessage().contains("链接") || c01.getMessage().contains("QQ群") || c01.getMessage().contains("配置")|| c01.getMessage().contains("Liquidbounce")
				|| c01.getMessage().contains("水影") || c01.getMessage().contains("buy")) {
			return true;
		}
		
		return false;
		
	}
	
	private final boolean getAbuse(C01PacketChatMessage c01) {
		
		if(c01.getMessage().contains("nmsl") || c01.getMessage().contains("NMSL") || c01.getMessage().contains("爬") || c01.getMessage().contains("fw")
				|| c01.getMessage().contains("废物") || c01.getMessage().contains("nt") || c01.getMessage().contains("脑瘫") || c01.getMessage().contains("妈")
				|| c01.getMessage().contains("nigger") || c01.getMessage().contains("fuck") || c01.getMessage().contains("bitch") || c01.getMessage().contains("窝囊废")
				|| c01.getMessage().contains("爪巴") || c01.getMessage().contains("全家") || c01.getMessage().contains("L") || c01.getMessage().contains("Loser")) {
			return true;
		}
		
		return false;
		
	}

}
