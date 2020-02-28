package xyz.phanta.psireagents.item;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.item.L9Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.phanta.psireagents.capability.impl.InfiniteReagentStore;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.init.ReagentsCaps;

import javax.annotation.Nullable;

public class ItemReagentStoreCreative extends L9Item {

    public ItemReagentStoreCreative() {
        super(LangConst.ITEM_REAGENT_STORE_CREATIVE);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CapabilityBroker().with(ReagentsCaps.REAGENT_STORE, new InfiniteReagentStore());
    }

}
