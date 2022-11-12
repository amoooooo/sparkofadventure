package sparkuniverse.amo.elemental.compat.bettercombat;

import net.bettercombat.api.EntityPlayer_BetterCombat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import sparkuniverse.amo.elemental.reactions.CombatHelper;
import sparkuniverse.amo.elemental.util.Pair;

import java.util.concurrent.atomic.AtomicReference;

import static sparkuniverse.amo.elemental.ForgeEventHandler.doFinalHurt;

public class BetterCombatCompat {
    public static Pair<Boolean, Boolean> betterCombatComboCheck(LivingAttackEvent event, LivingEntity attacker, LivingEntity hurtEntity, AtomicReference<Float> finalDamage, boolean avoidSO) {
        if(attacker instanceof Player pl && ((EntityPlayer_BetterCombat) pl).getCurrentAttack() != null &&  ((EntityPlayer_BetterCombat) pl).getCurrentAttack().combo() != null) {
            if (((EntityPlayer_BetterCombat) pl).getCurrentAttack().combo().current() != ((EntityPlayer_BetterCombat) pl).getCurrentAttack().combo().total()) {
                CombatHelper.handlePlayerCombo(hurtEntity, attacker, finalDamage);
                doFinalHurt(attacker, hurtEntity, finalDamage);
                avoidSO = false;
                event.setCanceled(true);
                return Pair.of(true, avoidSO);
            }
        }
        return Pair.of(false, avoidSO);
    }
}
