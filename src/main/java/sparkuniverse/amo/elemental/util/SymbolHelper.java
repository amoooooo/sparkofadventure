package sparkuniverse.amo.elemental.util;

import java.util.HashMap;
import java.util.Map;

public class SymbolHelper {
    public static final Map<String, String> symbols = new HashMap<>();

    public static void init(){
        symbols.put("fire", "A");
        symbols.put("cold", "B");
        symbols.put("lightning", "C");
        symbols.put("poison", "D");
        symbols.put("acid", "E");
        symbols.put("necrotic", "F");
        symbols.put("radiant", "G");
        symbols.put("force", "H");
        symbols.put("psychic", "I");
        symbols.put("thunder", "J");
        symbols.put("air", "K");
        symbols.put("earth", "L");
        symbols.put("water", "M");
        symbols.put("nature", "N");
    }

    public static String getSymbol(String name){
        if(name.contains("elemental:") || name.contains("apotheosis:")){
            name = name.substring(name.indexOf(":") + 1);
        }
        if(name.contains("_damage") || name.contains("_resistance") || name.contains("_reaction")){
            name = name.substring(0, name.indexOf("_"));
        }
        return symbols.get(name);
    }
}
