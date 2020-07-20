package me.Oxygen.modules.movement;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventPacket.PacketType;
import me.Oxygen.injection.interfaces.*;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.other.MoveUtil;
import me.Oxygen.utils.other.PlayerUtil;
import me.Oxygen.utils.timer.TimeHelper;
import me.Oxygen.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@ModuleRegister(name = "Phase", category = Category.MOVEMENT)
public class Phase extends Module{
	
	float yaw, pitch;
	double rot1, rot2;
	private int delay;
	boolean shouldSpeed = false;
	TimeHelper timer = new TimeHelper();
	
	public Value<String> mode = new Value<String>("Phase_Mode", "Mode", 0);

	public Phase() {
		this.mode.mode.add("NCP");
	}
	
	@Override
    public void onDisable(){
    	((IMinecraft)mc).getTimer().timerSpeed = 1;
    }
    @Override
    public void onEnable(){
    	if(mc.theWorld == null)
    		return;
    	shouldSpeed = isInsideBlock();
    	if((MoveUtil.isCollidedH(0.001) || Minecraft.getMinecraft().thePlayer.isCollidedHorizontally)){
      		Minecraft.getMinecraft().thePlayer.onGround = false;
    		Minecraft.getMinecraft().thePlayer.noClip = true;
    		Minecraft.getMinecraft().thePlayer.motionX *= 0;
    		Minecraft.getMinecraft().thePlayer.motionZ *= 0;
    		Minecraft.getMinecraft().thePlayer.jumpMovementFactor = 0;
			teleport(0.006000000238415);
			rot1 = 0;
			rot2 = 0;
    	}
    }
    
