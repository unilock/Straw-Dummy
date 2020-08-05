package me.steven.strawdummy.item

import me.steven.strawdummy.StrawDummy
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.SpawnReason
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult

class StrawDummyItem(settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        val world = context?.world
        if (world?.isClient == true) return ActionResult.SUCCESS
        val stack = context?.stack
        stack?.decrement(1)
        val entity = StrawDummy.DUMMY_ENTITY_TYPE.create(world as ServerWorld, null, null, context.player, context.blockPos, SpawnReason.MOB_SUMMONED, true, false)
        entity?.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, context.player?.pos)
        world.spawnEntity(entity)

        return super.useOnBlock(context)
    }
}