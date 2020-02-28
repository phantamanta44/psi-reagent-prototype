package xyz.phanta.psireagents.tile;

import io.github.phantamanta44.libnine.capability.impl.L9AspectSlot;
import io.github.phantamanta44.libnine.capability.provider.CapabilityBrokerDirectional;
import io.github.phantamanta44.libnine.tile.RegisterTile;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.tile.base.TileReagentCrafter;

@RegisterTile(PsiReagents.MOD_ID)
public class TileReagentWorkbench extends TileReagentCrafter {

    @Override
    protected ICapabilityProvider initCapabilities() {
        L9AspectSlot reagentHolderSlot = getReagentHolderSlot();
        return new CapabilityBrokerDirectional()
                .with(EnumFacing.UP, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getInputSlot())
                .with(EnumFacing.NORTH, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, reagentHolderSlot)
                .with(EnumFacing.EAST, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, reagentHolderSlot)
                .with(EnumFacing.SOUTH, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, reagentHolderSlot)
                .with(EnumFacing.WEST, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, reagentHolderSlot)
                .with(EnumFacing.DOWN, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, reagentHolderSlot);
    }

    @Override
    public int getMaxWork() {
        return 3;
    }

}
