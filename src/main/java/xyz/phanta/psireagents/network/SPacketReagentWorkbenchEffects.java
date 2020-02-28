package xyz.phanta.psireagents.network;

import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.common.Psi;
import xyz.phanta.psireagents.reagent.Reagent;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.util.WorkResult;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("NotNullFieldNotInitialized")
public class SPacketReagentWorkbenchEffects implements IMessage {

    private BlockPos pos;
    private WorkResult result;
    private ReagentQuantity reagents;

    public SPacketReagentWorkbenchEffects(BlockPos pos, WorkResult result, ReagentQuantity reagents) {
        this.pos = pos;
        this.result = result;
        this.reagents = reagents;
    }

    public SPacketReagentWorkbenchEffects() {
        // NO-OP
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        result = WorkResult.VALUES[buf.readInt()];
        reagents = ReagentQuantity.deserializeByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX()).writeInt(pos.getY()).writeInt(pos.getZ()).writeInt(result.ordinal());
        reagents.serializeByteBuf(buf);
    }

    public static class Handler implements IMessageHandler<SPacketReagentWorkbenchEffects, IMessage> {

        private static final int HIT_PARTICLE_DENSITY = 6, REAGENT_PARTICLE_DENSITY = 14;
        private static final Random rand = new Random();

        @Nullable
        @Override
        public IMessage onMessage(SPacketReagentWorkbenchEffects message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            //noinspection Convert2Lambda
            mc.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    BlockPos blockPos = message.pos;
                    double x = blockPos.getX() + 0.5D, y = blockPos.getY() + 1.15D, z = blockPos.getZ() + 0.5D;
                    switch (message.result) {
                        case WORK_DONE:
                            mc.world.playSound(x, y, z, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1F, 1.25F, false);
                            for (int i = 0; i < HIT_PARTICLE_DENSITY; i++) {
                                mc.world.spawnParticle(EnumParticleTypes.CRIT, x, y, z,
                                        0.5F * rand.nextGaussian(), 0D, 0.5F * rand.nextGaussian());
                            }
                            break;
                        case WORK_FINISHED:
                            mc.world.playSound(x, y, z, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1F, 1F, false);
                            for (int i = 0; i < HIT_PARTICLE_DENSITY; i++) {
                                mc.world.spawnParticle(EnumParticleTypes.CRIT, x, y, z,
                                        0.5F * rand.nextGaussian(), 0D, 0.5F * rand.nextGaussian());
                            }
                            Reagent[] reagents = message.reagents.getNonZeroReagents().toArray(new Reagent[0]);
                            for (int i = 0; i < REAGENT_PARTICLE_DENSITY; i++) {
                                int colour = TextFormatUtils.getTextColour(reagents[rand.nextInt(reagents.length)].colour);
                                Psi.proxy.sparkleFX(x, y, z,
                                        TextFormatUtils.getComponent(colour, 2),
                                        TextFormatUtils.getComponent(colour, 1),
                                        TextFormatUtils.getComponent(colour, 0),
                                        0.15F * (float)rand.nextGaussian(),
                                        0.05F * (float)rand.nextGaussian(),
                                        0.15F * (float)rand.nextGaussian(),
                                        2, 4);
                            }
                            break;
                    }
                }
            });
            return null;
        }

    }

}
