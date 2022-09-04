package cn.rainbow.oxygen.module.setting

open class Setting(val name: String) {

    fun getBooleanValue(): BooleanValue? {
        return if (this is BooleanValue) {
            this
        } else null
    }

    fun getNumberValue(): NumberValue? {
        return if (this is NumberValue) {
            this
        } else null
    }

    fun getModeValue(): ModeValue? {
        return if (this is ModeValue) {
            this
        } else null
    }
}