package sparkuniverse.amo.elemental.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.net.ClientboundMarkPacket;
import sparkuniverse.amo.elemental.net.PacketHandler;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;

import java.util.Iterator;
import java.util.List;

@Mixin(ThrownPotion.class)
public class ThrownPotionMixin {

    @Inject(at = @At("TAIL"), method = "applyWater", locals = LocalCapture.CAPTURE_FAILHARD)
    private void applyWaterMixin(CallbackInfo ci, AABB aabb) {
        List<LivingEntity> entities = (((ThrownPotion)(Object)this).level.getEntitiesOfClass(LivingEntity.class, aabb));
        entities.forEach(e -> {
            e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                if(!cap.hasMark(AttributeRegistry.WATER_DAMAGE.get().getDescriptionId())){
                    cap.addMark(AttributeRegistry.WATER_DAMAGE.get().getDescriptionId());
                    PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> PacketDistributor.TargetPoint.p(
                            e.getX(), e.getY(), e.getZ(), 128, e.level.dimension()
                    ).get()), new ClientboundMarkPacket(cap.serializeNBT(), e.getId()));
                }
            });
        });
    }
}
