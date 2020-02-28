package xyz.phanta.psireagents.inventory.slot;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// adapted from vanilla ContainerPlayer
public class SlotOffHand extends Slot {

    public SlotOffHand(InventoryPlayer inv, int posX, int posY) {
        super(inv, 40, posX, posY);
    }

    @SideOnly(Side.CLIENT)
    public String getSlotTexture() {
        return "minecraft:items/empty_armor_slot_shield";
    }

}
