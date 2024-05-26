package me.steven.strawdummy

import com.google.gson.GsonBuilder
import io.netty.buffer.Unpooled
import me.steven.strawdummy.entity.DamageNumberEntity
import me.steven.strawdummy.entity.StrawDummyEntity
import me.steven.strawdummy.item.StrawDummyItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.server.network.ServerPlayerEntity
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
        Registry.register(Registries.ENTITY_TYPE, identifier("strawdummy"), DUMMY_ENTITY_TYPE)
        Registry.register(Registries.ITEM, identifier("strawdummy"), DUMMY_ITEM)
        Registry.register(Registries.ENTITY_TYPE, identifier("damage_number_entity"), DAMAGE_NUMBER_ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(DUMMY_ENTITY_TYPE, LivingEntity.createLivingAttributes())

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE)
            .register(ModifyEntries { content ->
                content.add(DUMMY_ITEM)
            })

        ServerPlayConnectionEvents.JOIN
            .register(ServerPlayConnectionEvents.Join { handler, _, _ ->
                sendConfigPacket(handler.player)
            })
    }

    fun sendConfigPacket(playerEntity: ServerPlayerEntity) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeInt(CONFIG.dummyLimitPerUser)
        ServerPlayNetworking.send(playerEntity, CONFIG_SYNC_PACKET, buf)
    }

    const val MOD_ID = "strawdummy"

    val CONFIG_SYNC_PACKET = identifier("config_sync")
    val DAMAGE_NUMBER_PACKET = identifier("damage_number_packet")

    val CONFIG = Config()

    val DUMMY_ENTITY_TYPE = FabricEntityTypeBuilder.create<StrawDummyEntity>(SpawnGroup.MISC) { type, world -> StrawDummyEntity(type, world) }
        .dimensions(EntityDimensions(0.6f, 1.8f, false))
        .build()
    val DAMAGE_NUMBER_ENTITY_TYPE = FabricEntityTypeBuilder.create<DamageNumberEntity>(SpawnGroup.MISC) { type, world -> DamageNumberEntity(type, world) }
        .dimensions(EntityDimensions(0.6f, 0.6f, false))
        .disableSaving()
        .disableSummon()
        .build()

    val DUMMY_ITEM = StrawDummyItem(Item.Settings())
}