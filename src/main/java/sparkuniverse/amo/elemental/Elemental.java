package sparkuniverse.amo.elemental;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import sparkuniverse.amo.elemental.compat.ars.ArsEvents;
import sparkuniverse.amo.elemental.compat.ars.ArsRegistry;
import sparkuniverse.amo.elemental.net.PacketHandler;
import sparkuniverse.amo.elemental.reactions.effects.LastingEffectMap;
import sparkuniverse.amo.elemental.reactions.effects.particle.ParticleRegistry;
import sparkuniverse.amo.elemental.util.ColorHelper;

import java.util.Random;

import static sparkuniverse.amo.elemental.damagetypes.AttributeRegistry.*;
import static sparkuniverse.amo.elemental.reactions.effects.ReactionEffects.EFFECTS;
import static sparkuniverse.amo.elemental.reactions.entity.EntityRegistry.ENTITY_TYPES;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Elemental.MODID)
public class Elemental {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "elemental";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Random RAND = new Random();

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name);
    }

    public Elemental() {

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
            ElementalClient.onCtorClient(modEventBus, forgeEventBus);
        });
        if(ModList.get().isLoaded("ars_nouveau")){
            try {
                ArsRegistry.registerGlyphs();
                MinecraftForge.EVENT_BUS.register(ArsEvents.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
