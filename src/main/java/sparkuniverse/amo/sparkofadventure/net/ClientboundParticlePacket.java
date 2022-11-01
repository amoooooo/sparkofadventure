package sparkuniverse.amo.sparkofadventure.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import sparkuniverse.amo.sparkofadventure.client.ClientPacketHandler;
import sparkuniverse.amo.sparkofadventure.reactions.effects.particle.ParticleStates;
import sparkuniverse.amo.sparkofadventure.reactions.effects.particle.TextParticle;

import java.util.function.Supplier;
import java.util.logging.Level;

public class ClientboundParticlePacket {
    public final int uuid;
    public final String txt;
    public final int color;

    public ClientboundParticlePacket(int uuid, String txt, int color) {
        this.uuid = uuid;
        this.txt = txt;
        this.color = color;
    }

    public static void encode(ClientboundParticlePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.uuid);
        buf.writeUtf(msg.txt);
        buf.writeInt(msg.color);
    }

    public static ClientboundParticlePacket decode(FriendlyByteBuf buf) {
        return new ClientboundParticlePacket(buf.readInt(), buf.readUtf(), buf.readInt());
    }

    public static void handle(ClientboundParticlePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPacketHandler.handleTextParticle(msg, ctx);
        });
        ctx.get().setPacketHandled(true);
    }
}
