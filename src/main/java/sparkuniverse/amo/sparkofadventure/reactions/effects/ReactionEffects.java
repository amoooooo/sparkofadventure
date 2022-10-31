package sparkuniverse.amo.sparkofadventure.reactions.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sparkuniverse.amo.sparkofadventure.damagetypes.AttributeRegistry;

public class ReactionEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "sparkofadventure");

    public static final RegistryObject<MobEffect> OVERLOAD = EFFECTS.register("overload", () -> new OverloadMobEffect(MobEffectCategory.HARMFUL, 0xB08FC2).addAttributeModifier(AttributeRegistry.LIGHTNING_RESISTANCE.get(), "f3be1c96-5853-11ed-9b6a-0242ac120002", 0.6D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> FROZEN = EFFECTS.register("frozen", () -> new OverloadMobEffect(MobEffectCategory.HARMFUL, 0xCFFFFA).addAttributeModifier(Attributes.MOVEMENT_SPEED, "698d2af8-5876-11ed-9b6a-0242ac120002", 0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
}
