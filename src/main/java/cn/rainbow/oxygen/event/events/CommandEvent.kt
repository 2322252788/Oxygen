package cn.rainbow.oxygen.event.events

import cn.rainbow.oxygen.command.logger.CommandLogger
import cn.rainbow.oxygen.event.Event

class CommandEvent : Event {

    val command: String
    val messageType: MessageType
    lateinit var commandLogger: CommandLogger

    constructor(message: String) {
        this.command = message
        messageType = MessageType.Normal
    }

    constructor(message: String, commandLogger: CommandLogger) {
        this.command = message
        this.commandLogger = commandLogger
        this.messageType = MessageType.Other
    }

    enum class MessageType {
        Normal, Other
    }
}