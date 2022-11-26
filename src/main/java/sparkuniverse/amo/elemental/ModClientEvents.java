package sparkuniverse.amo.elemental;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sparkuniverse.amo.elemental.client.GenericRenderLayer;
import sparkuniverse.amo.elemental.client.ShieldRenderLayer;
import sparkuniverse.amo.elemental.reactions.entity.EarthCoreEntityRenderer;
import sparkuniverse.amo.elemental.reactions.entity.NatureCoreEntityRenderer;

import java.util.Map;

import static sparkuniverse.amo.elemental.reactions.entity.EntityRegistry.EARTH_CORE;
import static sparkuniverse.amo.elemental.reactions.entity.EntityRegistry.NATURE_CORE;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Elemental.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerLayersEvent(EntityRenderersEvent.AddLayers event){
        for(Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()){
            if(entry.getKey().equals(EntityType.ARMOR_STAND)){
                continue;
            }
            EntityRenderer<?> renderer = entry.getValue();
            if(renderer instanceof LivingEntityRenderer){
                EntityType<?> entityType = entry.getKey();
                addLayers(entityType, (LivingEntityRenderer<?, ?>) renderer);
            }
        }
    }
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NATURE_CORE.get(), NatureCoreEntityRenderer::new);
        event.registerEntityRenderer(EARTH_CORE.get(), EarthCoreEntityRenderer::new);
    }

    private static void addLayers(EntityType<?> entityType, LivingEntityRenderer renderer) {
        renderer.addLayer(new GenericRenderLayer(renderer));
        renderer.addLayer(new ShieldRenderLayer(renderer));
        Elemental.LOGGER.debug("Added render layer to entity type: {}", entityType.getDescription());
    }
}
