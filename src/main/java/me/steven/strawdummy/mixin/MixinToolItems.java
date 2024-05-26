package me.steven.strawdummy.mixin;

import me.steven.strawdummy.StrawDummy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {MiningToolItem.class, SwordItem.class, TridentItem.class})
public class MixinToolItems {
    @Inject(method = "postHit", at = @At("HEAD"), cancellable = true)
    private void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (target.getType() == StrawDummy.INSTANCE.getDUMMY_ENTITY_TYPE() && !StrawDummy.INSTANCE.getCONFIG().getDummyDamagesEquipment()) {
            cir.setReturnValue(true);
        }
    }
}
