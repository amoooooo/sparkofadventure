package sparkuniverse.amo.elemental.compat.ars;

import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import com.hollingsworth.arsnouveau.api.event.SpellResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;
import sparkuniverse.amo.elemental.Elemental;
import sparkuniverse.amo.elemental.compat.obscureapi.ObscureAPICompatLayer;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.damagetypes.DamageResistanceJSONListener;
import sparkuniverse.amo.elemental.damagetypes.TypedRangedAttribute;
import sparkuniverse.amo.elemental.net.ClientboundParticlePacket;
import sparkuniverse.amo.elemental.net.PacketHandler;
import sparkuniverse.amo.elemental.reactions.Reaction;
import sparkuniverse.amo.elemental.reactions.ReactionRegistry;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.effects.ReactionEffects;
import sparkuniverse.amo.elemental.reactions.entity.NatureCoreEntity;
import sparkuniverse.amo.elemental.util.Color;
import sparkuniverse.amo.elemental.util.ColorHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static sparkuniverse.amo.elemental.ForgeEventHandler.*;
import static sparkuniverse.amo.elemental.damagetypes.AttributeRegistry.RESISTANCE_ATTRIBUTES;

public class ArsEvents {
    @SubscribeEvent
    public static void onSpellCastEvent(SpellCastEvent event){
        Spell spell = event.spell;
        SpellContext context = event.context;
        for(int i = 0; i < spell.recipe.size(); i++){
            AbstractSpellPart part = spell.recipe.get(i);
            if(SpellUtil.getElementalEffect(part) != null){
                //spell.recipe.add(i+1, SpellUtil.getElementalEffect(part));
                Color col = new Color(ColorHelper.getColor(((ElementalEffect)SpellUtil.getElementalEffect(part)).getElement()));
                context.setColors(new ParticleColor(col.r, col.g, col.b));
            }
        }
        if(spell.recipe.stream().anyMatch(s -> s instanceof ElementalEffect)){
            List<AbstractSpellPart> l = spell.recipe.stream().filter(s -> s instanceof ElementalEffect).toList();
            ElementalEffect effect = (ElementalEffect) l.get(Math.max(l.size() - 1, 0));
            Color color = new Color(ColorHelper.getColor(effect.getElement()));
            context.setColors(new ParticleColor(color.r, color.g, color.b));
        }
    }

    // Wait for spell damage event to contain spell or effect reference
    @SubscribeEvent
    public static void onSpellDamageEvent(SpellDamageEvent.Pre event){
        Entity e = event.target;
        LivingEntity caster = event.caster;
        Spell spell = event.context.getSpell();
        if(e instanceof LivingEntity target){
            final EntityType<?> type = target.getType();
            AtomicReference<Float> finalDamage = new AtomicReference<>(0f);
            float critMultiplier = 1;
            if(ModList.get().isLoaded("obscure_api")){
                critMultiplier = 1+ ObscureAPICompatLayer.getObscureAPICritValue(caster);
            }
            final Map<Attribute, Double> resistanceMap = DamageResistanceJSONListener.attributeResistances.get(type);
            Map<Attribute, Double> newResMap = new HashMap<>();
            for (RegistryObject<Attribute> attr : RESISTANCE_ATTRIBUTES.getEntries()) {
                if (resistanceMap != null) {
                    if (resistanceMap.containsKey(attr.get())) {
                        newResMap.put(attr.get(), resistanceMap.get(attr.get()));
                    }
                } else {
                    newResMap.put(attr.get(), target.getAttributeValue(attr.get()));
                }
            }
            for (Map.Entry<Attribute, Double> mapEntry : newResMap.entrySet()) {
                handleTypedSpellDamage(caster, target, finalDamage, mapEntry, critMultiplier, event.context, event.damage);
            }
            if(finalDamage.get() > 0){
                //doFinalHurt(caster, target, new AtomicReference<Float>(0f));
            }
            event.setCanceled(true);
        }
        avoidSO = false;
    }

