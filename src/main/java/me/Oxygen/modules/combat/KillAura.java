package me.Oxygen.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javax.vecmath.Vector3d;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;
import net.minecraft.world.WorldSettings;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventPriority;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventAttack;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventRender3D;
import me.Oxygen.event.events.EventTick;
import me.Oxygen.utils.rotation.myAngle;
import me.Oxygen.utils.rotation.myAngleUtility;
import me.Oxygen.injection.interfaces.*;
import me.Oxygen.manager.FriendManager;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.player.PlayersCheck;
import me.Oxygen.modules.player.Teams;
import me.Oxygen.utils.Liquidbounce.rotation.Rotation;
import me.Oxygen.utils.Liquidbounce.rotation.VecRotation;
import me.Oxygen.utils.Liquidbounce.time.MSTimer;
import me.Oxygen.utils.Liquidbounce.time.TimeUtils;
import me.Oxygen.utils.other.RaycastUtils;
import me.Oxygen.utils.render.RenderUtil;
import me.Oxygen.utils.rotation.RotationUtil;
import me.Oxygen.utils.rotation.RotationUtils;
import me.Oxygen.utils.timer.TimeHelper;
import me.Oxygen.utils.utilities.angle.Angle;
import me.Oxygen.utils.utilities.angle.AngleUtility;
import me.Oxygen.utils.utilities.vector.impl.Vector3;
import me.Oxygen.value.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

@ModuleRegister(name = "KillAura", category = Category.COMBAT)
public class KillAura extends Module
{
	private final Value<String> priority = new Value<String>("KillAura", "Priority", 1);
	private final Value<String> blockMode = new Value<String>("KillAura", "BlockMode", 0);
	private final Value<String> rotMode = new Value<String>("KillAura", "RotationMode", 0);
    
	private final Value<Double> hurttime = new Value<Double>("KillAura_HurtTime", 15.0, 1.0, 15.0, 1.0);
	private final Value<Double> reach = new Value<Double>("KillAura_Range", 4.2, 3.0, 6.0, 0.1);
	private final Value<Double> blockReach = new Value<Double>("KillAura_BlockRange", 0.5, 0.0, 3.0, 0.1);
	private final Value<Double> maxcps = new Value<Double>("KillAura_MaxCPS", 10.0, 1.0, 20.0, 1.0);
	private final Value<Double> mincps = new Value<Double>("KillAura_MinCPS", 10.0, 1.0, 20.0, 1.0);
	private final Value<Double> fov = new Value<Double>("KillAura_Fov", 180.0, 1.0, 360.0, 5.0);
	private final Value<Double> turn = new Value<Double>("KillAura_TurnHeadSpeed", 15.0, 5.0, 120.0, 1.0);
	private final Value<Double> switchsize = new Value<Double>("KillAura_MaxTargets", 1.0, 1.0, 5.0, 1.0);
	private final Value<Double> switchdelay = new Value<Double>("KillAura_SwitchDelay", 3000.0, 1.0, 5000.0, 5.0);
	public final Value<Boolean> esp = new Value<Boolean>("KillAura_ESP", true);
    private final Value<Boolean> aac = new Value<Boolean>("KillAura_AAC", false);
    private final Value<Boolean> raycast = new Value<Boolean>("KillAura_Raycast", true);
    private final Value<Boolean> autoBlock = new Value<Boolean>("KillAura_AutoBlock", true);
    private final Value<Boolean> autodisable = new Value<Boolean>("KillAura_AutoDisable", true);
    private final Value<Boolean> attackPlayers = new Value<Boolean>("KillAura_Players", true);
    private final Value<Boolean> attackAnimals = new Value<Boolean>("KillAura_Animals", false);
    private final Value<Boolean> attackMobs = new Value<Boolean>("KillAura_Mobs", false);
    private final Value<Boolean> invisible = new Value<Boolean>("KillAura_Invisibles", false);
    
    public boolean isBlocking = false;
    public static ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
    public Random random = new Random();
    private ArrayList<EntityLivingBase> attacked = new ArrayList<EntityLivingBase>();
    public boolean needBlock = false;
    public boolean needUnBlock = false;
    public int index;
    public static EntityLivingBase target = null;
    public EntityLivingBase needHitBot = null;
    public static EntityLivingBase blockTarget;
    public TimeHelper switchTimer = new TimeHelper();
    public MSTimer attacktimer = new MSTimer();
    public TimeHelper TickexistCharge = new TimeHelper();
    AxisAlignedBB axisAlignedBB;
    float shouldAddYaw;
    float[] lastRotation = new float[] { 0.0f, 0.0f };
    private float getPitch = 0.0f;
    private float rotationYawHead;
    private float[] lastRotations;
    boolean Crit = false;
    float curHealthX = 0.0f;
    float curAbsorptionAmountX = 0.0f;
    float curY = (float)new ScaledResolution(mc).getScaledHeight();
    private float yaw;
    private float pitch;
    int attackSpeed;

