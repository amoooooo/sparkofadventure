package sparkuniverse.amo.elemental.util;

import java.util.HashMap;
import java.util.Map;

public class ColorHelper {
    public static Map<String, Integer> typeColorMap = new HashMap<>();
    public static void init(){
        typeColorMap.put("apotheosis:fire_damage", 0xd57239);
        typeColorMap.put("apotheosis:cold_damage", 0xCFFFFA);
        typeColorMap.put("elemental:lightning_damage", 0xB08FC2);
        typeColorMap.put("elemental:poison_damage", 0x00b029);
        typeColorMap.put("elemental:water_damage", 0x4bc3f1);
        typeColorMap.put("elemental:earth_damage", 0xfab632);
        typeColorMap.put("elemental:air_damage", 0x74c2a8);
        typeColorMap.put("elemental:acid_damage", 0xfc4e03);
        typeColorMap.put("elemental:necrotic_damage", 0x343A45);
        typeColorMap.put("elemental:radiant_damage", 0xF0E797);
        typeColorMap.put("elemental:psychic_damage", 0xE477F7);
        typeColorMap.put("elemental:force_damage", 0x9B77F7);
        typeColorMap.put("elemental:thunder_damage", 0x77C0F7);
        typeColorMap.put("elemental:nature_damage", 0x98b73d);
    }

    public static int getColor(String type){
        String key = type;
        if(type.contains("fire_damage") || type.contains("cold_damage")){
            key = type.replace("elemental:", "apotheosis:");
        }
        return typeColorMap.get(key);
    }
}
