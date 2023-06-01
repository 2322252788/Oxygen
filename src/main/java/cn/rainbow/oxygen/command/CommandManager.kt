package cn.rainbow.oxygen.command

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.command.commands.CBind
import cn.rainbow.oxygen.command.commands.CHelper
import cn.rainbow.oxygen.command.commands.CIRC
import cn.rainbow.oxygen.command.commands.CSetting
import cn.rainbow.oxygen.command.sender.CommandSender
import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventManager
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.CommandEvent
import java.util.Arrays

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

class CommandManager {

    val parser = CommandParser()

    val commands = CopyOnWriteArrayList<Command>()
    private val service = Executors.newFixedThreadPool(4)

    init {
        register(CHelper())
        register(CBind())
        register(CSetting())
        register(CIRC())
        EventManager.register(this)
    }

    fun register(command: Command) {
        val re = LinkedHashSet<String>()
        for (c in commands) {
            for (primaryName in c.primaryNames) {
                for (p1 in command.primaryNames) {
                    if (primaryName.equals(p1, ignoreCase = true)) {
                        re.add(p1)
                    }
                }
            }
        }
        if (re.size >= 1) {
            Oxygen.logger.error("[CommandMgr] 指令[${command.name}]的调用名${Arrays.toString(re.toArray())}存在重复, 无法添加")
            return
        }
        commands.add(command)
    }

    @EventTarget(events = [CommandEvent::class])
    private fun onEvent(event: Event) {
        if (event is CommandEvent) {
            event.setCancelled(true)
            when (event.messageType) {
                CommandEvent.MessageType.Normal -> {
                    service.execute(Thread { CommandSender.forMinecraft(event.command) })
                }
                CommandEvent.MessageType.Other -> {
                    service.execute(Thread { CommandSender.send(event.command, event.commandLogger) })
                }
            }
        }
    }
}
