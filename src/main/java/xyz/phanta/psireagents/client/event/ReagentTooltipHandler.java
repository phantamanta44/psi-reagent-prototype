package xyz.phanta.psireagents.client.event;

import io.github.phantamanta44.libnine.util.function.IIntBiConsumer;
import io.github.phantamanta44.libnine.util.helper.OptUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.psireagents.constant.ResConst;
import xyz.phanta.psireagents.init.ReagentsCaps;
import xyz.phanta.psireagents.reagent.Reagent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ReagentTooltipHandler {

    private static final int Z_INDEX = 300;
    private static final int PADDING_WIDTH = 2, PADDING_HEIGHT = 1;
    private static final int SYMBOL_INNER_DIMS = 12;
    private static final int SYMBOL_OUTER_WIDTH = SYMBOL_INNER_DIMS + 2 * PADDING_WIDTH;
    private static final int SYMBOL_OUTER_HEIGHT = SYMBOL_INNER_DIMS + 2 * PADDING_HEIGHT;
    private static final int SYMBOL_NON_TEXT_WIDTH = SYMBOL_OUTER_WIDTH + PADDING_WIDTH;

    private static int screenWidth;
    // we make the assumption that only one tooltip is rendered simultaneously
    @Nullable
    private static Map<Reagent, String> currentSymbols = null;

    public static void setSymbols(Map<Reagent, String> symbols) {
        currentSymbols = symbols;
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        OptUtils.capability(stack, ReagentsCaps.REAGENT_STORE).ifPresent(store -> {
            currentSymbols = new EnumMap<>(Reagent.class);
            for (Reagent reagent : Reagent.VALUES) {
                if (store.getReagentCapacity(reagent) > 0) {
                    int reagentQty = store.getReagentQuantity(reagent);
                    if (reagentQty == Integer.MAX_VALUE) {
                        currentSymbols.put(reagent, IngameOverlayHandler.INF_STR);
                    } else {
                        currentSymbols.put(reagent, String.format("%,d", reagentQty));
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public void onPrepareRenderTooltip(RenderTooltipEvent.Pre event) {
        screenWidth = event.getScreenWidth();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderTooltipText(RenderTooltipEvent.Color event) {
        if (currentSymbols != null) {
            ReagentRender render = new ReagentRender();
            int dx = 0;
            FontRenderer fr = event.getFontRenderer();
            int colBg = event.getBackground(), colBorderStart = event.getBorderStart(), colBorderEnd = event.getBorderEnd();

            // draw archetypal reagent row
            for (Reagent reagent : Reagent.ReagentType.ARCHETYPAL.getReagents()) {
                String text = currentSymbols.get(reagent);
                if (text != null) {
                    dx += addSymbolRender(render, dx, 0, fr, colBg, reagent, text);
                }
            }

            // make another row for elemental reagents if at least one archetypal reagent is rendered
            int dxPrev, dy;
            if (dx > 0) {
                dxPrev = dx;
                dx = 0;
                dy = -SYMBOL_OUTER_HEIGHT;
            } else {
                dxPrev = -1;
                dy = 0;
            }

            // draw elemental reagent row
            for (Reagent reagent : Reagent.ReagentType.ELEMENTAL.getReagents()) {
                String text = currentSymbols.get(reagent);
                if (text != null) {
                    dx += addSymbolRender(render, dx, dy, fr, colBg, reagent, text);
                }
            }
            currentSymbols = null;

            // check which reagent types are to be drawn
            if (dx == 0) {
                if (dxPrev == -1) { // case: no reagents; just abort
                    return;
                } else { // case: only archetypal
                    dx = dxPrev;
                    dy = 0;
                }
            } else if (dxPrev != -1) { // case: both; fill in remaining bg space for shorter row
                if (dx > dxPrev) {
                    addRectRender(render, dxPrev, 0, dx, SYMBOL_OUTER_HEIGHT, colBg, colBg);
                } else if (dx < dxPrev) {
                    addRectRender(render, dx, -SYMBOL_OUTER_HEIGHT, dxPrev, 0, colBg, colBg);
                    dx = dxPrev;
                }
            } // case: only elemental; nothing to do

            // draw padding
            addRectRender(render, -1, dy - 2, dx + 1, dy, colBg, colBg);
            addRectRender(render, -2, dy - 1, 0, SYMBOL_OUTER_HEIGHT + 1, colBg, colBg);
            addRectRender(render, -1, SYMBOL_OUTER_HEIGHT, dx + 1, SYMBOL_OUTER_HEIGHT + 2, colBg, colBg);
            addRectRender(render, dx, dy - 1, dx + 2, SYMBOL_OUTER_HEIGHT + 1, colBg, colBg);

            // draw border
            addRectRender(render, 0, dy - 1, dx, dy, colBorderStart, colBorderStart);
            addRectRender(render, -1, dy - 1, 0, SYMBOL_OUTER_HEIGHT + 1, colBorderStart, colBorderEnd);
            addRectRender(render, 0, SYMBOL_OUTER_HEIGHT, dx, SYMBOL_OUTER_HEIGHT + 1, colBorderEnd, colBorderEnd);
            addRectRender(render, dx, dy - 1, dx + 1, SYMBOL_OUTER_HEIGHT + 1, colBorderStart, colBorderEnd);

            // dispatch renders
            render.dispatch(event.getX() - 2, event.getY() - 21);
        }
    }

    private static int addSymbolRender(ReagentRender render, int dx, int dy, FontRenderer fr, int colBg,
                                       Reagent reagent, String text) {
        int strW = fr.getStringWidth(text);
        render.add((x, y) -> {
            GlStateManager.color(1F, 1F, 1F, 1F);
            GuiUtils.drawGradientRect(Z_INDEX, x, y, x + strW + SYMBOL_NON_TEXT_WIDTH, y + SYMBOL_OUTER_HEIGHT, colBg, colBg);
            ResConst.getReagentSymbol(reagent).draw(x + PADDING_WIDTH, y + PADDING_HEIGHT);
            fr.drawStringWithShadow(text, x + SYMBOL_OUTER_WIDTH, y + PADDING_HEIGHT + 2, 0xFFFFFF);
        }, dx, dy, strW + SYMBOL_NON_TEXT_WIDTH);
        return strW + SYMBOL_NON_TEXT_WIDTH;
    }

    private static void addRectRender(ReagentRender render, int dx1, int dy1, int dx2, int dy2, int colFrom, int colTo) {
        int ddx = dx2 - dx1, ddy = dy2 - dy1;
        render.add((x, y) -> GuiUtils.drawGradientRect(300, x, y, x + ddx, y + ddy, colFrom, colTo), dx1, dy1, ddx);
    }

    private static class ReagentRender {

        private int maxDx = 0;
        private final List<RenderComponentEntry> compEntries = new ArrayList<>();

        void add(IIntBiConsumer comp, int dx, int dy, int width) {
            if (dx + width > maxDx) {
                maxDx = dx + width;
            }
            compEntries.add(new RenderComponentEntry(comp, dx, dy));
        }

        void dispatch(int x, int y) {
            if (x + maxDx > screenWidth) {
                x = screenWidth - maxDx;
            }
            for (RenderComponentEntry entry : compEntries) {
                entry.render(x, y);
            }
            GlStateManager.color(1F, 1F, 1F, 1F);
        }

        private static class RenderComponentEntry {

            private final IIntBiConsumer comp;
            private final int dx, dy;

            RenderComponentEntry(IIntBiConsumer comp, int dx, int dy) {
                this.comp = comp;
                this.dx = dx;
                this.dy = dy;
            }

            void render(int x, int y) {
                comp.accept(x + dx, y + dy);
            }

        }

    }

}
