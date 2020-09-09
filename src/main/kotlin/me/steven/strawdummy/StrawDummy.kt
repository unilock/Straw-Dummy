package me.steven.strawdummy

import com.google.gson.GsonBuilder
import me.steven.strawdummy.entity.DamageNumberEntity
import me.steven.strawdummy.entity.StrawDummyEntity
import me.steven.strawdummy.item.StrawDummyItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry
import java.io.File

object StrawDummy : ModInitializer {
    override fun onInitialize() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val configFile = File(FabricLoader.getInstance().configDir.toFile(), "strawdummy.json")
        if (!configFile.exists()) {
            configFile.createNewFile()
            configFile.writeText(gson.toJson(CONFIG))
        } else {
            val config = gson.fromJson(configFile.readLines().joinToString(""), Config::class.java)
            CONFIG.dummyLimitPerUser = config.dummyLimitPerUser
        }
        Registry.register(Registry.ENTITY_TYPE, identifier("strawdummy"), DUMMY_ENTITY_TYPE)
        Registry.register(Registry.ITEM, identifier("strawdummy"), DUMMY_ITEM)
        Registry.register(Registry.ENTITY_TYPE, identifier("damage_number_entity"), DAMAGE_NUMBER_ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(DUMMY_ENTITY_TYPE, LivingEntity.createLivingAttributes())
    }

    val CONFIG = Config()

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