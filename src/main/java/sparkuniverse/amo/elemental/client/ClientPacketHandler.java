package sparkuniverse.amo.elemental.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
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
}
