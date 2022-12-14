package sparkuniverse.amo.elemental;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import sparkuniverse.amo.elemental.compat.ars.ArsEvents;
import sparkuniverse.amo.elemental.compat.ars.ArsRegistry;
import sparkuniverse.amo.elemental.compat.ars.SpellUtil;
import sparkuniverse.amo.elemental.config.ElementalConfig;
import sparkuniverse.amo.elemental.net.PacketHandler;
import sparkuniverse.amo.elemental.reactions.effects.LastingEffectMap;
import sparkuniverse.amo.elemental.reactions.effects.particle.ParticleRegistry;
import sparkuniverse.amo.elemental.util.ColorHelper;
import sparkuniverse.amo.elemental.util.SymbolHelper;

import java.util.Random;

import static sparkuniverse.amo.elemental.damagetypes.AttributeRegistry.*;
import static sparkuniverse.amo.elemental.reactions.effects.ReactionEffects.EFFECTS;
import static sparkuniverse.amo.elemental.reactions.entity.EntityRegistry.ENTITY_TYPES;

@Mod(Elemental.MODID)
public class Elemental {

    public static final String MODID = "elemental";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Random RAND = new Random();

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name);
    }

    public Elemental() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ElementalConfig.GENERAL_SPEC, "elemental.toml");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        DAMAGE_ATTRIBUTES.register(modEventBus);
        RESISTANCE_ATTRIBUTES.register(modEventBus);
        REACTION_ATTRIBUTES.register(modEventBus);
        EFFECTS.register(modEventBus);
        ColorHelper.init();
        LastingEffectMap.init();
        ENTITY_TYPES.register(modEventBus);
        PacketHandler.init();
        ParticleRegistry.register(modEventBus);
        SymbolHelper.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ElementalClient.onCtorClient(modEventBus, forgeEventBus);
        });
        if(ModList.get().isLoaded("ars_nouveau")){
            try {
                ArsRegistry.registerGlyphs();
                ArsRegistry.addAugments();
                SpellUtil.init();
                MinecraftForge.EVENT_BUS.register(ArsEvents.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setup(final FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            if(ModList.get().isLoaded("ars_nouveau")){
                try {
                    ArsRegistry.addAugments();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String getElement(String id){
        String key = id;
        if(id.contains("fire_damage") || id.contains("cold_damage")){
            key = id.replace("apotheosis:", "elemental:");
        }
        // check element name, disregard formatting
        if(key.contains("elemental:")){
            key = key.replace("elemental:", "");
        }
        if(key.contains("_damage")){
            key = key.replace("_damage", "");
        }
        if(key.contains("_resistance")){
            key = key.replace("_resistance", "");
        }
        return key;
    }

}
