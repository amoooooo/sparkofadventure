package sparkuniverse.amo.elemental;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sparkuniverse.amo.elemental.client.GenericRenderLayer;
import sparkuniverse.amo.elemental.client.ShieldRenderLayer;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Elemental.MODID)
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
                addIceLayer(entityType, (LivingEntityRenderer<?, ?>) renderer);
            }
        }
    }
    private static void addIceLayer(EntityType<?> entityType, LivingEntityRenderer renderer) {
        renderer.addLayer(new GenericRenderLayer(renderer));
        renderer.addLayer(new ShieldRenderLayer(renderer));
        Elemental.LOGGER.debug("Added Ice layer to entity type: {}", entityType.getDescription());
        System.out.println("Added Ice layer to entity type: " + entityType.getDescription());
    }
}
