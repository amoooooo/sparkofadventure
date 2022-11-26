package sparkuniverse.amo.elemental.reactions;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import sparkuniverse.amo.elemental.net.ClientBoundShieldPacket;
import sparkuniverse.amo.elemental.net.PacketHandler;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityProvider;

import java.util.concurrent.atomic.AtomicReference;

public class CombatHelper {
    public static void handlePlayerCombo(LivingEntity hurtEntity, LivingEntity attacker, AtomicReference<Float> finalDamage) {
        hurtEntity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(s -> {
//            if (s.hasShield()) {
//                float shieldDamage = (float) (finalDamage.get() * 0.35f);
//                finalDamage.set((float) (finalDamage.get() - shieldDamage));
//                s.damageShield(shieldDamage);
//                PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientBoundShieldPacket(0, shieldDamage, false, hurtEntity.getId(), false, ""));
//            }
        });
    }
}
