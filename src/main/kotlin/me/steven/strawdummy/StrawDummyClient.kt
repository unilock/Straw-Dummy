package me.steven.strawdummy

import me.steven.strawdummy.entity.DamageNumberRenderer
import me.steven.strawdummy.entity.StrawDummyEntity
import me.steven.strawdummy.entity.StrawDummyEntityModel
import me.steven.strawdummy.entity.StrawDummyEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.entity.EntityType

object StrawDummyClient : ClientModInitializer {
    override fun onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(StrawDummy.DUMMY_ENTITY_TYPE) { dispatcher, _ -> StrawDummyEntityRenderer(dispatcher, STRAW_DUMMY_MODEL) }
        EntityRendererRegistry.INSTANCE.register(StrawDummy.DAMAGE_NUMBER_ENTITY_TYPE) { dispatcher, _ -> DamageNumberRenderer(dispatcher) }
    }

    val STRAW_DUMMY_MODEL = StrawDummyEntityModel()
}