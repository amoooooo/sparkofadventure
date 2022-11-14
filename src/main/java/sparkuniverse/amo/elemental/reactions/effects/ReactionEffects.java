package sparkuniverse.amo.elemental.reactions.effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;

public class ReactionEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "elemental");

    public static final RegistryObject<MobEffect> ELECTROCHARGED = EFFECTS.register("electrocharged", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xB08FC2) {
        @Override
        public void applyEffectTick(LivingEntity entity, int amplifier) {
            entity.hurt(DamageSource.MAGIC, 0.25f * amplifier);
            entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach((entity1) -> {
                if (entity1 instanceof LivingEntity) {
                    entity1.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent((cap) -> {
                        if (cap.hasMark(AttributeRegistry.WATER_DAMAGE.get().getDescriptionId())) {
                            entity1.hurt(DamageSource.LIGHTNING_BOLT, 0.25f * amplifier);
                        }
                    });
                }
            });
        }
    }.addAttributeModifier(AttributeRegistry.LIGHTNING_RESISTANCE.get(), "f3be1c96-5853-11ed-9b6a-0242ac120002", 0.6D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> AGGRAVATE = EFFECTS.register("aggravate", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xB08FC2).addAttributeModifier(AttributeRegistry.LIGHTNING_RESISTANCE.get(), "3f40b744-5973-11ed-9b6a-0242ac120002", 0.85D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> SPREAD = EFFECTS.register("spread", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0x98b73d).addAttributeModifier(AttributeRegistry.NATURE_RESISTANCE.get(), "f11470b2-5bb5-44f0-b051-1ff571588f70", 0.75D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> QUICKEN = EFFECTS.register("quicken", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0x98b73d));
    public static final RegistryObject<MobEffect> FROZEN = EFFECTS.register("frozen", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xCFFFFA){
    }.addAttributeModifier(Attributes.MOVEMENT_SPEED, "698d2af8-5876-11ed-9b6a-0242ac120002", 0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> BURNING = EFFECTS.register("burning", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xFF0000) {
        @Override
        public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
            pLivingEntity.hurt(DamageSource.IN_FIRE, pAmplifier * 0.25f);
            super.applyEffectTick(pLivingEntity, pAmplifier);
        }
    });
    public static final RegistryObject<MobEffect> RUSTING = EFFECTS.register("rusting", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xfc4e03)
            .addAttributeModifier(Attributes.ARMOR, "4db4311e-4cbb-43ab-bd81-47f6c4e6186c", 0.6D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "a4bcdb9d-c9ff-479e-b07c-01c8d1942304", 0.6D, AttributeModifier.Operation.MULTIPLY_TOTAL));


    public static final RegistryObject<MobEffect> WATER_RESISTANCE = EFFECTS.register("water_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x0000FF).addAttributeModifier(AttributeRegistry.WATER_RESISTANCE.get(), "18cca568-9c22-4a3b-8745-05000debc774", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> NATURE_RESISTANCE = EFFECTS.register("nature_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x00FF00).addAttributeModifier(AttributeRegistry.NATURE_RESISTANCE.get(), "e58156b1-e31f-446d-a8f3-632388511d10", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> LIGHTNING_RESISTANCE = EFFECTS.register("lightning_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0xFFFF00).addAttributeModifier(AttributeRegistry.LIGHTNING_RESISTANCE.get(), "c6b7d7d0-73ee-4bff-9bfe-337aa0ad56c4", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> AIR_RESISTANCE = EFFECTS.register("air_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x00FFFF).addAttributeModifier(AttributeRegistry.AIR_RESISTANCE.get(), "4088afd2-5085-4b30-aae6-74f6ce8bef9d", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> EARTH_RESISTANCE = EFFECTS.register("earth_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0xFF00FF).addAttributeModifier(AttributeRegistry.EARTH_RESISTANCE.get(), "ba8c0fbb-a3aa-4933-9a6b-a03e94ead1a5", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> NECROTIC_RESISTANCE = EFFECTS.register("necrotic_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x000000).addAttributeModifier(AttributeRegistry.NECROTIC_RESISTANCE.get(), "4305ac42-4958-4358-894d-729d74e8651d", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> RADIANT_RESISTANCE = EFFECTS.register("radiant_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF).addAttributeModifier(AttributeRegistry.RADIANT_RESISTANCE.get(), "c0a4dacb-c26a-43bb-9763-54f897bf7164", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> ACID_RESISTANCE = EFFECTS.register("acid_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x00FF00).addAttributeModifier(AttributeRegistry.ACID_RESISTANCE.get(), "21a9f187-0fb1-4e9e-9f1a-9e1b58d9ded2", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> POISON_RESISTANCE = EFFECTS.register("poison_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x00FF00).addAttributeModifier(AttributeRegistry.POISON_RESISTANCE.get(), "aaab1214-896a-4cee-ace2-655a7812036e", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> COLD_RESISTANCE = EFFECTS.register("cold_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x00FF00).addAttributeModifier(AttributeRegistry.COLD_RESISTANCE.get(), "017b621c-8cbd-45be-bb6e-2c0c6df34721", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> FORCE_RESISTANCE = EFFECTS.register("force_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x00FF00).addAttributeModifier(AttributeRegistry.FORCE_RESISTANCE.get(), "d46d3503-a412-40d2-9a92-e54e7fb9ba88", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> PSYCHIC_RESISTANCE = EFFECTS.register("psychic_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x00FF00).addAttributeModifier(AttributeRegistry.PSYCHIC_RESISTANCE.get(), "5078afd6-872b-43eb-b396-a9ecea4c53f2", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> THUNDER_RESISTANCE = EFFECTS.register("thunder_resistance", () -> new ExtendedMobEffect(MobEffectCategory.BENEFICIAL, 0x00FF00).addAttributeModifier(AttributeRegistry.THUNDER_RESISTANCE.get(), "aff01342-6c12-4b01-a82e-b1bac752a4d7", 1.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> DISSOLVE = EFFECTS.register("dissolve", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0xADD013).addAttributeModifier(Attributes.MAX_HEALTH, "dbf46622-3a2c-4ec0-9a33-d96070e29e51", 0.8f, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> DECAY = EFFECTS.register("decay", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0x131313));
    public static final RegistryObject<MobEffect> FRAZZLE = EFFECTS.register("frazzle", () -> new ExtendedMobEffect(MobEffectCategory.HARMFUL, 0x131313) {
        @Override
        public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
            super.applyEffectTick(pLivingEntity, pAmplifier);
            if (pLivingEntity.level.random.nextFloat() > 0.5) {
                double damage = pLivingEntity.getAttribute(AttributeRegistry.PSYCHIC_RESISTANCE.get()).getValue() * (0.5f * pAmplifier);
                pLivingEntity.hurt(DamageSource.MAGIC, (float) damage);
            }

        }
    });
}
