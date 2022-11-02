package sparkuniverse.amo.alkahest;

import net.minecraftforge.eventbus.api.IEventBus;
import sparkuniverse.amo.alkahest.reactions.effects.particle.ParticleRegistry;

public class AlkahestClient {

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(ParticleRegistry::registerFactories);
    }
}