    public KillAura() {
        this.priority.mode.add("Angle");
        this.priority.mode.add("Range");
        this.priority.mode.add("Fov");
        this.rotMode.mode.add("Hypixel");
        this.rotMode.mode.add("Hypixel-F");
        this.rotMode.mode.add("Hypixel-AA");
        this.rotMode.mode.add("Hypixel-FA");
        this.blockMode.addValue("Hypixel");
        this.blockMode.addValue("Sigma");
        this.blockMode.addValue("Old");
        this.rotMode.mode.add("Smooth");
    }
    
    @EventTarget(priority = EventPriority.HIGH,
    		     events = {EventTick.class, EventMotion.class,
    		    		 EventPacket.class, EventRender3D.class}
    )
    public void onEvent(Event event) {
    	if(event instanceof EventTick) {
    		
    	}
    	if(event instanceof EventMotion) {
    		EventMotion em = (EventMotion)event;
    		if(em.getMotionType() == MotionType.PRE) {
    			this.getPitch = this.mc.thePlayer.rotationPitch;
    			this.rotationYawHead = mc.thePlayer.rotationYawHead;
    	        needHitBot = null;
    	        
    	        if (!this.mc.thePlayer.isEntityAlive() && this.autodisable.getValueState()) {
                    this.set(false);
                }
        		
        		if(aac.getValueState() && target != null) {
        			mc.thePlayer.setSprinting(false);
        		}
        		
    	        if (!targets.isEmpty() && this.index >= targets.size()) {
    	            this.index = 0;
    	        }
    	        
    	        this.getTarget();
    	        if (targets.size() == 0) {
    	            target = null;
    	            this.attackSpeed = 0;
    	        }
    	        else {
    	            target = targets.get(this.index);
    	            this.axisAlignedBB = null;
    	            if (mc.thePlayer.getDistanceToEntity(target) > reach.getValueState()) {
    	                target = targets.get(0);
    	            }
    	        }
    	        
    	        if (Oxygen.INSTANCE.ModMgr.getModuleByName("Scaffold").isEnabled() || Oxygen.INSTANCE.ModMgr.getModuleByName("Scaffold2").isEnabled()) {
    	            target = null;
    	            return;
    	        }
    	        
    	        if (target != null) {
    	            if (this.switchTimer.isDelayComplete(this.switchdelay.getValueState()) && targets.size() > 1) {
    	                this.switchTimer.reset();
    	                ++this.index;
    	            }
    	            //final float diff = Math.abs(Math.abs(MathHelper.wrapAngleTo180_float(this.rotationYawHead)) - Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.getRotations(target)[0])));
    	                if (this.rotMode.isCurrentMode("Hypixel-AA")) {
    	                    final float[] rotation = getEntityRotations(target, this.lastRotations, false, this.turn.getValueState().intValue());
    	                    this.lastRotations = new float[] { rotation[0], rotation[1] };
    	                    em.setYaw(rotation[0]);
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    mc.thePlayer.renderArmPitch = em.pitch;
    	                    em.setPitch(rotation[1]);
    	                    this.rotationYawHead = em.getYaw();
    	                    this.getPitch = em.getPitch();
    	                }
    	                else if (this.rotMode.isCurrentMode("Smooth")) {
    	                    final double posY = target.posY;
    	                    double abs2;
    	                    if (Math.abs(posY - mc.thePlayer.posY) > 1.8) {
    	                        final double posY2 = target.posY;
    	                        final double abs = Math.abs(posY2 - mc.thePlayer.posY);
    	                        final double posY3 = target.posY;
    	                        abs2 = abs / Math.abs(posY3 - mc.thePlayer.posY) / 2.0;
    	                    }
    	                    else {
    	                        final double posY4 = target.posY;
    	                        abs2 = Math.abs(posY4 - mc.thePlayer.posY);
    	                    }
    	                    final double comparison = abs2;
    	                    final Vector3<Double> enemyCoords = new Vector3<Double>((target.getEntityBoundingBox().minX + (target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX) / 2.0), (((target instanceof EntityPig || target instanceof EntitySpider) ? (target.getEntityBoundingBox().minY - target.getEyeHeight() * 1.2) : target.posY) - comparison), (target.getEntityBoundingBox().minZ + (target.getEntityBoundingBox().maxZ - target.getEntityBoundingBox().minZ) / 2.0));
    	                    final double minX = mc.thePlayer.getEntityBoundingBox().minX;
    	                    final double maxX = mc.thePlayer.getEntityBoundingBox().maxX;
    	                    final Double value = minX + (maxX - mc.thePlayer.getEntityBoundingBox().minX) / 2.0;
    	                    final Double value2 = mc.thePlayer.posY;
    	                    final double minZ = mc.thePlayer.getEntityBoundingBox().minZ;
    	                    final double maxZ = mc.thePlayer.getEntityBoundingBox().maxZ;
    	                    final Vector3<Double> myCoords = new Vector3<Double>(value, value2, (minZ + (maxZ - mc.thePlayer.getEntityBoundingBox().minZ) / 2.0));
    	                    final Angle srcAngle = new Angle(Float.valueOf(this.lastRotation[0]), Float.valueOf(this.lastRotation[1]));
    	                    final Angle dstAngle = AngleUtility.calculateAngle(enemyCoords, myCoords);
    	                    final Angle smoothedAngle = AngleUtility.smoothAngle(dstAngle, srcAngle, (this.turn.getValueState()).floatValue() * 8.0f, (this.turn.getValueState()).floatValue() * 7.5f);
    	                    em.setYaw(smoothedAngle.getYaw() + this.randomNumber(-2, 2));
    	                    em.setPitch(smoothedAngle.getPitch() + this.randomNumber(-3, 3));
    	                    this.lastRotation[0] = em.getYaw();
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    mc.thePlayer.renderArmPitch = em.pitch;
    	                    this.lastRotation[1] = em.getPitch();
    	                    this.rotationYawHead = em.getYaw();
    	                    this.getPitch = em.getPitch();
    	                }
    	                else if (this.rotMode.isCurrentMode("Hypixel-FA")) {
    	                    final float[] rot = RotationUtil.getRotations((Entity)target);
    	                    final Random rand2 = new Random();
    	                    em.setYaw(rot[0] + rand2.nextInt(10) - 5.0f);
    	                    mc.thePlayer.rotationYawHead = em.getYaw();
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    mc.thePlayer.renderArmPitch = em.pitch;
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    em.setPitch(rot[1] + rand2.nextInt(3) - 2.0f);
    	                    this.rotationYawHead = em.getYaw();
    	                    this.getPitch = em.getPitch();
    	                }
    	                else if (this.rotMode.isCurrentMode("Hypixel-F")) {
    	                    final float[] facing = getAnglesIgnoringNull(target, this.yaw, this.pitch);
    	                    this.lastRotations[0] = facing[0];
    	                    this.lastRotations[1] = facing[1];
    	                    em.setYaw(facing[0]);
    	                    mc.thePlayer.rotationYawHead = em.getYaw();
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    mc.thePlayer.renderArmPitch = em.pitch;
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    em.setPitch(facing[1]);
    	                    this.rotationYawHead = em.getYaw();
    	                    this.getPitch = em.getPitch();
    	                }
    	                else if (this.rotMode.isCurrentMode("Hypixel")) {
    	                    final float[] facing = RotationUtils.getRotations(target);
    	                    this.lastRotations[0] = facing[0];
    	                    this.lastRotations[1] = facing[1];
    	                    em.setYaw(facing[0]);
    	                    mc.thePlayer.rotationYawHead = em.getYaw();
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    mc.thePlayer.renderArmPitch = em.pitch;
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    em.setPitch(facing[1]);
    	                    this.rotationYawHead = em.getYaw();
    	                    this.getPitch = em.getPitch();
    	                }
    	                else if (this.rotMode.isCurrentMode("Hypixel-F")) {
    	                    final float[] facing = RotationUtils.getRotationNeededHypixelBetter(target);
    	                    this.lastRotations[0] = facing[0];
    	                    this.lastRotations[1] = facing[1];
    	                    em.setYaw(facing[0]);
    	                    mc.thePlayer.rotationYawHead = em.getYaw();
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    mc.thePlayer.renderArmPitch = em.pitch;
    	                    mc.thePlayer.renderYawOffset = em.getYaw();
    	                    em.setPitch(facing[1]);
    	                    this.rotationYawHead = em.getYaw();
    	                    this.getPitch = em.getPitch();
    	                }
    	                else /*if (this.rotMode.isCurrentMode("Liquidbounce")){

						}*/
    	                
    	                	
    	                if (target != null) {
    	                    if (autoBlock.getValueState() && blockMode.isCurrentMode("Sigma")) {
    	                        if (this.hasSword()) {
    	                        	if(hasSword() && isBlocking) {
    	                        		((IPlayerControllerMP)mc.playerController).SyncCurrentPlayItem();
    	                        		if(this.isServer("hypixel")) {
    	                        			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
    	                        		} else {
									mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
									}
    	                        		}
    	                        else if (mc.thePlayer.getItemInUseCount() == 0) {
    	                        	if(this.isServer("hypixel")) {
    	                        		mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
    	                        	} else {
    	                            	mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
    	                   		     mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
    	                        	}
										this.isBlocking = true;
    	                            }
									((IEntityPlayer)mc.thePlayer).setItemInUseCount(999);
    	                        } else if (mc.thePlayer.getItemInUseCount() == 999) {
									((IEntityPlayer)mc.thePlayer).setItemInUseCount(0);
    	                        }
    	                    } else if (mc.thePlayer.getItemInUseCount() == 999) {
    	                        if (this.isBlocking && this.hasSword()) {
									((IPlayerControllerMP)mc.playerController).SyncCurrentPlayItem();
									if(this.isServer("hypixel")) {
	                        			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
	                        		} else {
								mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
								}
									this.isBlocking = false;
    	                        }
								((IEntityPlayer)mc.thePlayer).setItemInUseCount(0);
    	                    }
    	                } else if (mc.thePlayer.getItemInUseCount() == 999) {
    	                    if (this.isBlocking && this.hasSword()) {
								((IPlayerControllerMP)mc.playerController).SyncCurrentPlayItem();
								if(this.isServer("hypixel")) {
                        			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                        		} else {
							mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
							}
								this.isBlocking = false;
    	                    }
							((IEntityPlayer)mc.thePlayer).setItemInUseCount(0);
    	                }
    	                
    	                if (mc.thePlayer.isBlocking()
    	    					|| mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
    	    							&& autoBlock.getValueState() && isBlocking && blockMode.isCurrentMode("Hypixel")) { // 锟斤拷
    	    				unBlock(!mc.thePlayer.isBlocking() && !autoBlock.getValueState()
    	    						&& mc.thePlayer.getItemInUseCount() > 0);
    	    			}
    	            
    	            
    	            if (target != null && aac.getValueState()) {
                        this.doAttack();
                    }
    	        }
    	        else {
    	            final float[] lastRotation = this.lastRotation;
    	            final int n = 0;
    	            lastRotation[n] = mc.thePlayer.rotationYaw;
    	            targets.clear();
    	            if (mc.thePlayer.getHeldItem() != null) {
    	                if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.getValueState() && isBlocking) {
    	                    this.unBlock(true);
    	                }
    	            }
    	        }
    		}
    		
            if(em.getMotionType() == MotionType.POST) {
            	if (target != null && !aac.getValueState()) {
                    this.doAttack();
                }
            	if (target != null
        				&& (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
        						&& autoBlock.getValueState() && !blockMode.isCurrentMode("Sigma")|| mc.thePlayer.isBlocking())
        				&& !isBlocking) { 
        			doBlock(true);
        		}
    		}
    	}
    	
