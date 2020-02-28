package xyz.phanta.psireagents.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import xyz.phanta.psireagents.inventory.base.ContainerReagentCrafter;
import xyz.phanta.psireagents.tile.TileReagentWorkbench;

public class ContainerReagentWorkbench extends ContainerReagentCrafter {

    public ContainerReagentWorkbench(TileReagentWorkbench tile, InventoryPlayer ipl) {
        super(tile, ipl);
        addSlotToContainer(new SlotItemHandler(tile.getInputSlot(), 0, 80, 18));
    }

}
