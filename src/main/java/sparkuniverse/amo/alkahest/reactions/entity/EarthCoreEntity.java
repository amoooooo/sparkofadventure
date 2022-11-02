package sparkuniverse.amo.alkahest.reactions.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import sparkuniverse.amo.alkahest.damagetypes.AttributeRegistry;
import sparkuniverse.amo.alkahest.util.ColorHelper;
import sparkuniverse.amo.alkahest.util.ParticleHelper;

import java.util.UUID;

public class EarthCoreEntity extends NatureCoreEntity{
    private UUID player;
    private MobEffect resistance;
    public EarthCoreEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void setPlayer(UUID p) {
        player = p;
    }

    public void setResistance(MobEffect effect) {
        resistance = effect;
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
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
    }

    @Override
    public void playerTouch(Player pPlayer) {
        if(pPlayer.getUUID().equals(player)) {
            double attr = pPlayer.getAttribute(AttributeRegistry.EARTH_REACTION_UP.get()) != null ? pPlayer.getAttribute(AttributeRegistry.EARTH_REACTION_UP.get()).getValue() / 5f : 0;
            pPlayer.addEffect(new MobEffectInstance(resistance, (int) Math.floor(100 * attr), 0, false, false));
            this.playSound(SoundEvents.AMETHYST_BLOCK_BREAK, this.getSoundVolume(), 0.65F);
            ParticleHelper.particleBurst(this.getX(), this.getY(), this.getZ(), 200, 2, 1, ColorHelper.typeColorMap.get(AttributeRegistry.EARTH_DAMAGE.get().getDescriptionId()), this.level);
            this.hurt(DamageSource.MAGIC, 1);
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
