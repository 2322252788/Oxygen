package cn.rainbow.oxygen.command

import cn.rainbow.oxygen.CommandInfo
import cn.rainbow.oxygen.command.logger.CommandLogger
import cn.rainbow.oxygen.command.logger.ConsoleCommandLogger
import java.util.ArrayList

abstract class Command() {

    val name: String
    val primaryNames: Array<String>
    val helpList: ArrayList<String> = ArrayList()

    var logger: CommandLogger = ConsoleCommandLogger.COMMAND_LOGGER

    init {
        val annotation = this::class.java.getAnnotation(CommandInfo::class.java)
        this.name = annotation.name
        this.primaryNames = annotation.primaryNames
    }

    protected fun addHelp(text: String) {
        helpList.add(text)
    }
}