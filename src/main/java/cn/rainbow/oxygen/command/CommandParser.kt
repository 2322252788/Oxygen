package cn.rainbow.oxygen.command

import cn.rainbow.oxygen.command.logger.CommandLogger
import cn.rainbow.oxygen.Oxygen
import java.lang.reflect.Method

class CommandParser {

    fun run(input: String, logger: CommandLogger){
        val parse = parseCommand(input) ?: return
        var command : Command? = null
        for (c in Oxygen.INSTANCE.commandManager.commands) {
            var get = false
            for (primaryName in c.primaryNames) {
                if (primaryName.lowercase().equals(parse.key, ignoreCase = true)) {
                    command = c
                    get = true
                    break
                }
            }
            if (get) break
        }
        if (command == null) {
            logger.error("使用.help查看帮助")
            return
        }
        val startTime = System.nanoTime()
        command.logger = logger
        when (command) {
            is RawCommand -> {
                if (!command.run()) helper(command)
            }
            is SimpleCommand -> {
                if (!command.run(parse.value.toTypedArray())) helper(command)
            }
            is MultiLevelCommand -> {
                val methodMap = getSubCommandMethod(command.javaClass) ?: return
                if (parse.value.size == 0) {
                    helper(command)
                    return
                }
                try {
                    val method = methodMap[parse.value.removeAt(0)]
                    if (method == null) {
                        logger.error("指令执行错误 未找到子指令!")
                        return
                    }
                    if (method.parameterCount != parse.value.size) {
                        logger.error("参数长度错误 （；´д｀）ゞ")
                        helper(command)
                        return
                    }
                    val o = ArrayList<Any>(parse.value)
                    var count = -1
                    for (parameter in method.parameters) {
                        count++
                        when (parameter.type) {
                            String::class.java -> continue
                            Long::class.java -> {
                                try {
                                    o[count] = (o[count].toString().toLong())
                                } catch (exception: NumberFormatException) {
                                    logger.error("输入的不是数字!")
                                }
                            }
                            Double::class.java -> {
                                try {
                                    o[count] = (o[count].toString().toDouble())
                                } catch (exception: NumberFormatException) {
                                    logger.error("输入的不是数字!")
                                }
                            }
                            Boolean::class.java -> {
                                o[count] = o[count].toString().equals("true", ignoreCase = true)
                            }
                        }
                    }
                    method.isAccessible = true
                    try {
                        if (!(method.invoke(command, *o.toArray()) as Boolean)) helper(command)
                    } catch (e: Exception) {
                        logger.debug("指令执行过程错误 请重新检查你的代码 {{{(>_<)}}}\n%s".format(e.toString()))
                    }
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                    logger.error("参数长度错误 （；´д｀）ゞ")
                    helper(command)
                }
            }
        }
        val endTime = System.nanoTime()
        logger.debug("执行时间: ${endTime - startTime}ns")
    }

    private fun helper(command: Command) {
        val sb = StringBuilder()
        if (command.helpList.size < 1) return
        for (help in command.helpList) {
            if (command.helpList.indexOf(help) == command.helpList.size - 1) {
                sb.append(help)
                break
            }
            sb.appendLine(help)
        }
        command.logger.info(sb.toString())
    }

    /**
     * 一个简单的命令处理
     * @author RakkiPipi
     * @param input 输入的所有除前缀的所有内容
     * @return 返回一个Entry K是要执行的命令 V是执行该命令的参数
     */
    private fun parseCommand(input: String): MutableMap.MutableEntry<String, ArrayList<String>>? {
        val args = ArrayList(listOf(*input.split(" ".toRegex()).toTypedArray()))
        val map = HashMap<String, ArrayList<String>>()
        map[args.removeAt(0)] = args
        for (e in map.entries) {
            return e
        }
        return null
    }

    /**
     * 获取一个Class中所有标记有@SubCommand的方法
     * @author RakkiPipi
     * @param clazz 需要获取的类
     * @return 当查找到一个或更多标记有@SubCommand的方法时会返回一个Map 否则返回null
     */
    private fun getSubCommandMethod(clazz: Class<*>): Map<String, Method>? {
        val methods = HashMap<String, Method>()
        for (method in clazz.declaredMethods) {
            if (method.isAnnotationPresent(SubCommand::class.java)) {
                val annotation = method.getAnnotation(SubCommand::class.java)
                if (annotation.name != "") {
                    methods[annotation.name] = method
                } else methods[method.name] = method
            }
        }
        return if (methods.isEmpty()) null else methods
    }

}