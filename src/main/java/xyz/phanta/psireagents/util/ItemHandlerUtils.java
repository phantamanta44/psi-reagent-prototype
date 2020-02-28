package xyz.phanta.psireagents.util;

import io.github.phantamanta44.libnine.util.collection.Accrue;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerUtils {

    public static void accrue(Accrue<ItemStack> stacks, IItemHandler... invs) {
        for (IItemHandler inv : invs) {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    stacks.accept(stack);
                }
            }
        }
    }

}