    @EventTarget(events = {EventMotion.class, EventPacket.class})
    private void onEvent(Event event) {
    	if(event instanceof EventMotion) {
    		EventMotion em = (EventMotion)event;
    	if ( this.mode.isCurrentMode("NCP")) {
            	if( isInsideBlock()){
             		Minecraft.getMinecraft().thePlayer.rotationYaw = yaw;
            		Minecraft.getMinecraft().thePlayer.rotationPitch = pitch;
            	}else{
            		yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
            		pitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
            	}
            	
            	if(shouldSpeed || isInsideBlock()){
            		if(!Minecraft.getMinecraft().thePlayer.isSneaking())
            			((IEntityPlayerSP)Minecraft.getMinecraft().thePlayer).setLastReportedPosY(0);  
            		((IEntityPlayerSP)Minecraft.getMinecraft().thePlayer).setlastReportedPitch(999);
            		Minecraft.getMinecraft().thePlayer.onGround = false;
            		Minecraft.getMinecraft().thePlayer.noClip = true;
            		Minecraft.getMinecraft().thePlayer.motionX = 0;
            		Minecraft.getMinecraft().thePlayer.motionZ = 0;
            		if(((IKeyBinding)mc.gameSettings.keyBindJump).getPress() &&  Minecraft.getMinecraft().thePlayer.posY == (int)Minecraft.getMinecraft().thePlayer.posY)
            			Minecraft.getMinecraft().thePlayer.jump();
            		
            		Minecraft.getMinecraft().thePlayer.jumpMovementFactor = 0;
            	}	
        		rot1 ++;
        		if(rot1 < 3){
        			if(rot1 == 1){
        				pitch += 15;
        			}else{
        				pitch -= 15;
        			}
        		}
        		if(((IKeyBinding) mc.gameSettings.keyBindSneak).getPress()){
        			((IEntityPlayerSP)Minecraft.getMinecraft().thePlayer).setlastReportedPitch(999);
    				double X = Minecraft.getMinecraft().thePlayer.posX; double Y = Minecraft.getMinecraft().thePlayer.posY; double Z = Minecraft.getMinecraft().thePlayer.posZ;
    				if(!PlayerUtil.isMoving2())
    				if(MoveUtil.isOnGround(0.001) && !isInsideBlock()){    				
    					((IEntityPlayerSP)Minecraft.getMinecraft().thePlayer).setLastReportedPosY(-99);
                      	em.setY(Y-1);
                      	Minecraft.getMinecraft().thePlayer.setPosition(X, Y-1, Z);
        				timer.reset();
        				Minecraft.getMinecraft().thePlayer.motionY = 0;					
    				}else if(timer.check(100) && Minecraft.getMinecraft().thePlayer.posY == (int)Minecraft.getMinecraft().thePlayer.posY){
        				Minecraft.getMinecraft().thePlayer.setPosition(X, Y - 0.3, Z);
    				}
    					
    			}
        		if(isInsideBlock() && rot1 >= 3){
        			if(shouldSpeed){
        				teleport(0.617);
        				
        				float sin = (float)Math.sin(rot2) * 0.1f;
        				float cos = (float)Math.cos(rot2) * 0.1f;
        				Minecraft.getMinecraft().thePlayer.rotationYaw += sin;
        				Minecraft.getMinecraft().thePlayer.rotationPitch += cos;
        				rot2 ++;
        			}else{
        				teleport(0.031);
        			}
        		}
    	}
    	}
    	if(event instanceof EventPacket) {
    		EventPacket ep = (EventPacket)event;
    		Packet p = ep.getPacket();
            
            if(p instanceof C03PacketPlayer){
            	C03PacketPlayer packet = (C03PacketPlayer)p;
            	double y = packet.getPositionY();
            	double x = packet.getPositionX();
            	double z = packet.getPositionZ();
            	String ground = packet.isOnGround()?"��a":"��c";
          
            	if(y != 0){
            	//	ChatUtil.printChat(packet.getClass().getSimpleName() + ground + " z : " + z);
            	}
            
            }
            if(p instanceof S08PacketPlayerPosLook){
                S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
                ((IS08PacketPlayerPosLook)pac).setYaw(Minecraft.getMinecraft().thePlayer.rotationYaw);    
                ((IS08PacketPlayerPosLook)pac).setYaw(Minecraft.getMinecraft().thePlayer.rotationPitch);
                shouldSpeed = true;
                if(!shouldSpeed)
                	rot2 = 0;
            }
            if (ep.getPacketType() == PacketType.Send) {
                if (isInsideBlock()) {
                    return;
                }
                final double multiplier = 0.2;
                final double mx = Math.cos(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(Minecraft.getMinecraft().thePlayer.rotationYaw + 90.0f));
                final double x = Minecraft.getMinecraft().thePlayer.movementInput.moveForward * multiplier * mx + Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe * multiplier * mz;
                final double z = Minecraft.getMinecraft().thePlayer.movementInput.moveForward * multiplier * mz - Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe * multiplier * mx;
                if (Minecraft.getMinecraft().thePlayer.isCollidedHorizontally && ep.getPacket() instanceof C03PacketPlayer) {
                    delay++;
                    Packet packet = ep.getPacket();
                    if (packet instanceof C03PacketPlayer) {
                    	C03PacketPlayer player = (C03PacketPlayer)packet;
                    	 double posx = ((IC03PacketPlayer)player).getPosX();
                    	 double posz = ((IC03PacketPlayer)player).getPosZ();
                    	 double posy = ((IC03PacketPlayer)player).getPosY();
                    if (this.delay >= 5) {
                    	posx += x;
                    	posz += z;
                        --posy;
                        this.delay = 0;
                    }
                    }
                }
            }
    	}
    }
    
    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(((IEntity)Minecraft.getMinecraft().thePlayer).getBoundingBox().minX); x < MathHelper.floor_double(((IEntity)Minecraft.getMinecraft().thePlayer).getBoundingBox().maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(((IEntity)Minecraft.getMinecraft().thePlayer).getBoundingBox().minY); y < MathHelper.floor_double(((IEntity)Minecraft.getMinecraft().thePlayer).getBoundingBox().maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(((IEntity)Minecraft.getMinecraft().thePlayer).getBoundingBox().minZ); z < MathHelper.floor_double(((IEntity)Minecraft.getMinecraft().thePlayer).getBoundingBox().maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if ((block != null) && (!(block instanceof BlockAir))) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if ((block instanceof BlockHopper)) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null) {
                            if (((IEntity)Minecraft.getMinecraft().thePlayer).getBoundingBox().intersectsWith(boundingBox)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void teleport(double dist) {
        double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        double strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (forward != 0.0D) {
        	if (strafe > 0.0D) {
        		yaw += (forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += (forward > 0.0D ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1;
            } else if (forward < 0.0D) {
                forward = -1;
            }
        }
        double x = Minecraft.getMinecraft().thePlayer.posX; double y = Minecraft.getMinecraft().thePlayer.posY; double z = Minecraft.getMinecraft().thePlayer.posZ;
        double xspeed = forward * dist * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * dist * Math.sin(Math.toRadians(yaw + 90.0F));
        double zspeed = forward * dist * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * dist * Math.cos(Math.toRadians(yaw + 90.0F));
        Minecraft.getMinecraft().thePlayer.setPosition(x + xspeed, y,  z + zspeed);
        
    }

}
