package sparkuniverse.amo.alkahest;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import sparkuniverse.amo.alkahest.net.PacketHandler;
import sparkuniverse.amo.alkahest.reactions.effects.LastingEffectMap;
import sparkuniverse.amo.alkahest.reactions.effects.particle.ParticleRegistry;
import sparkuniverse.amo.alkahest.util.ColorHelper;

import java.util.Random;

import static sparkuniverse.amo.alkahest.damagetypes.AttributeRegistry.*;
import static sparkuniverse.amo.alkahest.reactions.effects.ReactionEffects.EFFECTS;
import static sparkuniverse.amo.alkahest.reactions.entity.EntityRegistry.ENTITY_TYPES;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Alkahest.MODID)
public class Alkahest {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "alkahest";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Random RAND = new Random();

    public Alkahest() {

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
            AlkahestClient.onCtorClient(modEventBus, forgeEventBus);
        });
    }

}
