package me.steven.strawdummy.entity

import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.*
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.util.Identifier

class StrawDummyEntityRenderer(dispatcher: EntityRenderDispatcher, model: PlayerEntityModel<StrawDummyEntity>)
    : LivingEntityRenderer<StrawDummyEntity, PlayerEntityModel<StrawDummyEntity>>(dispatcher, model,  0.5f) {

    init {
        addFeature(ArmorFeatureRenderer(this, BipedEntityModel(0.5f), BipedEntityModel(1.0f)))
        addFeature(HeldItemFeatureRenderer(this))
        addFeature(StuckArrowsFeatureRenderer(this))
        addFeature(HeadFeatureRenderer(this))
        addFeature(ElytraFeatureRenderer(this))
        addFeature(TridentRiptideFeatureRenderer(this))
        addFeature(StuckStingersFeatureRenderer(this))
    }

    override fun getTexture(entity: StrawDummyEntity?): Identifier {
        return Identifier("textures/entity/steve.png")
    }
}