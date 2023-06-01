package cn.rainbow.oxygen.command.logger

interface CommandLogger {
    fun raw(o: Any)
    fun raw(o: Any, vararg objects: Any)
    fun info(o: Any)
    fun debug(o: Any)
    fun warn(o: Any)
    fun error(o: Any)
    fun tellPlayer(title: String, o: Any)
}