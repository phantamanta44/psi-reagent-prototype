package xyz.phanta.psireagents.client.gui;

import net.minecraft.client.resources.I18n;
import xyz.phanta.psireagents.client.gui.base.GuiReagentCrafter;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.constant.ResConst;
import xyz.phanta.psireagents.inventory.ContainerReagentWorkbench;

public class GuiReagentWorkbench extends GuiReagentCrafter {

    public GuiReagentWorkbench(ContainerReagentWorkbench cont) {
        super(cont);
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.GUI_REAGENT_WORKBENCH));
        drawWorkBar(ResConst.GUI_REAGENT_WORKBENCH_WORK);
    }

}
