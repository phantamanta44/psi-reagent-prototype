package xyz.phanta.psireagents.client.render;

import io.github.phantamanta44.libnine.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import xyz.phanta.psireagents.tile.TileReagentWorkbench;

public class RenderReagentWorkbench extends TileEntitySpecialRenderer<TileReagentWorkbench> {

    @Override
    public void render(TileReagentWorkbench tile, double x, double y, double z, float partialTicks,
                       int destroyStage, float alpha) {
        ItemStack stack = tile.getInputSlot().getStackInSlot();
        if (!stack.isEmpty()) {
            Minecraft mc = Minecraft.getMinecraft();
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5D, y + 1.015625D, z + 0.375D);
            GlStateManager.rotate(90F, 1F, 0F, 0F);
            mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
    }

}
