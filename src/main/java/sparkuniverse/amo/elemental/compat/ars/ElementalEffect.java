package sparkuniverse.amo.elemental.compat.ars;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import sparkuniverse.amo.elemental.Elemental;

import java.util.Set;

public class ElementalEffect extends AbstractAugment {
    public static final ElementalEffect FIRE = new ElementalEffect("fire_damage", "Fire"){

    };
    public static final ElementalEffect WATER = new ElementalEffect("water_damage", "Water");
    public static final ElementalEffect EARTH = new ElementalEffect("earth_damage", "Earth");
    public static final ElementalEffect AIR = new ElementalEffect("air_damage", "Air");
    public static final ElementalEffect LIGHTNING = new ElementalEffect("lightning_damage", "Lightning");
    public static final ElementalEffect COLD = new ElementalEffect("cold_damage", "Cold");
    public static final ElementalEffect POISON = new ElementalEffect("poison_damage", "Poison");
    public static final ElementalEffect RADIANT = new ElementalEffect("radiant_damage", "Radiant");
    public static final ElementalEffect NECROTIC = new ElementalEffect("necrotic_damage", "Necrotic");
    public static final ElementalEffect FORCE = new ElementalEffect("force_damage", "Force");
    public static final ElementalEffect PSYCHIC = new ElementalEffect("psychic_damage", "Psychic");
    public static final ElementalEffect ACID = new ElementalEffect("acid_damage", "Acid");
    public static final ElementalEffect THUNDER = new ElementalEffect("thunder_damage", "Thunder");
    public static final ElementalEffect NATURE = new ElementalEffect("nature_damage", "Nature");


    public ElementalEffect(String tag, String description) {
        super(new ResourceLocation(Elemental.MODID, tag), description);
    }

    public String getElement() {
        String key = getRegistryName().toString();
        key = key.replace("ars_nouveau:", "elemental:");
        return key;
    }

    @Override
    public int getDefaultManaCost() {
        return 10;
    }

}
