package sparkuniverse.amo.elemental.util;

import sparkuniverse.amo.elemental.Elemental;

import java.util.HashMap;
import java.util.Map;

public class ColorHelper {
    public static Map<String, Integer> typeColorMap = new HashMap<>();
    public static void init(){
        typeColorMap.put("fire", 0xea8c15);
        typeColorMap.put("cold", 0xCFFFFA);
        typeColorMap.put("lightning", 0xB08FC2);
        typeColorMap.put("poison", 0x00b029);
        typeColorMap.put("water", 0x4bc3f1);
        typeColorMap.put("earth", 0xfab632);
        typeColorMap.put("air", 0x74c2a8);
        typeColorMap.put("acid", 0xfc4e03);
        typeColorMap.put("necrotic", 0x343A45);
        typeColorMap.put("radiant", 0xF0E797);
        typeColorMap.put("psychic", 0xE477F7);
        typeColorMap.put("force", 0x9B77F7);
        typeColorMap.put("thunder", 0x77C0F7);
        typeColorMap.put("nature", 0x98b73d);
    }

    public static int getColor(String type){
        String key = type;
        if(type.contains("fire_damage") || type.contains("cold_damage")){
            key = type.replace("apotheosis:", "elemental:");
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

        if(!typeColorMap.containsKey(key)){
            Elemental.LOGGER.error("No color found for type: " + type);
        }
        return typeColorMap.get(key) != null ? typeColorMap.get(key) : 0xffffff;
    }
}
