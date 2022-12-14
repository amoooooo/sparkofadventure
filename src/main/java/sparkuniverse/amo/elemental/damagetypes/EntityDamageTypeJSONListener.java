package sparkuniverse.amo.elemental.damagetypes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.registries.ForgeRegistries;
import sparkuniverse.amo.elemental.Elemental;

import java.util.HashMap;
import java.util.Map;

public class EntityDamageTypeJSONListener extends SimpleJsonResourceReloadListener {
    public static Map<EntityType<?>, Map<Attribute, Double>> attributeDamageTypes = new HashMap<>();
    public static final Gson GSON = new Gson();

    public EntityDamageTypeJSONListener() {
        super(GSON, "damage_types");
    }

    public static void register(AddReloadListenerEvent event){
        event.addListener(new EntityDamageTypeJSONListener());
    }

    @Override
    protected void apply(java.util.Map<ResourceLocation, JsonElement> object, ResourceManager rm, ProfilerFiller profiler) {

        object.forEach((key, element) -> {

            JsonObject obj = element.getAsJsonObject();
            JsonObject mobs = obj.get("mobs").getAsJsonObject();

            for (Map.Entry<String, JsonElement> mob : mobs.entrySet()) {

                Map<Attribute, Double> damageMap = new HashMap<>();
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mob.getKey()));
                JsonObject damageTypes = mob.getValue().getAsJsonObject();
                for (Map.Entry<String, JsonElement> damageTypeEntry : damageTypes.entrySet()) {

                    Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(damageTypeEntry.getKey()));

                    if (attribute == null) {
                        Elemental.LOGGER.error("Invalid damage attribute found!" + damageTypeEntry.getKey());
                        Elemental.LOGGER.error("Allowed Attributes are: ");
                        ForgeRegistries.ATTRIBUTES.getValues().forEach(it -> Elemental.LOGGER.error(ForgeRegistries.ATTRIBUTES.getKey(it).toString()));

                        throw new IllegalArgumentException();
                    }

                    double resistance = damageTypeEntry.getValue().getAsDouble();
                    damageMap.put(attribute, resistance);
                }
                attributeDamageTypes.put(entityType, damageMap);
            }
        });
    }
}
