package cn.rainbow.oxygen.command

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SubCommand(val name: String = "", vararg val value: String = [""])