package xyz.phanta.psireagents.network;

import io.github.phantamanta44.libnine.util.world.WorldBlockPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.init.ReagentsInventories;

import javax.annotation.Nullable;

public class CPacketOpenReagentStoreInventory implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {
        // NO-OP
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // NO-OP
    }

    public static class Handler implements IMessageHandler<CPacketOpenReagentStoreInventory, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(CPacketOpenReagentStoreInventory message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            //noinspection Convert2Lambda
            ((WorldServer)player.world).addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PsiReagents.INSTANCE.getGuiHandler().openGui(
                            player, ReagentsInventories.REAGENT_HOLDER, new WorldBlockPos(player.world, 0, 0, 0));
                }
            });
            return null;
        }

    }

}
