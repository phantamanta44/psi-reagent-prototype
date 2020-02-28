package xyz.phanta.psireagents.init;

import io.github.phantamanta44.libnine.InitMe;
import io.github.phantamanta44.libnine.capability.StatelessCapabilitySerializer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.capability.ReagentStoreHolder;

@SuppressWarnings("NotNullFieldNotInitialized")
public class ReagentsCaps {

    @CapabilityInject(ReagentStore.class)
    public static Capability<ReagentStore> REAGENT_STORE;
    @CapabilityInject(ReagentStoreHolder.class)
    public static Capability<ReagentStoreHolder> REAGENT_STORE_HOLDER;

    @InitMe
    public static void init() {
        CapabilityManager.INSTANCE.register(
                ReagentStore.class, new StatelessCapabilitySerializer<>(), ReagentStore.Noop::new);
        CapabilityManager.INSTANCE.register(
                ReagentStoreHolder.class, new StatelessCapabilitySerializer<>(), ReagentStoreHolder.Noop::new);
    }

}
