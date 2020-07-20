package me.Oxygen.modules.movement;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMove;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.injection.interfaces.IMinecraft;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.ui.ClientNotification;
import me.Oxygen.utils.ClientUtil;
import me.Oxygen.utils.CustomVec3;
import me.Oxygen.utils.other.MathUtil;
import me.Oxygen.utils.other.MoveUtil;
import me.Oxygen.utils.other.PlayerUtil;
import me.Oxygen.value.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.Timer;

import java.util.ArrayDeque;

@ModuleRegister(name = "Fly2", category = Category.MOVEMENT)
public class Fly2 extends Module {

    private final Value<String> mode = new Value<String>("Fly2","Mode",0);
    private final Value<Boolean> viewBobbing = new Value<Boolean>("Fly2_ViewBobbing",true);
    private final Value<Boolean> multiplier = new Value<Boolean>("Fly2_Multiplier",false);
    private final Value<Double> multiplyspeed = new Value<Double>("Fly2_MultiplySpeed",1.8,0.1,3.0,0.05);
    private final Value<Double> multiplytime = new Value<Double>("Fly2_MultiplyTime",1190.0,100.0,3000.0,50.0);
    private final Value<Double> speed = new Value<Double>("Fly2_WatchdogSpeed",1.0,0.5,1.0,0.05);
    private final Value<Double> vanillaSpeed = new Value<Double>("Fly2_VanillaSpeed",5.0,0.1,7.0,0.1);

    private final ArrayDeque<Packet<?>> arrayDeque = new ArrayDeque();
    private final Stopwatch stopwatch = new Stopwatch();
    private int ticks;
    private int stage;
    //private CustomVec3 lastPos;
    private boolean tp;
    private double lastDist;
    private double moveSpeed;
    //private TargetStrafeMod targetStrafe;
    private double y;

    public Fly2() {
        mode.addValue("WatchDog");
        mode.addValue("Disabler");
        mode.addValue("CubeCraft");
        mode.addValue("MinePlex");
        mode.addValue("Vanilla");
    }

