package me.steven.strawdummy.entity

import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Arm

class StrawDummyEntityModel : PlayerEntityModel<StrawDummyEntity>(0.0f, false) {
    override fun setAngles(livingEntity: StrawDummyEntity?, f: Float, g: Float, h: Float, i: Float, j: Float) {}

    override fun setArmAngle(arm: Arm?, matrices: MatrixStack?) {}
}