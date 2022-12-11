package sparkuniverse.amo.elemental.reactions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.function.TriFunction;

public class CatalyzeReaction extends Reaction{
    public CatalyzeReaction(Pair<RegistryObject<Attribute>, RegistryObject<Attribute>> reaction, double multiplier, TriFunction<LivingEntity, LivingEntity, Double, Boolean> consumer) {
        super(reaction, multiplier, consumer);
    }

    public CatalyzeReaction(Pair<RegistryObject<Attribute>, RegistryObject<Attribute>> reaction, double multiplier) {
        super(reaction, multiplier);
    }
}
