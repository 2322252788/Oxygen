package cn.rainbow.oxygen.module.setting

class BooleanValue(name: String, var currentValue: Boolean) : Setting(name)

class NumberValue(name: String, var currentValue: Double, val minValue: Double, val maxValue: Double, val step: Double) : Setting(name)

class ModeValue : Setting {
    private val options = ArrayList<String>()
    val defaultValue: String
    var currentValue: String

    /**
     * @param name         SettingName
     * @param defaultValue 默认
     * @author Pipi
     * @implNote 该构造方法不会事先添加ValueList 必须使用addValue()方法添加Value
     */
    constructor(name: String, defaultValue: String) : super(name) {
        this.defaultValue = defaultValue
        currentValue = defaultValue
    }

    /**
     * @param name         SettingName
     * @param defaultValue 默认选项
     * @param options      ValueList
     * @author Pipi
     * @implNote 该构造方法事先添加了ValueList可以不用addValue()方法添加Value
     */
    constructor(name: String, defaultValue: String, options: Array<String>) : super(name) {
        this.defaultValue = defaultValue
        currentValue = defaultValue
        this.options.addAll(options)
    }

    fun getOptions(): ArrayList<String> {
        val cache = ArrayList<String>()
        cache.addAll(options)
        return cache
    }

    fun addValue(value: String) {
        options.add(value)
    }

    fun addAllValue(value: ArrayList<String>) {
        options.addAll(value)
    }

    fun getCurrentModeIndex(): Int {
        var index = 0
        for (s in options) {
            if (s.equals(currentValue, ignoreCase = true)) {
                return index
            }
            index++
        }
        return index
    }

    fun isCurrentMode(name: String): Boolean {
        return currentValue.equals(name, ignoreCase = true)
    }
}