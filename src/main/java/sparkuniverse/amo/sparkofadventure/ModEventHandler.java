package sparkuniverse.amo.sparkofadventure;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityHandles;

import static sparkuniverse.amo.sparkofadventure.damagetypes.AttributeRegistry.*;

@Mod.EventBusSubscriber(modid = SparkOfAdventure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void registerCaps(final RegisterCapabilitiesEvent event){
        event.register(ReactionMarkCapabilityHandles.class);
    }

    @SubscribeEvent
    public static void attrMod(final EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            for (RegistryObject<Attribute> registryObject : RESISTANCE_ATTRIBUTES.getEntries()) {
                event.add(type, registryObject.get(), 100.0);
            }
            for (RegistryObject<Attribute> registryObject : DAMAGE_ATTRIBUTES.getEntries()) {
                event.add(type, registryObject.get(), 0.0);
            }
            for (RegistryObject<Attribute> registryObject : REACTION_ATTRIBUTES.getEntries()) {
                event.add(type, registryObject.get(), 0.0);
            }
        }
    }
}
