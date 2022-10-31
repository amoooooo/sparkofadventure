package sparkuniverse.amo.sparkofadventure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityHandles;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityProvider;

@Mod.EventBusSubscriber(modid = SparkOfAdventure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void registerCaps(final RegisterCapabilitiesEvent event){
        event.register(ReactionMarkCapabilityHandles.class);
    }
}
