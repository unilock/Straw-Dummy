package me.steven.strawdummy

import me.steven.strawdummy.StrawDummy.DAMAGE_NUMBER_ENTITY_TYPE
import me.steven.strawdummy.entity.DamageNumberEntity
import me.steven.strawdummy.entity.DamageNumberRenderer
import me.steven.strawdummy.entity.StrawDummyEntityModel
import me.steven.strawdummy.entity.StrawDummyEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.world.ClientWorld

object StrawDummyClient : ClientModInitializer {
    override fun onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(StrawDummy.DUMMY_ENTITY_TYPE) { ctx -> StrawDummyEntityRenderer(
            ctx,
            StrawDummyEntityModel(ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR))
        ) }
        EntityRendererRegistry.INSTANCE.register(StrawDummy.DAMAGE_NUMBER_ENTITY_TYPE) { ctx -> DamageNumberRenderer(
            ctx
        ) }
        ClientSidePacketRegistry.INSTANCE.register(StrawDummy.CONFIG_SYNC_PACKET) { _, buf ->
            StrawDummy.CONFIG.dummyLimitPerUser = buf.readInt()
        }
        ClientSidePacketRegistry.INSTANCE.register(StrawDummy.DAMAGE_NUMBER_PACKET) { ctx, buf ->
            val e = DamageNumberEntity(DAMAGE_NUMBER_ENTITY_TYPE, ctx.player.world)

            val id = buf.readInt()
            e.uuid = buf.readUuid()
            val x = buf.readDouble()
            val y = buf.readDouble()
            val z = buf.readDouble()
            val pitch = buf.readByte()
            val yaw = buf.readByte()
            e.damage = buf.readFloat()
            e.setPos(x, y, z)
            e.updateTrackedPosition(x, y, z)
            e.pitch = (pitch * 360f)  / 256.0f
            e.yaw = (yaw * 360f) / 256.0f
            e.setEntityId(id)
            ctx.taskQueue.execute { (ctx.player.world as ClientWorld).addEntity(id, e) }
        }
    }
}