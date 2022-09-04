package cn.rainbow.oxygen.command.logger

import cn.rainbow.oxygen.Oxygen

class ConsoleCommandLogger : CommandLogger {

    private val logger = Oxygen.logger

    override fun raw(o: Any) {
        logger.info(o.toString())
    }

    override fun raw(o: Any, vararg objects: Any) {
        logger.info(o.toString().format(*objects))
    }

    override fun info(o: Any) {
        logger.info("[Command]$o")
    }

    override fun debug(o: Any) {
        logger.debug("[Command]$o")
    }

    override fun warn(o: Any) {
        logger.warn("[Command]$o")
    }

    override fun error(o: Any) {
        logger.error("[Command]$o")
    }

    override fun tellPlayer(title: String, o: Any) {
        logger.info("[$title] $o")
    }

    companion object {
        val COMMAND_LOGGER = ConsoleCommandLogger()
    }
}