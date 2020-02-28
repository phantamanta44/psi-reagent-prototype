package xyz.phanta.psireagents.inventory.slot;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

// adapted from vanilla ContainerPlayer
public class SlotArmour extends Slot {

    private static final EntityEquipmentSlot[] ARMOUR_SLOTS = {
            EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET
    };

    private final EntityPlayer player;
    private final EntityEquipmentSlot armourSlot;

    public SlotArmour(EntityPlayer player, int index, int posX, int posY) {
        super(player.inventory, 39 - index, posX, posY);
        this.player = player;
        this.armourSlot = ARMOUR_SLOTS[index];
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem().isValidArmor(stack, armourSlot, player);
    }

    public boolean canTakeStack(EntityPlayer playerIn) {
        ItemStack stack = getStack();
        return (stack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(stack))
                && super.canTakeStack(playerIn);
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public String getSlotTexture() {
        return ItemArmor.EMPTY_SLOT_NAMES[armourSlot.getIndex()];
    }

}
