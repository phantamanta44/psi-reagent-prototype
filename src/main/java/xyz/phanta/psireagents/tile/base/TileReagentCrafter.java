package xyz.phanta.psireagents.tile.base;

import io.github.phantamanta44.libnine.LibNine;
import io.github.phantamanta44.libnine.capability.impl.L9AspectSlot;
import io.github.phantamanta44.libnine.tile.L9TileEntityTicking;
import io.github.phantamanta44.libnine.util.collection.Accrue;
import io.github.phantamanta44.libnine.util.data.serialization.AutoSerialize;
import io.github.phantamanta44.libnine.util.data.serialization.IDatum;
import io.github.phantamanta44.libnine.util.helper.OptUtils;
import net.minecraft.item.ItemStack;
import xyz.phanta.psireagents.ReagentsConfig;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.capability.impl.SimpleReagentStore;
import xyz.phanta.psireagents.init.ReagentsCaps;
import xyz.phanta.psireagents.reagent.ReagentQuantity;
import xyz.phanta.psireagents.recipe.ReagentCraftingRecipe;
import xyz.phanta.psireagents.recipe.output.ReagentOutput;
import xyz.phanta.psireagents.util.ItemHandlerUtils;
import xyz.phanta.psireagents.util.WorkResult;

import javax.annotation.Nullable;

public abstract class TileReagentCrafter extends L9TileEntityTicking {

    @AutoSerialize
    private final SimpleReagentStore reagentStore
            = new SimpleReagentStore.Observable(ReagentsConfig.reagentWorkbenchCapacity, r -> setDirty());
    @AutoSerialize
    private final L9AspectSlot slotReagentHolder = new L9AspectSlot.Observable(
            s -> s.hasCapability(ReagentsCaps.REAGENT_STORE, null), (i, o, n) -> setDirty());
    @AutoSerialize
    private final L9AspectSlot slotInput = new L9AspectSlot.Observable((i, o, n) -> onInputChanged());
    @AutoSerialize
    private final IDatum.OfInt work = IDatum.ofInt(0);

    @Nullable
    private ReagentCraftingRecipe cachedRecipe = null;
    @Nullable
    private ReagentOutput cachedRecipeOutput = null;
    private boolean recipeDirty = true;

    public TileReagentCrafter() {
        setInitialized();
        markRequiresSync();
    }

    public ReagentStore getReagentStore() {
        return reagentStore;
    }

    public L9AspectSlot getReagentHolderSlot() {
        return slotReagentHolder;
    }

    public L9AspectSlot getInputSlot() {
        return slotInput;
    }

    public int getWorkDone() {
        return work.getInt();
    }

    public abstract int getMaxWork();

    @Nullable
    public ReagentQuantity getCachedOutput() {
        return cachedRecipeOutput == null ? null : cachedRecipeOutput.getReagents();
    }

    @Override
    protected void tick() {
        if (recipeDirty) {
            ItemStack inputStack = slotInput.getStackInSlot();
            if (!inputStack.isEmpty()) {
                ReagentCraftingRecipe recipe = LibNine.PROXY.getRecipeManager()
                        .getRecipeList(ReagentCraftingRecipe.class).findRecipe(inputStack);
                if (recipe != null) {
                    if (recipe != cachedRecipe) {
                        cachedRecipe = recipe;
                        if (!world.isRemote) {
                            work.setInt(0);
                            setDirty();
                        }
                    }
                    cachedRecipeOutput = recipe.mapToOutput(inputStack);
                } else {
                    cachedRecipe = null;
                    cachedRecipeOutput = null;
                    if (!world.isRemote) {
                        work.setInt(0);
                        setDirty();
                    }
                }
            } else {
                cachedRecipe = null;
                cachedRecipeOutput = null;
                if (!world.isRemote) {
                    work.setInt(0);
                    setDirty();
                }
            }
            recipeDirty = false;
        }
        if (!world.isRemote) {
            ItemStack holderStack = slotReagentHolder.getStackInSlot();
            if (!holderStack.isEmpty()) {
                OptUtils.capability(holderStack, ReagentsCaps.REAGENT_STORE).ifPresent(reagentStore::transferTo);
            }
        }
    }

    public WorkResult doWork() {
        if (canProcessReagent()) {
            int nextWork = work.getInt() + 1;
            if (nextWork >= getMaxWork()) {
                if (processReagent()) {
                    work.setInt(0);
                    setDirty();
                    return WorkResult.WORK_FINISHED;
                }
            } else {
                work.setInt(nextWork);
                setDirty();
                return WorkResult.WORK_DONE;
            }
        }
        return WorkResult.NONE;
    }

    public boolean canProcessReagent() {
        return cachedRecipeOutput != null;
    }

    protected boolean processReagent() {
        if (cachedRecipeOutput != null && !slotInput.extractItem(1, false).isEmpty()) {
            cachedRecipeOutput.getReagents().impart(reagentStore);
            return true;
        }
        return false;
    }

    protected void onInputChanged() {
        recipeDirty = true;
        setDirty();
    }

    public void accrueDrops(Accrue<ItemStack> drops) {
        ItemHandlerUtils.accrue(drops, slotReagentHolder, slotInput);
    }

}
