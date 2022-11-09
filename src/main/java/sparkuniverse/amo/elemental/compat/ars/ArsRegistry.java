package sparkuniverse.amo.elemental.compat.ars;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
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

}
