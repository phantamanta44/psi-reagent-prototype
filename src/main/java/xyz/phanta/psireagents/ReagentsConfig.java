package xyz.phanta.psireagents;

import net.minecraftforge.common.config.Config;

@Config(modid = PsiReagents.MOD_ID, name = "psi_reagents")
public class ReagentsConfig {

    @Config.Comment("Configuration for the iron reagent pouch.")
    public static final Pouch pouchIron = new Pouch(4000, 0.7D);
    @Config.Comment("Configuration for the gold reagent pouch.")
    public static final Pouch pouchGold = new Pouch(6000, 0.6D);
    @Config.Comment("Configuration for the psimetal reagent pouch.")
    public static final Pouch pouchPsimetal = new Pouch(8000, 0.8D);
    @Config.Comment("Configuration for the ebony-psimetal reagent pouch.")
    public static final Pouch pouchEbony = new Pouch(14000, 0.9D);
    @Config.Comment("Configuration for the ivory-psimetal reagent pouch.")
    public static final Pouch pouchIvory = new Pouch(10000, 1D);

    public static class Pouch {

        @Config.Comment("The quantity of each reagent this pouch can hold.")
        public int capacity;

        @Config.Comment("The reagent cost multiplier for this pouch.")
        public double efficiency;

        private Pouch(int defCapacity, double defEfficiency) {
            this.capacity = defCapacity;
            this.efficiency = defEfficiency;
        }

    }

    @Config.Comment("The quantity of each reagent the reagent workbench can hold.")
    public static int reagentWorkbenchCapacity = 32000;

}
