package xyz.phanta.psireagents.init;

import io.github.phantamanta44.libnine.InitMe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.item.ItemReagentStore;
import xyz.phanta.psireagents.item.ItemReagentStoreCreative;

@SuppressWarnings("NotNullFieldNotInitialized")
public class ReagentsItems {

    @GameRegistry.ObjectHolder(PsiReagents.MOD_ID + ":" + LangConst.ITEM_REAGENT_STORE)
    public static ItemReagentStore REAGENT_STORE;
    @GameRegistry.ObjectHolder(PsiReagents.MOD_ID + ":" + LangConst.ITEM_REAGENT_STORE_CREATIVE)
    public static ItemReagentStoreCreative REAGENT_STORE_CREATIVE;

    @InitMe(PsiReagents.MOD_ID)
    public static void init() {
        new ItemReagentStore();
        new ItemReagentStoreCreative();
    }

}
