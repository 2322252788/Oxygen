package cn.rainbow.oxygen.module.modules.render

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.Render3DEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.utils.render.RenderUtil
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11
import java.awt.Color

class ChestESP : Module("ChestESP", Category.Render) {

    val modeValue = ModeValue("Mode", "2D")
    val Chest: BooleanValue = BooleanValue( "Chest", true)
    val EnderChest: BooleanValue = BooleanValue( "EnderChest", true)

    val chestESPColours = Color(-14848033).brighter()

    init {
        modeValue.addValue("2D")
        modeValue.addValue("Box")
    }

    @EventTarget(events = [Render3DEvent::class])
    private fun onEvent(e: Event) {
        if (e is Render3DEvent) {
            for (tileEntity in mc.theWorld.loadedTileEntityList) {
                if (tileEntity is TileEntityChest) {
                    if (modeValue.isCurrentMode("2D")) {
                        drawESPOnStorage(tileEntity, tileEntity.pos.x.toDouble(), tileEntity.pos.y.toDouble(), tileEntity.pos.z.toDouble())
                    }
                }
            }
        }
    }

    private fun drawESPOnStorage(chest: TileEntityChest, x: Double, y: Double, z: Double) {
        assert(!chest.isLocked)
        //20 years of java experience service really helped me, I'm glad we bonded :)
        var vec = Vec3(0.0, 0.0, 0.0)
        var vec2 = Vec3(0.0, 0.0, 0.0)
        if (chest.adjacentChestZNeg != null) {
            vec = Vec3(x + 0.0625, y, z - 0.9375)
            vec2 = Vec3(x + 0.9375, y + 0.875, z + 0.9375)
        } else if (chest.adjacentChestXNeg != null) {
            vec = Vec3(x + 0.9375, y, z + 0.0625)
            vec2 = Vec3(x - 0.9375, y + 0.875, z + 0.9375)
        } else if (chest.adjacentChestXPos == null && chest.adjacentChestZPos == null) {
            vec = Vec3(x + 0.0625, y, z + 0.0625)
            vec2 = Vec3(x + 0.9375, y + 0.875, z + 0.9375)
        } else {
            return
        }
        GL11.glPushMatrix()
        RenderUtil.pre3D()
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2)
        if (chest.chestType == 1) {
            GL11.glColor4d(0.7, 0.1, 0.1, 0.5)
        } else {
            RenderUtil.color(RenderUtil.reAlpha(Color(-14848033).brighter().rgb, 0.35f))
        }
        RenderUtil.drawBoundingBox(AxisAlignedBB(vec.xCoord - mc.renderManager.renderPosX,
            vec.yCoord - mc.renderManager.renderPosY,
            vec.zCoord - mc.renderManager.renderPosZ,
            vec2.xCoord - mc.renderManager.renderPosX,
            vec2.yCoord - mc.renderManager.renderPosY,
            vec2.zCoord - mc.renderManager.renderPosZ))
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f)
        RenderUtil.post3D()
        GL11.glPopMatrix()
    }
}