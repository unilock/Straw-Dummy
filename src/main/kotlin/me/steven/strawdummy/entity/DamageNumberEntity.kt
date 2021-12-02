package me.steven.strawdummy.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.world.World
import java.text.DecimalFormat

class DamageNumberEntity(type: EntityType<DamageNumberEntity>, world: World) : Entity(type, world) {

    var ticks = 0
    var damage: Float
        get() = this.dataTracker[DAMAGE_AMOUNT]
        set(value) {
            this.dataTracker.set(DAMAGE_AMOUNT, value)
        }

    override fun createSpawnPacket(): Packet<*> = EntitySpawnS2CPacket(this)

    override fun initDataTracker() {
        this.dataTracker.startTracking(DAMAGE_AMOUNT, 0f)
    }

    override fun tick() {
        ticks++
        if (ticks >= 50) {
            remove(RemovalReason.DISCARDED)
            return
        }
        super.tick()
        val vec3d = velocity
        val d = this.x + vec3d.x
        val e = this.y + vec3d.y
        val f = this.z + vec3d.z

        velocity = vec3d.multiply(0.01)
        val vec3d2 = velocity
        val age = ticks / 2000f
        this.setVelocity(vec3d2.x + (0.01 - age), vec3d2.y - 0.03, vec3d2.z + (0.01 - age))
        updatePosition(d, e, f)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    companion object {
        val DAMAGE_AMOUNT = DataTracker.registerData(DamageNumberEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val FORMAT = DecimalFormat("##.##")
    }
}