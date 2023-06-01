package cn.rainbow.oxygen.command

abstract class SimpleCommand : Command() {
    abstract fun run(args: Array<String>): Boolean
}