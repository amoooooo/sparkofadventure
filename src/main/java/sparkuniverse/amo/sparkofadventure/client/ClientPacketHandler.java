package sparkuniverse.amo.sparkofadventure.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;
import sparkuniverse.amo.sparkofadventure.net.ClientboundParticlePacket;
import sparkuniverse.amo.sparkofadventure.reactions.effects.particle.ParticleStates;
import sparkuniverse.amo.sparkofadventure.reactions.effects.particle.TextParticle;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleTextParticle(ClientboundParticlePacket msg, Supplier<NetworkEvent.Context> ctx){
        if(ctx.get().getDirection().getReceptionSide().isClient())
            ParticleStates.PARTICLES.add(new TextParticle(Minecraft.getInstance().level.getEntity(msg.uuid), msg.txt, msg.color));
    }
}