    private static void handleTypedSpellDamage(LivingEntity attacker, LivingEntity hurtEntity, AtomicReference<Float> finalDamage, Map.Entry<Attribute, Double> mapEntry, float critMult, SpellContext context, float spelldamage) {
        final Attribute attribute = mapEntry.getKey();
        double mobResistance = hurtEntity.getAttributeValue(attribute) / 100f;
        Attribute playerAtt = ((TypedRangedAttribute) attribute).getType().get();
        double elementalSpellDamage = 0;
        boolean isEntireSpellElemental = SpellUtil.isEntireSpellElemental(context.getSpell());
        if (isEntireSpellElemental){
            String resElement = Elemental.getElement(attribute.getDescriptionId());
            ElementalEffect eff = SpellUtil.findElementalEffect(context);
            if(eff != null){
                String spellElement = Elemental.getElement(eff.getElement().toLowerCase());
                if(resElement.equals(spellElement)){
                    elementalSpellDamage = spelldamage * critMult;
                }
            }
        } else if(SpellUtil.isSpellElemental(context.getSpell().recipe.get(context.getCurrentIndex()-1))){
            String resElement = Elemental.getElement(attribute.getDescriptionId());
            ElementalEffect effect = (ElementalEffect) SpellUtil.getElementalEffect(context.getSpell().recipe.get(context.getCurrentIndex()-1));
            String spellElement = Elemental.getElement(effect.getElement().toLowerCase());
            if(resElement.equals(spellElement)){
                elementalSpellDamage = spelldamage * critMult;
            }
        } else {
            String resElement = Elemental.getElement(attribute.getDescriptionId());
            if(resElement.equals("force")){
                elementalSpellDamage = spelldamage * critMult;
            }
        }
        handleNatureCoreAttack(attacker, hurtEntity, playerAtt);
        if (hurtEntity instanceof NatureCoreEntity && playerAtt != AttributeRegistry.FIRE_DAMAGE.get())
            return;
        double attributeDamage = attacker.getAttributes().hasAttribute(playerAtt) ? attacker.getAttributeValue(playerAtt) + elementalSpellDamage : 0.0;
        if (attributeDamage * mobResistance == 0 && attributeDamage != 0) {
            if (attacker instanceof Player player) {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayer) player)), new ClientboundParticlePacket(hurtEntity.getId(), "Immune", Color.GRAY.getRGB()));
            }
        }
        if (attributeDamage > 0.001 && hurtEntity.getAttributes().hasAttribute(attribute)) {
            handleTypedDamage(attacker, hurtEntity, finalDamage, mobResistance, playerAtt, attributeDamage, critMult);
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

    private static void handleTypedDamage(LivingEntity attacker, LivingEntity hurtEntity, AtomicReference<Float> finalDamage, double mobResistance, Attribute playerAtt, double attributeDamage, float critMult) {
        float critDamage = (float) (attributeDamage * critMult);
        AtomicReference<Double> totalDamage = new AtomicReference<>(critDamage * mobResistance);
        AtomicReference<Float> shieldDamage = new AtomicReference<>((float) 0);
        hurtEntity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(s -> {
            handleShieldReaction(hurtEntity, finalDamage, playerAtt, totalDamage, shieldDamage, s);
        });
        hurtEntity.hurt(src(attacker), totalDamage.get().floatValue());
        hurtEntity.invulnerableTime = 0;
        float visualDamage = (float) (totalDamage.get() + shieldDamage.get());
        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(hurtEntity.getX(), hurtEntity.getY() + hurtEntity.level.random.nextFloat(), hurtEntity.getZ(), 128, hurtEntity.level.dimension())),
                new ClientboundParticlePacket(hurtEntity.getId(), "" + String.valueOf(Math.floor(visualDamage * 10) / 10), ColorHelper.getColor(playerAtt.getDescriptionId())));
        // 🛡
        if (mobResistance > 1) {
            hurtEntity.level.playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.HOSTILE, 1.0f, 0.25f);
        } else if (mobResistance < 1) {
            hurtEntity.level.playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.ANVIL_LAND, SoundSource.HOSTILE, 0.5f, 1.25f);
        }
        hurtEntity.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            handleDamageReaction(attacker, hurtEntity, playerAtt, attributeDamage, cap);
        });
        if (hurtEntity instanceof NatureCoreEntity ne) ne.remove(Entity.RemovalReason.DISCARDED);
    }
}
