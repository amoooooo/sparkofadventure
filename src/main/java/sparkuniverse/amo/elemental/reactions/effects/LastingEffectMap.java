package sparkuniverse.amo.elemental.reactions.effects;

import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.entity.NatureCoreEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LastingEffectMap {
    public static Map<String, Consumer<LivingEntity>> lastingEffectMap = new HashMap<>();
    public static void init(){
        lastingEffectMap.put("elemental:acid_damage", (entity) -> {
            if(entity.level.random.nextFloat() > 0.959){
                //entity.hurt(DamageSource.MAGIC, 0.25f);
                
            }
        });
        lastingEffectMap.put("elemental:air_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:earth_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:fire_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:cold_damage", (entity) -> {
            //entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
            if(entity.level.random.nextFloat() > 0.959){
                //entity.hurt(DamageSource.FREEZE, 0.25f);
                
            }
        });
        lastingEffectMap.put("elemental:lightning_damage", (entity) -> {
            if(entity.hasEffect(ReactionEffects.ELECTROCHARGED.get())){
                if(entity.level.random.nextFloat() > 0.959){
                    entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach((entity1) -> {
                        if(entity1 instanceof LivingEntity && !(entity1 instanceof Player)){
                            entity1.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent((cap) -> {
                                if(!cap.hasMark("elemental:lightning_damage")){
                                    cap.addMark("elemental:lightning_damage");
                                }
                            });
                            entity1.hurt(DamageSource.LIGHTNING_BOLT, 0.15f);
                            
                        }
                    });
                }
            }
        });
        lastingEffectMap.put("elemental:water_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:poison_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:necrotic_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:radiant_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:psychic_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:force_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:thunder_damage", (entity) -> {
        });
        lastingEffectMap.put("elemental:nature_damage", (entity) -> {
        });

    }
}
