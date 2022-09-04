package cn.rainbow.oxygen.command

abstract class RawCommand(name: String, primaryNames: Array<String>) : Command(name, primaryNames) {
    abstract fun run(): Boolean
}