package me.Oxygen.modules.combat;

import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.timer.TimeHelper;
import me.Oxygen.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleRegister(name = "Criticals", category = Category.COMBAT)
public class Criticals extends Module
{
    public Value<String> mode = new Value<String>("Criticals", "Mode", 0);
    public Value<Double> Delay = new Value<Double>("Criticals_Delay", 333.0, 0.0, 1000.0, 1.0);
    private TimeHelper timer = new TimeHelper();
    private Random random = new Random();
    int stage;
    int count;
    double y;
    private int groundTicks;
    
    
    public Criticals() {
        this.mode.mode.add("Packet");
        this.mode.mode.add("Packet2");
        this.mode.mode.add("Edit");
        this.mode.mode.add("Cracking");
        this.mode.mode.add("Hypixel");
        this.mode.mode.add("Hypixel2");
        this.mode.mode.add("HVH");
    }
    
    private boolean canCrit(final EntityLivingBase e) {
        final EntityLivingBase target = e;
        if (target.hurtTime > 8) {
            if (mc.thePlayer.isCollidedVertically) {
                if (mc.thePlayer.onGround) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @EventTarget(events = EventUpdate.class)
    private void onEvent(Event event) {
    	if(event instanceof EventUpdate) {
    		 super.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
    	}
    }
    
    public void onDisable() {
    }
    
    boolean autoCrit(final EntityLivingBase e) {
        //if (!this.isEnabled()) {
        //    return false;
        //}
        if (!(e instanceof EntityPlayer)) {
            return false;
        }
        if (this.canCrit(e) && this.timer.hasReached(((Double)this.Delay.getValueState()).longValue())) {
            this.timer.reset();
            switch (this.mode.getModeAt(this.mode.getCurrentMode())) {
                case "Packet": {
                    Crit(new Double[] { 0.625, -RandomUtils.nextDouble(0.0, 0.625) });
                    break;
                }
                case "Hypixel": {
                    Crit2(new Double[] { 0.051, 0.0125 });
                    break;
                }
                case "Hypixel2": {
                    Crit2(new Double[] { 0.0412622959183674, 0.01, 0.0412622959183674, 0.01, 0.001 });
                    break;
                }
                case "HVH": {
                    Crit2(new Double[] { 0.06250999867916107, -9.999999747378752E-6, 0.0010999999940395355 });
                    break;
                }
                case "Edit": {
                    Crit(new Double[] { 0.0, 0.419999986886978, 0.3331999936342235, 0.2481359985909455, 0.164773281826067, 0.083077817806467, 0.0, -0.078400001525879, -0.155232004516602, -0.230527368912964, -0.304316827457544, -0.376630498238655, -0.104080378093037 });
                    break;
                }
                case "Packet2": {
                    Crit2(new Double[] { 0.626, 0.0 });
                    break;
                }
                case "Cracking": {
                    break;
                }
                default:
                    break;
            }
            return true;
        }
        return false;
    }
    
    public void Crit2(final Double[] value) {
        final NetworkManager var1 = Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager();
        final Double curX = Minecraft.getMinecraft().thePlayer.posX;
        final Double curY = Minecraft.getMinecraft().thePlayer.posY;
        final Double curZ = Minecraft.getMinecraft().thePlayer.posZ;
        for (final Double offset : value) {
        	tellPlayer("Do Criticals", "Info");
            var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + offset, curZ, false));
        }
    }
    
    public void Crit(final Double[] value) {
        final Minecraft mc = Minecraft.getMinecraft();
        final NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
        final Double curX = mc.thePlayer.posX;
        Double curY = mc.thePlayer.posY;
        final Double curZ = mc.thePlayer.posZ;
        final Double RandomY = 0.0;
        for (final Double offset : value) {
            curY += offset;
            tellPlayer("Do Criticals", "Info");
            var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + RandomY, curZ, false));
        }
    }
    
    public static void CritAppointRandom(final Double[] value) {
        final NetworkManager var1 = Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager();
        final Double curX = Minecraft.getMinecraft().thePlayer.posX;
        Double curY = Minecraft.getMinecraft().thePlayer.posY;
        final Double curZ = Minecraft.getMinecraft().thePlayer.posZ;
        final Double RandomY = 0.0;
        for (final Double offset : value) {
            curY += offset;
            var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + RandomY, curZ, false));
        }
    }
}

