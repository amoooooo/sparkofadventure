package sparkuniverse.amo.alkahest.damagetypes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.RegistryObject;

public class TypedRangedAttribute extends RangedAttribute {
    private final RegistryObject<Attribute> type;
    public TypedRangedAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax, RegistryObject<Attribute> type) {
        super(pDescriptionId, pDefaultValue, pMin, pMax);
        this.type = type;
    }

    public RegistryObject<Attribute> getType() {
        return type;
    }
}
