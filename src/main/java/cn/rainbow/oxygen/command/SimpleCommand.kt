package cn.rainbow.oxygen.command

abstract class SimpleCommand(name: String, primaryNames: Array<String>) : Command(name, primaryNames) {
    abstract fun run(args: Array<String>): Boolean
}