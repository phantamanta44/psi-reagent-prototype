package xyz.phanta.psireagents.client.gui;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import xyz.phanta.psireagents.constant.ResConst;
import xyz.phanta.psireagents.inventory.ContainerReagentHolder;

public class GuiReagentHolder extends L9GuiContainer {

    public GuiReagentHolder(ContainerReagentHolder cont) {
        super(cont, ResConst.GUI_REAGENT_HOLDER.getTexture());
    }

    @Override
    public void drawBackground(float partialTicks, int mX, int mY) {
        super.drawBackground(partialTicks, mX, mY);
        GuiInventory.drawEntityOnScreen(guiLeft + 51, guiTop + 75, 30, 51 - mX, 25 - mY, mc.player);
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        // NO-OP
    }

}
