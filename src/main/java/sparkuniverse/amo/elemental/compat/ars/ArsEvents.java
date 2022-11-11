package sparkuniverse.amo.elemental.compat.ars;

import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import com.hollingsworth.arsnouveau.api.event.SpellResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sparkuniverse.amo.elemental.util.Color;
import sparkuniverse.amo.elemental.util.ColorHelper;

public class ArsEvents {
    @SubscribeEvent
    public static void onSpellCastEvent(SpellCastEvent event){
        Spell spell = event.spell;
        SpellContext context = event.context;
        if(spell.recipe.stream().anyMatch(s -> s instanceof ElementalEffect)){
            ElementalEffect effect = (ElementalEffect) spell.recipe.stream().filter(s -> s instanceof ElementalEffect).findFirst().get();
            Color color = new Color(ColorHelper.getColor(effect.getElement()));
            context.setColors(new ParticleColor(color.r, color.g, color.b));
        }
    }

    // Wait for spell damage event to contain spell or effect reference
    @SubscribeEvent
    public static void onSpellDamageEvent(SpellDamageEvent event){

    }
}
