package sparkuniverse.amo.elemental.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import sparkuniverse.amo.elemental.damagetypes.shields.Shield;
import sparkuniverse.amo.elemental.net.ClientBoundShieldPacket;
import sparkuniverse.amo.elemental.net.ClientboundMobEffectPacket;
import sparkuniverse.amo.elemental.net.ClientboundParticlePacket;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityHandler;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.effects.particle.ParticleStates;
import sparkuniverse.amo.elemental.reactions.effects.particle.TextParticle;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleTextParticle(ClientboundParticlePacket msg, Supplier<NetworkEvent.Context> ctx){
        if(ctx.get().getDirection().getReceptionSide().isClient()) {
            TextParticle particle = new TextParticle(Minecraft.getInstance().level.getEntity(msg.uuid), msg.txt, msg.color);
            ParticleStates.PARTICLES.add(particle);
        }
    }

    public static void handleMobEffect(ClientboundMobEffectPacket msg, Supplier<NetworkEvent.Context> ctx){
        if(ctx.get().getDirection().getReceptionSide().isClient()) {
            MobEffectInstance instance = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(msg.effect)), msg.duration, msg.amplifier, msg.ambient, msg.showParticles, msg.showIcon);
            ((LivingEntity)Minecraft.getInstance().level.getEntity(msg.uuid)).addEffect(instance);
        }
    }

    public static void handleShieldPacket(ClientBoundShieldPacket packet, Supplier<NetworkEvent.Context> context) {
        if(context.get().getDirection().getReceptionSide().isClient()) {
            if(packet.newShield){
                Minecraft.getInstance().level.getEntity(packet.id).getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(shield -> {
                    Shield sh = new Shield(packet.type, packet.type, (int) packet.heal, (int) packet.heal);
                    shield.setShield(sh);
                });
            }
            if(packet.clear) {
                Minecraft.getInstance().level.getEntity(packet.id).getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(ShieldCapabilityHandler::clearShield);
            } else {
                Minecraft.getInstance().level.getEntity(packet.id).getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(shield -> {
                    shield.damageShield(packet.damage);
                    shield.healShield((int) packet.heal);
                });
            }
        }
    }
}
