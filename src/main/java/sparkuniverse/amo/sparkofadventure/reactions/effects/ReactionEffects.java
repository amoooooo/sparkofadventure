package sparkuniverse.amo.sparkofadventure.reactions.effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sparkuniverse.amo.sparkofadventure.damagetypes.AttributeRegistry;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityProvider;

public class ReactionEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "sparkofadventure");

    public static final RegistryObject<MobEffect> ELECTROCHARGED = EFFECTS.register("electrocharged", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xB08FC2){
        @Override
        public void applyEffectTick(LivingEntity entity, int amplifier) {
            entity.hurt(DamageSource.MAGIC, 0.25f * amplifier);
            entity.invulnerableTime = 0;
            entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach((entity1) -> {
                if(entity1 instanceof LivingEntity){
                    entity1.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent((cap) -> {
                        if(cap.hasMark(AttributeRegistry.WATER_DAMAGE.get().getDescriptionId())){
                            entity1.hurt(DamageSource.LIGHTNING_BOLT, 0.25f * amplifier);
                            entity1.invulnerableTime = 0;
                        }
                    });
                }
            });
        }
    }.addAttributeModifier(AttributeRegistry.LIGHTNING_RESISTANCE.get(), "f3be1c96-5853-11ed-9b6a-0242ac120002", 0.6D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> AGGRAVATE = EFFECTS.register("aggravate", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xB08FC2).addAttributeModifier(AttributeRegistry.LIGHTNING_RESISTANCE.get(), "3f40b744-5973-11ed-9b6a-0242ac120002", 0.85D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> SPREAD = EFFECTS.register("spread", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0x98b73d).addAttributeModifier(AttributeRegistry.NATURE_RESISTANCE.get(), "f11470b2-5bb5-44f0-b051-1ff571588f70", 0.75D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> QUICKEN = EFFECTS.register("quicken", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0x98b73d));
    public static final RegistryObject<MobEffect> FROZEN = EFFECTS.register("frozen", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xCFFFFA).addAttributeModifier(Attributes.MOVEMENT_SPEED, "698d2af8-5876-11ed-9b6a-0242ac120002", 0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> BURNING = EFFECTS.register("burning", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xFF0000){
        @Override
        public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
            pLivingEntity.invulnerableTime = 0;
            pLivingEntity.hurt(DamageSource.IN_FIRE, pAmplifier * 0.25f);
            pLivingEntity.invulnerableTime = 0;
            super.applyEffectTick(pLivingEntity, pAmplifier);
        }
    });
}
