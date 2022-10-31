package sparkuniverse.amo.sparkofadventure.reactions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.function.TriFunction;

public class Reaction {
    private final Pair<RegistryObject<Attribute>, RegistryObject<Attribute>> reaction;
    private final double multiplier;
    private final TriFunction<LivingEntity, LivingEntity, Double, Boolean> consumer;

    public Reaction(Pair<RegistryObject<Attribute>, RegistryObject<Attribute>> reaction, double multiplier, TriFunction<LivingEntity, LivingEntity, Double, Boolean> consumer) {
        this.reaction = reaction;
        this.multiplier = multiplier;
        this.consumer = consumer;
    }

    public Pair<RegistryObject<Attribute>, RegistryObject<Attribute>> getReaction() {
        return reaction;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public boolean isReaction(RegistryObject<Attribute> type1, RegistryObject<Attribute> type2) {
        return (reaction.getFirst() == type1 && reaction.getSecond() == type2) || (reaction.getFirst() == type2 && reaction.getSecond() == type1);
    }

    public boolean isReaction(Attribute type1, Attribute type2) {
        return (reaction.getFirst().get() == type1 && reaction.getSecond().get() == type2) || (reaction.getFirst().get() == type2 && reaction.getSecond().get() == type1);
    }

    public boolean isReaction(Pair<RegistryObject<Attribute>, RegistryObject<Attribute>> reaction) {
        return isReaction(reaction.getFirst(), reaction.getSecond());
    }

    public String getOther(String type){
        if(reaction.getFirst().get().getDescriptionId().equals(type)){
            return reaction.getSecond().get().getDescriptionId();
        }else{
            return reaction.getFirst().get().getDescriptionId();
        }
    }

    public boolean isReaction(Reaction reaction) {
        return isReaction(reaction.getReaction());
    }

    public void applyReaction(LivingEntity entity, LivingEntity player, double damage) {
        consumer.apply(entity, player, damage);
    }
}
