package sparkuniverse.amo.sparkofadventure.reactions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.apotheosis.Apoth;
import sparkuniverse.amo.sparkofadventure.damagetypes.AttributeRegistry;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.sparkofadventure.reactions.effects.ReactionEffects;
import sparkuniverse.amo.sparkofadventure.util.ParticleHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactionRegistry {
    public static final List<Reaction> REACTIONS = new ArrayList<>();

    public static Reaction FIRE_COLD = registerReaction(new Reaction(new Pair<>(Apoth.Attributes.FIRE_DAMAGE, Apoth.Attributes.COLD_DAMAGE), 1.5, (entity, player, damage) -> {
        if(player.getAttributes().hasAttribute(Apoth.Attributes.FIRE_DAMAGE.get()) && player.getAttributes().hasAttribute(Apoth.Attributes.COLD_DAMAGE.get())){
            float dmg = (float) (player.getAttributeValue(Apoth.Attributes.FIRE_DAMAGE.get()) + player.getAttributeValue(Apoth.Attributes.COLD_DAMAGE.get()));
            float dmgPostCalc = (float) ((dmg * 2) * (entity.getAttribute(AttributeRegistry.FIRE_RESISTANCE.get()).getValue() * entity.getAttribute(AttributeRegistry.COLD_RESISTANCE.get()).getValue()));
            entity.hurt(DamageSource.MAGIC, dmgPostCalc);
            ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 1, 1, 0xd57239, entity.level);
            ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.FROZEN.get().getColor(), entity.level);
        }
        return true;
    }));

    public static Reaction NATURE_FIRE = registerReaction(new Reaction(new Pair<>(AttributeRegistry.NATURE_DAMAGE, Apoth.Attributes.FIRE_DAMAGE), 1.5, (entity, player, damage) -> {
        Level level = entity.level;
        level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(e -> {
            if(e instanceof LivingEntity){
                float dmg = (float) player.getAttribute(Apoth.Attributes.FIRE_DAMAGE.get()).getValue();
                float dmgPostCalc = (float) (dmg * (((LivingEntity) e).getAttribute(AttributeRegistry.FIRE_RESISTANCE.get()).getValue()));
                e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(s -> {
                    if(!s.hasMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId()))
                        s.addMark(player.getAttribute(Apoth.Attributes.FIRE_DAMAGE.get()).getAttribute().getDescriptionId());
                });
                e.hurt(DamageSource.MAGIC, dmgPostCalc);
                e.setSecondsOnFire(1);
                ParticleHelper.particleCircle(entity.getX(), entity.getY()+1, entity.getZ(), 100, 1, 1, 0xd57239, entity.level);
                ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 1, 2, 0x98b73d, entity.level);
            }
        });
        return true;
    }));

    public static Reaction FIRE_WATER = registerReaction(new Reaction(new Pair<>(Apoth.Attributes.FIRE_DAMAGE, AttributeRegistry.WATER_DAMAGE), 1.5, (entity, player, damage) -> {
        if(player.getAttributes().hasAttribute(Apoth.Attributes.FIRE_DAMAGE.get()) && player.getAttributes().hasAttribute(AttributeRegistry.WATER_DAMAGE.get())){
            float dmg = (float) (player.getAttributeValue(Apoth.Attributes.FIRE_DAMAGE.get()) + player.getAttributeValue(AttributeRegistry.WATER_DAMAGE.get()));
            float dmgPostCalc = (float) ((dmg * 2) * (entity.getAttribute(AttributeRegistry.FIRE_RESISTANCE.get()).getValue() * entity.getAttribute(AttributeRegistry.WATER_RESISTANCE.get()).getValue()));
            ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 1, 1, 0xd57239, entity.level);
            ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 2, 1, 0x4bc3f1, entity.level);
            entity.hurt(DamageSource.MAGIC, dmgPostCalc);
        }
        return true;
    }));

    public static Reaction FIRE_LIGHTNING = registerReaction(new Reaction(new Pair<>(AttributeRegistry.LIGHTNING_DAMAGE, Apoth.Attributes.FIRE_DAMAGE), 1.5, (entity, player, damage) -> {
        Level level = entity.level;
        level.getEntities(null, entity.getBoundingBox().inflate(5)).forEach(e -> {
            if(e instanceof LivingEntity){
                float dmg = (float) player.getAttribute(Apoth.Attributes.FIRE_DAMAGE.get()).getValue();
                float dmgPostCalc = (float) (dmg * (((LivingEntity) e).getAttribute(AttributeRegistry.FIRE_RESISTANCE.get()).getValue()));
                e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(s -> {
                    if(!s.hasMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId()))
                        s.addMark(player.getAttribute(Apoth.Attributes.FIRE_DAMAGE.get()).getAttribute().getDescriptionId());
                });
                e.hurt(DamageSource.MAGIC, dmgPostCalc);
                Vec3 knockback = e.position().subtract(entity.position()).normalize();
                e.setDeltaMovement(e.getDeltaMovement().add(knockback));
                e.setSecondsOnFire(1);
                ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 2, 3, 0xd57239, entity.level);
                ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 2, 3, ReactionEffects.OVERLOAD.get().getColor(), entity.level);
            }
        });
        level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 0, Explosion.BlockInteraction.NONE);
        return true;
    }));


    public static Reaction RADIANT_NECROTIC = registerReaction(new Reaction(new Pair<>(AttributeRegistry.RADIANT_DAMAGE, AttributeRegistry.NECROTIC_DAMAGE), 1.5, (entity, player, damage) -> {
        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, entity.level);
        bolt.setPos(entity.getX(), entity.getY(), entity.getZ());
        entity.level.addFreshEntity(bolt);
        return true;
    }));

    public static Reaction WATER_LIGHTNING = registerReaction(new Reaction(new Pair<>(AttributeRegistry.WATER_DAMAGE, AttributeRegistry.LIGHTNING_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(ReactionEffects.OVERLOAD.get(), 200));
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.OVERLOAD.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1);
        return true;
    }));

    public static Reaction WATER_COLD = registerReaction(new Reaction(new Pair<>(AttributeRegistry.WATER_DAMAGE, Apoth.Attributes.COLD_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(ReactionEffects.FROZEN.get(), 120));
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.FROZEN.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1, 0.75f);
        entity.setTicksFrozen(600);
        return true;
    }));


    public static Reaction registerReaction(Reaction reaction) {
        REACTIONS.add(reaction);
        return reaction;
    }

    public static Reaction getReaction(String catalyst, List<String> appliedMarks){
        for(Reaction reaction : REACTIONS){
            if(reaction.isReaction(ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(catalyst)), ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(appliedMarks.get(0))))){
                return reaction;
            }
        }
        return null;
    }

}
