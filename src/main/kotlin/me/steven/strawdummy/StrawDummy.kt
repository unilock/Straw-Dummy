package me.steven.strawdummy

import me.steven.strawdummy.entity.DamageNumberEntity
import me.steven.strawdummy.entity.StrawDummyEntity
import me.steven.strawdummy.item.StrawDummyItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

object StrawDummy : ModInitializer {
    override fun onInitialize() {
        Registry.register(Registry.ENTITY_TYPE, identifier("strawdummy"), DUMMY_ENTITY_TYPE)
        Registry.register(Registry.ITEM, identifier("strawdummy"), DUMMY_ITEM)
        Registry.register(Registry.ENTITY_TYPE, identifier("damage_number_entity"), DAMAGE_NUMBER_ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(DUMMY_ENTITY_TYPE, LivingEntity.createLivingAttributes())
    }

    val MOD_ID = "strawdummy"

    val DUMMY_ENTITY_TYPE = FabricEntityTypeBuilder.create<StrawDummyEntity>(SpawnGroup.MISC) { type, world -> StrawDummyEntity(type, world) }
        .dimensions(EntityDimensions(0.6f, 1.8f, false))
        .build()
    val DAMAGE_NUMBER_ENTITY_TYPE = FabricEntityTypeBuilder.create<DamageNumberEntity>(SpawnGroup.MISC) { type, world -> DamageNumberEntity(type, world) }
        .dimensions(EntityDimensions(0.6f, 0.6f, false))
        .disableSaving()
        .disableSummon()
        .build()

    val DUMMY_ITEM = StrawDummyItem(Item.Settings().group(ItemGroup.MISC))
}