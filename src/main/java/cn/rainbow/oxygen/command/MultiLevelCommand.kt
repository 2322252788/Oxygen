package cn.rainbow.oxygen.command

abstract class MultiLevelCommand(name: String, primaryNames: Array<String>) : Command(name, primaryNames)