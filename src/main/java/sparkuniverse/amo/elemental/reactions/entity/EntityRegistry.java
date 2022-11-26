package sparkuniverse.amo.elemental.reactions.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "elemental");

    public static final RegistryObject<EntityType<NatureCoreEntity>> NATURE_CORE = ENTITY_TYPES.register("nature_core", () -> EntityType.Builder.of(NatureCoreEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("nature_core"));
    public static final RegistryObject<EntityType<EarthCoreEntity>> EARTH_CORE = ENTITY_TYPES.register("earth_core", () -> EntityType.Builder.of(EarthCoreEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("earth_core"));

}
