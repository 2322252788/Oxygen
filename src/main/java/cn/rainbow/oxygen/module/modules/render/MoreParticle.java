package cn.rainbow.oxygen.module.modules.render;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.EventAttack;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import net.minecraft.util.EnumParticleTypes;

public class MoreParticle extends Module {

	private NumberValue CrackSize = new NumberValue("CrackSize", 2.0, 0.0, 10.0, 1);
	private BooleanValue Crit = new BooleanValue("CritParticle", true);
	private BooleanValue Normal = new BooleanValue("NormalParticle", true);

	public MoreParticle() {
		super("MoreParticle", Category.Render);
	}

	@EventTarget(events = EventAttack.class)
	private void onEvent(Event e) {
		if(e instanceof EventAttack) {
			EventAttack ea = (EventAttack)e;
			for (int index = 0; index < CrackSize.getCurrentValue(); ++index) {
	            if (Crit.getCurrentValue()) {
	                mc.effectRenderer.emitParticleAtEntity(ea.getEntity(), EnumParticleTypes.CRIT);
	            }
	            if (Normal.getCurrentValue()) {
	                mc.effectRenderer.emitParticleAtEntity(ea.getEntity(), EnumParticleTypes.CRIT_MAGIC);
	            }
	        }
		}
	}

}
