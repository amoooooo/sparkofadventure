package sparkuniverse.amo.elemental.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("elemental", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(id++, ClientboundParticlePacket.class, ClientboundParticlePacket::encode, ClientboundParticlePacket::decode, ClientboundParticlePacket::handle);
        INSTANCE.registerMessage(id++, ClientboundMobEffectPacket.class, ClientboundMobEffectPacket::encode, ClientboundMobEffectPacket::decode, ClientboundMobEffectPacket::handle);
        INSTANCE.registerMessage(id++, ClientBoundShieldPacket.class, ClientBoundShieldPacket::encode, ClientBoundShieldPacket::decode, ClientBoundShieldPacket::handle);
    }
}
