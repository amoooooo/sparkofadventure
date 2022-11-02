package sparkuniverse.amo.alkahest;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import sparkuniverse.amo.alkahest.reactions.capability.ReactionMarkCapabilityHandles;
import sparkuniverse.amo.alkahest.reactions.entity.EarthCoreEntity;
import sparkuniverse.amo.alkahest.reactions.entity.EntityRegistry;
import sparkuniverse.amo.alkahest.reactions.entity.NatureCoreEntity;

import static sparkuniverse.amo.alkahest.damagetypes.AttributeRegistry.*;

@Mod.EventBusSubscriber(modid = Alkahest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

    @SubscribeEvent
    public static void attrCreation(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.NATURE_CORE.get(), NatureCoreEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.KNOCKBACK_RESISTANCE, 255).build());
        event.put(EntityRegistry.EARTH_CORE.get(),  EarthCoreEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.KNOCKBACK_RESISTANCE, 255).build());
    }
}
