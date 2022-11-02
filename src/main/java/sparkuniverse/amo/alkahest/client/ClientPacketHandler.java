package sparkuniverse.amo.alkahest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import sparkuniverse.amo.alkahest.net.ClientboundParticlePacket;
import sparkuniverse.amo.alkahest.reactions.effects.particle.ParticleStates;
import sparkuniverse.amo.alkahest.reactions.effects.particle.TextParticle;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleTextParticle(ClientboundParticlePacket msg, Supplier<NetworkEvent.Context> ctx){
        if(ctx.get().getDirection().getReceptionSide().isClient()) {
            TextParticle particle = new TextParticle(Minecraft.getInstance().level.getEntity(msg.uuid), msg.txt, msg.color);
            ParticleStates.PARTICLES.add(particle);
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Particle added"));
        }
    }
}
