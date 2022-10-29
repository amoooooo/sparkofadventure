package sparkuniverse.amo.sparkofadventure.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, "sparkofadventure");

    public static final RegistryObject<Attribute> FIRE_RESISTANCE = ATTRIBUTES.register("fire_resistance", () -> new RangedAttribute("sparkofadventure.fire_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> COLD_RESISTANCE = ATTRIBUTES.register("cold_resistance", () -> new RangedAttribute("sparkofadventure.cold_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> LIGHTNING_RESISTANCE = ATTRIBUTES.register("lightning_resistance", () -> new RangedAttribute("sparkofadventure.lightning_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> POISON_RESISTANCE = ATTRIBUTES.register("poison_resistance", () -> new RangedAttribute("sparkofadventure.poison_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ACID_RESISTANCE = ATTRIBUTES.register("acid_resistance", () -> new RangedAttribute("sparkofadventure.acid_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> NECROTIC_RESISTANCE = ATTRIBUTES.register("necrotic_resistance", () -> new RangedAttribute("sparkofadventure.necrotic_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> RADIANT_RESISTANCE = ATTRIBUTES.register("radiant_resistance", () -> new RangedAttribute("sparkofadventure.radiant_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> FORCE_RESISTANCE = ATTRIBUTES.register("force_resistance", () -> new RangedAttribute("sparkofadventure.force_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> PSYCHIC_RESISTANCE = ATTRIBUTES.register("psychic_resistance", () -> new RangedAttribute("sparkofadventure.psychic_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> THUNDER_RESISTANCE = ATTRIBUTES.register("thunder_resistance", () -> new RangedAttribute("sparkofadventure.thunder_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> SLASHING_RESISTANCE = ATTRIBUTES.register("slashing_resistance", () -> new RangedAttribute("sparkofadventure.slashing_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> PIERCING_RESISTANCE = ATTRIBUTES.register("piercing_resistance", () -> new RangedAttribute("sparkofadventure.piercing_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
    public static final RegistryObject<Attribute> BLUDGEONING_RESISTANCE = ATTRIBUTES.register("bludgeoning_resistance", () -> new RangedAttribute("sparkofadventure.bludgeoning_resistance", 0.0D, 0.0D, 10.0D).setSyncable(true));
}
