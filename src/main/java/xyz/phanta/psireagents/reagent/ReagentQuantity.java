package xyz.phanta.psireagents.reagent;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import xyz.phanta.psireagents.capability.ReagentStore;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class ReagentQuantity {

    public static final ReagentQuantity NONE = new Builder().build();

    private final int[] amounts;

    private ReagentQuantity(int[] amounts) {
        this.amounts = amounts;
    }

    public int getAmount(Reagent reagent) {
        return amounts[reagent.ordinal()];
    }

    public boolean isEmpty() {
        return Arrays.stream(amounts).allMatch(n -> n == 0);
    }

    public ReagentQuantity scale(float factor) {
        return factor == 1F ? this
                : new ReagentQuantity(Arrays.stream(amounts).map(n -> (int)Math.ceil(n * factor)).toArray());
    }

    public boolean canFit(ReagentStore store) {
        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] > 0 && store.getRemainingReagentCapacity(Reagent.VALUES[i]) < amounts[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean isSatisfied(ReagentStore store) {
        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] > 0 && !store.containsReagent(Reagent.VALUES[i], amounts[i])) {
                return false;
            }
        }
        return true;
    }

    public void impart(ReagentStore store) {
        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] > 0) {
                store.injectReagent(Reagent.VALUES[i], amounts[i], true);
            }
        }
    }

    public void deduct(ReagentStore store) {
        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] > 0) {
                store.extractReagent(Reagent.VALUES[i], amounts[i], true);
            }
        }
    }

    public Set<Reagent> getNonZeroReagents() {
        Set<Reagent> resultSet = EnumSet.noneOf(Reagent.class);
        for (Reagent reagent : Reagent.VALUES) {
            if (amounts[reagent.ordinal()] != 0) {
                resultSet.add(reagent);
            }
        }
        return resultSet;
    }

    public JsonObject serializeJson() {
        JsonObject dto = new JsonObject();
        for (int i = 0; i < amounts.length; i++) {
            if (amounts[i] > 0) {
                dto.addProperty(Reagent.VALUES[i].name(), amounts[i]);
            }
        }
        return dto;
    }

    public static ReagentQuantity deserializeJson(JsonObject dto) {
        int[] amounts = new int[Reagent.VALUES.length];
        for (int i = 0; i < amounts.length; i++) {
            String key = Reagent.VALUES[i].name();
            if (dto.has(key)) {
                amounts[i] = Math.max(dto.get(key).getAsInt(), 0);
            }
        }
        return new ReagentQuantity(amounts);
    }

    public void serializeByteBuf(ByteBuf buf) {
        for (int amount : amounts) {
            buf.writeInt(amount);
        }
    }

    public static ReagentQuantity deserializeByteBuf(ByteBuf buf) {
        int[] amounts = new int[Reagent.VALUES.length];
        for (int i = 0; i < amounts.length; i++) {
            amounts[i] = Math.max(buf.readInt(), 0);
        }
        return new ReagentQuantity(amounts);
    }

    public static class Builder {

        private final int[] amounts = new int[Reagent.VALUES.length];

        public Builder add(Reagent reagent, int amount) {
            amounts[reagent.ordinal()] += amount;
            return this;
        }

        public Builder add(ReagentQuantity qty) {
            for (int i = 0; i < amounts.length; i++) {
                amounts[i] += qty.amounts[i];
            }
            return this;
        }

        public ReagentQuantity build() {
            return new ReagentQuantity(amounts);
        }

    }

}
