package xyz.phanta.psireagents.inventory.base;

import io.github.phantamanta44.libnine.gui.L9Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import xyz.phanta.psireagents.tile.base.TileReagentCrafter;

public abstract class ContainerReagentCrafter extends L9Container {

    private final TileReagentCrafter tile;

    public ContainerReagentCrafter(TileReagentCrafter tile, InventoryPlayer ipl) {
        super(ipl, 189);
        this.tile = tile;
        addSlotToContainer(new SlotItemHandler(tile.getReagentHolderSlot(), 0, 80, 75));
    }

    public TileReagentCrafter getCrafter() {
        return tile;
    }

}
