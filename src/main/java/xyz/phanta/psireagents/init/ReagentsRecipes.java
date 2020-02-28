package xyz.phanta.psireagents.init;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.libnine.recipe.IRecipeList;
import io.github.phantamanta44.libnine.recipe.input.ItemStackInput;
import io.github.phantamanta44.libnine.util.helper.ItemUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import xyz.phanta.psireagents.reagent.Reagent;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.recipe.ReagentCraftingRecipe;
import xyz.phanta.psireagents.recipe.output.ReagentOutput;

public class ReagentsRecipes {

    @InitMe
    public static void init() {
        LibNine.PROXY.getRecipeManager().addType(ReagentCraftingRecipe.class);

        IRecipeList<ItemStack, ItemStackInput, ReagentOutput, ReagentCraftingRecipe> rlReagentCrafting
                = LibNine.PROXY.getRecipeManager().getRecipeList(ReagentCraftingRecipe.class);
        rlReagentCrafting.add(new ReagentCraftingRecipe(
                ItemUtils.matchesWithWildcard(new ItemStack(Items.BLAZE_ROD)),
                new ReagentQuantity.Builder().add(Reagent.ELEM_FIRE, 1000).build()));
        rlReagentCrafting.add(new ReagentCraftingRecipe(
                ItemUtils.matchesWithWildcard(new ItemStack(Items.BLAZE_POWDER)),
                new ReagentQuantity.Builder().add(Reagent.ELEM_FIRE, 350).build()));
        rlReagentCrafting.add(new ReagentCraftingRecipe(
                ItemUtils.matchesWithWildcard(new ItemStack(Items.ENDER_PEARL)),
                new ReagentQuantity.Builder().add(Reagent.ELEM_AIR, 500).add(Reagent.ARCH_DECAY, 500).build()));
        // TODO default reagent crafting recipes
    }

}
