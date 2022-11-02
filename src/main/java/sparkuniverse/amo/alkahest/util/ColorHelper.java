package sparkuniverse.amo.alkahest.util;

import java.util.HashMap;
import java.util.Map;

public class ColorHelper {
    public static Map<String, Integer> typeColorMap = new HashMap<>();
    public static void init(){
        typeColorMap.put("apotheosis:fire_damage", 0xd57239);
        typeColorMap.put("apotheosis:cold_damage", 0xCFFFFA);
        typeColorMap.put("alkahest:lightning_damage", 0xB08FC2);
        typeColorMap.put("alkahest:poison_damage", 0x00b029);
        typeColorMap.put("alkahest:water_damage", 0x4bc3f1);
        typeColorMap.put("alkahest:earth_damage", 0xfab632);
        typeColorMap.put("alkahest:air_damage", 0x74c2a8);
        typeColorMap.put("alkahest:acid_damage", 0xfc4e03);
        typeColorMap.put("alkahest:necrotic_damage", 0x343A45);
        typeColorMap.put("alkahest:radiant_damage", 0xF0E797);
        typeColorMap.put("alkahest:psychic_damage", 0xE477F7);
        typeColorMap.put("alkahest:force_damage", 0x9B77F7);
        typeColorMap.put("alkahest:thunder_damage", 0x77C0F7);
        typeColorMap.put("alkahest:nature_damage", 0x98b73d);
    }
}
