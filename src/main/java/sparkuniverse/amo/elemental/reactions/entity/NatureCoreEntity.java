package sparkuniverse.amo.elemental.reactions.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.damagetypes.TypedRangedAttribute;
import sparkuniverse.amo.elemental.reactions.ReactionRegistry;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.elemental.util.ColorHelper;
import sparkuniverse.amo.elemental.util.ParticleHelper;

import java.util.List;

import static net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY;
import static sparkuniverse.amo.elemental.ForgeEventHandler.src;

public class NatureCoreEntity extends Mob {

    public NatureCoreEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.getAttribute(ENTITY_GRAVITY.get()).addPermanentModifier(new AttributeModifier("Gravity modifier", -0.8D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    @Override
    public void tick() {
        super.tick();
        BlockHitResult result = level.clip(new ClipContext(this.position(), this.position().add(0,-1,0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, this));
        if(result.getType() != BlockHitResult.Type.MISS) {
            this.setPos(this.getX(), result.getLocation().y + 1, this.getZ());
        }
        if(this.tickCount > 10){
            this.setSharedFlag(7, false);
        }
        if(this.tickCount > 100) {
            this.remove(RemovalReason.DISCARDED);
        }
        if(!(this instanceof EarthCoreEntity)){
            this.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                if(!cap.hasMark(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId())){
                    cap.addMark(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId());
                }
            });
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }


    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.AMETHYST_CLUSTER_BREAK;
    }

    @Override
    public boolean canStandOnFluid(FluidState p_204042_) {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        //this.playSound(SoundEvents.AMETHYST_BLOCK_BREAK, this.getSoundVolume(), 0.65F);
    }

    @Override
    protected void playBlockFallSound() {
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return pEntity instanceof Player;
    }

    @Override
    public boolean isFallFlying() {
        return !(this.tickCount > 10);
    }

    @Override
    public void makePoofParticles() {
        ParticleHelper.particleBurst(this.getX(), this.getY(), this.getZ(), 200, 2, 1, ColorHelper.getColor(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId()), this.level);
    }

    @Override
    protected void playHurtSound(DamageSource pSource) {
        SoundEvent soundevent = SoundEvents.AMETHYST_CLUSTER_BREAK;
        this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
        this.playSound(SoundEvents.SPLASH_POTION_BREAK, this.getSoundVolume(), 0.65f);
    }

    @Override
    protected AABB makeBoundingBox() {
        return AABB.ofSize(this.position().add(0,0,0), 0.5, 0.5, 0.5);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static void onNatureCoreAttack(Entity attacker, Entity target, Attribute triggeringAttribute){
        if(!(target instanceof NatureCoreEntity)) return;
        NatureCoreEntity core = (NatureCoreEntity) target;
        if(attacker instanceof LivingEntity att){
            if(triggeringAttribute == AttributeRegistry.LIGHTNING_DAMAGE.get()){
                ReactionRegistry.HYPERBLOOM.applyReaction(core, att, att.getAttributeValue(triggeringAttribute));
            }
            if(triggeringAttribute == AttributeRegistry.FIRE_DAMAGE.get()){
                ReactionRegistry.BURGEON.applyReaction(core, att, att.getAttributeValue(triggeringAttribute));
            }
        }
    }
}
