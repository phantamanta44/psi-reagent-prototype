package xyz.phanta.psireagents.client.event;

import io.github.phantamanta44.libnine.util.format.TextFormatUtils;
import io.github.phantamanta44.libnine.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.constant.ResConst;
import xyz.phanta.psireagents.reagent.Reagent;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.util.PsiReflect;
import xyz.phanta.psireagents.util.ReagentStoreUtils;
import xyz.phanta.psireagents.util.ScreenSide;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

// partially adapted from Psi's HUDHandler, under the Psi license
// https://github.com/Vazkii/Psi/blob/master/src/main/java/vazkii/psi/client/core/handler/HUDHandler.java
// https://psi.vazkii.net/license.php
public class IngameOverlayHandler {

    private static final int SECONDARY_TEX_UNIT = 0x07;
    private static final float BIG_BAR_TEXT_OFFSET = 4F;
    public static final String INF_STR = "\u221E";

    private static boolean registeredMask = false;

    @SubscribeEvent
    public void onPostRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            ScaledResolution res = event.getResolution();
            float partialTicks = event.getPartialTicks();
            PsiReflect.dispatchHudRenders(res, partialTicks);
            dispatchPsiHudRender(res, partialTicks);
        }
    }

    private static void dispatchPsiHudRender(ScaledResolution res, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        ItemStack cadStack = PsiAPI.getPlayerCAD(player);
        if (!cadStack.isEmpty()) {
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            if (data.level != 0 || player.capabilities.isCreativeMode) {
                int totalPsi = data.getTotalPsi();
                int currPsi = data.getAvailablePsi();
                if (!ConfigHandler.contextSensitiveBar || currPsi != totalPsi
                        || showsBar(data, player.getHeldItemMainhand()) || showsBar(data, player.getHeldItemOffhand())) {
                    GlStateManager.enableBlend();
                    GlStateManager.enableTexture2D();
                    GlStateManager.pushMatrix();
                    int scaleFactor = res.getScaleFactor();
                    if (scaleFactor > ConfigHandler.maxPsiBarScale) {
                        GameSettings settings = mc.gameSettings;
                        int origGuiScale = settings.guiScale;
                        settings.guiScale = ConfigHandler.maxPsiBarScale;
                        res = new ScaledResolution(mc);
                        settings.guiScale = origGuiScale;
                        float unscale = ConfigHandler.maxPsiBarScale / (float)scaleFactor;
                        GlStateManager.scale(unscale, unscale, unscale);
                    }
                    ScreenSide side = ConfigHandler.psiBarOnRight ? ScreenSide.RIGHT : ScreenSide.LEFT;
                    renderMainPsiBar(mc, res, partialTicks, side, cadStack, data);
                    renderReagentBars(res, partialTicks, side, player);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    private static void renderMainPsiBar(Minecraft mc, ScaledResolution res, float partialTicks, ScreenSide side,
                                         ItemStack cadStack, PlayerDataHandler.PlayerData data) {
        ICAD cad = (ICAD)cadStack.getItem();
        int playerPsi = data.availablePsi, cadPsi = cad.getStoredPsi(cadStack), totalPsi = data.getTotalPsi();

        if (!registeredMask) {
            ResConst.GUI_COMP_REAGENT_BAR_MASK.bind();
            ResConst.GUI_COMP_REAGENT_BAR_SHATTER.bind();
            registeredMask = true;
        }

        final int hudWidth = ResConst.GUI_COMP_REAGENT_BAR_BG.getWidth();
        final int hudHeight = ResConst.GUI_COMP_REAGENT_BAR_BG.getHeight();
        int x = side.transformX(1, hudWidth, res);
        int y = res.getScaledHeight() / 2 - hudHeight / 2;
        ResConst.GUI_COMP_REAGENT_BAR_BG.draw(x, y);

        int texture = 0;
        boolean shaders = ShaderHandler.useShaders();
        if (shaders) {
            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + SECONDARY_TEX_UNIT);
            texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        }
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        x += 14;
        y += 20;
        float r = 0.6F, g = 0.65F, b = 1F;
        for (PlayerDataHandler.PlayerData.Deduction deduction : data.deductions) {
            float alpha = deduction.getPercentile(partialTicks);
            GlStateManager.color(r, g, b, alpha);
            float beforeFrac = deduction.current / (float)totalPsi;
            float afterFrac = beforeFrac - deduction.deduct / (float)totalPsi;
            ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(alpha, deduction.shatter, data.overflowed));
            drawBigBar(x, y, 1F - beforeFrac, 1F - afterFrac);
        }

        float playerFrac = totalPsi > 0 ? playerPsi / (float)totalPsi : 0F;
        GlStateManager.color(r, g, b, 1F);
        ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(1F, false, data.overflowed));
        drawBigBar(x, y, 1F - playerFrac, 1F);

        ShaderHandler.releaseShader();
        if (shaders) {
            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + SECONDARY_TEX_UNIT);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
        }

        final int barWidth = ResConst.GUI_COMP_REAGENT_BAR_FG_BIG.getWidth();
        final int barHeight = ResConst.GUI_COMP_REAGENT_BAR_FG_BIG.getHeight();
        FontRenderer fr = mc.fontRenderer;
        GlStateManager.color(1F, 1F, 1F, 1F);
        if (cadPsi != -1) {
            String playerPsiStr = Integer.toString(data.availablePsi);
            String cadPsiStr = Integer.toString(cadPsi);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0F);
            GlStateManager.rotate(90F, 0F, 0F, 1F);
            float posY = (barWidth + fr.FONT_HEIGHT) / -2F + 1F;
            fr.drawStringWithShadow(playerPsiStr, BIG_BAR_TEXT_OFFSET, posY, 0xFFFFFF);
            fr.drawStringWithShadow(cadPsiStr, barHeight - BIG_BAR_TEXT_OFFSET - fr.getStringWidth(cadPsiStr), posY, 0xFFFFFF);
            GlStateManager.popMatrix();
        } else {
            fr.drawStringWithShadow(INF_STR,
                    x + barWidth / 2F - fr.getStringWidth(INF_STR) / 2F,
                    y + barHeight / 2F - fr.FONT_HEIGHT / 2F,
                    0xFFFFFF);
        }
    }

    private static void renderReagentBars(ScaledResolution res, float partialTicks, ScreenSide side, EntityPlayer player) {
        ReagentStore store = ReagentStoreUtils.getEquipped(player).orElse(ReagentStore.EMPTY);
        ReagentQuantity cost = ReagentQuantity.NONE;
        ItemStack costStack = player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack bullet = getEquippedBullet(costStack);
        if (bullet != null) {
            cost = computeSpellCost(bullet, store);
        } else {
            costStack = player.getHeldItem(EnumHand.OFF_HAND);
            bullet = getEquippedBullet(costStack);
            if (bullet != null) {
                cost = computeSpellCost(bullet, store);
            }
        }

        final int hudWidth = ResConst.GUI_COMP_REAGENT_BAR_BG.getWidth();
        final int hudHeight = ResConst.GUI_COMP_REAGENT_BAR_BG.getHeight();
        int x = side.transformX(1, hudWidth, res);
        int y = res.getScaledHeight() / 2 - hudHeight / 2;
        int reagentIndex = 0;
        for (Reagent reagent : Reagent.ReagentType.ELEMENTAL.getReagents()) {
            renderReagentBar(x + 4, y + 20 + 27 * reagentIndex, reagent, store, cost);
            ++reagentIndex;
        }
        reagentIndex = 0;
        for (Reagent reagent : Reagent.ReagentType.ARCHETYPAL.getReagents()) {
            renderReagentBar(x + 34, y + 20 + 27 * reagentIndex, reagent, store, cost);
            ++reagentIndex;
        }
    }

    private static void renderReagentBar(int x, int y, Reagent reagent, ReagentStore store, ReagentQuantity cost) {
        // lower-bound storeCap at 1 so the cost still renders if no store is equipped
        int storeQty = store.getReagentQuantity(reagent), storeCap = Math.max(store.getReagentCapacity(reagent), 1);
        int costQty = cost.getAmount(reagent);
        if (costQty > 0) {
            costQty = Math.min(costQty, storeCap);
            float alphaOsc = MathHelper.sin((System.currentTimeMillis() % 1000) * MathUtils.PI_F / 1000F);
            float storeY = 1F - storeQty / (float)storeCap;
            if (storeQty < costQty) {
                TextFormatUtils.setGlColour(reagent.colour, alphaOsc);
                ResConst.GUI_COMP_REAGENT_BAR_FG_SMALL.drawPartial(x, y, 0F, storeY, 1F, 1F);
                GlStateManager.color(1F, 0F, 0F, alphaOsc);
                ResConst.GUI_COMP_REAGENT_BAR_FG_SMALL
                        .drawPartial(x, y, 0F, storeY - (costQty - storeQty) / (float)storeCap, 1F, storeY);
            } else {
                TextFormatUtils.setGlColour(reagent.colour, 1F);
                float remY = storeY + costQty / (float)storeCap;
                ResConst.GUI_COMP_REAGENT_BAR_FG_SMALL.drawPartial(x, y, 0F, remY, 1F, 1F);
                TextFormatUtils.setGlColour(reagent.colour, alphaOsc);
                ResConst.GUI_COMP_REAGENT_BAR_FG_SMALL.drawPartial(x, y, 0F, storeY, 1F, remY);
            }
        } else if (storeQty > 0) {
            TextFormatUtils.setGlColour(reagent.colour);
            ResConst.GUI_COMP_REAGENT_BAR_FG_SMALL.drawPartial(x, y, 0F, 1F - storeQty / (float)storeCap, 1F, 1F);
        }
    }

    private static boolean showsBar(PlayerDataHandler.PlayerData data, ItemStack stack) {
        return !stack.isEmpty() && IPsiBarDisplay.isDisplay(stack) && IPsiBarDisplay.display(stack).shouldShow(data);
    }

    private static Consumer<Integer> generateCallback(float percentile, boolean shatter, boolean overflowed) {
        return (shader) -> {
            int percentileUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "percentile");
            int overflowedUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "overflowed");
            int imageUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "image");
            int maskUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "mask");

            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
            ResConst.GUI_COMP_REAGENT_BAR.bind();
            ARBShaderObjects.glUniform1iARB(imageUniform, 0);

            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + SECONDARY_TEX_UNIT);
            if (shatter) {
                ResConst.GUI_COMP_REAGENT_BAR_SHATTER.bind();
            } else {
                ResConst.GUI_COMP_REAGENT_BAR_MASK.bind();
            }
            ARBShaderObjects.glUniform1iARB(maskUniform, SECONDARY_TEX_UNIT);
            ARBShaderObjects.glUniform1fARB(percentileUniform, percentile);
            ARBShaderObjects.glUniform1iARB(overflowedUniform, overflowed ? 1 : 0);
        };
    }

    // from libnine TextureRegion
    private static void drawBigBar(int x, int y, float y1, float y2) {
        double xEnd = x + ResConst.GUI_COMP_REAGENT_BAR_FG_BIG.getWidth();
        int height = ResConst.GUI_COMP_REAGENT_BAR_FG_BIG.getHeight();
        double yStart = y + height * y1, yEnd = y + height * y2;
        float du = ResConst.GUI_COMP_REAGENT_BAR_FG_BIG.getUDifferential();
        float uStart = ResConst.GUI_COMP_REAGENT_BAR_FG_BIG.getU1(), uEnd = uStart + du;
        float v1 = ResConst.GUI_COMP_REAGENT_BAR_FG_BIG.getV1();
        float dv = ResConst.GUI_COMP_REAGENT_BAR_FG_BIG.getVDifferential();
        float vStart = v1 + dv * y1, vEnd = v1 + dv * y2;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(x, yEnd, 0D).tex(uStart, vEnd).endVertex();
        buf.pos(xEnd, yEnd, 0D).tex(uEnd, vEnd).endVertex();
        buf.pos(xEnd, yStart, 0D).tex(uEnd, vStart).endVertex();
        buf.pos(x, yStart, 0D).tex(uStart, vStart).endVertex();
        tess.draw();
    }

    @Nullable
    private static ItemStack getEquippedBullet(ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof ISocketable) {
                ISocketable socketableItem = (ISocketable)stack.getItem();
                return socketableItem.getBulletInSocket(stack, socketableItem.getSelectedSlot(stack));
            } else if (stack.hasCapability(ISocketableCapability.CAPABILITY, null)) {
                ISocketableCapability socketable = Objects.requireNonNull(
                        stack.getCapability(ISocketableCapability.CAPABILITY, null));
                return socketable.getBulletInSocket(socketable.getSelectedSlot());
            }
        }
        return null;
    }

    private static ReagentQuantity computeSpellCost(ItemStack bullet, ReagentStore store) {
        return (bullet.getItem() instanceof ISpellContainer ? PsiReagents.PROXY.getPieceCosts()
                .computeTotalCost(((ISpellContainer)bullet.getItem()).getSpell(bullet).grid) : ReagentQuantity.NONE)
                .scale(store.getCostMultiplier());
    }

}