    @EventTarget(events = {EventPacket.class, EventMove.class, EventMotion.class})
    private void onEvent(Event e){
        if(e instanceof  EventPacket){
          EventPacket ep = (EventPacket)e;
          if(ep.getPacketType() == EventPacket.PacketType.Send){
              if(mode.isCurrentMode("WatchDog") && this.stage == 0){
                  ep.setCancelled(true);
              } else if (mode.isCurrentMode("MinePlex") && !this.stopwatch.elapsed(6100L)) {
                  ep.setCancelled(true);
              } else if (mode.isCurrentMode("Disabler") && ep.getPacket() instanceof C03PacketPlayer && !this.tp) {
                  ep.setCancelled(true);
              }
          }
            if(ep.getPacketType() == EventPacket.PacketType.Recieve){
                if (mode.isCurrentMode("MinePlex")){
                    if ((ep.getPacket() instanceof S02PacketChat || ep.getPacket() instanceof S45PacketTitle) && !this.stopwatch.elapsed(6000L)) {
                        ep.setCancelled(true);
                    }
                } else if (mode.isCurrentMode("WatchDog")) {
                    if (ep.getPacket() instanceof S08PacketPlayerPosLook) {
                        ClientUtil.sendClientMessage("Disabled Flight due to flag.", ClientNotification.Type.INFO);
                        this.toggle();
                    }
                } else if (mode.isCurrentMode("Disabler") && ep.getPacket() instanceof S08PacketPlayerPosLook) {
                    ClientUtil.sendClientMessage( "Teleported after lagback.", ClientNotification.Type.INFO);
                    if (!this.tp) {
                        this.tp = true;
                    }
                }
            }
        }
        if (e instanceof EventMove){
            EventMove em = (EventMove)e;
            EntityPlayerSP player = mc.thePlayer;
            GameSettings gameSettings = mc.gameSettings;
            switch (this.mode.getModeAt(this.mode.getCurrentMode())) {
                case "WatchDog": {
                    if (!PlayerUtil.isMoving2()) break;
                    switch (this.stage) {
                        case 0: {
                            if (!mc.thePlayer.onGround || !mc.thePlayer.isCollidedVertically) break;
                            PlayerUtil.damage();
                            this.moveSpeed = 0.5 * (Double)this.speed.getValue();
                            break;
                        }
                        case 1: {
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                em.setY(player.motionY = MoveUtil.getJumpBoostModifier(0.39999994)) ;
                            }
                            this.moveSpeed *= 2.149;
                            break;
                        }
                        case 2: {
                            this.moveSpeed = 1.3 * (Double)this.speed.getValue();
                            break;
                        }
                        default: {
                            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                        }
                    }
                    MoveUtil.setSpeed(em, Math.max(this.moveSpeed, MoveUtil.getBaseMoveSpeed()));
                    ++this.stage;
                    break;
                }
                case "MinePlex": {
                    player.motionY = 0.0;
                    em.setY(0.0);
                    if (this.stopwatch.elapsed(6100L)) {
                        MoveUtil.setSpeed(em, 6.0);
                        if (gameSettings.keyBindJump.isKeyDown()) {
                            player.motionY = 1.0;
                            em.setY(1.0);
                        }
                        if (gameSettings.keyBindSneak.isKeyDown()) {
                            player.motionY = -1.0;
                            em.setY(-1.0);
                        }
                    } else {
                        MoveUtil.setSpeed(em, 0.0);
                    }
                    if (!this.stopwatch.elapsed(7000L)) break;
                    MoveUtil.setSpeed(em, 0.0);
                    this.mineplexDamage(player);
                    this.stopwatch.reset();
                    break;
                }
                case "Vanilla": {
                    MoveUtil.setSpeed(em, ((Double)this.vanillaSpeed.getValue()).intValue());
                    break;
                }
                case "Disabler": {
                    mc.thePlayer.motionY = 0.0;
                    em.setY(0.0);
                    if (this.tp) {
                        if (gameSettings.keyBindJump.isKeyDown()) {
                            mc.thePlayer.motionY = 2.0;
                            em.setY(2.0);
                        } else if (gameSettings.keyBindSneak.isKeyDown()) {
                            mc.thePlayer.motionY = -2.0;
                            em.setY(-2.0);
                        }
                        MoveUtil.setSpeed(em, 2.0);
                        break;
                    }
                    MoveUtil.setSpeed(em, 0.0);
                    break;
                }
                case "CubeCraft": {
                    ((IMinecraft)mc).getTimer().timerSpeed = 1.0f;
                    MoveUtil.setSpeed(em, 0.0);
                    if (this.ticks > 12) {
                        if (this.stage == 4) {
                            MoveUtil.setSpeed(em, 0.953532);
                            this.stage = 0;
                            break;
                        }
                        MoveUtil.setSpeed(em, 0.121984218421847);
                        break;
                    }
                    MoveUtil.setSpeed(em, 0.0);
                }
            }
        }
        if (e instanceof EventMotion){
            EventMotion em = (EventMotion)e;
            EntityPlayerSP player = mc.thePlayer;
            Timer timer = ((IMinecraft)mc).getTimer();
            GameSettings gameSettings = mc.gameSettings;
            Stopwatch stopwatch = this.stopwatch;
            block0 : switch (this.mode.getModeAt(this.mode.getCurrentMode())) {
                case "WatchDog": {
                    if (this.multiplier.getValueState().booleanValue()) {
                        ((IMinecraft)mc).getTimer().timerSpeed = !stopwatch.elapsed(this.multiplytime.getValueState().longValue()) ? ((Double)this.multiplyspeed.getValueState()).floatValue() : 1.0f;
                    }
                    if (em.getMotionType() == EventMotion.MotionType.PRE) {
                        if (this.stage > 2) {
                            player.motionY = 0.0;
                        }
                        if (this.viewBobbing.getValueState().booleanValue()) {
                            player.cameraYaw = 0.105f;
                        }
                        if (this.stage <= 2) break;
                        player.setPosition(player.posX, player.posY - 0.003, player.posZ);
                        ++this.ticks;
                        double offset = 3.25E-4;
                        switch (this.ticks) {
                            case 1: {
                                this.y *= (double)-0.95f;
                                break;
                            }
                            case 2:
                            case 3:
                            case 4: {
                                this.y += 3.25E-4;
                                break;
                            }
                            case 5: {
                                this.y += 5.0E-4;
                                this.ticks = 0;
                            }
                        }
                        em.setY(player.posY + this.y);
                        break;
                    }
                    if (this.stage <= 2) break;
                    player.setPosition(player.posX, player.posY + 0.003, player.posZ);
                    break;
                }
                case "Vanilla": {
                    if (em.getMotionType() == EventMotion.MotionType.POST) break;
                    player.motionY = 0.0;
                    if (gameSettings.keyBindJump.isKeyDown()) {
                        player.motionY = 2.0;
                        break;
                    }
                    if (!gameSettings.keyBindSneak.isKeyDown()) break;
                    player.motionY = -2.0;
                    break;
                }
                case "Disabler": {
                    break;
                }
                case "MinePlex": {
                    /*if (player.hurtTime == 9) {
                        stopwatch.reset();
                    }
                    if (!stopwatch.elapsed(6000L) || this.tp) break;
                    NetHandlerPlayClient netHandler = mc.getNetHandler();
                    netHandler.addToSendQueue(new C0CPacketInput(0.0f, 0.0f, true, true));
                    CustomVec3 lastPos = this.lastPos;
                    ArrayList<CustomVec3> computePath = PathfindingUtils.computePath(new CustomVec3(player.posX, player.posY, player.posZ), lastPos);
                    int computePathSize = computePath.size();
                    for (int i = 0; i < computePathSize; ++i) {
                        CustomVec3 vec3 = computePath.get(i);
                        netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
                    }
                    ClientUtil.sendClientMessage("Exploit completed.", ClientNotification.Type.SUCCESS);
                    mc.thePlayer.setPosition(lastPos.getX(), lastPos.getY(), lastPos.getZ());
                    this.tp = true;*/
                    break;
                }
                case "CubeCraft": {
                    if (em.getMotionType() == EventMotion.MotionType.POST) break;
                    timer.timerSpeed = 0.82f;
                    ++this.ticks;
                    float y = (float)Math.floor(player.posY);
                    if (this.ticks == 1 && !player.onGround) {
                        this.toggle();
                    }
                    player.motionY = 0.0;
                    if (this.ticks < 10) {
                        em.setY((double)y - 1.01);
                        player.motionY = -0.101;
                        break;
                    }
                    if (this.ticks <= 12) break;
                    ++this.stage;
                    switch (this.stage) {
                        case 1: {
                            em.setY((double)y + 0.381 + MathUtil.randomNumber(0.03, 0.05));
                            break block0;
                        }
                        case 2: {
                            em.setY((double)y + 0.355 + MathUtil.randomNumber(0.03, 0.05));
                            break block0;
                        }
                        case 3: {
                            em.setY((double)y + 0.325 + MathUtil.randomNumber(0.03, 0.05));
                            break block0;
                        }
                        case 4: {
                            em.setY((double)y + MathUtil.randomNumber(0.03, 0.05));
                        }
                    }
                }
            }
            if (em.getMotionType() == EventMotion.MotionType.PRE) {
                double xDif = player.posX - player.prevPosX;
                double zDif = player.posZ - player.prevPosZ;
                this.lastDist = Math.sqrt(xDif * xDif + zDif * zDif);
            }
        }
    }

    private void mineplexDamage(EntityPlayerSP playerRef) {
        NetHandlerPlayClient netHandler = mc.getNetHandler();
        double offset = 0.0601f;
        for (int i = 0; i < 20; ++i) {
            int j = 0;
            while ((double)j < (double) PlayerUtil.getMaxFallDist() / (double)0.0601f + 1.0) {
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + (double)0.0601f, mc.thePlayer.posZ, false));
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + (double)5.0E-4f, mc.thePlayer.posZ, false));
                ++j;
            }
        }
        netHandler.addToSendQueue(new C03PacketPlayer(true));
    }

    public void onEnabled() {
        this.arrayDeque.clear();
        this.tp = false;
        //this.lastPos = new CustomVec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        EntityPlayerSP player = mc.thePlayer;
        if (mode.isCurrentMode("MinePlex")) {
            this.mineplexDamage(player);
        }
        if (mode.isCurrentMode("Disabler")) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.18, mc.thePlayer.posZ, true));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.08, mc.thePlayer.posZ, true));
        }
        this.stopwatch.reset();
        this.y = 0.0;
        this.lastDist = 0.0;
        this.moveSpeed = 0.0;
        this.stage = 0;
        this.ticks = 0;
        player.stepHeight = 0.0f;
        player.motionX = 0.0;
        player.motionZ = 0.0;
        /*if (this.targetStrafe == null) {
            this.targetStrafe = Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(TargetStrafeMod.class);
        }*/
    }

    public void onDisabled() {
        EntityPlayerSP player = mc.thePlayer;
        ((IMinecraft)mc).getTimer().timerSpeed = 1.0f;
        player.stepHeight = 0.625f;
        player.motionX = 0.0;
        player.motionZ = 0.0;
        if (mode.isCurrentMode("WatchDog")) {
            player.setPosition(player.posX, player.posY + this.y, player.posZ);
        }
    }

    private static class Stopwatch {
        long ms = getCurrentMS();

        long getCurrentMS() {
            return System.currentTimeMillis();
        }

        final long getElapsedTime() {
            return getCurrentMS() - this.ms;
        }

        final boolean elapsed(long milliseconds) {
            return getCurrentMS() - this.ms > milliseconds;
        }

        final void reset() {
            ms = getCurrentMS();
        }
    }
}

