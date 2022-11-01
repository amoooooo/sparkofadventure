package sparkuniverse.amo.sparkofadventure.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("sparkofadventure", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(id++, ClientboundParticlePacket.class, ClientboundParticlePacket::encode, ClientboundParticlePacket::decode, ClientboundParticlePacket::handle);
    }
}
