package sparkuniverse.amo.alkahest.reactions;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.apotheosis.Apoth;
import sparkuniverse.amo.alkahest.damagetypes.AttributeRegistry;
import sparkuniverse.amo.alkahest.net.ClientboundParticlePacket;
import sparkuniverse.amo.alkahest.net.PacketHandler;
import sparkuniverse.amo.alkahest.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.alkahest.reactions.effects.ReactionEffects;
import sparkuniverse.amo.alkahest.reactions.entity.EarthCoreEntity;
import sparkuniverse.amo.alkahest.reactions.entity.EntityRegistry;
import sparkuniverse.amo.alkahest.reactions.entity.NatureCoreEntity;
import sparkuniverse.amo.alkahest.util.ColorHelper;
import sparkuniverse.amo.alkahest.util.ParticleHelper;

import java.util.ArrayList;
import java.util.List;

public class ReactionRegistry {
    public static final List<Reaction> REACTIONS = new ArrayList<>();

    public static Reaction FIRE_COLD = registerReaction(new Reaction(new Pair<>(Apoth.Attributes.FIRE_DAMAGE, Apoth.Attributes.COLD_DAMAGE), 1.5, (entity, player, damage) -> {
        if(player.getAttributes().hasAttribute(Apoth.Attributes.FIRE_DAMAGE.get()) && player.getAttributes().hasAttribute(Apoth.Attributes.COLD_DAMAGE.get())){
            float dmg = (float) (player.getAttributeValue(Apoth.Attributes.FIRE_DAMAGE.get()) + player.getAttributeValue(Apoth.Attributes.COLD_DAMAGE.get())  + player.getAttribute(AttributeRegistry.FIRE_REACTION_UP.get()).getValue()  + player.getAttribute(AttributeRegistry.COLD_REACTION_UP.get()).getValue());
            float dmgPostCalc = (float) ((dmg * 2) * ((entity.getAttribute(AttributeRegistry.FIRE_RESISTANCE.get()).getValue()/100 * entity.getAttribute(AttributeRegistry.COLD_RESISTANCE.get()).getValue()/100)));
            entity.hurt(DamageSource.MAGIC, dmgPostCalc);
            ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 1, 1.75f, 0xd57239, entity.level);
            ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.FROZEN.get().getColor(), entity.level);
        }
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Melt!", 0xd57239));
        return true;
    }));

    public static Reaction NATURE_FIRE = registerReaction(new Reaction(new Pair<>(AttributeRegistry.NATURE_DAMAGE, Apoth.Attributes.FIRE_DAMAGE), 1.5, (entity, player, damage) -> {
        Level level = entity.level;
        int radius = entity instanceof NatureCoreEntity ? 5 : 1;
        level.getEntities(null, entity.getBoundingBox().inflate(radius)).forEach(e -> {
            if(e instanceof LivingEntity && !e.equals(player)){
                float dmg = (float) (player.getAttribute(Apoth.Attributes.FIRE_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.FIRE_REACTION_UP.get()).getValue()  + player.getAttribute(AttributeRegistry.NATURE_REACTION_UP.get()).getValue());
                float dmgPostCalc = (float) (dmg * ((((LivingEntity) e).getAttribute(AttributeRegistry.FIRE_RESISTANCE.get()).getValue())/100));
                e.hurt(DamageSource.MAGIC, dmgPostCalc);
                ((LivingEntity) e).addEffect(new MobEffectInstance(ReactionEffects.BURNING.get(), 100, 2));
                if(entity instanceof NatureCoreEntity) {
                    ParticleHelper.particleCircle(entity.getX(), entity.getY()+0.5, entity.getZ(), 150, 0.5, 5, 0xd57239, entity.level);
                    ParticleHelper.particleCircle(entity.getX(), entity.getY()+0.5, entity.getZ(), 150, 0.5, 5, 0x98b73d, entity.level);
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.AMBIENT, 1, 0.65F);
                    e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(s -> {
                        if(!s.hasMark(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId()))
                            s.addMark(player.getAttribute(AttributeRegistry.NATURE_DAMAGE.get()).getAttribute().getDescriptionId());
                    });
                } else {
                    ParticleHelper.particleCircle(entity.getX(), entity.getY()+1, entity.getZ(), 100, 1, 1.75f, 0xd57239, entity.level);
                    ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 1, 2, 0x98b73d, entity.level);
                    e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(s -> {
                        if(!s.hasMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId()))
                            s.addMark(player.getAttribute(Apoth.Attributes.FIRE_DAMAGE.get()).getAttribute().getDescriptionId());
                    });
                }
            }
        });
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Burning!", ReactionEffects.BURNING.get().getColor()));
        return true;
    }));

    public static Reaction FIRE_WATER = registerReaction(new Reaction(new Pair<>(Apoth.Attributes.FIRE_DAMAGE, AttributeRegistry.WATER_DAMAGE), 1.5, (entity, player, damage) -> {
        if(player.getAttributes().hasAttribute(Apoth.Attributes.FIRE_DAMAGE.get()) && player.getAttributes().hasAttribute(AttributeRegistry.WATER_DAMAGE.get())){
            float dmg = (float) ((player.getAttributeValue(Apoth.Attributes.FIRE_DAMAGE.get()) + player.getAttribute(AttributeRegistry.FIRE_REACTION_UP.get()).getValue()) + (player.getAttributeValue(AttributeRegistry.WATER_DAMAGE.get()) + player.getAttribute(AttributeRegistry.WATER_REACTION_UP.get()).getValue()));
            float dmgPostCalc = (float) ((dmg * 2) * ((entity.getAttribute(AttributeRegistry.FIRE_RESISTANCE.get()).getValue()/100 + entity.getAttribute(AttributeRegistry.WATER_RESISTANCE.get()).getValue()/100)));
            ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 1, 1.75f, 0xd57239, entity.level);
            ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 2, 1, 0x4bc3f1, entity.level);
            entity.hurt(DamageSource.MAGIC, dmgPostCalc);
        }
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Vaporize!", 0x4bc3f1));
        return true;
    }));

    public static Reaction FIRE_LIGHTNING = registerReaction(new Reaction(new Pair<>(AttributeRegistry.LIGHTNING_DAMAGE, Apoth.Attributes.FIRE_DAMAGE), 1.5, (entity, player, damage) -> {
        Level level = entity.level;
        level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(e -> {
            if(e instanceof LivingEntity && !e.equals(player)){
                float dmg = (float) (player.getAttribute(Apoth.Attributes.FIRE_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.FIRE_REACTION_UP.get()).getValue()  + player.getAttribute(AttributeRegistry.LIGHTNING_REACTION_UP.get()).getValue());
                float dmgPostCalc = (float) (dmg * ((((LivingEntity) e).getAttribute(AttributeRegistry.FIRE_RESISTANCE.get()).getValue())/100));
                e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(s -> {
                    if(!s.hasMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId()))
                        s.addMark(player.getAttribute(Apoth.Attributes.FIRE_DAMAGE.get()).getAttribute().getDescriptionId());
                });
                e.hurt(DamageSource.MAGIC, dmgPostCalc);
                Vec3 knockback = e.position().subtract(entity.position()).normalize();
                e.setDeltaMovement(e.getDeltaMovement().add(knockback));
                e.setSecondsOnFire(1);
                ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 2, 3, 0xd57239, entity.level);
                ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 2, 3, ReactionEffects.ELECTROCHARGED.get().getColor(), entity.level);
            }
        });
        level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 0, Explosion.BlockInteraction.NONE);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Overloaded!", ReactionEffects.ELECTROCHARGED.get().getColor()));
        return true;
    }));


    public static Reaction RADIANT_NECROTIC = registerReaction(new Reaction(new Pair<>(AttributeRegistry.RADIANT_DAMAGE, AttributeRegistry.NECROTIC_DAMAGE), 1.5, (entity, player, damage) -> {
        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, entity.level);
        bolt.setPos(entity.getX(), entity.getY(), entity.getZ());
        entity.level.addFreshEntity(bolt);
        return true;
    }));

    public static Reaction WATER_LIGHTNING = registerReaction(new Reaction(new Pair<>(AttributeRegistry.WATER_DAMAGE, AttributeRegistry.LIGHTNING_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(ReactionEffects.ELECTROCHARGED.get(), 200));
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.ELECTROCHARGED.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Electrocharged!", ReactionEffects.ELECTROCHARGED.get().getColor()));
        return true;
    }));

    public static Reaction NATURE_LIGHTNING = registerReaction(new Reaction(new Pair<>(AttributeRegistry.NATURE_DAMAGE, AttributeRegistry.LIGHTNING_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(ReactionEffects.QUICKEN.get(), 200));
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.QUICKEN.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Quicken!", ReactionEffects.QUICKEN.get().getColor()));
        return true;
    }));

    public static Reaction WATER_COLD = registerReaction(new Reaction(new Pair<>(AttributeRegistry.WATER_DAMAGE, Apoth.Attributes.COLD_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(ReactionEffects.FROZEN.get(), 120));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 127));
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.FROZEN.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1, 0.45f);
        entity.setTicksFrozen(600);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Frozen!", ReactionEffects.FROZEN.get().getColor()));
        return true;
    }));

    public static Reaction WATER_NATURE = registerReaction(new Reaction(new Pair<>(AttributeRegistry.WATER_DAMAGE, AttributeRegistry.NATURE_DAMAGE), 1.5, (entity, player, damage) -> {
        NatureCoreEntity core = new NatureCoreEntity(EntityRegistry.NATURE_CORE.get(), entity.level);
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Bloom!", ColorHelper.typeColorMap.get(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction COLD_LIGHTNING = registerReaction(new Reaction(new Pair<>(Apoth.Attributes.COLD_DAMAGE, AttributeRegistry.LIGHTNING_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(Apoth.Effects.SUNDERING.get(), 200));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(Apoth.Attributes.COLD_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.COLD_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
            ((LivingEntity) e).addEffect(new MobEffectInstance(Apoth.Effects.SUNDERING.get(), 200));
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.ELECTROCHARGED.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Superconduct!", ReactionEffects.ELECTROCHARGED.get().getColor()));
        return true;
    }));

    public static Reaction AIR_FIRE = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, Apoth.Attributes.FIRE_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.FIRE_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.FIRE_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction AIR_NATURE = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.NATURE_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.NATURE_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.NATURE_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction AIR_LIGHTNING = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.LIGHTNING_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.LIGHTNING_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.LIGHTNING_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction AIR_WATER = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.WATER_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.WATER_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.WATER_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        if(entity != player)
            PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction AIR_ACID = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.ACID_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.ACID_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.ACID_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        if(entity != player)
            PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));

        return true;
    }));

    public static Reaction AIR_POISON = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.POISON_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.POISON_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.POISON_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        if(entity != player)
            PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction AIR_NECROTIC = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.NECROTIC_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.NECROTIC_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.NECROTIC_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        if(entity != player)
            PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction AIR_RADIANT = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.RADIANT_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.RADIANT_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.RADIANT_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        if(entity != player)
            PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction AIR_PSYCHIC = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.PSYCHIC_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.PSYCHIC_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.PSYCHIC_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        if(entity != player)
            PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction AIR_THUNDER = registerReaction(new Reaction(new Pair<>(AttributeRegistry.AIR_DAMAGE, AttributeRegistry.THUNDER_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.THUNDER_REACTION_UP.get()).getValue()));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2.5)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.AIR_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.THUNDER_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, 0x74c2a8, entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.STRAY_DEATH, SoundSource.PLAYERS, 1, 1.75f);
        if(entity != player)
            PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Swirl!", 0x74c2a8));
        return true;
    }));

    public static Reaction WATER_ACID = registerReaction(new Reaction(new Pair<>(AttributeRegistry.WATER_DAMAGE, AttributeRegistry.ACID_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(Apoth.Effects.SUNDERING.get(), 200));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.POISON_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.POISON_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
            ((LivingEntity) e).addEffect(new MobEffectInstance(ReactionEffects.RUSTING.get(), 200));
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.RUSTING.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Rust!", ReactionEffects.RUSTING.get().getColor()));
        return true;
    }));

    public static Reaction EARTH_FIRE = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, Apoth.Attributes.FIRE_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.FIRE_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.EARTH_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    // Do the same as above but for the other elements

    public static Reaction EARTH_COLD = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, Apoth.Attributes.COLD_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.COLD_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.EARTH_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_POISON = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.POISON_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.POISON_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.EARTH_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_ACID = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.ACID_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.ACID_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.EARTH_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_NATURE = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.NATURE_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.NATURE_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.EARTH_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_LIGHTNING = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.LIGHTNING_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.LIGHTNING_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.EARTH_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_THUNDER = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.THUNDER_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.THUNDER_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.THUNDER_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_PSYCHIC = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.PSYCHIC_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.PSYCHIC_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.PSYCHIC_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_WATER = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.WATER_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.WATER_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.WATER_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_AIR = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.AIR_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.AIR_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.AIR_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_NECROTIC = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.NECROTIC_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.NECROTIC_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.NECROTIC_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction EARTH_RADIANT = registerReaction(new Reaction(new Pair<>(AttributeRegistry.EARTH_DAMAGE, AttributeRegistry.RADIANT_DAMAGE), 1.5, (entity, player, damage) -> {
        EarthCoreEntity core = new EarthCoreEntity(EntityRegistry.EARTH_CORE.get(), entity.level);
        core.setPlayer(player.getUUID());
        core.setResistance(ReactionEffects.RADIANT_RESISTANCE.get());
        core.setPos(entity.getEyePosition().add(0,0.05,0));
        entity.level.addFreshEntity(core);
        core.setDeltaMovement(core.getDeltaMovement().add((entity.level.random.nextInt(5)-2.5f)/20f,0.1,(entity.level.random.nextInt(5)-2.5f)/20f));
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Crystallize!", ColorHelper.typeColorMap.get(AttributeRegistry.RADIANT_DAMAGE.get().getDescriptionId())));
        return true;
    }));

    public static Reaction ACID_POISON = registerReaction(new Reaction(new Pair<>(AttributeRegistry.POISON_DAMAGE, AttributeRegistry.ACID_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(ReactionEffects.DISSOLVE.get(), 200));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.POISON_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.POISON_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
            ((LivingEntity) e).addEffect(new MobEffectInstance(ReactionEffects.DISSOLVE.get(), 200));
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.DISSOLVE.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Dissolve!", ReactionEffects.DISSOLVE.get().getColor()));
        return true;
    }));

    public static Reaction POISON_NECROTIC = registerReaction(new Reaction(new Pair<>(AttributeRegistry.POISON_DAMAGE, AttributeRegistry.NECROTIC_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(ReactionEffects.DECAY.get(), 200));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.POISON_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.POISON_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
            ((LivingEntity) e).addEffect(new MobEffectInstance(ReactionEffects.DECAY.get(), 200));
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.DECAY.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Decay!", ReactionEffects.DECAY.get().getColor()));
        return true;
    }));

    public static Reaction ACID_PSYCHIC = registerReaction(new Reaction(new Pair<>(AttributeRegistry.ACID_DAMAGE, AttributeRegistry.PSYCHIC_DAMAGE), 1.5, (entity, player, damage) -> {
        entity.addEffect(new MobEffectInstance(ReactionEffects.FRAZZLE.get(), 200));
        entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(e -> {
            if(e.equals(player)) return;
            e.hurt(DamageSource.MAGIC, (float) (player.getAttribute(AttributeRegistry.PSYCHIC_DAMAGE.get()).getValue() + player.getAttribute(AttributeRegistry.PSYCHIC_REACTION_UP.get()).getValue()));
            e.invulnerableTime = 0;
            ((LivingEntity) e).addEffect(new MobEffectInstance(ReactionEffects.FRAZZLE.get(), 200));
        });
        ParticleHelper.particleBurst(entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.75, 2, ReactionEffects.FRAZZLE.get().getColor(), entity.level);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1, 1.75f);
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), 32, entity.level.dimension())), new ClientboundParticlePacket(entity.getId(), "Frazzle!", ReactionEffects.FRAZZLE.get().getColor()));
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