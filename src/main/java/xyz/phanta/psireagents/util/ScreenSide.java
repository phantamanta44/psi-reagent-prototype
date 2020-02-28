package xyz.phanta.psireagents.util;

import net.minecraft.client.gui.ScaledResolution;

public enum ScreenSide {

    LEFT {
        @Override
        public int transformX(int x, int width, ScaledResolution res) {
            return x;
        }
    },
    RIGHT {
        @Override
        public int transformX(int x, int width, ScaledResolution res) {
            return res.getScaledWidth() - width + x;
        }
    };

    public abstract int transformX(int x, int width, ScaledResolution res);

}
