package sparkuniverse.amo.sparkofadventure.mechanics.damagetypes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.registries.ForgeRegistries;
import sparkuniverse.amo.sparkofadventure.SparkOfAdventure;

import java.util.HashMap;
import java.util.Map;

public class DamageTypeJSONListener extends SimpleJsonResourceReloadListener {
    public static Map<EntityType<?>, Map<DamageSource, Float>> damageTypeMap = new HashMap<>();
    public static final Gson GSON = new Gson();
    public DamageTypeJSONListener() {
        super(GSON, "damagetypes");
    }

    public static void register(AddReloadListenerEvent event){
        event.addListener(new DamageTypeJSONListener());
    }

    @Override
    protected void apply(java.util.Map<ResourceLocation, JsonElement> object, ResourceManager rm, ProfilerFiller profiler) {
        SparkOfAdventure.LOGGER.info("Loading Damage Types");
        object.forEach((key, element) -> {
            JsonObject obj = element.getAsJsonObject();
            JsonObject mobs = obj.get("mobs").getAsJsonObject();
            for (Map.Entry<String, JsonElement> mob : mobs.entrySet()) {
                Map<DamageSource, Float> damageType = new HashMap<>();
                EntityType<?> entity = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mob.getKey()));
                JsonObject damageTypes = mob.getValue().getAsJsonObject();
                for (Map.Entry<String, JsonElement> damageTypeEntry : damageTypes.entrySet()) {
                    DamageSource source = DamageSources.damageSources.get(damageTypeEntry.getKey());
                    float resistance = damageTypeEntry.getValue().getAsFloat();
                    damageType.put(source, resistance);
                }
                damageTypeMap.put(entity, damageType);
            }
        });
        SparkOfAdventure.LOGGER.info(damageTypeMap.toString());
    }
}
