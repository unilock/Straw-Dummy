package me.steven.strawdummy.entity

import net.minecraft.client.model.Dilation
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.entity.model.PlayerEntityModel

class StrawDummyEntityModel(root: ModelPart) : PlayerEntityModel<StrawDummyEntity>(getTexturedModelData(Dilation.NONE, false).root.createPart(64, 64), false) {
    override fun setAngles(livingEntity: StrawDummyEntity?, f: Float, g: Float, h: Float, i: Float, j: Float) {}
}