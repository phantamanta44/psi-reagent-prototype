package xyz.phanta.psireagents.recipe;

import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.recipe.input.ItemStackInput;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import net.minecraft.item.ItemStack;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.recipe.output.ReagentOutput;

public class ReagentCraftingRecipe implements IRcp<ItemStack, ItemStackInput, ReagentOutput> {

    private final ItemStackInput input;
    private final ReagentOutput output;

    public ReagentCraftingRecipe(IDisplayableMatcher<ItemStack> inputMatcher, ReagentQuantity outputReagents) {
        this.input = new ItemStackInput(inputMatcher, 1);
        this.output = new ReagentOutput(outputReagents);
    }

    @Override
    public ItemStackInput input() {
        return input;
    }

    @Override
    public ReagentOutput mapToOutput(ItemStack input) {
        return output;
    }

}
