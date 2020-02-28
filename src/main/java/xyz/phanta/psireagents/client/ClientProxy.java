package xyz.phanta.psireagents.client;

import io.github.phantamanta44.libnine.LibNine;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.psi.client.core.handler.HUDHandler;
import xyz.phanta.psireagents.CommonProxy;
import xyz.phanta.psireagents.client.event.IngameOverlayHandler;
import xyz.phanta.psireagents.client.event.PlayerInventoryHandler;
import xyz.phanta.psireagents.client.event.ReagentTooltipHandler;
import xyz.phanta.psireagents.client.event.SpellProgrammerGuiHandler;
import xyz.phanta.psireagents.client.render.RenderReagentWorkbench;
import xyz.phanta.psireagents.tile.TileReagentWorkbench;

public class ClientProxy extends CommonProxy {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        MinecraftForge.EVENT_BUS.register(new IngameOverlayHandler());
        MinecraftForge.EVENT_BUS.register(new ReagentTooltipHandler());
        MinecraftForge.EVENT_BUS.register(new SpellProgrammerGuiHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerInventoryHandler());
        LibNine.PROXY.getRegistrar().queueTESRReg(TileReagentWorkbench.class, new RenderReagentWorkbench());
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
        MinecraftForge.EVENT_BUS.unregister(HUDHandler.class); // we're gonna hijack this
    }

}
