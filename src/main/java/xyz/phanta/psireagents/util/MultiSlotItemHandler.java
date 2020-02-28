package xyz.phanta.psireagents.util;

import io.github.phantamanta44.libnine.capability.impl.L9AspectSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class MultiSlotItemHandler implements IItemHandlerModifiable {

    private final L9AspectSlot[] slots;

    public MultiSlotItemHandler(L9AspectSlot... slots) {
        this.slots = slots;
    }

    @Override
    public int getSlots() {
        return slots.length;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slots[slot].getStackInSlot();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        slots[slot].setStackInSlot(stack);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return slots[slot].insertItem(stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slots[slot].extractItem(amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return slots[slot].getSlotLimit();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return slots[slot].isItemValid(0, stack);
    }

}
