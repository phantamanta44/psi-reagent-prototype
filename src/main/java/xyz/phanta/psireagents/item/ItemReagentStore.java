package xyz.phanta.psireagents.item;

import io.github.phantamanta44.libnine.capability.provider.CapabilityBroker;
import io.github.phantamanta44.libnine.client.model.ParameterizedItemModel;
import io.github.phantamanta44.libnine.item.L9ItemSubs;
import io.github.phantamanta44.libnine.util.format.FormatUtils;
import io.github.phantamanta44.libnine.util.helper.OptUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.phanta.psireagents.ReagentsConfig;
import xyz.phanta.psireagents.capability.impl.ItemStackReagentStore;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.init.ReagentsCaps;
import xyz.phanta.psireagents.reagent.Reagent;

import javax.annotation.Nullable;
import java.util.List;

public class ItemReagentStore extends L9ItemSubs implements ParameterizedItemModel.IParamaterized {

    public ItemReagentStore() {
        super(LangConst.ITEM_REAGENT_STORE, Type.VALUES.length);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        Type type = Type.getForStack(stack);
        return new CapabilityBroker()
                .with(ReagentsCaps.REAGENT_STORE, new ItemStackReagentStore(stack, type.getCapacity(), type.getCostMultiplier()));
    }

    @Override
    public void getModelMutations(ItemStack stack, ParameterizedItemModel.Mutation m) {
        m.mutate("type", Type.getForStack(stack).name());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        // FIXME remove test code
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            OptUtils.capability(stack, ReagentsCaps.REAGENT_STORE).ifPresent(store -> {
                for (Reagent reagent : Reagent.VALUES) {
                    store.injectReagent(reagent, 1000, true);
                }
            });
        } else {
            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0.64F);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        Type type = Type.getForStack(stack);
        tooltip.add(String.format(TextFormatting.GOLD + "%s: " + TextFormatting.GRAY + "%s",
                I18n.format(LangConst.TT_EFFICIENCY), FormatUtils.formatPercentage(type.getEfficiency())));
        tooltip.add(String.format(TextFormatting.GOLD + "%s: " + TextFormatting.GRAY + "%,d",
                I18n.format(LangConst.TT_CAPACITY), type.getCapacity()));
    }

    public enum Type {

        IRON(ReagentsConfig.pouchIron),
        GOLD(ReagentsConfig.pouchGold),
        PSIMETAL(ReagentsConfig.pouchPsimetal),
        EBONY(ReagentsConfig.pouchEbony),
        IVORY(ReagentsConfig.pouchIvory);

        public static final Type[] VALUES = values();

        public static Type getForMeta(int meta) {
            return VALUES[meta];
        }

        public static Type getForStack(ItemStack stack) {
            return getForMeta(stack.getMetadata());
        }

        private final ReagentsConfig.Pouch pouchConfig;

        Type(ReagentsConfig.Pouch pouchConfig) {
            this.pouchConfig = pouchConfig;
        }

        public int getCapacity() {
            return pouchConfig.capacity;
        }

        public float getEfficiency() {
            return (float)pouchConfig.efficiency;
        }

        public float getCostMultiplier() {
            return 1F / getEfficiency();
        }

    }

}
