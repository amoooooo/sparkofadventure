package sparkuniverse.amo.elemental.util;

import sparkuniverse.amo.elemental.Elemental;

import java.util.HashMap;
import java.util.Map;

public class ColorHelper {
    public static Map<String, Integer> typeColorMap = new HashMap<>();
    public static void init(){
        typeColorMap.put("fire", 0xFB7E00);
        typeColorMap.put("cold", 0xEEEEEE);
        typeColorMap.put("lightning", 0xF3C520);
        typeColorMap.put("poison", 0x8EC62B);
        typeColorMap.put("water", 0x40C6FF);
        typeColorMap.put("earth", 0x7E6048);
        typeColorMap.put("air", 0xBEE9E7);
        typeColorMap.put("acid", 0xCB5112);
        typeColorMap.put("necrotic", 0x623E4C);
        typeColorMap.put("radiant", 0xFFFE8D);
        typeColorMap.put("psychic", 0x904CAB);
        typeColorMap.put("force", 0xFFB2F6);
        typeColorMap.put("thunder", 0xD877F0);
        typeColorMap.put("nature", 0x70922D);
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
