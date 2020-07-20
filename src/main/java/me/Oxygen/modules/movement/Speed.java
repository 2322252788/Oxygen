package me.Oxygen.modules.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMove;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.event.events.EventPacket.PacketType;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.injection.interfaces.IMinecraft;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.combat.KillAura;
import me.Oxygen.modules.player.Collective;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import me.Oxygen.utils.other.MoveUtil;
import me.Oxygen.utils.other.PlayerUtil;
import me.Oxygen.utils.timer.TimeHelper;
import me.Oxygen.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@ModuleRegister(name = "Speed", category = Category.MOVEMENT)
public class Speed extends Module {
	
	TimeHelper ticks = new TimeHelper();
	TimeHelper other = new TimeHelper();
	TimeHelper lastCheck = new TimeHelper();
	public static Value<String> mode = new Value<String>("Speed", "Mode", 0);
	private boolean firstjump;
	private int stage;
	public boolean shouldslow = false;
    int counter, level;
    boolean collided = false, lessSlow;
    double less, stair;
    private double speed;
    private double movementSpeed;

	   public Speed() {
	      mode.mode.add("AAC");
	      mode.mode.add("AAC198");
	      mode.mode.add("AACLowhop");
	      mode.mode.add("QiuYue");
	      mode.mode.add("GudHop");
	      mode.mode.add("Hypixel");
	      mode.mode.add("Hypixel2");
	      mode.mode.add("Mineplex");
	      //this.showValue = mode;
	   }

