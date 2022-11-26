package sparkuniverse.amo.elemental.reactions.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class ExtendedMobEffect extends net.tslat.effectslib.api.ExtendedMobEffect {
    public ExtendedMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public void tick(LivingEntity entity, int amplifier){
        super.tick(entity, null, amplifier);
    }

    @Override
    public MobEffectInstance onReapplication(MobEffectInstance existingEffectInstance, MobEffectInstance newEffectInstance, LivingEntity entity) {
        return existingEffectInstance;
    }
}
