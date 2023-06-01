package cn.rainbow.oxygen.module

import by.radioegor146.nativeobfuscator.Native
import cn.rainbow.oxygen.module.modules.combat.*
import cn.rainbow.oxygen.module.modules.client.Rotations
import cn.rainbow.oxygen.module.modules.client.TargetEntity
import cn.rainbow.oxygen.module.modules.render.*
import cn.rainbow.oxygen.module.modules.client.NoCommand
import cn.rainbow.oxygen.module.modules.movement.*
import cn.rainbow.oxygen.module.modules.player.*
import cn.rainbow.oxygen.module.modules.world.Check
import cn.rainbow.oxygen.module.modules.world.NoFall
import cn.rainbow.oxygen.module.modules.world.Scaffold

@Native
class ModuleManager {

    val modules: ArrayList<Module> = ArrayList()

    init {
        loadModules()
    }

    private fun loadModules() {
        //Combat
        addModule(Aimbot())
        addModule(AntiBot())
        addModule(AutoClicker())
        addModule(AutoPot())
        addModule(AutoSword())
        addModule(Criticals())
        addModule(Hitbox())
        addModule(KillAura())
        addModule(Reach)
        addModule(Velocity())

        //Movement
        addModule(InvMove())
        addModule(NoSlow())
        addModule(Speed())
        addModule(Sprint())
        addModule(TargetStrafe())

        //Render
        addModule(Animation())
        addModule(BlockOverlay())
        addModule(ChestESP())
        addModule(ClickGui())
        addModule(DMGParticle())
        addModule(ESP())
        addModule(Fullbright())
        addModule(HUD())
        addModule(ItemPhysic())
        addModule(KeyStrokes())
        addModule(MoreParticle())
        addModule(Nametags())
        addModule(Projectiles())
        addModule(TrueSight)

        //Player
        addModule(AutoArmor())
        addModule(AutoTool())
        addModule(ChestStealer())
        addModule(Eagle())
        addModule(FastUse())
        addModule(Teams())

        //World
        addModule(Check())
        addModule(NoFall())
        addModule(Scaffold())

        //Misc
        addModule(Rotations)
        addModule(TargetEntity())
        addModule(NoCommand())
    }

    fun addModule(module: Module) {
        modules.add(module)
    }

    fun getModule(clazz: Class<out Module>): Module? {
        for (module in modules) {
            if (module.javaClass == clazz) {
                return module
            }
        }
        return null
    }

    fun getModule(moduleName: String): Module? {
        for (module in modules) {
            if (module.name.equals(moduleName, ignoreCase = true)) {
                return module
            }
        }
        return null
    }

    fun setKeyCode(m: Module, key: Int) {
        for (mod in modules) {
            if (mod == m) {
                mod.keyCode = key
            }
        }
    }

    fun getEnableMods(): ArrayList<Module> {
        val list = ArrayList<Module>()
        for (m in modules) {
            if (m.enabled)
                list.add(m)
        }
        return list
    }
}