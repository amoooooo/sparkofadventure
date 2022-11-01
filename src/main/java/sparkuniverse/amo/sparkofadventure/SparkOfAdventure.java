package sparkuniverse.amo.sparkofadventure;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import sparkuniverse.amo.sparkofadventure.net.PacketHandler;
import sparkuniverse.amo.sparkofadventure.reactions.effects.LastingEffectMap;
import sparkuniverse.amo.sparkofadventure.reactions.effects.particle.ParticleRegistry;
import sparkuniverse.amo.sparkofadventure.util.ColorHelper;

import java.util.Random;

import static sparkuniverse.amo.sparkofadventure.damagetypes.AttributeRegistry.*;
import static sparkuniverse.amo.sparkofadventure.reactions.effects.ReactionEffects.EFFECTS;
import static sparkuniverse.amo.sparkofadventure.reactions.entity.EntityRegistry.ENTITY_TYPES;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SparkOfAdventure.MODID)
public class SparkOfAdventure {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "sparkofadventure";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Random RAND = new Random();

    public SparkOfAdventure() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        DAMAGE_ATTRIBUTES.register(modEventBus);
        RESISTANCE_ATTRIBUTES.register(modEventBus);
        REACTION_ATTRIBUTES.register(modEventBus);
        EFFECTS.register(modEventBus);
        ColorHelper.init();
        LastingEffectMap.init();
        ENTITY_TYPES.register(modEventBus);
        PacketHandler.init();
        ParticleRegistry.register(modEventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            SparkOfAdventureClient.onCtorClient(modEventBus, forgeEventBus);
        });
    }

}
