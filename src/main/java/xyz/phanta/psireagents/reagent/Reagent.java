package xyz.phanta.psireagents.reagent;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import xyz.phanta.psireagents.constant.LangConst;

import javax.annotation.Nullable;
import java.util.*;

public enum Reagent {

    ELEM_FIRE(ReagentType.ELEMENTAL, TextFormatting.RED),
    ELEM_WATER(ReagentType.ELEMENTAL, TextFormatting.AQUA),
    ELEM_EARTH(ReagentType.ELEMENTAL, TextFormatting.DARK_GREEN),
    ELEM_AIR(ReagentType.ELEMENTAL, TextFormatting.GRAY),

    ARCH_CONJURE(ReagentType.ARCHETYPAL, TextFormatting.BLUE),
    ARCH_MODIFY(ReagentType.ARCHETYPAL, TextFormatting.YELLOW),
    ARCH_GROWTH(ReagentType.ARCHETYPAL, TextFormatting.LIGHT_PURPLE),
    ARCH_DECAY(ReagentType.ARCHETYPAL, TextFormatting.DARK_GRAY);

    public static final Reagent[] VALUES = values();

    @Nullable
    private static Map<String, Reagent> keyMapping;

    @Nullable
    public static Reagent getByKey(String uniqueKey) {
        if (keyMapping == null) {
            keyMapping = new HashMap<>();
            for (Reagent reagent : VALUES) {
                keyMapping.put(reagent.uniqueKey, reagent);
            }
        }
        return keyMapping.get(uniqueKey);
    }

    public final ReagentType type;
    public final TextFormatting colour;
    public final String uniqueKey;
    public final String langKey;

    Reagent(ReagentType type, TextFormatting colour) {
        this.type = type;
        this.colour = colour;
        this.uniqueKey = "reagent_" + name().toLowerCase();
        this.langKey = LangConst.MISC_KEY + "reagent." + name().toLowerCase();
    }

    public String getLocalizedName() {
        return I18n.format(langKey);
    }

    public enum ReagentType {

        ELEMENTAL, ARCHETYPAL;

        @Nullable
        private Set<Reagent> reagents = null;

        public Set<Reagent> getReagents() {
            if (reagents == null) {
                reagents = EnumSet.noneOf(Reagent.class);
                for (Reagent reagent : Reagent.VALUES) {
                    if (reagent.type == this) {
                        reagents.add(reagent);
                    }
                }
            }
            return Collections.unmodifiableSet(reagents);
        }

    }

}
