package sparkuniverse.amo.sparkofadventure;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import static sparkuniverse.amo.sparkofadventure.AttributeRegistry.ATTRIBUTES;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SparkOfAdventure.MODID)
public class SparkOfAdventure {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "sparkofadventure";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public SparkOfAdventure() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ATTRIBUTES.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void attrMod(final EntityAttributeModificationEvent event) {
            for (EntityType<? extends LivingEntity> type : event.getTypes()) {
                for (RegistryObject<Attribute> registryObject : ATTRIBUTES.getEntries()) {
                    event.add(type, registryObject.get(), 1.0);
                }
            }
        }
    }

}
