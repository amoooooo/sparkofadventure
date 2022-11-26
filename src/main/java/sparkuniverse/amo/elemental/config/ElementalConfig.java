package sparkuniverse.amo.elemental.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ElementalConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;
    public static ForgeConfigSpec.BooleanValue enableDamageIndicators;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder){
        enableDamageIndicators = builder.comment("Enable damage indicators for attacks and reactions").translation("elemental.enabledamageindicators").define("enableDamageIndicators", true);
    }
}
