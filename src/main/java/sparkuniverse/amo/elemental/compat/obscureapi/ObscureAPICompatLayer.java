package sparkuniverse.amo.elemental.compat.obscureapi;

import com.obscuria.obscureapi.registry.ObscureAPIAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;

public class ObscureAPICompatLayer {
    public static float getObscureAPICritValue(LivingEntity player) {
        if(ModList.get().isLoaded("obscure_api")){
            float critChance = (float) ObscureAPIAttributes.getCriticalHit(player);
            if(critChance >= player.level.random.nextFloat()){
                return player.getAttributes().hasAttribute(ObscureAPIAttributes.CRITICAL_DAMAGE.get()) ? ObscureAPIAttributes.getCriticalDamage(player) : 0;
            }
        }
        return 0;
    }
}
