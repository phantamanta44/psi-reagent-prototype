package xyz.phanta.psireagents.block;

import io.github.phantamanta44.libnine.block.L9Block;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.world.WorldBlockPos;
import io.github.phantamanta44.libnine.util.world.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.init.ReagentsInventories;
import xyz.phanta.psireagents.network.SPacketReagentWorkbenchEffects;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.tile.TileReagentWorkbench;
import xyz.phanta.psireagents.util.WorkResult;

import java.util.ArrayList;
import java.util.List;

public class BlockReagentWorkbench extends L9Block {

    public BlockReagentWorkbench() {
        super(LangConst.BLOCK_REAGENT_WORKBENCH, Material.IRON);
        setHardness(3.5F);
        setTileFactory((w, m) -> new TileReagentWorkbench());
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing face, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (face == EnumFacing.UP) {
                TileReagentWorkbench tile = getTileEntity(world, pos);
                if (tile != null) {
                    WorkResult result = tile.doWork();
                    if (result != WorkResult.NONE) {
                        ReagentQuantity output = tile.getCachedOutput();
                        PsiReagents.INSTANCE.getNetworkHandler().sendToAllAround(
                                new SPacketReagentWorkbenchEffects(
                                        pos, result, output != null ? output : ReagentQuantity.NONE),
                                new NetworkRegistry.TargetPoint(
                                        world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64D));
                    }
                }
            } else {
                PsiReagents.INSTANCE.getGuiHandler().openGui(
                        player, ReagentsInventories.REAGENT_WORKBENCH, new WorldBlockPos(world, pos));
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileReagentWorkbench tile = getTileEntity(world, pos);
        if (tile != null) {
            List<ItemStack> drops = new ArrayList<>();
            tile.accrueDrops(new Accrue<>(drops));
            for (ItemStack dropStack : drops) {
                WorldUtils.dropItem(world, pos, dropStack);
            }
        }
        super.breakBlock(world, pos, state);
    }

}