    	if(event instanceof EventPacket) {
    		EventPacket ep = (EventPacket)event;
    		if (ep.getPacket() instanceof S08PacketPlayerPosLook) {
                final S08PacketPlayerPosLook s08PacketPlayerPosLook;
                final S08PacketPlayerPosLook look = s08PacketPlayerPosLook = (S08PacketPlayerPosLook)ep.getPacket();
                ((IS08PacketPlayerPosLook)s08PacketPlayerPosLook).setYaw(mc.thePlayer.rotationYaw);
                final S08PacketPlayerPosLook s08PacketPlayerPosLook2 = look;
                ((IS08PacketPlayerPosLook)s08PacketPlayerPosLook2).setPitch(mc.thePlayer.rotationPitch);
            }
    	}
    	
    	if(event instanceof EventRender3D) {
        if (target == null || !this.esp.getValueState()) {
            return;
        }
        for (final EntityLivingBase entityLivingBase : KillAura.targets) {
            mc.getRenderManager();
            final double n = entityLivingBase.lastTickPosX + (entityLivingBase.posX - entityLivingBase.lastTickPosX) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
            mc.getRenderManager();
            final double n2 = entityLivingBase.lastTickPosY + (entityLivingBase.posY - entityLivingBase.lastTickPosY) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
            mc.getRenderManager();
            RenderUtil.drawEntityESP(n, n2, entityLivingBase.lastTickPosZ + (entityLivingBase.posZ - entityLivingBase.lastTickPosZ) * ((IMinecraft)mc).getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ(), entityLivingBase.getEntityBoundingBox().maxX - entityLivingBase.getEntityBoundingBox().minX, entityLivingBase.getEntityBoundingBox().maxY - entityLivingBase.getEntityBoundingBox().minY + 0.25, (entityLivingBase.hurtTime > 1) ? 1.0f : 1.0f, (entityLivingBase.hurtTime > 1) ? 1.0f : 0.0f, 0.0f, 0.2f, (entityLivingBase.hurtTime > 1) ? 1.0f : 0.0f, (entityLivingBase.hurtTime > 1) ? 0.0f : 1.0f, 0.0f, 0.0f, 2.0f);
        }
    	}
    	}
    
    public static double getRandomDoubleInRange(final double minDouble, final double maxDouble) {
        return (minDouble >= maxDouble) ? minDouble : (new Random().nextDouble() * (maxDouble - minDouble) + minDouble);
    }

	private boolean isRaycast() {
		if(this.raycast.getValueState()) {
			return RaycastUtils.raycastEntity(mc.thePlayer.getDistanceToEntity(target), rotationYawHead, getPitch, target) == target;
		} else {
			return true;
		}
	}
	
	private boolean hasSword() {
        return mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
    }
    
    private void doBlock(final boolean setItemUseInCount) {
        if (setItemUseInCount) {
            final EntityPlayerSP thePlayer = mc.thePlayer;
            ((IEntityPlayer)thePlayer).setItemInUseCount(mc.thePlayer.getHeldItem().getMaxItemUseDuration());
        }
        final NetworkManager networkManager = mc.thePlayer.sendQueue.getNetworkManager();
        final int n = 255;
        if(this.blockMode.isCurrentMode("Hypixel")) {
        networkManager.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), n, mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
        } else {
        	 mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
		     mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
        }
        isBlocking = true;
    }
    
    private void unBlock(final boolean setItemUseInCount) {
        if (setItemUseInCount) {
            ((IEntityPlayer)mc.thePlayer).setItemInUseCount(0);
        }
        if(this.blockMode.isCurrentMode("Hypixel")) {
        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
        } else {
        	mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        isBlocking = false;
    }
    
    private float[] getEntityRotations(final EntityLivingBase target, final float[] lastrotation, final boolean aac, final int smooth) {
        final myAngleUtility angleUtility = new myAngleUtility(aac, (float)smooth);
        final Vector3d enemyCoords = new Vector3d(target.posX, target.posY + target.getEyeHeight(), target.posZ);
        final double posX = mc.thePlayer.posX;
        final double posY = mc.thePlayer.posY;
        final double n = posY + mc.thePlayer.getEyeHeight();
        final Vector3d myCoords = new Vector3d(posX, n, mc.thePlayer.posZ);
        final myAngle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);
        final myAngle srcAngle = new myAngle(lastrotation[0], lastrotation[1]);
        final myAngle smoothedAngle = angleUtility.smoothAngle(dstAngle, srcAngle);
        float yaw = smoothedAngle.getYaw();
        final float pitch = smoothedAngle.getPitch();
        final float n2 = yaw;
        final float yaw2 = MathHelper.wrapAngleTo180_float(n2 - mc.thePlayer.rotationYaw);
        yaw = mc.thePlayer.rotationYaw + yaw2;
        return new float[] { yaw, pitch };
    }
    
    /*private final boolean updateRotations(Entity entity) {
        VecRotation vecRotation;
        AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
        if (((Boolean)this.predictValue.get()).booleanValue()) {
            boundingBox = boundingBox.offset((entity.posX - entity.prevPosX) * (double)RandomUtils.nextFloat((float)((Number)this.minPredictSize.get()).floatValue(), (float)((Number)this.maxPredictSize.get()).floatValue()), (entity.posY - entity.prevPosY) * (double)RandomUtils.nextFloat((float)((Number)this.minPredictSize.get()).floatValue(), (float)((Number)this.maxPredictSize.get()).floatValue()), (entity.posZ - entity.prevPosZ) * (double)RandomUtils.nextFloat((float)((Number)this.minPredictSize.get()).floatValue(), (float)((Number)this.maxPredictSize.get()).floatValue()));
        }
        EntityPlayerSP entityPlayerSP = mc.thePlayer;
        if (me.Oxygen.utils.Liquidbounce.rotation.RotationUtils.searchCenter(boundingBox, (boolean)((Boolean)this.outborderValue.get() != false && !this.attacktimer.hasTimePassed(this.attackDelay / (long)2)), (boolean)((Boolean)this.randomCenterValue.get()), (boolean)((Boolean)this.predictValue.get()), (boolean)(getDistanceToEntityBox((Entity)((Entity)entityPlayerSP), (Entity)entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue())) == null) {
            return false;
        }
        VecRotation vecRotation2 = vecRotation;
        Rotation rotation = vecRotation2.component2();
        Rotation rotation2 = me.Oxygen.utils.Liquidbounce.rotation.RotationUtils.limitAngleChange((Rotation)me.Oxygen.utils.Liquidbounce.rotation.RotationUtils.serverRotation, (Rotation)rotation, (float)((float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue())));
        Rotation limitedRotation = rotation2;
        if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
        	me.Oxygen.utils.Liquidbounce.rotation.RotationUtils.setTargetRotation((Rotation)limitedRotation, (int)((Boolean)this.aac.getValueState() != false ? 15 : 0));
        } else {
            EntityPlayerSP entityPlayerSP2 = mc.thePlayer;
            limitedRotation.toPlayer((EntityPlayer)entityPlayerSP2);
        }
        return true;
    }*/
    
    private int randomNumber(final int max, final int min) {
        return (int)(Math.random() * (max - min)) + min;
    }
    
    private void doAttack() {
    	long attackDelay = TimeUtils.randomClickDelay(this.mincps.getValueState().intValue(), this.maxcps.getValueState().intValue());
        if (this.attacktimer.hasTimePassed(attackDelay)) {
            boolean miss = false;
            final boolean isInRange = mc.thePlayer.getDistanceToEntity(target) <= reach.getValueState();
            if (isInRange) {
                
                if (target.hurtTime > this.hurttime.getValueState()) {
                    miss = true;
                }
                
            }
            if (mc.thePlayer.isBlocking() || mc.thePlayer.getHeldItem() != null
					&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.getValueState() && !blockMode.isCurrentMode("Sigma")) {
				unBlock(!mc.thePlayer.isBlocking() && !autoBlock.getValueState()
						&& mc.thePlayer.getItemInUseCount() > 0);
			}
            if (isInRange) {
                this.attack(miss);
            }
            this.attacktimer.reset();
        }
    }
    
    private void attack(final boolean mistake) {
        this.Crit = false;
        
            this.needBlock = true;
            final ArrayList<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
            for (final Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityZombie && entity.isInvisible()) {
                    final Entity entity2 = entity;
                    if (entity2 == mc.thePlayer) {
                        continue;
                    }
                    list.add((EntityLivingBase)entity);
                }
            }
            if (list.size() == 0) {
                list.add(target);
            }
            needHitBot = list.get(this.random.nextInt(list.size()));
           
           if (!mistake && isRaycast()) {
            final Criticals Crit = (Criticals)Oxygen.INSTANCE.ModMgr.getModuleByName("Criticals");
            if (Crit.isEnabled()) {
            	Crit.autoCrit(target);
                this.Crit = true;
                this.attackSpeed = 0;
            }
            ++this.attackSpeed;
            new EventAttack(target).call();
            mc.thePlayer.swingItem();
            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(this.aac.getValueState() ? needHitBot : target, C02PacketUseEntity.Action.ATTACK));
			   KeepSprint keepSprint = (KeepSprint)Oxygen.INSTANCE.ModMgr.getModuleByName("KeepSprint");
            if (keepSprint.isEnabled()) {
				   // Critical Effect
				   if (mc.thePlayer.fallDistance > 0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() &&
						   !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && !mc.thePlayer.isRiding()) {
					   mc.thePlayer.onCriticalHit(target);
				   }
				   // Enchant Effect
				   if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), target.getCreatureAttribute()) > 0F)
					   mc.thePlayer.onEnchantmentCritical(target);
			   } else {
				   if (mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR)
					   mc.thePlayer.attackTargetEntityWithCurrentItem(target);
			   }
            if (!attacked.contains(target) && target instanceof EntityPlayer) {
                attacked.add(target);
            }
            needHitBot = null;
        } else {
        	mc.thePlayer.swingItem();
        }
    }
    
    private void getTarget() {
        final int maxSize = switchsize.getValueState().intValue();
        if (maxSize > 1) {
            this.setDisplayName("Switch");
        }
        else {
            this.setDisplayName("Single");
        }
        
        for (final EntityLivingBase ent : targets) {
            if (this.isValidEntity(ent)) {
                continue;
            }
            targets.remove(ent);
        }
        
        for (final Entity o3 : mc.theWorld.loadedEntityList) {
            final EntityLivingBase curEnt;
            if (o3 instanceof EntityLivingBase && this.isValidEntity((curEnt = (EntityLivingBase)o3)) && !targets.contains(curEnt)) {
                targets.add(curEnt);
            }
            if (targets.size() >= maxSize) {
                break;
            }
        }
        if (this.priority.isCurrentMode("Range")) {
            targets.sort((o1, o2) -> (int)(o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
        }
        if (this.priority.isCurrentMode("Fov")) {
            targets.sort(Comparator.comparingDouble(o -> {
                return (double)RotationUtil.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, RotationUtil.getRotations(o)[0]);
            }));
        }
        if (this.priority.isCurrentMode("Angle")) {
            targets.sort((o1, o2) -> {
                float[] rot1 = RotationUtil.getRotations((Entity)o1);
                float[] rot2 = RotationUtil.getRotations((Entity)o2);
                return (int)(mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
            });
        }
    }
    
    
    private final boolean isValidEntity(final EntityLivingBase entity) {
		if (entity.isEntityAlive()) {
			boolean canAttack = (mc.thePlayer.canEntityBeSeen(entity)
					? mc.thePlayer.getDistanceToEntity(entity) <= reach.getValueState() + blockReach.getValueState()
					: 3.8F >= mc.thePlayer.getDistanceToEntity(entity));
			if (canAttack) {
				if (Oxygen.INSTANCE.ModMgr.getModule(Antibot.class).isEnabled()) {
					if (Antibot.isBot(entity) || entity.isPlayerSleeping()) {// Check AntiBot
						return false;
					}
				}
				if (!this.isInFOV(entity)) {// Check Fov
					return false;
				}
				if (entity instanceof EntityPlayer && this.attackPlayers.getValueState()) {// Check Player
					if (entity != mc.thePlayer) {
						return !Teams.isOnSameTeam(entity)// Check Team
								&& !FriendManager.isFriend(entity.getName())
								&& !PlayersCheck.isPlayer(entity.getName())
								&& (!entity.isInvisible() || this.invisible.getValueState());// Check Invisible
					}
				}
				if ((entity instanceof EntityMob || entity instanceof EntityVillager || entity instanceof EntityBat)
						&& this.attackMobs.getValueState()) {// Check Mob
					return true;
				}
				if (entity instanceof EntityAnimal && this.attackAnimals.getValueState()) {// Check Animal
					return true;
				}
			}
		}
		return false;
	}
    
  //Fov判断
  	private final boolean isInFOV(Entity entity) {
  		int fov = (int) this.fov.getValueState().intValue();
  		return Math.abs(RotationUtils.getYawChange(entity.posX, entity.posZ)) <= fov
  				&& Math.abs(RotationUtils.getPitchChange(entity, entity.posY)) <= fov;
  	}
    
    public void onEnable() {
        this.curY = (float)new ScaledResolution(mc).getScaledHeight();
        this.Crit = false;
        this.shouldAddYaw = 0.0f;
        attacked = new ArrayList<EntityLivingBase>();
        this.axisAlignedBB = null;
        if (mc.thePlayer != null) {
            final float[] lastRotation = this.lastRotation;
            final int n = 0;
            lastRotation[n] = mc.thePlayer.rotationYaw;
            final float[] lastRotations = new float[2];
            final int n2 = 0;
            lastRotations[n2] = mc.thePlayer.rotationYaw;
            final int n3 = 1;
            lastRotations[n3] = mc.thePlayer.rotationPitch;
            this.lastRotations = lastRotations;
        }
        this.index = 0;
        super.onEnable();
    }
    
    public void onDisable() {
        this.curY = (float)new ScaledResolution(mc).getScaledHeight();
        this.Crit = false;
        this.axisAlignedBB = null;
        if (mc.thePlayer != null) {
            final float[] lastRotation = this.lastRotation;
            final int n = 0;
            lastRotation[n] = mc.thePlayer.rotationYaw;
            this.getPitch = this.mc.thePlayer.rotationPitch;
        }
        targets.clear();
        target = null;
        this.attacktimer.reset();
        this.unBlock(true);
        super.onDisable();
    }
    
    private double isInFov(final float var0, final float var1, final double var2, final double var4, final double var6) {
        final Vec3 var7 = new Vec3((double)var0, (double)var1, 0.0);
        final double posX = mc.thePlayer.posX;
        final double posY = mc.thePlayer.posY;
        final float[] var8 = getAngleBetweenVecs(new Vec3(posX, posY, mc.thePlayer.posZ), new Vec3(var2, var4, var6));
        final double var9 = MathHelper.wrapAngleTo180_double(var7.xCoord - var8[0]);
        return Math.abs(var9) * 2.0;
    }
    
    private float[] getAngleBetweenVecs(final Vec3 var0, final Vec3 var1) {
        final double var2 = var1.xCoord - var0.xCoord;
        final double var3 = var1.yCoord - var0.yCoord;
        final double var4 = var1.zCoord - var0.zCoord;
        final double var5 = Math.sqrt(var2 * var2 + var4 * var4);
        final float var6 = (float)(Math.atan2(var4, var2) * 180.0 / 3.141592653589793) - 90.0f;
        final float var7 = (float)(-(Math.atan2(var3, var5) * 180.0 / 3.141592653589793));
        return new float[] { var6, var7 };
    }
    
    private float[] getAnglesIgnoringNull(final Entity var0, final float var1, final float var2) {
        final float[] var3 = getAngles(var0);
        if (var3 == null) {
            return new float[] { 0.0f, 0.0f };
        }
        final float var4 = var3[0];
        final float var5 = var3[1];
        return new float[] { var1 + MathHelper.wrapAngleTo180_float(var4 - var1), var2 + MathHelper.wrapAngleTo180_float(var5 - var2) + 5.0f };
    }
    
    private float[] getAngles(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double posX = entity.posX;
        final double var1 = posX - mc.thePlayer.posX;
        final double posZ = entity.posZ;
        final double var2 = posZ - mc.thePlayer.posZ;
        double var4;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase var3 = (EntityLivingBase)entity;
            final double n = var3.posY + (var3.getEyeHeight() - 0.4);
            final double posY = mc.thePlayer.posY;
            var4 = n - (posY + mc.thePlayer.getEyeHeight());
        }
        else {
            final double n2 = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0;
            final double posY2 = mc.thePlayer.posY;
            var4 = n2 - (posY2 + mc.thePlayer.getEyeHeight());
        }
        final double var5 = MathHelper.sqrt_double(var1 * var1 + var2 * var2);
        final float var6 = (float)(Math.atan2(var2, var1) * 180.0 / 3.141592653589793) - 90.0f;
        final float var7 = (float)(-(Math.atan2(var4, var5) * 180.0 / 3.141592653589793));
        return new float[] { var6, var7 };
    }
    
    public boolean isValidToRotate(final double var0, final double var2) {
        if (mc.thePlayer != null) {
            if (mc.theWorld != null) {
                if (mc.thePlayer.getEntityWorld() != null) {
                    for (final Entity var4 : mc.thePlayer.getEntityWorld().loadedEntityList) {
                        if (var4 instanceof EntityPlayer) {
                            final Entity entity = var4;
                            if (entity == mc.thePlayer) {
                                continue;
                            }
                            if (mc.thePlayer.getDistanceToEntity(var4) >= var0) {
                                continue;
                            }
                            final float rotationYaw = mc.thePlayer.rotationYaw;
                            if (isInFov(rotationYaw, mc.thePlayer.rotationPitch, var4.posX, var4.posY, var4.posZ) < var2) {
                                return true;
                            }
                            continue;
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }
    
    private double normalizeAngle(final double var0, final double var2) {
        double var3 = Math.abs(var0 % 360.0 - var2 % 360.0);
        var3 = Math.min(360.0 - var3, var3);
        return Math.abs(var3);
    }
    
    private final double getDistanceToEntityBox(Entity $this$getDistanceToEntityBox,Entity entity) {
        Vec3 eyes;
        Vec3 vec3 = eyes = $this$getDistanceToEntityBox.getPositionEyes(0.0f);
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        Vec3 pos = getNearestPointBB(vec3, axisAlignedBB);
        double d = pos.xCoord - eyes.xCoord;
        boolean bl = false;
        double xDist = Math.abs((double)d);
        double d2 = pos.yCoord - eyes.yCoord;
        boolean bl2 = false;
        double yDist = Math.abs((double)d2);
        double d3 = pos.zCoord - eyes.zCoord;
        int n = 0;
        double zDist = Math.abs((double)d3);
        d3 = xDist;
        n = 2;
        boolean bl3 = false;
        double d4 = Math.pow((double)d3, (double)n);
        d3 = yDist;
        n = 2;
        double d5 = d4;
        bl3 = false;
        double d6 = Math.pow((double)d3, (double)n);
        d3 = zDist;
        n = 2;
        d5 += d6;
        bl3 = false;
        d6 = Math.pow((double)d3, (double)n);
        d3 = d5 + d6;
        n = 0;
        return Math.sqrt((double)d3);
    }
    
    private final Vec3 getNearestPointBB(Vec3 eye, AxisAlignedBB box) {
        double[] origin = new double[]{eye.xCoord, eye.yCoord, eye.zCoord};
        double[] destMins = new double[]{box.minX, box.minY, box.minZ};
        double[] destMaxs = new double[]{box.maxX, box.maxY, box.maxZ};
        int i = 0;

        for(byte var6 = 2; i <= var6; ++i) {
           if(origin[i] > destMaxs[i]) {
              origin[i] = destMaxs[i];
           } else if(origin[i] < destMins[i]) {
              origin[i] = destMins[i];
           }
        }

        return new Vec3(origin[0], origin[1], origin[2]);
     }

}
