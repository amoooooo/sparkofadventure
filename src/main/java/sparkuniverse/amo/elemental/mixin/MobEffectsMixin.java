package sparkuniverse.amo.elemental.mixin;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.reactions.effects.ExtendedMobEffect;

@Mixin(MobEffects.class)
public class MobEffectsMixin {

    @Inject(method = "register", at = @At("HEAD"), remap = false, cancellable = true)
    private static void onRegister(int pId, String pKey, MobEffect pEffect, CallbackInfoReturnable<MobEffect> cir) {
        if(pKey.equals("fire_resistance")){
            cir.setReturnValue(Registry.registerMapping(Registry.MOB_EFFECT, pId, pKey, new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 1498169000).addAttributeModifier(AttributeRegistry.fireAttr, "de7c4c83-cfcf-4dce-9adc-deee2f8407ee", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL)));
        }
    }
}
