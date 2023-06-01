package cn.rainbow.oxygen.command.commands

import cn.rainbow.oxygen.CommandInfo
import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.command.RawCommand
import cn.rainbow.oxygen.utils.Message

@CommandInfo(name = "Help", primaryNames = ["help"])
class CHelper: RawCommand() {
    override fun run(): Boolean {
        val sb = StringBuilder()
        val mgr = Oxygen.INSTANCE.commandManager
        for (c in mgr.commands) {
            for (h in c.helpList) {
                sb.append(h + if (c.helpList.indexOf(h) == c.helpList.size -1 &&
                    mgr.commands.indexOf(c) == mgr.commands.size - 1)
                    "" else "\n"
                )
            }
        }
        Message.tellPlayer("Help", sb.toString())
        return true
    }
}