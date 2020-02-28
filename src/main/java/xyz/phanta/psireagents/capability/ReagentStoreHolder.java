package xyz.phanta.psireagents.capability;

import io.github.phantamanta44.libnine.util.helper.OptUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.phanta.psireagents.init.ReagentsCaps;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface ReagentStoreHolder extends IItemHandlerModifiable {

    ItemStack getReagentStoreStack();

    default Optional<ReagentStore> getReagentStore() {
        return OptUtils.capability(getReagentStoreStack(), ReagentsCaps.REAGENT_STORE);
    }

    void setReagentStoreStack(ItemStack stack);

    @Override
    default int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    default ItemStack getStackInSlot(int slot) {
        return slot == 0 ? getReagentStoreStack() : ItemStack.EMPTY;
    }

    @Override
    default void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot == 0) {
            setReagentStoreStack(stack);
        }
    }

    @Nonnull
    @Override
    default ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot != 0 || stack.isEmpty() || !getReagentStoreStack().isEmpty() || !isItemValid(slot, stack)) {
            return stack;
        }
        if (!simulate) {
            setReagentStoreStack(ItemHandlerHelper.copyStackWithSize(stack, 1));
        }
        return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
    }

    @Nonnull
    @Override
    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0 || slot != 0) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = getReagentStoreStack();
        if (!simulate) {
            setReagentStoreStack(ItemStack.EMPTY);
        }
        return stack;
    }

    @Override
    default int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    default boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return slot == 0 && stack.hasCapability(ReagentsCaps.REAGENT_STORE, null);
    }

    class Noop implements ReagentStoreHolder {

        @Override
        public ItemStack getReagentStoreStack() {
            return ItemStack.EMPTY;
        }

        @Override
        public Optional<ReagentStore> getReagentStore() {
            return Optional.empty();
        }

        @Override
        public void setReagentStoreStack(ItemStack stack) {
            // NO-OP
        }

    }

}
