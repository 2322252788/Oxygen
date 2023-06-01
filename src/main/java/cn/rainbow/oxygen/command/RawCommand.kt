package cn.rainbow.oxygen.command

abstract class RawCommand() : Command() {
    abstract fun run(): Boolean
}