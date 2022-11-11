package sparkuniverse.amo.elemental.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import sparkuniverse.amo.elemental.net.ClientboundMobEffectPacket;
import sparkuniverse.amo.elemental.net.ClientboundParticlePacket;
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
}
