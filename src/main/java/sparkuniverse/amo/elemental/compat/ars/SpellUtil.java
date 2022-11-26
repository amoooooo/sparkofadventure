package sparkuniverse.amo.elemental.compat.ars;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.common.items.EnchantersSword;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.hollingsworth.arsnouveau.common.items.SpellBow;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class SpellUtil {
    public static Map<AbstractSpellPart, ElementalEffect> spellElementMap = new HashMap<>();

    public static boolean isPlayerHoldingCastable(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ICasterTool && !( player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof EnchantersSword ||  player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpellBow);
    }

    public static boolean isEntireSpellElemental(Spell spell) {
        return !spell.recipe.stream().filter(s -> s instanceof ElementalEffect).toList().isEmpty();
    }

    public static ElementalEffect findElementalEffect(SpellContext spell){
        return spell.getRemainingSpell().recipe.stream().filter(s -> s instanceof ElementalEffect).map(s -> (ElementalEffect) s).findFirst().orElse(null);
    }

    public static void init(){
        spellElementMap.put(EffectColdSnap.INSTANCE, ElementalEffect.COLD);
        spellElementMap.put(EffectFreeze.INSTANCE, ElementalEffect.COLD);
        spellElementMap.put(EffectIgnite.INSTANCE, ElementalEffect.FIRE);
        spellElementMap.put(EffectFlare.INSTANCE, ElementalEffect.FIRE);
        spellElementMap.put(EffectHarm.INSTANCE, ElementalEffect.FORCE);
        spellElementMap.put(EffectFangs.INSTANCE, ElementalEffect.FORCE);
        spellElementMap.put(EffectLightning.INSTANCE, ElementalEffect.LIGHTNING);
        spellElementMap.put(EffectHex.INSTANCE, ElementalEffect.NECROTIC);
        spellElementMap.put(EffectWither.INSTANCE, ElementalEffect.NECROTIC);
        spellElementMap.put(EffectWindshear.INSTANCE, ElementalEffect.AIR);
    }

    public static AbstractSpellPart getElementalEffect(AbstractSpellPart spellPart) {
        if(spellElementMap.containsKey(spellPart)) {
            return spellElementMap.get(spellPart);
        }
        return null;
    }

    public static boolean isSpellElemental(AbstractSpellPart spellPart) {
        return spellElementMap.containsKey(spellPart);
    }
}
