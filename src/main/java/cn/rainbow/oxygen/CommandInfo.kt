package cn.rainbow.oxygen

@Retention(AnnotationRetention.RUNTIME)
annotation class CommandInfo(
    val name: String,
    val primaryNames: Array<String>
)
