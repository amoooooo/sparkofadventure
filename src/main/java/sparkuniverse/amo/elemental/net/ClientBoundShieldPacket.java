package sparkuniverse.amo.elemental.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import sparkuniverse.amo.elemental.client.ClientPacketHandler;

import java.util.function.Supplier;

public class ClientBoundShieldPacket {
    public float heal;
    public float damage;
    public int id;
    public boolean clear;
    public boolean newShield;
    public String type;

    public ClientBoundShieldPacket(float heal, float damage, boolean clear, int id, boolean newShield, String type) {
        this.heal = heal;
        this.damage = damage;
        this.clear = clear;
        this.id = id;
        this.newShield = newShield;
        this.type = type;
    }

    public static void encode(ClientBoundShieldPacket packet, FriendlyByteBuf buffer){
        buffer.writeFloat(packet.heal);
        buffer.writeFloat(packet.damage);
        buffer.writeBoolean(packet.clear);
        buffer.writeInt(packet.id);
        buffer.writeBoolean(packet.newShield);
        buffer.writeUtf(packet.type);
    }

    public static ClientBoundShieldPacket decode(FriendlyByteBuf buffer){
        return new ClientBoundShieldPacket(buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), buffer.readInt(), buffer.readBoolean(), buffer.readUtf());
    }

    public static void handle(ClientBoundShieldPacket packet, Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> {
            ClientPacketHandler.handleShieldPacket(packet, context);
        });
        context.get().setPacketHandled(true);
    }
}
