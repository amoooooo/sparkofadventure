package sparkuniverse.amo.elemental.reactions.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import sparkuniverse.amo.elemental.damagetypes.Shield;

import java.util.List;

public class ShieldCapability implements ShieldCapabilityHandler{
    List<Shield> shields;
    @Override
    public int getShield() {
        return 0;
    }

    @Override
    public void setShield(Shield shield) {
        Shield finalShield = shield.copy();
        if(shields.stream().filter(shield1 -> shield1.getDefType().equals(shield.getDefType())).count() > 0){
            for(Shield shield1 : shields){
                if(shield1.getDefType().equals(shield.getDefType())){
                    finalShield.setMaxHealth(finalShield.getMaxHealth()+shield1.getMaxHealth());
                    finalShield.setHealth(finalShield.getHealth()+shield1.getHealth());
                    shields.remove(shield1);
                }
            }
        }
        shields.add(finalShield);
    }

    @Override
    public void damageShield(float amount, String defType) {
        shields.stream().filter(shield -> shield.getDefType().equals(defType)).forEach(shield -> shield.damage(amount));
    }

    @Override
    public void healShield(int amount, String defType) {
        shields.stream().filter(shield -> shield.getDefType().equals(defType)).forEach(shield -> shield.heal(amount));
    }

    @Override
    public void clearShield() {
        shields.clear();
    }

    @Override
    public void removeShield(Shield shield) {
        shields.remove(shield);
    }

    @Override
    public void removeShield(String defType) {
        shields.removeIf(shield -> shield.getDefType().equals(defType));
    }

    @Override
    public boolean hasShield(Shield shield) {
        return shields.contains(shield);
    }

    @Override
    public boolean hasShield(String defType) {
        return shields.stream().anyMatch(shield -> shield.getDefType().equals(defType));
    }

    @Override
    public Shield getShield(int index) {
        return shields.get(index);
    }

    @Override
    public Shield getShield(String defType) {
        return shields.stream().filter(shield -> shield.getDefType().equals(defType)).findFirst().orElse(null);
    }

    @Override
    public int getShieldCount() {
        return shields.size();
    }

    @Override
    public List<Shield> getShields() {
        return shields;
    }

    @Override
    public boolean hasShield() {
        return !shields.isEmpty();
    }

    @Override
    public CompoundTag serializeNBT() {
        ListTag list = new ListTag();
        shields.forEach(shield -> list.add(shield.serializeNbt()));
        CompoundTag nbt = new CompoundTag();
        nbt.put("shields", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag list = nbt.getList("shields", 10);
        list.forEach(tag -> shields.add(Shield.deserializeNbt((CompoundTag) tag)));
    }
}
