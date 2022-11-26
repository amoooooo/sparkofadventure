package sparkuniverse.amo.elemental.net;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import sparkuniverse.amo.elemental.client.ClientPacketHandler;

import java.util.function.Supplier;

public class ClientboundMarkPacket {
    public final CompoundTag mark;
    public final int id;

    public ClientboundMarkPacket(CompoundTag mark, int id) {
        this.mark = mark;
        this.id = id;
    }

    public static void encode(ClientboundMarkPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.mark);
        buf.writeInt(msg.id);
    }

    public static ClientboundMarkPacket decode(FriendlyByteBuf buf) {
        return new ClientboundMarkPacket(buf.readNbt(), buf.readInt());
    }

    public static void handle(ClientboundMarkPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPacketHandler.handleMark(msg, ctx);
        });
        ctx.get().setPacketHandled(true);
    }
}
