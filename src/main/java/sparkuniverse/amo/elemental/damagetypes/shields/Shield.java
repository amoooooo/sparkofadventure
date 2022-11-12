package sparkuniverse.amo.elemental.damagetypes.shields;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityProvider;

public class Shield {
    public String defType;
    public String breakType;
    public int health;
    public int maxHealth;

    public Shield(String defType, String breakType, int health, int maxHealth){
        this.defType = defType;
        this.breakType = breakType;
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public void damage(int amount){
        health -= amount;
    }

    public void heal(int amount){
        health += amount;
    }

    public boolean isBroken(){
        return health <= 0;
    }

    public void setHealth(int health){
        this.health = health;
    }

    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }

    public int getHealth(){
        return health;
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    public String getDefType(){
        return defType;
    }

    public String getBreakType(){
        return breakType;
    }

    public void setDefType(String defType){
        this.defType = defType;
    }

    public void setBreakType(String breakType){
        this.breakType = breakType;
    }

    public void setShield(String defType, String breakType, int health, int maxHealth){
        this.defType = defType;
        this.breakType = breakType;
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public void setShield(Shield shield){
        this.defType = shield.defType;
        this.breakType = shield.breakType;
        this.health = shield.health;
        this.maxHealth = shield.maxHealth;
    }

    public void copy(Shield shield){
        this.defType = shield.defType;
        this.breakType = shield.breakType;
        this.health = shield.health;
        this.maxHealth = shield.maxHealth;
    }

    public Shield copy(){
        return new Shield(defType, breakType, health, maxHealth);
    }

    public void copyFrom(Shield shield){
        this.defType = shield.defType;
        this.breakType = shield.breakType;
        this.health = shield.health;
        this.maxHealth = shield.maxHealth;
    }

    public void copyTo(Shield shield){
        shield.defType = this.defType;
        shield.breakType = this.breakType;
        shield.health = this.health;
        shield.maxHealth = this.maxHealth;
    }

    public CompoundTag serializeNbt(){
        CompoundTag nbt = new CompoundTag();
        nbt.putString("defType", defType);
        nbt.putString("breakType", breakType);
        nbt.putInt("health", health);
        nbt.putInt("maxHealth", maxHealth);
        return nbt;
    }

    public static Shield deserializeNbt(CompoundTag nbt){
        return new Shield(nbt.getString("defType"), nbt.getString("breakType"), nbt.getInt("health"), nbt.getInt("maxHealth"));
    }

    public static void giveShield(LivingEntity entity, String defType, String breakType, int maxHealth){
        Shield shield = new Shield(defType, breakType, maxHealth, maxHealth);
        entity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent((cap) -> {
            cap.setShield(shield);
        });
    }

    public static void removeShield(LivingEntity entity, String defType){
        entity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent((cap) -> {
            cap.removeShield(defType);
        });
    }

    public void tick(LivingEntity entity){
        if(this.breakType.equals("none")) return;
        if(health > maxHealth){
            health = maxHealth;
        }
        if(health < 0){
            health = 0;
        }
        if(health == 0){
            entity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent((cap) -> {
                cap.removeShield(this);
            });
            return;
        }
    }

    public void damageShield(int amount){
        health -= amount;
    }
}
