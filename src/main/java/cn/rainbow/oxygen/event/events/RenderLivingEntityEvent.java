package cn.rainbow.oxygen.event.events;

import cn.rainbow.oxygen.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class RenderLivingEntityEvent extends Event {

    private Type type;
	private EntityLivingBase entity;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float rotationYawHead;
    private float rotationPitch;
    private float chestRot;
    private float offset;

    public RenderLivingEntityEvent(final EntityLivingBase entity, final Type type, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float rotationYawHead, final float rotationPitch, final float chestRot, final float offset) {
        this.entity = entity;
        this.type = type;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.rotationYawHead = rotationYawHead;
        this.rotationPitch = rotationPitch;
        this.chestRot = chestRot;
        this.offset = offset;
    }

    public RenderLivingEntityEvent(final EntityLivingBase entity, final Type type) {
        this.entity = entity;
        this.type = type;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public Type getType(){
        return this.type;
    }

    public float getLimbSwing() {
        return this.limbSwing;
    }

    public void setLimbSwing(final float limbSwing) {
        this.limbSwing = limbSwing;
    }

    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }

    public void setLimbSwingAmount(final float limbSwingAmount) {
        this.limbSwingAmount = limbSwingAmount;
    }

    public float getAgeInTicks() {
        return this.ageInTicks;
    }

    public void setAgeInTicks(final float ageInTicks) {
        this.ageInTicks = ageInTicks;
    }

    public float getRotationYawHead() {
        return this.rotationYawHead;
    }

    public void setRotationYawHead(final float rotationYawHead) {
        this.rotationYawHead = rotationYawHead;
    }

    public float getRotationPitch() {
        return this.rotationPitch;
    }

    public void setRotationPitch(final float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }

    public float getOffset() {
        return this.offset;
    }

    public void setOffset(final float offset) {
        this.offset = offset;
    }

    public float getRotationChest() {
        return this.chestRot;
    }

    public void setRotationChest(final float rotationChest) {
        this.chestRot = rotationChest;
    }

    public enum Type{
        PRE, POST
    }

}
