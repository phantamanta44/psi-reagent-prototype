package xyz.phanta.psireagents.init;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.gui.GuiIdentity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.client.gui.GuiReagentHolder;
import xyz.phanta.psireagents.client.gui.GuiReagentWorkbench;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.inventory.ContainerReagentHolder;
import xyz.phanta.psireagents.inventory.ContainerReagentWorkbench;

import java.util.Objects;

public class ReagentsInventories {

    public static final GuiIdentity<ContainerReagentHolder, GuiReagentHolder> REAGENT_HOLDER
            = new GuiIdentity<>(LangConst.INVENTORY_REAGENT_HOLDER, ContainerReagentHolder.class);
    public static final GuiIdentity<ContainerReagentWorkbench, GuiReagentWorkbench> REAGENT_WORKBENCH
            = new GuiIdentity<>(LangConst.INVENTORY_REAGENT_WORKBENCH, ContainerReagentWorkbench.class);

    @InitMe
    public static void init() {
        PsiReagents.INSTANCE.getGuiHandler().registerServerGui(REAGENT_HOLDER,
                (p, w, x, y, z) -> new ContainerReagentHolder(p));
        PsiReagents.INSTANCE.getGuiHandler().registerServerGui(REAGENT_WORKBENCH,
                (p, w, x, y, z) -> new ContainerReagentWorkbench(getTileUnchecked(w, x, y, z), p.inventory));
    }

    @SideOnly(Side.CLIENT)
    @InitMe(sides = { Side.CLIENT })
    public static void initClient() {
        PsiReagents.INSTANCE.getGuiHandler().registerClientGui(REAGENT_HOLDER,
                (c, p, w, x, y, z) -> new GuiReagentHolder(c));
        PsiReagents.INSTANCE.getGuiHandler().registerClientGui(REAGENT_WORKBENCH,
                (c, p, w, x, y, z) -> new GuiReagentWorkbench(c));
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> T getTileUnchecked(World world, int x, int y, int z) {
        return (T)Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z)));
    }

}
