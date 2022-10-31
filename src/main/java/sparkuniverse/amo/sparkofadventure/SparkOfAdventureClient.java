package sparkuniverse.amo.sparkofadventure;

import net.minecraftforge.eventbus.api.IEventBus;
import sparkuniverse.amo.sparkofadventure.reactions.effects.particle.ParticleRegistry;

public class SparkOfAdventureClient {

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(ParticleRegistry::registerFactories);
    }
}
