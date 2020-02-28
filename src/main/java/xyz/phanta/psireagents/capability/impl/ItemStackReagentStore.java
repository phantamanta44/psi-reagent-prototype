package xyz.phanta.psireagents.capability.impl;

import io.github.phantamanta44.libnine.util.helper.OptUtils;
import io.github.phantamanta44.libnine.util.nbt.ChainingTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.reagent.Reagent;

import java.util.Objects;

public class ItemStackReagentStore implements ReagentStore {

    private final ItemStack stack;
    private final int capacity;
    private final float costMultiplier;

    public ItemStackReagentStore(ItemStack stack, int capacity, float costMultiplier) {
        this.stack = stack;
        this.capacity = capacity;
        this.costMultiplier = costMultiplier;
    }

    @Override
    public int getReagentQuantity(Reagent reagent) {
        return OptUtils.stackTag(stack)
                .map(tag -> tag.getInteger(reagent.uniqueKey))
                .orElse(0);
    }

    @Override
    public int getReagentCapacity(Reagent reagent) {
        return capacity;
    }

    @Override
    public int extractReagent(Reagent reagent, int maxQty, boolean commit) {
        return OptUtils.stackTag(stack)
                .map(tag -> {
                    int qty = tag.getInteger(reagent.uniqueKey);
                    if (qty <= 0) {
                        return 0;
                    }
                    int toTransfer = Math.min(qty, maxQty);
                    if (commit) {
                        tag.setInteger(reagent.uniqueKey, qty - toTransfer);
                    }
                    return toTransfer;
                })
                .orElse(0);
    }

    @Override
    public int injectReagent(Reagent reagent, int maxQty, boolean commit) {
        if (maxQty <= 0) {
            return 0;
        }
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = Objects.requireNonNull(stack.getTagCompound());
            int qty = tag.getInteger(reagent.uniqueKey);
            int toTransfer = Math.min(capacity - qty, maxQty);
            if (commit) {
                tag.setInteger(reagent.uniqueKey, qty + toTransfer);
            }
            return maxQty - toTransfer;
        } else {
            int toTransfer = Math.min(maxQty, capacity);
            if (commit) {
                stack.setTagCompound(new ChainingTagCompound().withInt(reagent.uniqueKey, toTransfer));
            }
            return maxQty - toTransfer;
        }
    }

    @Override
    public float getCostMultiplier() {
        return costMultiplier;
    }

}
