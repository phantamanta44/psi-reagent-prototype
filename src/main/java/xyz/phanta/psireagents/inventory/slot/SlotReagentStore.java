package xyz.phanta.psireagents.inventory.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import xyz.phanta.psireagents.capability.ReagentStoreHolder;
import xyz.phanta.psireagents.init.ReagentsCaps;

import javax.annotation.Nonnull;

public class SlotReagentStore extends SlotItemHandler {

    public SlotReagentStore(ReagentStoreHolder inv, int posX, int posY) {
        super(inv, 0, posX, posY);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return stack.hasCapability(ReagentsCaps.REAGENT_STORE, null);
    }

}
