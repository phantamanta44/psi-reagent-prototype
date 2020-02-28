package xyz.phanta.psireagents.client.gui.base;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import io.github.phantamanta44.libnine.util.math.MathUtils;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.client.gui.component.GuiComponentReagentTank;
import xyz.phanta.psireagents.constant.ResConst;
import xyz.phanta.psireagents.inventory.base.ContainerReagentCrafter;
import xyz.phanta.psireagents.reagent.Reagent;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.tile.base.TileReagentCrafter;

import java.util.Set;

public abstract class GuiReagentCrafter extends L9GuiContainer {

    private static final float SYMBOL_RADIUS = 9F;
    private static final int HALF_SYMBOL_DIMS = 6;

    private final ContainerReagentCrafter cont;

    public GuiReagentCrafter(ContainerReagentCrafter cont) {
        super(cont, ResConst.GUI_REAGENT_WORKBENCH.getTexture(), 176, 189);
        this.cont = cont;
        ReagentStore store = cont.getCrafter().getReagentStore();
        int barY = 17;
        for (Reagent reagent : Reagent.ReagentType.ELEMENTAL.getReagents()) {
            addComponent(new GuiComponentReagentTank(159, barY, store, reagent));
            barY += 19;
        }
        barY = 17;
        for (Reagent reagent : Reagent.ReagentType.ARCHETYPAL.getReagents()) {
            addComponent(new GuiComponentReagentTank(165, barY, store, reagent));
            barY += 19;
        }
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        GlStateManager.color(1F, 1F, 1F, 1F);
        ReagentQuantity output = cont.getCrafter().getCachedOutput();
        if (output != null) {
            Set<Reagent> reagents = output.getNonZeroReagents();
            float dAngle = MathUtils.PI_F * 2F / reagents.size();
            float angle = (System.currentTimeMillis() % 3000) * MathUtils.PI_F / 1500F;
            float x = 88F - HALF_SYMBOL_DIMS, y = 57F - HALF_SYMBOL_DIMS;
            for (Reagent reagent : reagents) {
                ResConst.getReagentSymbol(reagent).draw(
                        x + SYMBOL_RADIUS * MathHelper.cos(angle), y + SYMBOL_RADIUS * MathHelper.sin(angle));
                angle += dAngle;
            }
        }
    }

    protected void drawWorkBar(TextureRegion tex) {
        TileReagentCrafter tile = cont.getCrafter();
        if (tile.canProcessReagent()) {
            tex.drawPartial(70, 38, 0F, 0F, 1F - tile.getWorkDone() / (float)tile.getMaxWork(), 1F);
        }
    }

}
