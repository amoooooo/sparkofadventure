package sparkuniverse.amo.sparkofadventure.damagetypes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import shadows.apotheosis.Apoth;

public class AttributeRegistry {
    public static final DeferredRegister<Attribute> DAMAGE_ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, "sparkofadventure");
    public static final DeferredRegister<Attribute> RESISTANCE_ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, "sparkofadventure");
    public static final DeferredRegister<Attribute> REACTION_ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, "sparkofadventure");

    public static final RegistryObject<Attribute> LIGHTNING_DAMAGE = DAMAGE_ATTRIBUTES.register("lightning_damage", () -> new RangedAttribute("sparkofadventure:lightning_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> POISON_DAMAGE = DAMAGE_ATTRIBUTES.register("poison_damage", () -> new RangedAttribute("sparkofadventure:poison_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> ACID_DAMAGE = DAMAGE_ATTRIBUTES.register("acid_damage", () -> new RangedAttribute("sparkofadventure:acid_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> NECROTIC_DAMAGE = DAMAGE_ATTRIBUTES.register("necrotic_damage", () -> new RangedAttribute("sparkofadventure:necrotic_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> RADIANT_DAMAGE = DAMAGE_ATTRIBUTES.register("radiant_damage", () -> new RangedAttribute("sparkofadventure:radiant_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> FORCE_DAMAGE = DAMAGE_ATTRIBUTES.register("force_damage", () -> new RangedAttribute("sparkofadventure:force_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> PSYCHIC_DAMAGE = DAMAGE_ATTRIBUTES.register("psychic_damage", () -> new RangedAttribute("sparkofadventure:psychic_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> THUNDER_DAMAGE = DAMAGE_ATTRIBUTES.register("thunder_damage", () -> new RangedAttribute("sparkofadventure:thunder_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> AIR_DAMAGE = DAMAGE_ATTRIBUTES.register("air_damage", () -> new RangedAttribute("sparkofadventure:air_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> EARTH_DAMAGE = DAMAGE_ATTRIBUTES.register("earth_damage", () -> new RangedAttribute("sparkofadventure:earth_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> WATER_DAMAGE = DAMAGE_ATTRIBUTES.register("water_damage", () -> new RangedAttribute("sparkofadventure:water_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> NATURE_DAMAGE = DAMAGE_ATTRIBUTES.register("nature_damage", () -> new RangedAttribute("sparkofadventure:nature_damage", 0.0D, 0.0D, 1024D).setSyncable(true));

    public static final RegistryObject<Attribute> FIRE_RESISTANCE = RESISTANCE_ATTRIBUTES.register("fire_resistance", () -> new TypedRangedAttribute("sparkofadventure:fire_resistance", 0.0D, 0.0D, 1024D, Apoth.Attributes.FIRE_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> COLD_RESISTANCE = RESISTANCE_ATTRIBUTES.register("cold_resistance", () -> new TypedRangedAttribute("sparkofadventure:cold_resistance", 0.0D, 0.0D, 1024D, Apoth.Attributes.COLD_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> LIGHTNING_RESISTANCE = RESISTANCE_ATTRIBUTES.register("lightning_resistance", () -> new TypedRangedAttribute("sparkofadventure:lightning_resistance", 0.0D, 0.0D, 1024D, LIGHTNING_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> POISON_RESISTANCE = RESISTANCE_ATTRIBUTES.register("poison_resistance", () -> new TypedRangedAttribute("sparkofadventure:poison_resistance", 0.0D, 0.0D, 1024D, POISON_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> ACID_RESISTANCE = RESISTANCE_ATTRIBUTES.register("acid_resistance", () -> new TypedRangedAttribute("sparkofadventure:acid_resistance", 0.0D, 0.0D, 1024D, ACID_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> NECROTIC_RESISTANCE = RESISTANCE_ATTRIBUTES.register("necrotic_resistance", () -> new TypedRangedAttribute("sparkofadventure:necrotic_resistance", 0.0D, 0.0D, 1024D, NECROTIC_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> RADIANT_RESISTANCE = RESISTANCE_ATTRIBUTES.register("radiant_resistance", () -> new TypedRangedAttribute("sparkofadventure:radiant_resistance", 0.0D, 0.0D, 1024D, RADIANT_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> FORCE_RESISTANCE = RESISTANCE_ATTRIBUTES.register("force_resistance", () -> new TypedRangedAttribute("sparkofadventure:force_resistance", 0.0D, 0.0D, 1024D, FORCE_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> PSYCHIC_RESISTANCE = RESISTANCE_ATTRIBUTES.register("psychic_resistance", () -> new TypedRangedAttribute("sparkofadventure:psychic_resistance", 0.0D, 0.0D, 1024D, PSYCHIC_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> THUNDER_RESISTANCE = RESISTANCE_ATTRIBUTES.register("thunder_resistance", () -> new TypedRangedAttribute("sparkofadventure:thunder_resistance", 0.0D, 0.0D, 1024D, THUNDER_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> AIR_RESISTANCE = RESISTANCE_ATTRIBUTES.register("air_resistance", () -> new TypedRangedAttribute("sparkofadventure:air_resistance", 0.0D, 0.0D, 1024D, AIR_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> EARTH_RESISTANCE = RESISTANCE_ATTRIBUTES.register("earth_resistance", () -> new TypedRangedAttribute("sparkofadventure:earth_resistance", 0.0D, 0.0D, 1024D, EARTH_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> WATER_RESISTANCE = RESISTANCE_ATTRIBUTES.register("water_resistance", () -> new TypedRangedAttribute("sparkofadventure:water_resistance", 0.0D, 0.0D, 1024D, WATER_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> NATURE_RESISTANCE = RESISTANCE_ATTRIBUTES.register("nature_resistance", () -> new TypedRangedAttribute("sparkofadventure:nature_resistance", 0.0D, 0.0D, 1024D, NATURE_DAMAGE).setSyncable(true));

    public static final RegistryObject<Attribute> FIRE_REACTION_UP = REACTION_ATTRIBUTES.register("fire_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:fire_reaction_up", 0.0D, 0.0D, 1024D, Apoth.Attributes.FIRE_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> COLD_REACTION_UP = REACTION_ATTRIBUTES.register("cold_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:cold_reaction_up", 0.0D, 0.0D, 1024D, Apoth.Attributes.COLD_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> LIGHTNING_REACTION_UP = REACTION_ATTRIBUTES.register("lightning_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:lightning_reaction_up", 0.0D, 0.0D, 1024D, LIGHTNING_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> POISON_REACTION_UP = REACTION_ATTRIBUTES.register("poison_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:poison_reaction_up", 0.0D, 0.0D, 1024D, POISON_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> ACID_REACTION_UP = REACTION_ATTRIBUTES.register("acid_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:acid_reaction_up", 0.0D, 0.0D, 1024D, ACID_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> NECROTIC_REACTION_UP = REACTION_ATTRIBUTES.register("necrotic_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:necrotic_reaction_up", 0.0D, 0.0D, 1024D, NECROTIC_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> RADIANT_REACTION_UP = REACTION_ATTRIBUTES.register("radiant_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:radiant_reaction_up", 0.0D, 0.0D, 1024D, RADIANT_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> FORCE_REACTION_UP = REACTION_ATTRIBUTES.register("force_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:force_reaction_up", 0.0D, 0.0D, 1024D, FORCE_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> PSYCHIC_REACTION_UP = REACTION_ATTRIBUTES.register("psychic_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:psychic_reaction_up", 0.0D, 0.0D, 1024D, PSYCHIC_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> THUNDER_REACTION_UP = REACTION_ATTRIBUTES.register("thunder_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:thunder_reaction_up", 0.0D, 0.0D, 1024D, THUNDER_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> AIR_REACTION_UP = REACTION_ATTRIBUTES.register("air_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:air_reaction_up", 0.0D, 0.0D, 1024D, AIR_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> EARTH_REACTION_UP = REACTION_ATTRIBUTES.register("earth_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:earth_reaction_up", 0.0D, 0.0D, 1024D, EARTH_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> WATER_REACTION_UP = REACTION_ATTRIBUTES.register("water_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:water_reaction_up", 0.0D, 0.0D, 1024D, WATER_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> NATURE_REACTION_UP = REACTION_ATTRIBUTES.register("nature_reaction_up", () -> new TypedRangedAttribute("sparkofadventure:nature_reaction_up", 0.0D, 0.0D, 1024D, NATURE_DAMAGE).setSyncable(true));
}
