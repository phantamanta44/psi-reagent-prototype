package xyz.phanta.psireagents.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.Spell;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.util.ReagentStoreUtils;

public class SpellCastHandler {

    @SubscribeEvent
    public void onPreSpellCast(PreSpellCastEvent event) {
        if (!checkSpellCast(event.getPlayer(), event.getBullet(), event.getSpell())) {
            if (event.getPlayer().world.isRemote) {
                event.setCancellationMessage(LangConst.SPELL_CANCEL_MISSING_REAGENTS);
            } else {
                event.setCancellationMessage(null);
            }
            event.setCanceled(true);
        }
    }

    private static boolean checkSpellCast(EntityPlayer player, ItemStack bullet, Spell spell) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }
        ReagentQuantity cost = PsiReagents.PROXY.getPieceCosts().computeTotalCost(spell.grid);
        return cost.isEmpty() || ReagentStoreUtils.getEquipped(player).map(store -> {
            float costFactor = store.getCostMultiplier();
            if (costFactor == 0F) {
                return true;
            }
            ReagentQuantity scaledCost = cost.scale(costFactor);
            if (scaledCost.isSatisfied(store)) {
                scaledCost.deduct(store);
                return true;
            }
            return false;
        }).orElse(false);
    }

}
