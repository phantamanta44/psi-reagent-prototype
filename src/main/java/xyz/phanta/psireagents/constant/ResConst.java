package xyz.phanta.psireagents.constant;

import io.github.phantamanta44.libnine.util.ImpossibilityRealizedException;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.libnine.util.render.TextureResource;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.reagent.Reagent;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ResConst {

    private static final String TEXTURES_KEY = "textures/";

    private static final String GUI_KEY = TEXTURES_KEY + "gui/";

    public static final TextureResource GUI_REAGENT_HOLDER
            = PsiReagents.INSTANCE.newTextureResource(GUI_KEY + "reagent_holder.png", 256, 256);

    public static final TextureResource GUI_REAGENT_WORKBENCH
            = PsiReagents.INSTANCE.newTextureResource(GUI_KEY + "reagent_workbench.png", 256, 256);
    public static final TextureRegion GUI_REAGENT_WORKBENCH_WORK = GUI_REAGENT_WORKBENCH.getRegion(176, 0, 36, 1);

    private static final String GUI_COMP_KEY = GUI_KEY + "component/";

    public static final TextureResource GUI_COMP_REAGENT_BAR
            = PsiReagents.INSTANCE.newTextureResource(GUI_COMP_KEY + "reagent_bar.png", 64, 256);
    public static final TextureRegion GUI_COMP_REAGENT_BAR_BG = GUI_COMP_REAGENT_BAR.getRegion(0, 0, 40, 132);
    public static final TextureRegion GUI_COMP_REAGENT_BAR_FG_BIG = GUI_COMP_REAGENT_BAR.getRegion(40, 0, 12, 106);
    public static final TextureRegion GUI_COMP_REAGENT_BAR_FG_SMALL = GUI_COMP_REAGENT_BAR.getRegion(40, 106, 2, 25);
    public static final TextureResource GUI_COMP_REAGENT_BAR_MASK
            = PsiReagents.INSTANCE.newTextureResource(GUI_COMP_KEY + "reagent_bar_mask.png", 64, 256);
    public static final TextureResource GUI_COMP_REAGENT_BAR_SHATTER
            = PsiReagents.INSTANCE.newTextureResource(GUI_COMP_KEY + "reagent_bar_shatter.png", 64, 256);

    public static final TextureResource GUI_COMP_REAGENT_PIE
            = PsiReagents.INSTANCE.newTextureResource(GUI_COMP_KEY + "reagent_pie.png", 24, 24);
    public static final TextureRegion GUI_COMP_REAGENT_PIE_FULL = GUI_COMP_REAGENT_PIE.asRegion();

    private static final TextureResource GUI_COMP_REAGENT_SYMBOL
            = PsiReagents.INSTANCE.newTextureResource(GUI_COMP_KEY + "reagent_symbol.png", 12, 96);
    private static final Map<Reagent, TextureRegion> GUI_COMP_REAGENT_SYMBOL_TABLE = Arrays.stream(Reagent.VALUES)
            .collect(Collectors.toMap(r -> r, r -> GUI_COMP_REAGENT_SYMBOL.getRegion(0, r.ordinal() * 12, 12, 12), (x, y) -> {
                throw new ImpossibilityRealizedException();
            }, () -> new EnumMap<>(Reagent.class)));

    private static final TextureResource GUI_COMP_REAGENT_TANK
            = PsiReagents.INSTANCE.newTextureResource(GUI_COMP_KEY + "reagent_tank.png", 6, 18);
    public static final TextureRegion GUI_COMP_REAGENT_TANK_BG = GUI_COMP_REAGENT_TANK.getRegion(0, 0, 4, 18);
    public static final TextureRegion GUI_COMP_REAGENT_TANK_FG = GUI_COMP_REAGENT_TANK.getRegion(4, 0, 2, 16);

    public static TextureRegion getReagentSymbol(Reagent reagent) {
        return GUI_COMP_REAGENT_SYMBOL_TABLE.get(reagent);
    }

    private static final TextureResource GUI_COMP_SMALL_BUTTON
            = PsiReagents.INSTANCE.newTextureResource(GUI_COMP_KEY + "small_button.png", 12, 36);
    public static final TextureRegion GUI_COMP_SMALL_BUTTON_NORMAL = GUI_COMP_SMALL_BUTTON.getRegion(0, 0, 12, 12);
    public static final TextureRegion GUI_COMP_SMALL_BUTTON_DISABLED = GUI_COMP_SMALL_BUTTON.getRegion(0, 12, 12, 12);
    public static final TextureRegion GUI_COMP_SMALL_BUTTON_HOVERED = GUI_COMP_SMALL_BUTTON.getRegion(0, 24, 12, 12);

}
