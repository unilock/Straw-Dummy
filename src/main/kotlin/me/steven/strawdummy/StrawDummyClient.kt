package me.steven.strawdummy

import me.steven.strawdummy.StrawDummy.DAMAGE_NUMBER_ENTITY_TYPE
import me.steven.strawdummy.entity.DamageNumberEntity
import me.steven.strawdummy.entity.DamageNumberRenderer
import me.steven.strawdummy.entity.StrawDummyEntityModel
import me.steven.strawdummy.entity.StrawDummyEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.world.ClientWorld

object StrawDummyClient : ClientModInitializer {
    override fun onInitializeClient() {
        EntityRendererRegistry.register(StrawDummy.DUMMY_ENTITY_TYPE) { ctx -> StrawDummyEntityRenderer(
            ctx,
            StrawDummyEntityModel(ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR))
        ) }
        EntityRendererRegistry.register(StrawDummy.DAMAGE_NUMBER_ENTITY_TYPE) { ctx -> DamageNumberRenderer(
            ctx
        ) }
        ClientPlayNetworking.registerGlobalReceiver(StrawDummy.CONFIG_SYNC_PACKET) { client, handler, buf, responseSender ->
            StrawDummy.CONFIG.dummyDamagesEquipment = buf.readBoolean()
            StrawDummy.CONFIG.dummyLimitPerUser = buf.readInt()
        }
        ClientPlayNetworking.registerGlobalReceiver(StrawDummy.DAMAGE_NUMBER_PACKET) { client, handler, buf, responseSender ->
            val e = DamageNumberEntity(DAMAGE_NUMBER_ENTITY_TYPE, client.player!!.world)

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
            client.execute { (client.player!!.world as ClientWorld).addEntity(-1, e) }
        }
    }
}