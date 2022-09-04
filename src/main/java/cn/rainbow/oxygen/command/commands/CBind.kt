package cn.rainbow.oxygen.command.commands

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.command.SimpleCommand
import org.lwjgl.input.Keyboard

class CBind: SimpleCommand("Bind", arrayOf("bind")) {
    override fun run(args: Array<String>): Boolean {
        if (args.size == 2) {
            val mode = Oxygen.INSTANCE.moduleManager.getModule(args[0])
            val key = Keyboard.getKeyIndex(args[1].uppercase())
            if (mode != null) {
                mode.keyCode = key
                logger.tellPlayer("CBind", "${mode.name} bindKey to ${args[1].uppercase()}")
            }
        }
        return false
    }
}