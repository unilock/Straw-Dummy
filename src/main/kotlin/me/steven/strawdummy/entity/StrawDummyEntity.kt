package me.steven.strawdummy.entity

import io.netty.buffer.Unpooled
import me.steven.strawdummy.StrawDummy
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.MathHelper
import net.minecraft.world.TeleportTarget
import net.minecraft.world.World

class StrawDummyEntity(type: EntityType<StrawDummyEntity>, world: World) : LivingEntity(type, world) {

    private var inventory = DefaultedList.ofSize(6, ItemStack.EMPTY)
    var ownerUuid: String? = null

    override fun getMainArm(): Arm = Arm.RIGHT

    override fun equipStack(slot: EquipmentSlot, stack: ItemStack?) {
        inventory[slot.armorStandSlotId] = stack
    }

    override fun getEquippedStack(slot: EquipmentSlot): ItemStack {
        return inventory[slot.armorStandSlotId]
    }

    override fun getArmorItems(): MutableIterable<ItemStack> {
        return EquipmentSlot.values().filter { it.type == EquipmentSlot.Type.ARMOR }.map { inventory[it.armorStandSlotId] }.toMutableList()
    }

    override fun getTeleportTarget(destination: ServerWorld?): TeleportTarget? = null

    override fun isSpectator(): Boolean = false

    override fun isPushable(): Boolean = false

    override fun canBreatheInWater(): Boolean = true

    override fun pushAway(entity: Entity?) {}

    override fun takeKnockback(strength: Double, x: Double, z: Double) {
    }

    override fun heal(amount: Float) {
    }

    override fun setHealth(health: Float) {
        val damage = getHealth() - health
        if (damage > 0 && !world.isClient && !isDead) {
            val entity = StrawDummy.DAMAGE_NUMBER_ENTITY_TYPE.create(world) ?: return
            val side = horizontalFacing.rotateYClockwise()
            entity.setPos(this.x + side.offsetX, this.y + 2, this.z + side.offsetZ)
            entity.damage = damage
            val buf = PacketByteBuf(Unpooled.buffer())
            buf.writeInt(entity.id)
            buf.writeUuid(MathHelper.randomUuid(world.random))
            buf.writeDouble(entity.x)
            buf.writeDouble(entity.y)
            buf.writeDouble(entity.z)
            buf.writeByte(MathHelper.floor(pitch * 256.0f / 360.0f))
            buf.writeByte(MathHelper.floor(yaw * 256.0f / 360.0f))
            buf.writeFloat(entity.damage)
            world.players.forEach {
                ServerPlayNetworking.send(it as ServerPlayerEntity, StrawDummy.DAMAGE_NUMBER_PACKET, buf)
            }
        }
    }

    override fun applyDamage(source: DamageSource?, amount: Float) {
        if (source == damageSources.outOfWorld() || source == damageSources.inWall()) super.setHealth(0f)
        super.applyDamage(source, amount)
    }

    override fun interact(player: PlayerEntity?, hand: Hand?): ActionResult {
        val handStack = player?.getStackInHand(hand)
        val item = handStack?.item
        if (player?.isSneaking == true) {
            (0 until inventory.size).forEach {
                val stack = inventory[it]
                if (player.inventory.insertStack(stack)) {
                    inventory[it] = ItemStack.EMPTY
                }
            }
            if (player.inventory?.insertStack(ItemStack(StrawDummy.DUMMY_ITEM)) == true) remove(RemovalReason.DISCARDED)
            return ActionResult.SUCCESS
        }
        val slot = when (item) {
            is ArmorItem -> item.slotType.armorStandSlotId
            else -> 0
        }
        val previous = inventory[slot]
        player?.inventory?.removeOne(handStack)
        if (previous.isEmpty || player?.inventory?.insertStack(previous) == true) {
            inventory[slot] = handStack
            return ActionResult.SUCCESS
        }
        return super.interact(player, hand)
    }

    override fun writeNbt(tag: NbtCompound?): NbtCompound {
        val inv = NbtList()
        (0 until inventory.size).forEach { slot ->
            val stack = inventory[slot]
            val data = stack.writeNbt(NbtCompound())
            data.putInt("Slot", slot)
            inv.add(data)
        }
        tag?.put("Inventory", inv)
        if (ownerUuid != null)
            tag?.putString("OwnerUuid", ownerUuid)
        return super.writeNbt(tag)
    }

    override fun readNbt(tag: NbtCompound?) {
        if (tag?.contains("Inventory", 9) == true) {
            val inv = tag.get("Inventory") as NbtList
            inv.forEach { data ->
                data as NbtCompound
                inventory[data.getInt("Slot")] = ItemStack.fromNbt(data)
            }
            ownerUuid = tag.getString("OwnerUuid")
        }
        super.readNbt(tag)
    }
}