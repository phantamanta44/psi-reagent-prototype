package xyz.phanta.psireagents.capability;

import io.github.phantamanta44.libnine.util.data.INbtSerializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WritableCapabilityProvider<C extends INbtSerializable>
        implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    private final Capability<?> capability;
    private final C instance;

    public WritableCapabilityProvider(Capability<? super C> capability, C instance) {
        this.capability = capability;
        this.instance = instance;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == this.capability;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        //noinspection unchecked
        return capability == this.capability ? (T)instance : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        instance.serNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        instance.deserNBT(tag);
    }

}
