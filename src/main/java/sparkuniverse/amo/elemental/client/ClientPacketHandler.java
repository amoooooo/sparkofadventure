package sparkuniverse.amo.elemental.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import sparkuniverse.amo.elemental.Elemental;
import sparkuniverse.amo.elemental.config.ElementalConfig;
import sparkuniverse.amo.elemental.damagetypes.shields.Shield;
import sparkuniverse.amo.elemental.net.ClientBoundShieldPacket;
import sparkuniverse.amo.elemental.net.ClientboundMarkPacket;
import sparkuniverse.amo.elemental.net.ClientboundMobEffectPacket;
import sparkuniverse.amo.elemental.net.ClientboundParticlePacket;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityHandler;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.effects.particle.ParticleStates;
import sparkuniverse.amo.elemental.reactions.effects.particle.TextParticle;
import sparkuniverse.amo.elemental.util.ColorHelper;
import sparkuniverse.amo.elemental.util.SymbolHelper;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleTextParticle(ClientboundParticlePacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            if (ElementalConfig.enableDamageIndicators.get()) {
                MutableComponent text = Component.literal("");
                String txt = msg.txt;
                String first = getSymbolFromColor(msg.color);
                text.append(Component.literal(first + "").withStyle(s -> s.withFont(Elemental.prefix("symbols"))).append(Component.literal(" " + txt).withStyle(s -> s.withFont(Style.DEFAULT_FONT))));
                TextParticle particle = new TextParticle(Minecraft.getInstance().level.getEntity(msg.uuid), text, msg.color);
                ParticleStates.PARTICLES.add(particle);
            }
        }
    }

    public static String getSymbolFromColor(int color) {
        StringBuilder symbol = new StringBuilder();
        for (int col : ColorHelper.typeColorMap.values()) {
            if (col == color) {
                String element = ColorHelper.typeColorMap.entrySet().stream().filter(entry -> entry.getValue().equals(color)).findFirst().get().getKey();
                symbol.append(SymbolHelper.getSymbol(element));
            }
        }
        return symbol.toString();
    }

    public static boolean doesStringContainSymbol(String str) {
        for (String symbol : SymbolHelper.symbols.values()) {
            if (str.substring(0, 1).equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    public static void handleMobEffect(ClientboundMobEffectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (msg.remove) {
            ((LivingEntity) Minecraft.getInstance().level.getEntity(msg.uuid)).removeEffect(ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse(msg.effect)));
            return;
        }
        MobEffectInstance instance = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse(msg.effect)), msg.duration, msg.amplifier, msg.ambient, msg.showParticles, msg.showIcon);
        ((LivingEntity) Minecraft.getInstance().level.getEntity(msg.uuid)).addEffect(instance);

    }

    public static void handleShieldPacket(ClientBoundShieldPacket packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isClient()) {
            if (packet.newShield) {
                Minecraft.getInstance().level.getEntity(packet.id).getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(shield -> {
                    Shield sh = new Shield(packet.type, packet.type, (int) packet.heal, (int) packet.heal);
                    shield.setShield(sh);
                });
            }
            if (packet.clear) {
                Minecraft.getInstance().level.getEntity(packet.id).getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(ShieldCapabilityHandler::clearShield);
            } else {
                Minecraft.getInstance().level.getEntity(packet.id).getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(shield -> {
                    shield.damageShield(packet.damage);
                    shield.healShield((int) packet.heal);
                });
            }
        }
    }

    public static void handleMark(ClientboundMarkPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            Minecraft.getInstance().level.getEntity(msg.id).getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(s -> {
                s.deserializeNBT(msg.mark);
            });
        }
    }
}
