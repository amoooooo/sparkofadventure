package sparkuniverse.amo.sparkofadventure.mechanics.damagetypes;

import net.minecraft.world.damagesource.DamageSource;

public class AdventureDamageSource extends DamageSource {
    private boolean bypassBludgeoning;
    private boolean bypassPiercing;
    private boolean bypassSlashing;
    private boolean bypassFire;
    private boolean bypassCold;
    private boolean bypassLightning;
    private boolean bypassPoison;
    private boolean bypassAcid;
    private boolean bypassNecrotic;
    private boolean bypassRadiant;
    private boolean bypassForce;
    private boolean bypassPsychic;
    private boolean bypassThunder;
    public AdventureDamageSource(String pMessageId) {
        super(pMessageId);
    }

    public AdventureDamageSource bypassBludgeoning(){
        this.bypassBludgeoning = true;
        return this;
    }

    public AdventureDamageSource bypassPiercing(){
        this.bypassPiercing = true;
        return this;
    }

    public AdventureDamageSource bypassSlashing(){
        this.bypassSlashing = true;
        return this;
    }

    public AdventureDamageSource bypassFire(){
        this.bypassFire = true;
        return this;
    }

    public AdventureDamageSource bypassCold(){
        this.bypassCold = true;
        return this;
    }

    public AdventureDamageSource bypassLightning(){
        this.bypassLightning = true;
        return this;
    }

    public AdventureDamageSource bypassPoison(){
        this.bypassPoison = true;
        return this;
    }

    public AdventureDamageSource bypassAcid(){
        this.bypassAcid = true;
        return this;
    }

    public AdventureDamageSource bypassNecrotic(){
        this.bypassNecrotic = true;
        return this;
    }

    public AdventureDamageSource bypassRadiant(){
        this.bypassRadiant = true;
        return this;
    }

    public AdventureDamageSource bypassForce(){
        this.bypassForce = true;
        return this;
    }

    public AdventureDamageSource bypassPsychic(){
        this.bypassPsychic = true;
        return this;
    }

    public AdventureDamageSource bypassThunder(){
        this.bypassThunder = true;
        return this;
    }

    public boolean bypassesBludgeoning(){
        return this.bypassBludgeoning;
    }

    public boolean bypassesPiercing(){
        return this.bypassPiercing;
    }

    public boolean bypassesSlashing(){
        return this.bypassSlashing;
    }

    public boolean bypassesFire(){
        return this.bypassFire;
    }

    public boolean bypassesCold(){
        return this.bypassCold;
    }

    public boolean bypassesLightning(){
        return this.bypassLightning;
    }

    public boolean bypassesPoison(){
        return this.bypassPoison;
    }

    public boolean bypassesAcid(){
        return this.bypassAcid;
    }

    public boolean bypassesNecrotic(){
        return this.bypassNecrotic;
    }

    public boolean bypassesRadiant(){
        return this.bypassRadiant;
    }

    public boolean bypassesForce(){
        return this.bypassForce;
    }

    public boolean bypassesPsychic(){
        return this.bypassPsychic;
    }

    public boolean bypassesThunder(){
        return this.bypassThunder;
    }

}
