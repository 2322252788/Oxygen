package cn.rainbow.oxygen.command.logger

import cn.rainbow.oxygen.utils.Message

class CMinecraftLogger: CommandLogger {
    override fun raw(o: Any) {
        Message.tellPlayer("Command", o.toString())
    }

    override fun raw(o: Any, vararg objects: Any) {
        Message.tellPlayer("Command", o.toString().format(*objects))
    }

    override fun info(o: Any) {
        Message.tellPlayer("Info", o.toString())
    }

    override fun debug(o: Any) {
        Message.tellPlayer("Debug", o.toString())
    }

    override fun warn(o: Any) {
        Message.tellPlayer("Warn", o.toString())
    }

    override fun error(o: Any) {
        Message.tellPlayer("Error", o.toString())
    }

    override fun tellPlayer(title: String, o: Any) {
        Message.tellPlayer(title, o.toString())
    }
}