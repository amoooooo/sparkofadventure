package sparkuniverse.amo.elemental.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.reactions.capability.ElementalArrowCapabilityProvider;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Shadow public abstract int getUseDuration(ItemStack pStack);

    @Shadow @Final public static int MAX_DRAW_DURATION;

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onReleaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci, Player player, boolean flag, ItemStack itemstack, int i, float f, boolean flag1, ArrowItem arrowitem, AbstractArrow abstractarrow) {
        float drawTime = pStack.getUseDuration() - pTimeLeft;
        if(drawTime > MAX_DRAW_DURATION) {
            abstractarrow.getCapability(ElementalArrowCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                player.getAttributes().getDirtyAttributes().forEach(attr -> {
                    if (AttributeRegistry.DAMAGE_ATTRIBUTES.getEntries().stream().anyMatch(entry -> entry.get().getDescriptionId().equals(attr.getAttribute().getDescriptionId()))) {
                        cap.addElement(attr.getAttribute().getDescriptionId());
                    }
                });
            });
        }

    }
}
