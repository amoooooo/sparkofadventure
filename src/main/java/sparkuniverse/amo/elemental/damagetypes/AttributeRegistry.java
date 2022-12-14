package sparkuniverse.amo.elemental.damagetypes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class AttributeRegistry {
    public static final DeferredRegister<Attribute> DAMAGE_ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, "elemental");
    public static final DeferredRegister<Attribute> RESISTANCE_ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, "elemental");
    public static final DeferredRegister<Attribute> REACTION_ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, "elemental");

    public static final RegistryObject<Attribute> FIRE_DAMAGE = DAMAGE_ATTRIBUTES.register("fire_damage", () -> new RangedAttribute("elemental:fire_damage", 0, 0, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> COLD_DAMAGE = DAMAGE_ATTRIBUTES.register("cold_damage", () -> new RangedAttribute("elemental:cold_damage", 0, 0, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> LIGHTNING_DAMAGE = DAMAGE_ATTRIBUTES.register("lightning_damage", () -> new RangedAttribute("elemental:lightning_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> POISON_DAMAGE = DAMAGE_ATTRIBUTES.register("poison_damage", () -> new RangedAttribute("elemental:poison_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> ACID_DAMAGE = DAMAGE_ATTRIBUTES.register("acid_damage", () -> new RangedAttribute("elemental:acid_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> NECROTIC_DAMAGE = DAMAGE_ATTRIBUTES.register("necrotic_damage", () -> new RangedAttribute("elemental:necrotic_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> RADIANT_DAMAGE = DAMAGE_ATTRIBUTES.register("radiant_damage", () -> new RangedAttribute("elemental:radiant_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> FORCE_DAMAGE = DAMAGE_ATTRIBUTES.register("force_damage", () -> new RangedAttribute("elemental:force_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> PSYCHIC_DAMAGE = DAMAGE_ATTRIBUTES.register("psychic_damage", () -> new RangedAttribute("elemental:psychic_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> THUNDER_DAMAGE = DAMAGE_ATTRIBUTES.register("thunder_damage", () -> new RangedAttribute("elemental:thunder_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> AIR_DAMAGE = DAMAGE_ATTRIBUTES.register("air_damage", () -> new RangedAttribute("elemental:air_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> EARTH_DAMAGE = DAMAGE_ATTRIBUTES.register("earth_damage", () -> new RangedAttribute("elemental:earth_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> WATER_DAMAGE = DAMAGE_ATTRIBUTES.register("water_damage", () -> new RangedAttribute("elemental:water_damage", 0.0D, 0.0D, 1024D).setSyncable(true));
    public static final RegistryObject<Attribute> NATURE_DAMAGE = DAMAGE_ATTRIBUTES.register("nature_damage", () -> new RangedAttribute("elemental:nature_damage", 0.0D, 0.0D, 1024D).setSyncable(true));

    public static final Attribute fireAttr = new TypedRangedAttribute("elemental:fire_resistance", 100.0D, 0.0D, 1024D, AttributeRegistry.FIRE_DAMAGE).setSyncable(true);
    public static final RegistryObject<Attribute> FIRE_RESISTANCE = RESISTANCE_ATTRIBUTES.register("fire_resistance", () -> fireAttr);
    public static final RegistryObject<Attribute> COLD_RESISTANCE = RESISTANCE_ATTRIBUTES.register("cold_resistance", () -> new TypedRangedAttribute("elemental:cold_resistance", 100.0D, 0.0D, 1024D, AttributeRegistry.COLD_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> LIGHTNING_RESISTANCE = RESISTANCE_ATTRIBUTES.register("lightning_resistance", () -> new TypedRangedAttribute("elemental:lightning_resistance", 100.0D, 0.0D, 1024D, LIGHTNING_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> POISON_RESISTANCE = RESISTANCE_ATTRIBUTES.register("poison_resistance", () -> new TypedRangedAttribute("elemental:poison_resistance", 100.0D, 0.0D, 1024D, POISON_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> ACID_RESISTANCE = RESISTANCE_ATTRIBUTES.register("acid_resistance", () -> new TypedRangedAttribute("elemental:acid_resistance", 100.0D, 0.0D, 1024D, ACID_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> NECROTIC_RESISTANCE = RESISTANCE_ATTRIBUTES.register("necrotic_resistance", () -> new TypedRangedAttribute("elemental:necrotic_resistance", 100.0D, 0.0D, 1024D, NECROTIC_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> RADIANT_RESISTANCE = RESISTANCE_ATTRIBUTES.register("radiant_resistance", () -> new TypedRangedAttribute("elemental:radiant_resistance", 100.0D, 0.0D, 1024D, RADIANT_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> FORCE_RESISTANCE = RESISTANCE_ATTRIBUTES.register("force_resistance", () -> new TypedRangedAttribute("elemental:force_resistance", 100.0D, 0.0D, 1024D, FORCE_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> PSYCHIC_RESISTANCE = RESISTANCE_ATTRIBUTES.register("psychic_resistance", () -> new TypedRangedAttribute("elemental:psychic_resistance", 100.0D, 0.0D, 1024D, PSYCHIC_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> THUNDER_RESISTANCE = RESISTANCE_ATTRIBUTES.register("thunder_resistance", () -> new TypedRangedAttribute("elemental:thunder_resistance", 100.0D, 0.0D, 1024D, THUNDER_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> AIR_RESISTANCE = RESISTANCE_ATTRIBUTES.register("air_resistance", () -> new TypedRangedAttribute("elemental:air_resistance", 100.0D, 0.0D, 1024D, AIR_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> EARTH_RESISTANCE = RESISTANCE_ATTRIBUTES.register("earth_resistance", () -> new TypedRangedAttribute("elemental:earth_resistance", 100.0D, 0.0D, 1024D, EARTH_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> WATER_RESISTANCE = RESISTANCE_ATTRIBUTES.register("water_resistance", () -> new TypedRangedAttribute("elemental:water_resistance", 100.0D, 0.0D, 1024D, WATER_DAMAGE).setSyncable(true));
    public static final RegistryObject<Attribute> NATURE_RESISTANCE = RESISTANCE_ATTRIBUTES.register("nature_resistance", () -> new TypedRangedAttribute("elemental:nature_resistance", 100.0D, 0.0D, 1024D, NATURE_DAMAGE).setSyncable(true));

    public static final RegistryObject<Attribute> ELEMENTAL_MASTERY = REACTION_ATTRIBUTES.register("elemental_mastery", () -> new RangedAttribute("elemental:elemental_mastery", 0.0D, 0.0D, 1024D).setSyncable(true));

}
