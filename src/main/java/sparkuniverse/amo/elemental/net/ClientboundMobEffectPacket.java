package sparkuniverse.amo.elemental.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import sparkuniverse.amo.elemental.client.ClientPacketHandler;

import java.util.function.Supplier;

public class ClientboundMobEffectPacket {
    public final int uuid;
    public final String effect;
    public final int duration;
    public final int amplifier;
    public final boolean ambient;
    public final boolean showParticles;
    public final boolean showIcon;
    public final boolean remove;

    public ClientboundMobEffectPacket(int uuid, String effect, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, boolean remove) {
        this.uuid = uuid;
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.showParticles = showParticles;
        this.showIcon = showIcon;
        this.remove = remove;
    }

    public static void encode(ClientboundMobEffectPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.uuid);
        buf.writeUtf(msg.effect);
        buf.writeInt(msg.duration);
        buf.writeInt(msg.amplifier);
        buf.writeBoolean(msg.ambient);
        buf.writeBoolean(msg.showParticles);
        buf.writeBoolean(msg.showIcon);
        buf.writeBoolean(msg.remove);
    }

    public static ClientboundMobEffectPacket decode(FriendlyByteBuf buf) {
        return new ClientboundMobEffectPacket(buf.readInt(), buf.readUtf(), buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(ClientboundMobEffectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPacketHandler.handleMobEffect(msg, ctx);
        });
        ctx.get().setPacketHandled(true);
    }
}
