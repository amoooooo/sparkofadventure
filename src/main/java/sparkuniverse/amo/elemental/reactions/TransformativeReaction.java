package sparkuniverse.amo.elemental.reactions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.function.TriFunction;
// TODO: rewrite reactions
public class TransformativeReaction extends Reaction {

    public TransformativeReaction(Pair<RegistryObject<Attribute>, RegistryObject<Attribute>> reaction, double multiplier, TriFunction<LivingEntity, LivingEntity, Double, Boolean> consumer) {
        super(reaction, multiplier, consumer);
    }

    public TransformativeReaction(Pair<RegistryObject<Attribute>, RegistryObject<Attribute>> reaction, double multiplier) {
        super(reaction, multiplier);
    }
}
