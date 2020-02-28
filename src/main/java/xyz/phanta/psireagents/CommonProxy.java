package xyz.phanta.psireagents;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import xyz.phanta.psireagents.event.PlayerReagentStoreHandler;
import xyz.phanta.psireagents.event.SpellCastHandler;
import xyz.phanta.psireagents.network.CPacketOpenReagentStoreInventory;
import xyz.phanta.psireagents.network.SPacketReagentWorkbenchEffects;
import xyz.phanta.psireagents.network.SPacketSyncReagentStoreHolder;
import xyz.phanta.psireagents.piececost.PieceCostProviderFactoryRegistry;
import xyz.phanta.psireagents.piececost.PieceCostRegistry;

import javax.annotation.Nullable;

public class CommonProxy {

    @Nullable
    private PieceCostProviderFactoryRegistry costProviderRegistry = null;
    @Nullable
    private PieceCostRegistry costRegistry = null;

    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerReagentStoreHandler());
        MinecraftForge.EVENT_BUS.register(new SpellCastHandler());
        costProviderRegistry = new PieceCostProviderFactoryRegistry();
        costRegistry = new PieceCostRegistry(event.getModConfigurationDirectory().toPath());
        costRegistry.reloadFromDisk(true);
        PsiReagents.INSTANCE.getNetworkHandler().registerMessage(
                new SPacketSyncReagentStoreHolder.Handler(), SPacketSyncReagentStoreHolder.class, 0, Side.CLIENT);
        PsiReagents.INSTANCE.getNetworkHandler().registerMessage(
                new CPacketOpenReagentStoreInventory.Handler(), CPacketOpenReagentStoreInventory.class, 1, Side.SERVER);
        PsiReagents.INSTANCE.getNetworkHandler().registerMessage(
                new SPacketReagentWorkbenchEffects.Handler(), SPacketReagentWorkbenchEffects.class, 2, Side.CLIENT);
    }

    public void onInit(FMLInitializationEvent event) {
        // NO-OP
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        // NO-OP
    }

    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new PsiReagentsCommand());
    }

    public PieceCostProviderFactoryRegistry getPieceCostProviders() {
        if (costProviderRegistry == null) {
            throw new IllegalStateException("Piece cost provider factory registry not initialized!");
        }
        return costProviderRegistry;
    }

    public PieceCostRegistry getPieceCosts() {
        if (costRegistry == null) {
            throw new IllegalStateException("Piece cost registry not initialized!");
        }
        return costRegistry;
    }

}
