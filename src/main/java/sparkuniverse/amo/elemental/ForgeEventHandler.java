package sparkuniverse.amo.elemental;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import sparkuniverse.amo.elemental.compat.bettercombat.BetterCombatCompat;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.damagetypes.DamageResistanceJSONListener;
import sparkuniverse.amo.elemental.damagetypes.EntityDamageTypeJSONListener;
import sparkuniverse.amo.elemental.damagetypes.TypedRangedAttribute;
import sparkuniverse.amo.elemental.damagetypes.shields.SelfShieldGoal;
import sparkuniverse.amo.elemental.net.ClientBoundShieldPacket;
import sparkuniverse.amo.elemental.net.ClientboundParticlePacket;
import sparkuniverse.amo.elemental.net.PacketHandler;
import sparkuniverse.amo.elemental.reactions.Reaction;
import sparkuniverse.amo.elemental.reactions.ReactionRegistry;
import sparkuniverse.amo.elemental.reactions.capability.*;
import sparkuniverse.amo.elemental.reactions.effects.LastingEffectMap;
import sparkuniverse.amo.elemental.reactions.effects.ReactionEffects;
import sparkuniverse.amo.elemental.reactions.effects.particle.CubeParticleData;
import sparkuniverse.amo.elemental.reactions.effects.particle.ParticleRenderer;
import sparkuniverse.amo.elemental.reactions.effects.particle.ParticleStates;
import sparkuniverse.amo.elemental.reactions.entity.NatureCoreEntity;
import sparkuniverse.amo.elemental.util.Color;
import sparkuniverse.amo.elemental.util.ColorHelper;
import sparkuniverse.amo.elemental.util.Pair;
import sparkuniverse.amo.elemental.util.SymbolHelper;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static sparkuniverse.amo.elemental.damagetypes.AttributeRegistry.RESISTANCE_ATTRIBUTES;

@Mod.EventBusSubscriber(modid = Elemental.MODID)
public class ForgeEventHandler {

    private static boolean avoidSO;

    @SubscribeEvent
    public static void attackEvent(final LivingAttackEvent event) {
        if (event.getEntity().level.isClientSide || avoidSO) return;
        avoidSO = true;
        if (event.getEntity().isInvulnerable() || event.getEntity().invulnerableTime > 10F) {
            avoidSO = false;
            return;
        }
        Entity a = event.getSource().getDirectEntity() instanceof AbstractArrow ? ((AbstractArrow) event.getSource().getDirectEntity()).getOwner() : event.getSource().getDirectEntity();
        if (a instanceof LivingEntity attacker) {
            AtomicBoolean isCharged = new AtomicBoolean(false);
            if (event.getSource().getDirectEntity() instanceof AbstractArrow arr) {
                arr.getCapability(ElementalArrowCapabilityProvider.CAPABILITY).ifPresent(s -> {
                    if (s.hasElement()) {
                        isCharged.set(true);
                    }
                });
            }
            final LivingEntity hurtEntity = event.getEntity();
            final EntityType<?> entityType = event.getEntity().getType();
            AtomicReference<Float> finalDamage = new AtomicReference<>(event.getAmount());
            if (isCharged.get() || !(event.getSource().getDirectEntity() instanceof AbstractArrow)) {
                if (ModList.get().isLoaded("bettercombat")) {
                    if (BetterCombatCompat.betterCombatComboCheck(event, attacker, hurtEntity, finalDamage, avoidSO).getFirst()) {
                        avoidSO = false;
                        return;
                    }
                }
                final Map<Attribute, Double> resistanceMap = DamageResistanceJSONListener.attributeResistances.get(entityType);
                Map<Attribute, Double> newResMap = new HashMap<>();
                for (RegistryObject<Attribute> attr : RESISTANCE_ATTRIBUTES.getEntries()) {
                    if (resistanceMap != null) {
                        if (resistanceMap.containsKey(attr.get())) {
                            newResMap.put(attr.get(), resistanceMap.get(attr.get()));
                        }
                    } else {
                        newResMap.put(attr.get(), hurtEntity.getAttributeValue(attr.get()));
                    }
                }
                for (Map.Entry<Attribute, Double> mapEntry : newResMap.entrySet()) {
                    handleTypedDamage(attacker, hurtEntity, finalDamage, mapEntry);
                }
            }
            doFinalHurt(attacker, hurtEntity, finalDamage);
            event.setCanceled(true);

        }
        avoidSO = false;
    }

