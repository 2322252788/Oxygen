package cn.rainbow.oxygen.command.commands

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.command.SimpleCommand
import com.google.gson.JsonObject

class CIRC: SimpleCommand("IRC", arrayOf("irc")) {
    override fun run(args: Array<String>): Boolean {
        if (Oxygen.INSTANCE.client.login) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("Type", "IRC")
            jsonObject.addProperty("Data", args[0])
            Oxygen.INSTANCE.client.send(jsonObject.toString())
        } else {
            logger.error("? 你为什么没登录")
            Thread.sleep(5000)
            Oxygen.INSTANCE.exit()
        }
        return true
    }
}