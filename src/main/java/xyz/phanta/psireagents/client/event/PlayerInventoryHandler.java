package xyz.phanta.psireagents.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.constant.ResConst;
import xyz.phanta.psireagents.network.CPacketOpenReagentStoreInventory;

public class PlayerInventoryHandler {

    @SubscribeEvent
    public void onGuiOpen(GuiScreenEvent.InitGuiEvent event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiInventory) {
            GuiInventory invGui = (GuiInventory)gui;
            event.getButtonList().add(new ButtonOpenReagentHolder(invGui.getGuiLeft() + 79, invGui.getGuiTop() + 46));
        }
    }

    private static class ButtonOpenReagentHolder extends GuiButton {

        public ButtonOpenReagentHolder(int x, int y) {
            super(69, x, y, 12, 12, TextFormatting.BOLD + "\u03A8");
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                PsiReagents.INSTANCE.getNetworkHandler().sendToServer(new CPacketOpenReagentStoreInventory());
                playPressSound(mc.getSoundHandler());
            }
            return false;
        }

        @Override
        public void drawButton(Minecraft mc, int mX, int mY, float partialTicks) {
            if (visible) {
                GlStateManager.color(1F, 1F, 1F, 1F);
                hovered = mX >= x && mY >= y && mX < x + width && mY < y + height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(
                        GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                if (!enabled) {
                    ResConst.GUI_COMP_SMALL_BUTTON_DISABLED.draw(x, y);
                } else if (hovered) {
                    ResConst.GUI_COMP_SMALL_BUTTON_HOVERED.draw(x, y);
                } else {
                    ResConst.GUI_COMP_SMALL_BUTTON_NORMAL.draw(x, y);
                }
                mouseDragged(mc, mX, mY);
                int textCol = 0xE0E0E0;
                if (packedFGColour != 0) {
                    textCol = packedFGColour;
                } else if (!enabled) {
                    textCol = 0xA0A0A0;
                } else if (hovered) {
                    textCol = 0xFFFFA0;
                }
                drawCenteredString(
                        Minecraft.getMinecraft().fontRenderer, displayString, x + width / 2, y + (height - 8) / 2, textCol);
            }
        }

    }

}
