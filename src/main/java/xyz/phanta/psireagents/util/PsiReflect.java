package xyz.phanta.psireagents.util;

import io.github.phantamanta44.libnine.util.helper.MirrorUtils;
import net.minecraft.client.gui.ScaledResolution;
import vazkii.psi.client.core.handler.HUDHandler;

public class PsiReflect {

    private static final MirrorUtils.IMethod<Void> HUDHandler_renderSocketableEquippedName = MirrorUtils.reflectMethod(
            HUDHandler.class, "renderSocketableEquippedName", ScaledResolution.class, Float.TYPE);
    private static final MirrorUtils.IMethod<Void> HUDHandler_renderLevelUpIndicator = MirrorUtils.reflectMethod(
            HUDHandler.class, "renderLevelUpIndicator", ScaledResolution.class);
    private static final MirrorUtils.IMethod<Void> HUDHandler_renderRemainingItems = MirrorUtils.reflectMethod(
            HUDHandler.class, "renderRemainingItems", ScaledResolution.class, Float.TYPE);
    private static final MirrorUtils.IMethod<Void> HUDHandler_renderHUDItem = MirrorUtils.reflectMethod(
            HUDHandler.class, "renderHUDItem", ScaledResolution.class, Float.TYPE);

    public static void dispatchHudRenders(ScaledResolution res, float partialTicks) {
        HUDHandler_renderSocketableEquippedName.invoke(null, res, partialTicks);
        HUDHandler_renderLevelUpIndicator.invoke(null, res);
        HUDHandler_renderRemainingItems.invoke(null, res, partialTicks);
        HUDHandler_renderHUDItem.invoke(null, res, partialTicks);
    }

}