    private static void handleTypedDamage(LivingEntity attacker, LivingEntity hurtEntity, AtomicReference<Float> finalDamage, Map.Entry<Attribute, Double> mapEntry) {
        final Attribute attribute = mapEntry.getKey();
        double mobResistance = hurtEntity.getAttributeValue(attribute) / 100f;
        Attribute playerAtt = ((TypedRangedAttribute) attribute).getType().get();
        handleNatureCoreAttack(attacker, hurtEntity, playerAtt);
        if (hurtEntity instanceof NatureCoreEntity && playerAtt != AttributeRegistry.FIRE_DAMAGE.get())
            return;
        double attributeDamage = attacker.getAttributes().hasAttribute(playerAtt) ? attacker.getAttributeValue(playerAtt) : 0.0;
        if (attributeDamage * mobResistance == 0 && attributeDamage != 0) {
            if (attacker instanceof Player player) {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayer) player)), new ClientboundParticlePacket(hurtEntity.getId(), "Immune", Color.GRAY.getRGB()));
            }
        }
        if (attributeDamage > 0.001 && hurtEntity.getAttributes().hasAttribute(attribute)) {
            handleTypedDamage(attacker, hurtEntity, finalDamage, mobResistance, playerAtt, attributeDamage);
        }
    }

    private static void handleNatureCoreAttack(LivingEntity attacker, LivingEntity hurtEntity, Attribute triggeringAtt) {
        NatureCoreEntity.onNatureCoreAttack(attacker, hurtEntity, triggeringAtt);
        if (hurtEntity.hasEffect(ReactionEffects.QUICKEN.get())) {
            if (attacker.getAttribute(AttributeRegistry.NATURE_DAMAGE.get()).getValue() > 0 && triggeringAtt.equals(AttributeRegistry.NATURE_DAMAGE.get())) {
                PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(hurtEntity.getX(), hurtEntity.getY() + hurtEntity.level.random.nextFloat(), hurtEntity.getZ(), 128, hurtEntity.level.dimension())), new ClientboundParticlePacket(hurtEntity.getId(), "Spread!", ColorHelper.getColor(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId())));
                hurtEntity.addEffect(new MobEffectInstance(ReactionEffects.SPREAD.get(), 100, 1));
            }
            if (attacker.getAttribute(AttributeRegistry.LIGHTNING_DAMAGE.get()).getValue() > 0 && triggeringAtt.equals(AttributeRegistry.LIGHTNING_DAMAGE.get())) {
                PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(hurtEntity.getX(), hurtEntity.getY() + hurtEntity.level.random.nextFloat(), hurtEntity.getZ(), 128, hurtEntity.level.dimension())), new ClientboundParticlePacket(hurtEntity.getId(), "Aggravate!", ColorHelper.getColor(AttributeRegistry.LIGHTNING_DAMAGE.get().getDescriptionId())));
                hurtEntity.addEffect(new MobEffectInstance(ReactionEffects.AGGRAVATE.get(), 100, 1));
            }
            if (attacker.getAttribute(AttributeRegistry.FIRE_DAMAGE.get()).getValue() > 0 && triggeringAtt.equals(AttributeRegistry.FIRE_DAMAGE.get())) {
                Reaction reaction = ReactionRegistry.getReaction(AttributeRegistry.FIRE_DAMAGE.get().getDescriptionId(), List.of(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId()));
                reaction.applyReaction(hurtEntity, attacker, attacker.getAttribute(AttributeRegistry.FIRE_DAMAGE.get()).getValue());
            }
            if (attacker.getAttribute(AttributeRegistry.WATER_DAMAGE.get()).getValue() > 0 && triggeringAtt.equals(AttributeRegistry.WATER_DAMAGE.get())) {
                Reaction reaction = ReactionRegistry.getReaction(AttributeRegistry.WATER_DAMAGE.get().getDescriptionId(), List.of(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId()));
                reaction.applyReaction(hurtEntity, attacker, attacker.getAttribute(AttributeRegistry.WATER_DAMAGE.get()).getValue());
            }
        }
    }

    private static void handleTypedDamage(LivingEntity attacker, LivingEntity hurtEntity, AtomicReference<Float> finalDamage, double mobResistance, Attribute playerAtt, double attributeDamage) {
        AtomicReference<Double> totalDamage = new AtomicReference<>(attributeDamage * mobResistance);
        AtomicReference<Float> shieldDamage = new AtomicReference<>((float) 0);
        hurtEntity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(s -> {
            handleShieldReaction(hurtEntity, finalDamage, playerAtt, totalDamage, shieldDamage, s);
        });
        hurtEntity.hurt(src(attacker), totalDamage.get().floatValue());
        float visualDamage = (float) (totalDamage.get() + shieldDamage.get());
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(hurtEntity.getX(), hurtEntity.getY() + hurtEntity.level.random.nextFloat(), hurtEntity.getZ(), 128, hurtEntity.level.dimension())),
                new ClientboundParticlePacket(hurtEntity.getId(), "" + String.valueOf(Math.floor(visualDamage * 10) / 10), ColorHelper.getColor(playerAtt.getDescriptionId())));
        // 🛡
        if (mobResistance > 1) {
            hurtEntity.level.playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.5f, 0.75f);
        } else if (mobResistance < 1) {
            hurtEntity.level.playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.5f, 1.25f);
        }
        hurtEntity.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            handleDamageReaction(attacker, hurtEntity, playerAtt, attributeDamage, cap);
        });
        if (hurtEntity instanceof NatureCoreEntity ne) ne.remove(Entity.RemovalReason.DISCARDED);
    }

    private static void handleDamageReaction(LivingEntity attacker, LivingEntity hurtEntity, Attribute playerAtt, double attributeDamage, ReactionMarkCapabilityHandler cap) {
        if (!cap.hasMark(playerAtt.getDescriptionId())) {
            cap.addMark(playerAtt.getDescriptionId());
        }
        Reaction reaction = ReactionRegistry.getReaction(playerAtt.getDescriptionId(), cap.getMarks().stream().map(Pair::getFirst).collect(Collectors.toList()));
        if (reaction != null) {
            reaction.applyReaction(hurtEntity, attacker, attributeDamage);
            cap.removeMark(playerAtt.getDescriptionId());
            cap.removeMark(reaction.getOther(playerAtt.getDescriptionId()));
        }
    }

    private static void handleShieldReaction(LivingEntity hurtEntity, AtomicReference<Float> finalDamage, Attribute playerAtt, AtomicReference<Double> totalDamage, AtomicReference<Float> shieldDamage, ShieldCapabilityHandler s) {
        Reaction reaction = ReactionRegistry.getReaction(playerAtt.getDescriptionId(), Collections.singletonList(s.getShieldBreakType()));
        if (reaction != null && s.hasShield()) {
            shieldDamage.set((float) (totalDamage.get() * 0.75f));
            totalDamage.updateAndGet(v -> (v - shieldDamage.get()));
            finalDamage.set((float) (totalDamage.get() * 0.25f));
            shieldDamage.set(shieldDamage.get() * 2);
            damageShield(hurtEntity, playerAtt, shieldDamage, s);
        } else if (s.hasShield()) {
            shieldDamage.set((float) (totalDamage.get() * 0.35f));
            totalDamage.updateAndGet(v -> (v - shieldDamage.get()));
            finalDamage.set((float) (totalDamage.get() * 0.65f));
            damageShield(hurtEntity, playerAtt, shieldDamage, s);
        }
    }

    private static void damageShield(LivingEntity hurtEntity, Attribute playerAtt, AtomicReference<Float> shieldDamage, @NotNull ShieldCapabilityHandler s) {
        s.damageShield(shieldDamage.get(), playerAtt.getDescriptionId());
        if (s.getShield().getHealth() < 0) {
            s.clearShield();
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientBoundShieldPacket(0, shieldDamage.get(), true, hurtEntity.getId(), false, ""));
            PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(hurtEntity.getX(), hurtEntity.getY() + hurtEntity.level.random.nextFloat(), hurtEntity.getZ(), 128, hurtEntity.level.dimension())),
                    new ClientboundParticlePacket(hurtEntity.getId(), "Broken!", ColorHelper.getColor(playerAtt.getDescriptionId())));
            hurtEntity.level.playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 2f, hurtEntity.level.random.nextFloat() * 0.1f);
        } else {
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientBoundShieldPacket(0, shieldDamage.get(), false, hurtEntity.getId(), false, ""));
            //hurtEntity.level.playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0f, hurtEntity.level.random.nextFloat()+0.1f);
        }
    }


    public static void doFinalHurt(LivingEntity attacker, LivingEntity hurtEntity, AtomicReference<Float> finalDamage) {
        if (Math.floor(finalDamage.get() * 10) / 10 <= 0) return;
        AtomicReference<String> damageStr = new AtomicReference<>(String.valueOf(Math.floor(finalDamage.get() * 10) / 10));
        if (hurtEntity.getCapability(ShieldCapabilityProvider.CAPABILITY).resolve().isPresent()) {
            hurtEntity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(s -> {
                if (s.hasShield()) {
                    damageStr.set("" + damageStr);
                    //🛡
                }
            });
        }
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(hurtEntity.getX(), hurtEntity.getY() + hurtEntity.level.random.nextFloat(), hurtEntity.getZ(), 128, hurtEntity.level.dimension())),
                new ClientboundParticlePacket(hurtEntity.getId(), damageStr.get(), Color.WHITE.getRGB()));
        hurtEntity.hurt(src(attacker), finalDamage.get());
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {

    }

    /**
     * Credit to this method and the above boolean goes to Apotheosis and ShadowsOfFire
     *
     * @param entity
     * @return A DamageSource pertaining to whatever living entity caused the attack.
     */

    public static DamageSource src(LivingEntity entity) {
        return entity instanceof Player p ? DamageSource.playerAttack(p) : DamageSource.mobAttack(entity);
    }

    @SubscribeEvent
    public static void onJsonListener(AddReloadListenerEvent event) {
        DamageResistanceJSONListener.register(event);
        EntityDamageTypeJSONListener.register(event);
    }


    @SubscribeEvent
    public static void mobSpawn(final LivingSpawnEvent event) {
        if (event.getEntity().level.isClientSide) return;

        if (DamageResistanceJSONListener.attributeResistances.containsKey(event.getEntity().getType())) {
            final LivingEntity entity = event.getEntity();
            final Map<Attribute, Double> attributeResistanceMap = DamageResistanceJSONListener.attributeResistances.get(entity.getType());

            for (Map.Entry<Attribute, Double> entry : attributeResistanceMap.entrySet()) {
                entity.getAttribute(entry.getKey()).setBaseValue(entry.getValue());
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        parseEntityDamageModifiers(event);
        if (event.getEntity() instanceof AbstractArrow abstractarrow) {
            if (abstractarrow.isCritArrow() && abstractarrow.getOwner() instanceof Player player) {
                abstractarrow.getCapability(ElementalArrowCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                    AttributeRegistry.DAMAGE_ATTRIBUTES.getEntries().forEach(entry -> {
                        if (player.getAttribute(entry.get()).getValue() > 0) {
                            cap.addElement(entry.get().getDescriptionId());
                        }
                    });
                });
            }
        }
        if (event.getEntity().getPersistentData().contains("apoth.boss")) {
            Entity entity = event.getEntity();
            if (entity instanceof Mob le) {
                le.goalSelector.addGoal(3, new SelfShieldGoal(le));
            }
        }
    }

    private static void parseEntityDamageModifiers(EntityJoinLevelEvent event) {
        if(EntityDamageTypeJSONListener.attributeDamageTypes.containsKey(event.getEntity().getType())){
            final LivingEntity entity = (LivingEntity) event.getEntity();
            final Map<Attribute, Double> attributeDamageMap = EntityDamageTypeJSONListener.attributeDamageTypes.get(entity.getType());
            if(attributeDamageMap == null) return;
            entity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.0f);
            for (Map.Entry<Attribute, Double> entry : attributeDamageMap.entrySet()) {
                entity.getAttribute(entry.getKey()).addPermanentModifier(new AttributeModifier("BaseDamageBonus", entry.getValue(), AttributeModifier.Operation.ADDITION));
                System.out.println("Added " + entry.getValue() + " to " + entry.getKey().getDescriptionId());
            }
        }
    }

    public static MutableComponent removeShieldFromName(Component name, String extension) {
        return Component.literal(name.getString().replace(" +" + extension, "")).withStyle(name.getStyle());
    }

    @SubscribeEvent
    public static void entityTick(LivingEvent.LivingTickEvent event) {
        doShieldRename(event);
        if (event.getEntity().level.isClientSide) return;
        if (event.getEntity() instanceof Player) return;
        event.getEntity().getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            if (event.getEntity().isInWaterOrRain() && !cap.hasMark(AttributeRegistry.WATER_DAMAGE.get().getDescriptionId())) {
                cap.addMark(AttributeRegistry.WATER_DAMAGE.get().getDescriptionId());
            }
            if (event.getEntity().isOnFire() && !cap.hasMark(AttributeRegistry.FIRE_DAMAGE.get().getDescriptionId())) {
                cap.addMark(AttributeRegistry.FIRE_DAMAGE.get().getDescriptionId());
            }
            if ((event.getEntity().isInPowderSnow || event.getEntity().level.isRaining() && event.getEntity().level.getBiome(event.getEntity().blockPosition()).get().coldEnoughToSnow(event.getEntity().blockPosition())) && !cap.hasMark(AttributeRegistry.COLD_DAMAGE.get().getDescriptionId())) {
                cap.addMark(AttributeRegistry.COLD_DAMAGE.get().getDescriptionId());
            }
            List<String> toRemove = new ArrayList<>();
            cap.getMarks().forEach(s -> {
                Reaction r = ReactionRegistry.getReaction(s.getFirst(), cap.getMarks().stream().map(Pair::getFirst).collect(Collectors.toList()));
                if (r != null && event.getEntity().getLastHurtByMob() != null && event.getEntity().level.getGameTime() % 10 == 0) {
                    r.applyReaction(event.getEntity(), event.getEntity().getLastHurtByMob(), 0);
                    toRemove.add(s.getFirst());
                    toRemove.add(r.getOther(s.getFirst()));
                }
                if (s.getSecond() < 100) {
                    s.setSecond(s.getSecond() + 1);
                } else {
                    toRemove.add(s.getFirst());
                }
                //LastingEffectMap.lastingEffectMap.get(s.getFirst()).accept(event.getEntity());
                Color color = new Color(ColorHelper.getColor(s.getFirst()));
                CubeParticleData particleData = new CubeParticleData(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.025f, 1, false);
                event.getEntity().level.getServer().getLevel(event.getEntity().level.dimension()).sendParticles(particleData, event.getEntity().getX() + (event.getEntity().level.random.nextFloat() - 0.5), (event.getEntity().getEyeY() - (event.getEntity().getBbHeight() / 2)) + (event.getEntity().level.random.nextFloat() - 0.5), event.getEntity().getZ() + (event.getEntity().level.random.nextFloat() - 0.5), 3, 0, 0, 0, 0.1);
            });
            toRemove.forEach(cap::removeMark);
            //}
        });
    }

    private static void doShieldRename(LivingEvent.LivingTickEvent event) {
        event.getEntity().getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(s -> {
            String type = SymbolHelper.getSymbol(s.getShieldBreakType());
            s.getShield().tick(event.getEntity());
            if (s.hasShield() && event.getEntity().hasCustomName()) {
                MutableComponent name = event.getEntity().getCustomName().toString().contains("+") ?
                        removeShieldFromName(event.getEntity().getCustomName(), type) : (MutableComponent) event.getEntity().getCustomName();
                event.getEntity().setCustomName(name.append(" ").append(Component.literal("+").withStyle(c -> c.withFont(Elemental.prefix("symbols")).withColor(0xFFFFFF))).append(Component.literal(type).withStyle(c -> c.withFont(Elemental.prefix("symbols")).withColor(0xFFFFFF))));
            } else if (!s.hasShield() && event.getEntity().hasCustomName()) {
                event.getEntity().setCustomName(removeShieldFromName(event.getEntity().getCustomName(), type));
            }
        });
    }


    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(ReactionMarkCapabilityProvider.ID, new ReactionMarkCapabilityProvider());
            event.addCapability(ShieldCapabilityProvider.ID, new ShieldCapabilityProvider());
        }
        if (event.getObject() instanceof AbstractArrow) {
            event.addCapability(ElementalArrowCapabilityProvider.ID, new ElementalArrowCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            ParticleRenderer.renderParticles(event.getPoseStack(), Minecraft.getInstance().gameRenderer.getMainCamera(), event.getPartialTick());
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level.isClientSide) {
            ParticleStates.tick();
        }
    }

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (event.getEntity().hasEffect(ReactionEffects.DECAY.get())) {
            event.setCanceled(true);
        }
    }

}
