package cn.rainbow.oxygen.module.modules.render

import java.util.Locale.Category

class TabGui(private val topX: Float, private val topY: Float) {

    fun render() {
        //画边框
        //RenderUtil.drawRect(topX.toDouble(), topY.toDouble(), )

        //Render Category

    }

    fun categoryRender() {
        var x = topY + 1
        var y = topY + 1
        for (c in Category.values()) {

        }
    }

}

class Sub(val x: Float, val y: Float) {

}