	   @EventTarget(events = {EventMotion.class, EventUpdate.class,
			   EventMove.class ,EventPacket.class})
	   public void onPost(Event event) {
		   if(event instanceof EventMotion) {
			   EventMotion em = (EventMotion)event;
		   if(em.getMotionType() == MotionType.POST) {
	      this.mc.thePlayer.cameraPitch = 0.0F;
		   }
		   }
		   
		   if(event instanceof EventUpdate) {
			   if(mode.isCurrentMode("GudHop")) {
			    	 this.setDisplayName("GudHop");
			         if(this.mc.thePlayer.onGround && PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
			            ((IMinecraft)this.mc).getTimer().timerSpeed = 1.0F;
			            this.mc.thePlayer.jump();
			         } else if(PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
			            PlayerUtil.setSpeed(0.8D);
			         }

			         if(!PlayerUtil.MovementInput()) {
			            this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = 0.0D;
			         }
			      } else if(mode.isCurrentMode("AAC")) {
			    	  this.setDisplayName("AAC");
			         if(PlayerUtil.MovementInput()) {
			            if(this.mc.thePlayer.hurtTime < 1) {
			               if(this.mc.thePlayer.onGround) {
			                  if(!this.firstjump) {
			                     this.firstjump = true;
			                  }

			                  this.mc.thePlayer.jump();
			                  this.mc.thePlayer.motionY = 0.405D;
			                  
			               } else {
			                  this.firstjump = false;
			                  this.mc.thePlayer.motionY -= 0.0149D;
			               }
			            }
			         } else {
			            this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = (double)(0);
			         }
			         PlayerUtil.setSpeed(PlayerUtil.getSpeed());
			      }else if(mode.isCurrentMode("AACLowhop")) {
			    	  this.setDisplayName("AACLowhop");
			          if(PlayerUtil.MovementInput()) {
			              if(this.mc.thePlayer.hurtTime < 1) {
			                  if(this.mc.thePlayer.onGround) {
			                     if(!this.firstjump) {
			                        this.firstjump = true;
			                     }

			                     this.mc.thePlayer.jump();
			                     this.mc.thePlayer.motionY = 0.15D;
			                     
			                  } else {
			                     this.firstjump = false;
			                     this.mc.thePlayer.motionY -= 0.8;
			                  }
			               }
			          }
			          PlayerUtil.setSpeed(PlayerUtil.getSpeed());
			       }else if(mode.isCurrentMode("Mineplex")) {
					   this.setDisplayName("Mineplex");
					   ((IMinecraft)this.mc).getTimer().timerSpeed = 1.1F;
			           if(PlayerUtil.MovementInput()) {
			               if(Minecraft.getMinecraft().thePlayer.onGround) {
			                   Minecraft.getMinecraft().thePlayer.jump();
			                   if(KillAura.target == null) {
			                       this.mc.thePlayer.motionY = 0.4229;
			                       PlayerUtil.setSpeed(0.475);
			                   }else {
			                       PlayerUtil.setSpeed(0.41);
			                   }
			                }else {
			             	   this.mc.thePlayer.motionY -= 0.001D;
			                }
			           }
			           PlayerUtil.setSpeed(PlayerUtil.getSpeed());
			       }else if(mode.isCurrentMode("AAC198")) {
			    	  this.setDisplayName("AAC198");
			          if(PlayerUtil.MovementInput()) {
			              if(this.mc.thePlayer.hurtTime < 1) {
			                 if(this.mc.thePlayer.onGround) {
			                    if(!this.firstjump) {
			                       this.firstjump = true;
			                    }

			                    this.mc.thePlayer.jump();
			                    this.mc.thePlayer.motionY = 0.42D;
			                    
			                 } else {
			                    this.firstjump = false;
			                 }
			              }
			           } else {
			              this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = (double)(0);
			           }

			          PlayerUtil.setSpeed(PlayerUtil.getSpeed());
			       }else if(mode.isCurrentMode("QiuYue")) {
			    	  this.setDisplayName("QiuYue");
			          if(PlayerUtil.MovementInput()) {
			              if(this.mc.thePlayer.hurtTime < 1) {
			                 if(this.mc.thePlayer.onGround) {
			                    if(!this.firstjump) {
			                       this.firstjump = true;
			                    }

			                    this.mc.thePlayer.jump();
			                    this.mc.thePlayer.motionY = 0.405D;
			                    
			                 } else {
			                    this.firstjump = false;
			                    this.mc.thePlayer.motionY -= 0.0135D;
			                 }
			              }
			           } else {
			              this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = (double)(0);
			           }

			          PlayerUtil.setSpeed(PlayerUtil.getSpeed());
			       }
		   }
		   
		   if(event instanceof EventMove) {
			   EventMove emove = (EventMove)event; 
			   if(mode.isCurrentMode("Hypixel")) {
			   		this.setDisplayName("Hypixel");
			   		if (mc.thePlayer.isCollidedHorizontally) { this.collided = true; } if (this.collided) {
			   			((IMinecraft) mc).getTimer().timerSpeed = 1.0f; this.stage = -1; 
			   			} if (this.stair > 0.0) {
			   				this.stair -= 0.25; 
			   				} this.less -= this.less > 1.0 ? 0.12 : 0.11; 
			   				if (this.less < 0.0) {
			   					this.less = 0.0; 
			   					} if (!isInLiquid() && MoveUtil.isOnGround(0.01) && PlayerUtil.isMoving2()) {
			   						this.collided = mc.thePlayer.isCollidedHorizontally;
			   						if (this.stage >= 0 || this.collided) {
			   							this.stage = 0; double motY = 0.4086666 + (double)MoveUtil.getJumpEffect() * 0.1;
			   							if (this.stair == 0.0) {
			   								mc.thePlayer.jump(); mc.thePlayer.motionY = motY; emove.setY(mc.thePlayer.motionY); 
			   								} 
			   							this.less += 1.0; this.lessSlow = this.less > 1.0 && !this.lessSlow; 
			   							if (this.less > 1.12) {
			   								this.less = 1.12; 
			   								} 
			   							} 
			   						} 
			   					this.movementSpeed = this.getHypixelSpeed(this.stage) + 0.0331; 
			   					this.movementSpeed *= 0.91; 
			   					if (this.stair > 0.0) {
			   						this.movementSpeed *= 0.7 - (double)getSpeedEffect() * 0.1; 
			   						} 
			   					if (this.stage < 0) {
			   						this.movementSpeed = defaultSpeed(); 
			   						} 
			   					if (this.lessSlow) {
			   						this.movementSpeed *= 0.96; 
			   						} if (this.lessSlow) {
			   							this.movementSpeed *= 0.95; 
			   							} if (isInLiquid()) {
			   								this.movementSpeed = 0.12; 
			   								} if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
			   									TargetStrafe ts = (TargetStrafe)Oxygen.INSTANCE.ModMgr.getModuleByName("TargetStrafe");
			   										if(ts.canStrafe()) {
			   											ts.strafe(emove, this.movementSpeed);
			   										} else {
			   											this.setMotion(emove, this.movementSpeed); 
			   										}
			   									++this.stage; 
			   										
			   		}
			   }
			   		if(mode.isCurrentMode("Hypixel2")) {
			   			this.setDisplayName("Hypixel2");
			            if(mc.thePlayer.isCollidedHorizontally) {
			               this.collided = true;
			            }

			            if(this.collided) {
			            	((IMinecraft) mc).getTimer();
			               ((IMinecraft) mc).getTimer().timerSpeed = 1.0F;
			               this.stage = -1;
			            }

			            if(this.stair > 0.0D) {
			               this.stair -= 0.25D;
			            }

			            this.less -= this.less > 1.0D?0.12D:0.11D;
			            if(this.less < 0.0D) {
			               this.less = 0.0D;
			            }

			            if(!PlayerUtil.isInLiquid() && isOnGround(0.01D) && this.isMoving()) {
			               double a;
			               this.collided = mc.thePlayer.isCollidedHorizontally;
			               if(this.stage >= 0 || this.collided) {
			                  this.stage = 0;
			                  a = 0.4086666D + (double)PlayerUtil.getJumpEffect() * 0.1D;
			                  if(this.stair == 0.0D) {
			                     mc.thePlayer.jump();
			                     mc.thePlayer.motionY = a;
			                     emove.setY(mc.thePlayer.motionY);
			                  }

			                  ++this.less;
			                  this.lessSlow = this.less > 1.0D && !this.lessSlow;
			                  if(this.less > 1.12D) {
			                     this.less = 1.12D;
			                  }
			               }
			            }

			            this.speed = this.getHypixelSpeed(this.stage) + 0.0331D;
			            this.speed *= 0.91D;
			            if(this.stair > 0.0D) {
			               this.speed *= 0.66D - (double)getSpeedEffect() * 0.1D;
			            }

			            if(this.stage < 0) {
			               this.speed = this.getBaseMoveSpeed();
			            }

			            if(this.lessSlow) {
			               this.speed *= 0.93D;
			            }

			            if(PlayerUtil.isInLiquid()) {
			               this.speed = 0.12D;
			            }

			            if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
								this.setMotion(emove, this.movementSpeed); 
							++this.stage; 
                       }
			         }
		   }
		   
		   if(event instanceof EventPacket) {
			   EventPacket ep = (EventPacket)event;
			   if(ep.getPacketType() == PacketType.Recieve) {
				   if (ep.getPacket() instanceof S08PacketPlayerPosLook) {
						if(Collective.lagcheck) {
							this.set(false);
							mc.thePlayer.onGround = false;
							mc.thePlayer.motionX *= 0;
							mc.thePlayer.motionZ *= 0;
							mc.thePlayer.jumpMovementFactor = 0;
							ClientUtil.sendClientMessage("[LagBackCheck] Speed Disable!", Type.WARNING);
						}
					}
			   }
		   }
	   }

		public int getRandom(int cap) {
	        Random rng = new Random();
	        return rng.nextInt(cap);
	    }
		
		public double round(double value, int places) {
	        if (places < 0) {
	            throw new IllegalArgumentException();
	        }
	        BigDecimal bd2 = new BigDecimal(value);
	        bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
	        return bd2.doubleValue();
	    }
	   
	    public boolean isBlockUnder(Material blockMaterial) {
	        return this.mc.theWorld.getBlockState(this.underPlayer()).getBlock().getMaterial() == blockMaterial;
	    }
	    
	    public BlockPos underPlayer() {
	        return new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.getEntityBoundingBox().minY - 1.0, this.mc.thePlayer.posZ);
	    }
	   
	   private void setMotion(EventMove em, double speed) {
	        double forward = mc.thePlayer.movementInput.moveForward;
	        double strafe = mc.thePlayer.movementInput.moveStrafe;
	        float yaw = mc.thePlayer.rotationYaw;
	        if ((forward == 0.0D) && (strafe == 0.0D)) {
	            em.setX(0.0D);
	            em.setZ(0.0D);
	        } else {
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
	            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
	            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
	        }
	    }
	   
	   protected boolean isMoving() {
		      if(mc.thePlayer.movementInput.moveForward == 0.0F) {
		         if(mc.thePlayer.movementInput.moveStrafe == 0.0F) {
		            return false;
		         }
		      }

		      return true;
		   }
	    
	    private double getHypixelSpeed(int stage) {
	        double value = defaultSpeed() + (0.028 * getSpeedEffect()) + (double) getSpeedEffect() / 15;
	        double firstvalue = 0.4145 + (double) getSpeedEffect() / 12.5;
	        double decr = (((double) stage / 500) * 2);


	        if (stage == 0) {
	            //JUMP
	            if (other.delay(300)) {
	            	other.reset();
	                //mc.timer.timerSpeed = 1.354f;
	            }
	            if (!lastCheck.delay(500)) {
	                if (!shouldslow)
	                    shouldslow = true;
	            } else {
	                if (shouldslow)
	                    shouldslow = false;
	            }
	            value = 0.64 + (getSpeedEffect() + (0.028 * getSpeedEffect())) * 0.134;

	        } else if (stage == 1) {
	            if (((IMinecraft)this.mc).getTimer().timerSpeed == 1.354f) {
	                //mc.timer.timerSpeed = 1.254f;
	            }
	            value = firstvalue;
	        } else if (stage >= 2) {
	            if (((IMinecraft)this.mc).getTimer().timerSpeed == 1.254f) {
	                //mc.timer.timerSpeed = 1f;
	            }
	            value = firstvalue - decr;
	        }
	        if (shouldslow || !lastCheck.delay(500) || collided) {
	            value = 0.2;
	            if (stage == 0)
	                value = 0;
	        }


	        return Math.max(value, shouldslow ? value : defaultSpeed() + (0.028 * getSpeedEffect()));
	    }
	    
	    public static boolean isOnGround(double height) {
	        if (!Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	    
	    public static double defaultSpeed() {
	        double baseSpeed = 0.2873D;
	        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
	            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	          //  if(((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Hypixel")){
	           // 	baseSpeed *= (1.0D + 0.225D * (amplifier + 1));
	           // }else
	            	baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
	        }
	        return baseSpeed;
	    }
	    
	    public static int getSpeedEffect() {
	        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed))
	            return Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
	        else
	            return 0;
	    }
	    
	    public static int getJumpEffect() {
	        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump))
	            return Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
	        else
	            return 0;
	    }
	    
	    public static boolean isInLiquid() {
	        if (Minecraft.getMinecraft().thePlayer.isInWater()) {
	            return true;
	        }
	        boolean inLiquid = false;
	        final int y = (int) Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY;
	        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
	            for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
	                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	                if (block != null && block.getMaterial() != Material.air) {
	                    if (!(block instanceof BlockLiquid)) return false;
	                    inLiquid = true;
	                }
	            }
	        }
	        return inLiquid;
	    }
	   
	   public void goToGround()
	   {
	     double minY = mc.thePlayer.posY;
	     if (minY <= 0.0D) {
	       return;
	     }
	     for (double y = mc.thePlayer.posY; y > minY;)
	     {
	       y -= 8.0D;
	       if (y < minY) {
	         y = minY;
	       }
	       
	       C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
	       
	       mc.thePlayer.sendQueue.addToSendQueue(packet);
	     }
	     for (double y = minY; y < mc.thePlayer.posY;)
	     {
	       y += 8.0D;
	       if (y > mc.thePlayer.posY) {
	         y = mc.thePlayer.posY;
	       }
	       
	       C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
	       
	       mc.thePlayer.sendQueue.addToSendQueue(packet);
	     }
	   }
	   
	   @Override
	   public void onDisable() {
	       this.mc.thePlayer.motionX *= 1.0;
	       this.mc.thePlayer.motionY *= 1.0;
	       this.mc.thePlayer.motionZ *= 1.0;
	       ((IMinecraft)this.mc).getTimer().timerSpeed = 1.0f;
	       //mc.thePlayer.speedInAir = 0.02f;
	       super.onDisable();
	   } 

	   @Override
	   public void onEnable() {
	       this.mc.thePlayer.motionX *= 0.0;
	       this.mc.thePlayer.motionZ *= 0.0;
	       super.onEnable();
	   }

	   private double getBaseMoveSpeed() {
	      double baseSpeed = 0.2873D;
	      if(this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
	         int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
	      }

	      return baseSpeed;
	   }
	}
