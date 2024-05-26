package me.steven.strawdummy.entity

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class DamageNumberRenderer(context: EntityRendererFactory.Context) : EntityRenderer<DamageNumberEntity>(context) {
    override fun getTexture(entity: DamageNumberEntity?): Identifier {
        throw RuntimeException("this shouldn't ever be called, what the fuck")
    }

    override fun render(entity: DamageNumberEntity, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
        matrices.push()
        matrices.multiply(this.dispatcher.rotation)
        val age = entity.ticks / 1000f
        matrices.scale(-0.05f + age, -0.05f + age, 0.05f - age)
        //if (0.05f - age <= 0) entity.remove(Entity.RemovalReason.DISCARDED)
        val textRenderer = MinecraftClient.getInstance().textRenderer
        val text = Text.literal(DamageNumberEntity.FORMAT.format(entity.damage))
        val h = -textRenderer.getWidth(text) / 2f
        textRenderer.draw(text, h, 0f, (0xeeee2222).toInt(), false, matrices.peek().positionMatrix, vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, light)
        matrices.pop()
    }
}