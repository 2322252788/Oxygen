package cn.rainbow.oxygen.injection.interfaces;

import net.minecraft.util.AxisAlignedBB;

public interface IEntity {
	int getNextStepDistance();

	void setNextStepDistance(int i);

	int getFire();

	void setFire(final int p0);

	boolean getInWeb();

	void setInWeb(boolean inWeb);

	AxisAlignedBB getBoundingBox();
}
