package sparkuniverse.amo.elemental;

import net.minecraftforge.eventbus.api.IEventBus;
import sparkuniverse.amo.elemental.reactions.effects.particle.ParticleRegistry;

public class ElementalClient {

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(ParticleRegistry::registerFactories);
    }
}
