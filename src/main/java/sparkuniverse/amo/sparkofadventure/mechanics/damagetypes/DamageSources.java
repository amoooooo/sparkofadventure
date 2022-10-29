package sparkuniverse.amo.sparkofadventure.mechanics.damagetypes;

import net.minecraft.world.damagesource.DamageSource;

import java.util.HashMap;
import java.util.Map;

public class DamageSources {
    public static Map<String, DamageSource> damageSources = new HashMap<>();
    public static final DamageSource BLUDGEONING = new AdventureDamageSource("bludgeoning");
    public static final DamageSource PIERCING = new AdventureDamageSource("piercing");
    public static final DamageSource SLASHING = new AdventureDamageSource("slashing");
    public static final DamageSource FIRE = new AdventureDamageSource("fire");
    public static final DamageSource COLD = new AdventureDamageSource("cold");
    public static final DamageSource LIGHTNING = new AdventureDamageSource("lightning");
    public static final DamageSource POISON = new AdventureDamageSource("poison");
    public static final DamageSource ACID = new AdventureDamageSource("acid");
    public static final DamageSource NECROTIC = new AdventureDamageSource("necrotic");
    public static final DamageSource RADIANT = new AdventureDamageSource("radiant");
    public static final DamageSource FORCE = new AdventureDamageSource("force");
    public static final DamageSource PSYCHIC = new AdventureDamageSource("psychic");
    public static final DamageSource THUNDER = new AdventureDamageSource("thunder");

    public static void registerDamageSources(){
        damageSources.put("bludgeoning", BLUDGEONING);
        damageSources.put("piercing", PIERCING);
        damageSources.put("slashing", SLASHING);
        damageSources.put("fire", FIRE);
        damageSources.put("cold", COLD);
        damageSources.put("lightning", LIGHTNING);
        damageSources.put("poison", POISON);
        damageSources.put("acid", ACID);
        damageSources.put("necrotic", NECROTIC);
        damageSources.put("radiant", RADIANT);
        damageSources.put("force", FORCE);
        damageSources.put("psychic", PSYCHIC);
        damageSources.put("thunder", THUNDER);
    }
}
