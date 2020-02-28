package xyz.phanta.psireagents.client.event;

import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
import io.github.phantamanta44.libnine.util.render.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.spell.SpellCompiler;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.constant.LangConst;
import xyz.phanta.psireagents.constant.ResConst;
import xyz.phanta.psireagents.reagent.Reagent;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.util.PieSegment;
import xyz.phanta.psireagents.util.ReagentStoreUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class SpellProgrammerGuiHandler {

    private static final int PIE_DIMS = 12;

    @Nullable
    private SpellCompiler lastKnownCompiler = null;
    @Nullable
    private ReagentQuantity lastKnownCost = null;

    @SubscribeEvent
    public void onDrawGui(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof GuiProgrammer) {
            GuiProgrammer gui = (GuiProgrammer)event.getGui();
            if (gui.compiler != lastKnownCompiler) {
                cacheReagentCosts(gui.compiler);
            }
            int x = gui.left + gui.xSize + 3, y = gui.top + (gui.takingScreenshot ? 40 : 20) + 100;
            if (lastKnownCost != null && lastKnownCompiler != null && !lastKnownCompiler.isErrored()) {
                ReagentStore store = ReagentStoreUtils.getEquipped(Minecraft.getMinecraft().player).orElse(ReagentStore.EMPTY);
                drawCostPie(gui, x, y, store, lastKnownCost, event.getMouseX(), event.getMouseY());
                GlStateManager.color(1F, 1F, 1F, 1F);
            }
        }
    }

    @SubscribeEvent
    public void onCloseGui(PlayerContainerEvent.Close event) {
        lastKnownCompiler = null;
        lastKnownCost = null;
    }

    private void cacheReagentCosts(@Nullable SpellCompiler compiler) {
        lastKnownCompiler = compiler;
        if (compiler != null && !compiler.isErrored()) {
            CompiledSpell spell = compiler.getCompiledSpell();
            lastKnownCost = PsiReagents.PROXY.getPieceCosts().computeTotalCost(spell.sourceSpell.grid);
        } else {
            lastKnownCost = null;
        }
    }

    private static void drawCostPie(GuiProgrammer gui, int x, int y, ReagentStore store, ReagentQuantity cost, int mX, int mY) {
        int totalCost = 0;
        for (Reagent reagent : Reagent.VALUES) {
            totalCost += cost.getAmount(reagent);
        }
        if (totalCost == 0) {
            drawEmptyCostPie(gui, x, y, mX, mY);
        } else {
            ResConst.GUI_COMP_REAGENT_PIE.bind();
            float pieFilled = 0;
            for (Reagent reagent : Reagent.VALUES) {
                int reagentCost = cost.getAmount(reagent);
                if (reagentCost > 0) {
                    float nextPieFilled = pieFilled + reagentCost / (float)totalCost;
                    drawPieSegment(x, y, pieFilled, nextPieFilled, reagent);
                    pieFilled = nextPieFilled;
                }
            }

            ReagentQuantity scaledCost;
            int totalScaledCost;
            float storeEff = store.getCostMultiplier();
            if (storeEff == 1F) {
                scaledCost = cost;
                totalScaledCost = totalCost;
            } else {
                scaledCost = cost.scale(storeEff);
                totalScaledCost = 0;
                for (Reagent reagent : Reagent.VALUES) {
                    totalScaledCost += scaledCost.getAmount(reagent);
                }
            }

            Minecraft.getMinecraft().fontRenderer.drawString(String.format("%d (%d)", totalCost, totalScaledCost),
                    x + 16, y + 2, isStoreLargeEnough(store, scaledCost) ? 0xFFFFFF : 0xFF6666);

            if (GuiUtils.isMouseOver(x, y, PIE_DIMS, PIE_DIMS, mX, mY)) {
                setUpCostTooltip(store, cost, scaledCost);
                drawReagentCostTooltip(gui, mX, mY);
            }
        }
    }

    private static void drawPieSegment(int x, int y, float perFrom, float perTo, Reagent reagent) {
        TextFormatUtils.setGlColour(reagent.colour);
        GL11.glFrontFace(GL11.GL_CW);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_TEX);

        vertex(buf, x, y, new Vec2f(0.5F, 0.5F));
        PieSegment seg = PieSegment.get(perFrom);
        vertex(buf, x, y, seg.fracToSquare(perFrom));
        PieSegment lastSeg = PieSegment.get(perTo);
        while (seg != lastSeg) {
            seg = seg.next();
            vertex(buf, x, y, seg.boundPoint);
        }
        vertex(buf, x, y, lastSeg.fracToSquare(perTo));

        tess.draw();
        GL11.glFrontFace(GL11.GL_CCW);
    }

    private static void drawEmptyCostPie(GuiProgrammer gui, int x, int y, int mX, int mY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        ResConst.GUI_COMP_REAGENT_PIE_FULL.draw(x, y, PIE_DIMS, PIE_DIMS);
        Minecraft.getMinecraft().fontRenderer.drawString("0", x + 16, y + 2, 0xFFFFFF);
        if (GuiUtils.isMouseOver(x, y, PIE_DIMS, PIE_DIMS, mX, mY)) {
            drawReagentCostTooltip(gui, mX, mY);
        }
    }

    private static boolean isStoreLargeEnough(ReagentStore store, ReagentQuantity cost) {
        for (Reagent reagent : Reagent.VALUES) {
            if (store.getReagentCapacity(reagent) < cost.getAmount(reagent)) {
                return false;
            }
        }
        return true;
    }

    private static void setUpCostTooltip(ReagentStore store, ReagentQuantity cost, ReagentQuantity scaledCost) {
        Map<Reagent, String> symbols = new EnumMap<>(Reagent.class);
        for (Reagent reagent : Reagent.VALUES) {
            int reagentCost = cost.getAmount(reagent);
            if (reagentCost > 0) {
                int reagentScaledCost = scaledCost.getAmount(reagent);
                if (reagentScaledCost > store.getReagentCapacity(reagent)) {
                    symbols.put(reagent, TextFormatting.RED + String.format("%,d (%,d)", reagentCost, reagentScaledCost));
                } else {
                    symbols.put(reagent, String.format("%,d (%,d)", reagentCost, reagentScaledCost));
                }
            }
        }
        ReagentTooltipHandler.setSymbols(symbols);
    }

    private static void drawReagentCostTooltip(GuiProgrammer gui, int mX, int mY) {
        gui.drawHoveringText(Arrays.asList(
                TextFormatting.AQUA + I18n.format(LangConst.TT_REAGENT_COST),
                TextFormatting.GRAY + I18n.format(LangConst.TT_REAGENT_COST_DESC)
        ), mX, mY);
    }

    private static void vertex(BufferBuilder buf, int x, int y, Vec2f vec) {
        buf.pos(x + PIE_DIMS * vec.x, y + PIE_DIMS * vec.y, 0D).tex(vec.x, vec.y).endVertex();
    }

}
