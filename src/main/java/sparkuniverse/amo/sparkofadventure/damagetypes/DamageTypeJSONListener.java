package sparkuniverse.amo.sparkofadventure.damagetypes;

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
import sparkuniverse.amo.sparkofadventure.SparkOfAdventure;

import java.util.HashMap;
import java.util.Map;

public class DamageTypeJSONListener extends SimpleJsonResourceReloadListener {

    public static Map<EntityType<?>, Map<Attribute, Double>> attributeResistances = new HashMap<>();
    public static final Gson GSON = new Gson();

    public DamageTypeJSONListener() {
        super(GSON, "damage_resistances");
    }

    public static void register(AddReloadListenerEvent event){
        event.addListener(new DamageTypeJSONListener());
    }

    @Override
    protected void apply(java.util.Map<ResourceLocation, JsonElement> object, ResourceManager rm, ProfilerFiller profiler) {


        object.forEach((key, element) -> {

            JsonObject obj = element.getAsJsonObject();
            JsonObject mobs = obj.get("mobs").getAsJsonObject();

            for (Map.Entry<String, JsonElement> mob : mobs.entrySet()) {

                Map<Attribute, Double> resistanceMap = new HashMap<>();
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mob.getKey()));
                JsonObject damageTypes = mob.getValue().getAsJsonObject();
                for (Map.Entry<String, JsonElement> damageTypeEntry : damageTypes.entrySet()) {

                    Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(damageTypeEntry.getKey()));

                    if (attribute == null) {
                        SparkOfAdventure.LOGGER.error("Invalid Attribute Resistance found!" + damageTypeEntry.getKey());
                        SparkOfAdventure.LOGGER.error("Allowed Attributes are: ");
                        ForgeRegistries.ATTRIBUTES.getValues().forEach(it -> SparkOfAdventure.LOGGER.error(ForgeRegistries.ATTRIBUTES.getKey(it).toString()));

                        throw new IllegalArgumentException();
                    }
                    
                    double resistance = damageTypeEntry.getValue().getAsDouble();
                    resistanceMap.put(attribute, resistance);
                }
                attributeResistances.put(entityType, resistanceMap);
            }
        });
    }

}
