package xyz.phanta.psireagents.capability.impl;

import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.ISerializable;
import io.github.phantamanta44.libnine.util.function.IIntBiConsumer;
import net.minecraft.nbt.NBTTagCompound;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.reagent.Reagent;

import java.util.function.Consumer;

public class SimpleReagentStore implements ReagentStore, ISerializable {

    private final int capacity;
    private final int[] reservoirs = new int[Reagent.VALUES.length];

    public SimpleReagentStore(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int getReagentQuantity(Reagent reagent) {
        return reservoirs[reagent.ordinal()];
    }

    @Override
    public int getReagentCapacity(Reagent reagent) {
        return capacity;
    }

    @Override
    public int extractReagent(Reagent reagent, int maxQty, boolean commit) {
        int index = reagent.ordinal();
        int toTransfer = Math.min(reservoirs[index], maxQty);
        if (commit) {
            reservoirs[index] -= toTransfer;
        }
        return toTransfer;
    }

    @Override
    public int injectReagent(Reagent reagent, int maxQty, boolean commit) {
        int index = reagent.ordinal();
        int toTransfer = Math.min(capacity - reservoirs[index], maxQty);
        if (commit) {
            reservoirs[index] += toTransfer;
        }
        return toTransfer;
    }

    @Override
    public void serBytes(ByteUtils.Writer data) {
        for (int reservoir : reservoirs) {
            data.writeInt(reservoir);
        }
    }

    @Override
    public void deserBytes(ByteUtils.Reader data) {
        for (int i = 0; i < reservoirs.length; i++) {
            reservoirs[i] = data.readInt();
        }
    }

    @Override
    public void serNBT(NBTTagCompound tag) {
        for (int i = 0; i < reservoirs.length; i++) {
            tag.setInteger(Reagent.VALUES[i].name(), reservoirs[i]);
        }
    }

    @Override
    public void deserNBT(NBTTagCompound tag) {
        for (String reagentName : tag.getKeySet()) {
            try {
                reservoirs[Reagent.valueOf(reagentName).ordinal()] = tag.getInteger(reagentName);
            } catch (IllegalArgumentException ignored) {
                // NO-OP
            }
        }
    }

    public static class Observable extends SimpleReagentStore {

        private final Consumer<Reagent> observer;

        public Observable(int capacity, Consumer<Reagent> observer) {
            super(capacity);
            this.observer = observer;
        }

        @Override
        public int extractReagent(Reagent reagent, int maxQty, boolean commit) {
            int extracted = super.extractReagent(reagent, maxQty, commit);
            if (commit && extracted != 0) {
                observer.accept(reagent);
            }
            return extracted;
        }

        @Override
        public int injectReagent(Reagent reagent, int maxQty, boolean commit) {
            int remainder = super.injectReagent(reagent, maxQty, commit);
            if (commit && remainder != maxQty) {
                observer.accept(reagent);
            }
            return remainder;
        }

    }

}
