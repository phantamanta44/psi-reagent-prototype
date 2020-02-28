package xyz.phanta.psireagents.init;

import io.github.phantamanta44.libnine.InitMe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.block.BlockReagentWorkbench;
import xyz.phanta.psireagents.constant.LangConst;

@SuppressWarnings("NotNullFieldNotInitialized")
public class ReagentsBlocks {

    @GameRegistry.ObjectHolder(PsiReagents.MOD_ID + ":" + LangConst.BLOCK_REAGENT_WORKBENCH)
    public static BlockReagentWorkbench REAGENT_WORKBENCH;

    @InitMe(PsiReagents.MOD_ID)
    public static void init() {
        new BlockReagentWorkbench();
    }

}
