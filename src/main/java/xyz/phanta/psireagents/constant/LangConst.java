package xyz.phanta.psireagents.constant;

import xyz.phanta.psireagents.PsiReagents;

public class LangConst {

    public static final String ITEM_REAGENT_STORE = "reagent_store";
    public static final String ITEM_REAGENT_STORE_CREATIVE = "reagent_store_creative";

    public static final String BLOCK_REAGENT_WORKBENCH = "reagent_workbench";

    public static final String INVENTORY_REAGENT_HOLDER = "reagent_holder";
    public static final String INVENTORY_REAGENT_WORKBENCH = "reagent_workbench";

    public static final String MISC_KEY = PsiReagents.MOD_ID + ".misc.";

    private static final String GUI_KEY = MISC_KEY + "gui.";
    public static final String GUI_REAGENT_WORKBENCH = GUI_KEY + INVENTORY_REAGENT_WORKBENCH;

    private static final String TT_KEY = MISC_KEY + "tooltip.";
    public static final String TT_EFFICIENCY = TT_KEY + "efficiency";
    public static final String TT_CAPACITY = TT_KEY + "capacity";
    public static final String TT_REAGENT_COST = TT_KEY + "reagent_cost";
    public static final String TT_REAGENT_COST_DESC = TT_KEY + "reagent_cost_desc";

    private static final String SPELL_CANCEL_KEY = MISC_KEY + "spell_cancel.";
    public static final String SPELL_CANCEL_MISSING_REAGENTS = SPELL_CANCEL_KEY + "missing_reagents";

    private static final String CMD_FEEDBACK_KEY = MISC_KEY + "cmd_feedback.";
    public static final String CMD_FEEDBACK_RELOADED = CMD_FEEDBACK_KEY + "reloaded";

    private static final String KEYBIND_KEY = MISC_KEY + "keybind.";
    public static final String KEYBIND_OPEN_REAGENT_STORE_INV = KEYBIND_KEY + "reagent_store_inv";

}
