package cn.rainbow.oxygen.command.commands

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.command.SimpleCommand
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.module.setting.NumberValue
import java.util.*
import java.util.regex.Pattern

class CSetting : SimpleCommand("Setting", arrayOf("setting", "set")) {

    override fun run(args: Array<String>): Boolean {
        if (args.size == 3) {
            val module = Oxygen.INSTANCE.moduleManager.getModule(args[0])
            if (module == null) {
                logger.error("该Module不存在!")
                return false
            }
            val setting = Oxygen.INSTANCE.settingManager.getSetting(module, args[1])
            if (setting == null) {
                logger.error("该Setting不存在!")
                return false
            }
            when (setting) {
                is ModeValue -> {
                    setModeValue(module, setting, args[2])
                }
                is NumberValue -> {
                    setNumberValue(module, setting, args[2])
                }
                is BooleanValue -> {
                    setBooleanValue(module, setting, args[2])
                }
            }
            return true
        } else {
            logger.error("指令长度有误！")
        }
        return false
    }

    private fun setModeValue(module: Module, setting: ModeValue, currentValue: String) {
        var set = false
        for (sub in setting.getOptions()) {
            if (sub.equals(currentValue, ignoreCase = true)) {
                setting.currentValue = sub
                logger.info("[${module.name}] 的 ${setting.name} 已设置为 $sub")
                set = true
                break
            }
        }
        if (!set) {
            logger.error("未找到该Mode, 支持的范围列表如下: ${Arrays.toString(setting.getOptions().toArray())}")
        }
    }

    private fun setNumberValue(module: Module, setting: NumberValue, currentValue: String) {
        if (isNumeric(currentValue)) {
            setting.currentValue = currentValue.toDouble()
            logger.info("[${module.name}] 的 ${setting.name} 已设置为 $currentValue")
        } else {
            logger.error("你输入的不是数字!")
        }
    }

    private fun setBooleanValue(module: Module, setting: BooleanValue, currentValue: String) {
        setting.currentValue = currentValue.equals("true", ignoreCase = true)
        logger.info("[${module.name}] 的 ${setting.name} 已设置为 ${setting.currentValue}")
    }

    private fun isNumeric(str: String): Boolean {
        val pattern: Pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }
}