package xyz.phanta.psireagents.inventory;

import io.github.phantamanta44.libnine.gui.L9Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import xyz.phanta.psireagents.init.ReagentsCaps;
import xyz.phanta.psireagents.inventory.slot.SlotArmour;
import xyz.phanta.psireagents.inventory.slot.SlotOffHand;
import xyz.phanta.psireagents.inventory.slot.SlotReagentStore;

import java.util.Objects;

public class ContainerReagentHolder extends L9Container {

    public ContainerReagentHolder(EntityPlayer player) {
        super(player.inventory);
        addSlotToContainer(new SlotReagentStore(
                Objects.requireNonNull(player.getCapability(ReagentsCaps.REAGENT_STORE_HOLDER, null)), 116, 35));
        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SlotArmour(player, i, 8, 8 + i * 18));
        }
        addSlotToContainer(new SlotOffHand(player.inventory, 77, 62));
    }

}
