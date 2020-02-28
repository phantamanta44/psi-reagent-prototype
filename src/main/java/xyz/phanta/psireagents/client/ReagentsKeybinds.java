package xyz.phanta.psireagents.client;

import io.github.phantamanta44.libnine.InitMe;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.network.CPacketOpenReagentStoreInventory;

public class ReagentsKeybinds {

    private static final String CAT_INVENTORY = "key.categories.inventory";

    private static final KeyBinding OPEN_REAGENT_STORE_INV = new KeyBinding(
            LangConst.KEYBIND_OPEN_REAGENT_STORE_INV, KeyConflictContext.IN_GAME, Keyboard.KEY_O, CAT_INVENTORY);

    @InitMe(sides = { Side.CLIENT })
    public static void init() {
        ClientRegistry.registerKeyBinding(OPEN_REAGENT_STORE_INV);
        MinecraftForge.EVENT_BUS.register(new KeybindListener());
    }

    private static class KeybindListener {

        @SubscribeEvent
        public void onInput(InputEvent event) {
            if (OPEN_REAGENT_STORE_INV.isPressed()) {
                PsiReagents.INSTANCE.getNetworkHandler().sendToServer(new CPacketOpenReagentStoreInventory());
            }
        }

    }

}
