package sparkuniverse.amo.elemental.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;

import java.util.Iterator;
import java.util.List;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onTick(CallbackInfo ci, List list1, Iterator var2, Entity entity) {
        List<LivingEntity> entities = ((LightningBolt)(Object)this).level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(2));
        entities.forEach(e -> {
            e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                if(!cap.hasMark(AttributeRegistry.LIGHTNING_DAMAGE.get().getDescriptionId())){
                    cap.addMark(AttributeRegistry.LIGHTNING_DAMAGE.get().getDescriptionId());
                }
            });
        });
    }
}
