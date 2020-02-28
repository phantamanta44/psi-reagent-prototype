package xyz.phanta.psireagents.network;

import io.github.phantamanta44.libnine.util.helper.OptUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.phanta.psireagents.init.ReagentsCaps;

import javax.annotation.Nullable;

@SuppressWarnings("NotNullFieldNotInitialized")
public class SPacketSyncReagentStoreHolder implements IMessage {

    private ItemStack stack;

    public SPacketSyncReagentStoreHolder(ItemStack stack) {
        this.stack = stack;
    }

    public SPacketSyncReagentStoreHolder() {
        // NO-OP
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
    }

    public static class Handler implements IMessageHandler<SPacketSyncReagentStoreHolder, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(SPacketSyncReagentStoreHolder message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() ->
                    OptUtils.capability(Minecraft.getMinecraft().player, ReagentsCaps.REAGENT_STORE_HOLDER)
                            .ifPresent(inv -> inv.setReagentStoreStack(message.getStack())));
            return null;
        }

    }

}
