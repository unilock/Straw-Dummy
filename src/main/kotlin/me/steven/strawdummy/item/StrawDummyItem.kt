package me.steven.strawdummy.item

import me.steven.strawdummy.Config
import me.steven.strawdummy.StrawDummy
import me.steven.strawdummy.entity.StrawDummyEntity
import net.minecraft.client.item.TooltipContext
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.SpawnReason
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class StrawDummyItem(settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        val world = context?.world
        val player = context?.player
        if (world?.isClient == true) return ActionResult.SUCCESS
        val dummies = (world as ServerWorld).getEntitiesByType(StrawDummy.DUMMY_ENTITY_TYPE) { (it as StrawDummyEntity).ownerUuid == player?.uuidAsString }.size
        if (StrawDummy.CONFIG.dummyLimitPerUser in 1..dummies) {
            val key = if (StrawDummy.CONFIG.dummyLimitPerUser == 1) "item.strawdummy.strawdummy.tooltip1" else "item.strawdummy.strawdummy.tooltip2"
            player?.sendMessage(Text.translatable(key, StrawDummy.CONFIG.dummyLimitPerUser).formatted(Formatting.RED, Formatting.BOLD), true)
            return ActionResult.PASS
        }
        val stack = context.stack
        stack?.decrement(1)
        val pos = when (context.side) {
            Direction.UP -> context.blockPos
            Direction.DOWN -> context.blockPos.up()
            else -> context.blockPos.offset(context.side).down()
        }
        val entity = StrawDummy.DUMMY_ENTITY_TYPE.create(world, null, null, pos, SpawnReason.MOB_SUMMONED, true, false)
        if (world.getBlockCollisions(entity, Box(pos.up()).stretch(0.0, 1.0, 0.0)).any { !it.isEmpty }) {
            player?.sendMessage(Text.translatable("item.strawdummy.strawdummy.too_tight").formatted(Formatting.RED, Formatting.BOLD), true)
            return ActionResult.PASS
        }
        entity?.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, context.player?.pos)
        entity?.ownerUuid = player?.uuidAsString
        world.spawnEntity(entity)
        return super.useOnBlock(context)
    }

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        if (StrawDummy.CONFIG.dummyLimitPerUser > 0) {
            val key = if (StrawDummy.CONFIG.dummyLimitPerUser == 1) "item.strawdummy.strawdummy.tooltip1" else "item.strawdummy.strawdummy.tooltip2"
            tooltip?.add(Text.translatable(key, StrawDummy.CONFIG.dummyLimitPerUser).formatted(Formatting.BLUE, Formatting.ITALIC))
        }
    }
}