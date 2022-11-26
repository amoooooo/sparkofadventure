package sparkuniverse.amo.elemental.reactions.capability;

import net.minecraft.nbt.CompoundTag;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.damagetypes.shields.Shield;

public class ShieldCapability implements ShieldCapabilityHandler{
    public Shield EMPTY = new Shield("none", "none", 1, 1);
    public Shield shield = EMPTY;
    @Override
    public int getShieldHealth() {
        return shield.getHealth();
    }

    @Override
    public int getShieldMaxHealth() {
        return shield.getMaxHealth();
    }

    @Override
    public void setShield(Shield shield) {
        this.shield = shield;
    }

    @Override
    public void damageShield(float amount, String defType) {
        if(shield.getDefType().equals(defType)){
            shield.damage((int) amount);
        }
    }

    @Override
    public void damageShield(float amount) {
        shield.damage((int) amount);
    }

    @Override
    public void healShield(int amount, String defType) {
        if(shield.getDefType().equals(defType)){
            shield.heal(amount);
        }
    }

    @Override
    public void healShield(int amount) {
        shield.heal(amount);
    }

    @Override
    public void clearShield() {
        shield = EMPTY;
    }

    @Override
    public void removeShield(Shield shield) {
        if(this.shield.equals(shield)){
            this.shield = EMPTY;
        }
    }

    @Override
    public void removeShield(String defType) {
        if(shield.getDefType().equals(defType)){
            shield = EMPTY;
        }
    }

    @Override
    public boolean hasShield(Shield shield) {
        return this.shield.equals(shield);
    }

    @Override
    public boolean hasShield(String defType) {
        return shield.getDefType().equals(defType);
    }

    @Override
    public Shield getShield() {
        return shield;
    }

    @Override
    public int getShieldCount() {
        return this.shield.equals(EMPTY) ? 0 : 1;
    }

    @Override
    public boolean hasShield() {
        return !shield.equals(EMPTY);
    }

    @Override
    public String getShieldDefType() {
        return shield.getDefType();
    }

    @Override
    public String getShieldBreakType() {
        return shield.getBreakType();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("shield", shield.serializeNbt());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        shield = Shield.deserializeNbt(nbt.getCompound("shield"));
    }
}
