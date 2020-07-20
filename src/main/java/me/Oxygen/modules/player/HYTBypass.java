package me.Oxygen.modules.player;

import io.netty.buffer.Unpooled;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.timer.TimeHelper;
import me.Oxygen.value.Value;

import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@ModuleRegister(name = "HYTBypass", category = Category.PLAYER)
public class HYTBypass extends Module {
   TimeHelper delay = new TimeHelper();
   Random random = new Random();
   double state = 0.0D;
   
   private Value<Double> spammerdelay = new Value<Double>("HYTBypass_Delay", 2000.0, 500.0, 10000.0, 100.0);

   @EventTarget(events = EventUpdate.class)
   public void onUpdate(Event event) throws IOException {
      new Random();
      if(this.delay.isDelayComplete(this.spammerdelay.getValueState().longValue())) {
         ++this.state;
         if(Minecraft.getMinecraft().getNetHandler() != null) {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C17PacketCustomPayload(
            		"AntiCheat", (new PacketBuffer(Unpooled.buffer())).writeString(
            				"{\"base64\":\"ecbeb575677ab9a37410748a5f429f9f\",\"cltitle\":\"\u6211\u7684\u4e16\u754c 1.8.9\",\"isLiquidbounce\":false,\"path\":\"mixins.mcwrapper.json\",\"player\":\"" + 
            		Minecraft.getMinecraft().thePlayer.getName() + "\"}")));
         }

         this.delay.reset();
      }

   }

   public void onDisable() {
      this.state = 0.0D;
      super.onDisable();
   }
}
