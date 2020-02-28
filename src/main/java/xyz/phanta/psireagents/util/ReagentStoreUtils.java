package xyz.phanta.psireagents.util;

import io.github.phantamanta44.libnine.util.helper.OptUtils;
import net.minecraft.entity.player.EntityPlayer;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.capability.ReagentStoreHolder;
import xyz.phanta.psireagents.init.ReagentsCaps;

import java.util.Optional;

public class ReagentStoreUtils {

    public static Optional<ReagentStore> getEquipped(EntityPlayer player) {
        return OptUtils.capability(player, ReagentsCaps.REAGENT_STORE_HOLDER).flatMap(ReagentStoreHolder::getReagentStore);
    }

}
