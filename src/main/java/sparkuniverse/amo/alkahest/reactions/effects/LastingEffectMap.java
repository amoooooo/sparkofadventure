package sparkuniverse.amo.alkahest.reactions.effects;

import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import shadows.apotheosis.Apoth;
import sparkuniverse.amo.alkahest.damagetypes.AttributeRegistry;
import sparkuniverse.amo.alkahest.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.alkahest.reactions.entity.NatureCoreEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LastingEffectMap {
    public static Map<String, Consumer<LivingEntity>> lastingEffectMap = new HashMap<>();
    public static void init(){
        lastingEffectMap.put("alkahest:acid_damage", (entity) -> {
            if(entity.level.random.nextFloat() > 0.959){
                entity.hurt(DamageSource.MAGIC, 0.25f);
                entity.invulnerableTime = 0;
            }
        });
        lastingEffectMap.put("alkahest:air_damage", (entity) -> {
        });
        lastingEffectMap.put("alkahest:earth_damage", (entity) -> {
        });
        lastingEffectMap.put("apotheosis:fire_damage", (entity) -> {
            entity.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(r -> {
                if(r.hasMark(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId())){
                    entity.hurt(DamageSource.IN_FIRE, 0.25f);
                    entity.level.getEntities(null, entity.getBoundingBox().inflate(0.1f)).forEach(e -> {
                        e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(r2 -> {
                            if(!r2.hasMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId())){
                                r2.addMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId());
                            }
                        });
                    });

                }
            });
        });
        lastingEffectMap.put("apotheosis:cold_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
            if(entity.level.random.nextFloat() > 0.959){
                entity.hurt(DamageSource.FREEZE, 0.25f);
                entity.invulnerableTime = 0;
            }
        });
        lastingEffectMap.put("alkahest:lightning_damage", (entity) -> {
            if(entity.hasEffect(ReactionEffects.ELECTROCHARGED.get())){
                if(entity.level.random.nextFloat() > 0.959){
                    entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach((entity1) -> {
                        if(entity1 instanceof LivingEntity && !(entity1 instanceof Player)){
                            entity1.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent((cap) -> {
                                if(!cap.hasMark("alkahest:lightning_damage")){
                                    cap.addMark("alkahest:lightning_damage");
                                }
                            });
                            entity1.hurt(DamageSource.LIGHTNING_BOLT, 0.15f);
                            entity.invulnerableTime = 0;
                        }
                    });
                }
            }
        });
        lastingEffectMap.put("alkahest:water_damage", (entity) -> {
            if(entity.level.random.nextFloat() > 0.959){
                entity.hurt(DamageSource.DROWN, 0.15f);
                entity.invulnerableTime = 0;
            }
        });
        lastingEffectMap.put("alkahest:poison_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1));
        });
        lastingEffectMap.put("alkahest:necrotic_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20, 1));
        });
        lastingEffectMap.put("alkahest:radiant_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2, 1));
        });
        lastingEffectMap.put("alkahest:psychic_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 2, 1));
        });
        lastingEffectMap.put("alkahest:force_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(Apoth.Effects.SUNDERING.get(), 1, 1));
        });
        lastingEffectMap.put("alkahest:thunder_damage", (entity) -> {
            RandomSource random = entity.level.random;
            entity.setDeltaMovement(entity.getDeltaMovement().add((random.nextFloat()-0.5)/10f, 0, (random.nextFloat()-0.5)/10f));
        });
        lastingEffectMap.put("alkahest:nature_damage", (entity) -> {
            if(entity instanceof NatureCoreEntity) return;
            if(entity.level.random.nextFloat() > 0.959){
                entity.hurt(DamageSource.CACTUS, 0.15f);
                entity.invulnerableTime = 0;
            }
        });

    }
}
