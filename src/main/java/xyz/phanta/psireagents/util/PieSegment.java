package xyz.phanta.psireagents.util;

import io.github.phantamanta44.libnine.util.math.MathUtils;
import net.minecraft.util.math.Vec2f;

public enum PieSegment {

    TOP_RIGHT(0.5F, 0F) {
        @Override
        public Vec2f mapToSquare(float angle) {
            return new Vec2f(0.5F + 0.5F * (float)Math.tan(angle), 0F);
        }
    },
    RIGHT(1F, 0F) {
        @Override
        public Vec2f mapToSquare(float angle) {
            return new Vec2f(1F, 0.5F + 0.5F * (float)Math.tan(angle - MathUtils.PI_F * 0.5F));
        }
    },
    BOTTOM(1F, 1F) {
        @Override
        public Vec2f mapToSquare(float angle) {
            return new Vec2f(0.5F - 0.5F * (float)Math.tan(angle - MathUtils.PI_F), 1F);
        }
    },
    LEFT(0F, 1F) {
        @Override
        public Vec2f mapToSquare(float angle) {
            return new Vec2f(0F, 0.5F - 0.5F * (float)Math.tan(angle - MathUtils.PI_F * 1.5F));
        }
    },
    TOP_LEFT(0F, 0F) {
        @Override
        public Vec2f mapToSquare(float angle) {
            return new Vec2f(0.5F + 0.5F * (float)Math.tan(angle - MathUtils.PI_F * 2F), 0F);
        }
    };

    private static final PieSegment[] VALUES = values();

    public static PieSegment get(float frac) {
        if (frac <= 0.125F) {
            return TOP_RIGHT;
        } else if (frac <= 0.375F) {
            return RIGHT;
        } else if (frac <= 0.625F) {
            return BOTTOM;
        } else if (frac <= 0.875F) {
            return LEFT;
        } else {
            return TOP_LEFT;
        }
    }

    public final Vec2f boundPoint;

    PieSegment(float boundX, float boundY) {
        this.boundPoint = new Vec2f(boundX, boundY);
    }

    public PieSegment next() {
        return VALUES[ordinal() + 1];
    }

    public abstract Vec2f mapToSquare(float angle);

    public Vec2f fracToSquare(float frac) {
        return mapToSquare(frac * MathUtils.PI_F * 2F);
    }

}
