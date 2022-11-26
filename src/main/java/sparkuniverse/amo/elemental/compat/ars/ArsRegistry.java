package sparkuniverse.amo.elemental.compat.ars;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.IDamageEffect;
import sparkuniverse.amo.elemental.Elemental;

import java.util.ArrayList;
import java.util.List;

public class ArsRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void registerGlyphs(){
        Elemental.LOGGER.debug("Registering Ars Nouveau Glyphs");
        register(ElementalEffect.AIR);
        register(ElementalEffect.EARTH);
        register(ElementalEffect.FIRE);
        register(ElementalEffect.WATER);
        register(ElementalEffect.LIGHTNING);
        register(ElementalEffect.COLD);
        register(ElementalEffect.POISON);
        register(ElementalEffect.RADIANT);
        register(ElementalEffect.NECROTIC);
        register(ElementalEffect.FORCE);
        register(ElementalEffect.PSYCHIC);
        register(ElementalEffect.ACID);
        register(ElementalEffect.THUNDER);
        register(ElementalEffect.NATURE);
    }

    public static void register(AbstractSpellPart part) {
        ArsNouveauAPI.getInstance().registerSpell(part);
        registeredSpells.add(part);
    }

    public static void addAugments(){
        for(AbstractSpellPart spell : ArsNouveauAPI.getInstance().getSpellpartMap().values().stream().filter(s -> s instanceof IDamageEffect).toList()){
            spell.compatibleAugments.add(ElementalEffect.AIR);
            spell.compatibleAugments.add(ElementalEffect.EARTH);
            spell.compatibleAugments.add(ElementalEffect.FIRE);
            spell.compatibleAugments.add(ElementalEffect.WATER);
            spell.compatibleAugments.add(ElementalEffect.LIGHTNING);
            spell.compatibleAugments.add(ElementalEffect.COLD);
            spell.compatibleAugments.add(ElementalEffect.POISON);
            spell.compatibleAugments.add(ElementalEffect.RADIANT);
            spell.compatibleAugments.add(ElementalEffect.NECROTIC);
            spell.compatibleAugments.add(ElementalEffect.FORCE);
            spell.compatibleAugments.add(ElementalEffect.PSYCHIC);
            spell.compatibleAugments.add(ElementalEffect.ACID);
            spell.compatibleAugments.add(ElementalEffect.THUNDER);
            spell.compatibleAugments.add(ElementalEffect.NATURE);
        }
    }

}
