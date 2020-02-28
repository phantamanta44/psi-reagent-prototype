package xyz.phanta.psireagents.capability.impl;

import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.ISerializable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.capability.ReagentStoreHolder;
import xyz.phanta.psireagents.network.SPacketSyncReagentStoreHolder;

public class PlayerReagentStoreHolder implements ReagentStoreHolder, ISerializable {

    private final EntityPlayer player;

    private ItemStack stack = ItemStack.EMPTY;

    public PlayerReagentStoreHolder(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public ItemStack getReagentStoreStack() {
        return stack;
    }

    @Override
    public void setReagentStoreStack(ItemStack stack) {
        this.stack = stack;
        if (player instanceof EntityPlayerMP) {
            PsiReagents.INSTANCE.getNetworkHandler()
                    .sendTo(new SPacketSyncReagentStoreHolder(stack), (EntityPlayerMP)player);
        }
    }

    @Override
    public void serNBT(NBTTagCompound tag) {
        tag.setTag("ReagentStore", stack.serializeNBT());
    }

    @Override
    public void deserNBT(NBTTagCompound tag) {
        stack = new ItemStack(tag.getCompoundTag("ReagentStore"));
    }

    @Override
    public void serBytes(ByteUtils.Writer data) {
        data.writeItemStack(stack);
    }

    @Override
    public void deserBytes(ByteUtils.Reader data) {
        stack = data.readItemStack();
    }

}
