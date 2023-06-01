package cn.rainbow.oxygen.module.modules.render;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.AttackEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.ModuleInfo;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import net.minecraft.util.EnumParticleTypes;

@ModuleInfo(name = "MoreParticle", category = Category.Render)
public class MoreParticle extends Module {

	private final NumberValue crackSize = new NumberValue("CrackSize", 2.0, 0.0, 10.0, 1);
	private final BooleanValue crit = new BooleanValue("CritParticle", true);
	private final BooleanValue normal = new BooleanValue("NormalParticle", true);

	@EventTarget(events = AttackEvent.class)
	private void onEvent(Event e) {
		if(e instanceof AttackEvent) {
			AttackEvent ea = (AttackEvent)e;
			for (int index = 0; index < crackSize.getCurrentValue(); ++index) {
	            if (crit.getCurrentValue()) {
	                mc.effectRenderer.emitParticleAtEntity(ea.getEntity(), EnumParticleTypes.CRIT);
	            }
	            if (normal.getCurrentValue()) {
	                mc.effectRenderer.emitParticleAtEntity(ea.getEntity(), EnumParticleTypes.CRIT_MAGIC);
	            }
	        }
		}
	}

}
