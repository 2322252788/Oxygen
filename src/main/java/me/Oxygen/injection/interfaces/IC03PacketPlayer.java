package me.Oxygen.injection.interfaces;

public interface IC03PacketPlayer {
	
	double getPosX();
	
	double getPosY();
	
	double getPosZ();

	void setPosX(double x);

	void setPosY(double y);

	void setPosZ(double z);

	void setYaw(float yaw);

	float getYaw();

	void setPitch(float pitch);

	float getPitch();

	boolean isOnGround();

	void setOnGround(boolean ground);

	void setRotating(boolean rotating);

	boolean getRotating();

}
