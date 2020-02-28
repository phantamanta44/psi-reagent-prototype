package xyz.phanta.psireagents.client.gui.component;

import io.github.phantamanta44.libnine.client.gui.component.GuiComponent;
import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
import net.minecraft.client.renderer.GlStateManager;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.client.event.ReagentTooltipHandler;
import xyz.phanta.psireagents.constant.ResConst;
import xyz.phanta.psireagents.reagent.Reagent;

import java.util.Collections;

public class GuiComponentReagentTank extends GuiComponent {

    private final ReagentStore store;
    private final Reagent reagent;

    public GuiComponentReagentTank(int x, int y, ReagentStore store, Reagent reagent) {
        super(x, y, 4, 18);
        this.store = store;
        this.reagent = reagent;
    }

    @Override
    public void render(float partialTicks, int mX, int mY, boolean mouseOver) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        ResConst.GUI_COMP_REAGENT_TANK_BG.draw(x, y);
        TextFormatUtils.setGlColour(reagent.colour);
        ResConst.GUI_COMP_REAGENT_TANK_FG.drawPartial(x + 1, y + 1,
                0F, 1F - store.getReagentQuantity(reagent) / (float)store.getReagentCapacity(reagent), 1F, 1F);
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public void renderTooltip(float partialTicks, int mX, int mY) {
        ReagentTooltipHandler.setSymbols(Collections.singletonMap(reagent,
                String.format("%,d / %,d", store.getReagentQuantity(reagent), store.getReagentCapacity(reagent))));
        drawTooltip(reagent.colour + reagent.getLocalizedName(), mX, mY);
    }

}
