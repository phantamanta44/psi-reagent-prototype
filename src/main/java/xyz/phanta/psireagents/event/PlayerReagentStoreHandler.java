package xyz.phanta.psireagents.event;

import io.github.phantamanta44.libnine.util.helper.OptUtils;
import io.github.phantamanta44.libnine.util.world.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.capability.WritableCapabilityProvider;
import xyz.phanta.psireagents.capability.impl.PlayerReagentStoreHolder;
import xyz.phanta.psireagents.init.ReagentsCaps;
import xyz.phanta.psireagents.network.SPacketSyncReagentStoreHolder;

public class PlayerReagentStoreHandler {

    private static final ResourceLocation CAP_ID = PsiReagents.INSTANCE.newResourceLocation("player_reagent_store_inv");

    @SubscribeEvent
    public void onPlayerCapabilityAttach(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntityPlayer) {
            event.addCapability(CAP_ID, new WritableCapabilityProvider<>(
                    ReagentsCaps.REAGENT_STORE_HOLDER, new PlayerReagentStoreHolder((EntityPlayer)entity)));
        }
    }

    @SubscribeEvent
    public void onPlayerConnect(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP) {
            OptUtils.capability(entity, ReagentsCaps.REAGENT_STORE_HOLDER).ifPresent(inv ->
                    PsiReagents.INSTANCE.getNetworkHandler().sendTo(
                            new SPacketSyncReagentStoreHolder(inv.getReagentStoreStack()), (EntityPlayerMP)entity));
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        OptUtils.capability(event.getOriginal(), ReagentsCaps.REAGENT_STORE_HOLDER).ifPresent(invOrig ->
                OptUtils.capability(event.getEntityPlayer(), ReagentsCaps.REAGENT_STORE_HOLDER).ifPresent(invNew ->
                        invNew.setReagentStoreStack(invOrig.getReagentStoreStack())));
    }

    @SubscribeEvent
    public void onPlayerDropItems(PlayerDropsEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.world.isRemote && !player.world.getGameRules().getBoolean("keepInventory")) {
            OptUtils.capability(player, ReagentsCaps.REAGENT_STORE_HOLDER).ifPresent(inv -> {
                ItemStack stack = inv.getReagentStoreStack();
                if (!stack.isEmpty()) {
                    WorldUtils.dropItem(player.world, player.getPosition().add(0D, player.getEyeHeight() - 0.3D, 0D), stack);
                    inv.setReagentStoreStack(ItemStack.EMPTY);
                }
            });
        }
    }

}
