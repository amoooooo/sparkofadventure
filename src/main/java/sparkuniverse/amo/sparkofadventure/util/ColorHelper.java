package sparkuniverse.amo.sparkofadventure.util;

import java.util.HashMap;
import java.util.Map;

public class ColorHelper {
    public static Map<String, Integer> typeColorMap = new HashMap<>();
    public static void init(){
        typeColorMap.put("apotheosis:fire_damage", 0xd57239);
        typeColorMap.put("apotheosis:cold_damage", 0xCFFFFA);
        typeColorMap.put("sparkofadventure:lightning_damage", 0xB08FC2);
        typeColorMap.put("sparkofadventure:poison_damage", 0x00b029);
        typeColorMap.put("sparkofadventure:water_damage", 0x4bc3f1);
        typeColorMap.put("sparkofadventure:earth_damage", 0xfab632);
        typeColorMap.put("sparkofadventure:air_damage", 0x74c2a8);
        typeColorMap.put("sparkofadventure:acid_damage", 0xfc4e03);
        typeColorMap.put("sparkofadventure:necrotic_damage", 0x343A45);
        typeColorMap.put("sparkofadventure:radiant_damage", 0xF0E797);
        typeColorMap.put("sparkofadventure:psychic_damage", 0xE477F7);
        typeColorMap.put("sparkofadventure:force_damage", 0x9B77F7);
        typeColorMap.put("sparkofadventure:thunder_damage", 0x77C0F7);
        typeColorMap.put("sparkofadventure:nature_damage", 0x98b73d);
    }
}
