package me.steven.strawdummy.mixin;

import me.steven.strawdummy.StrawDummy;
import me.steven.strawdummy.entity.DamageNumberEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(at = @At("TAIL"), method = "onEntitySpawn")
    public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo cbi) {
        ClientPlayNetworkHandler cpnh = (ClientPlayNetworkHandler) (Object) this;

        if (packet.getEntityTypeId() == StrawDummy.INSTANCE.getDAMAGE_NUMBER_ENTITY_TYPE()) {
            DamageNumberEntity e = new DamageNumberEntity(StrawDummy.INSTANCE.getDAMAGE_NUMBER_ENTITY_TYPE(), cpnh.getWorld());

            int i = packet.getId();
            e.setPos(packet.getX(), packet.getY(), packet.getZ());
            e.updateTrackedPosition(packet.getX(), packet.getY(), packet.getZ());
            e.pitch = (float) (packet.getPitch() * 360) / 256.0F;
            e.yaw = (float) (packet.getYaw() * 360) / 256.0F;
            e.setEntityId(i);
            e.setUuid(packet.getUuid());
            cpnh.getWorld().addEntity(i, e);
        }
    }
}
