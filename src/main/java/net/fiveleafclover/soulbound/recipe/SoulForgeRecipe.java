package net.fiveleafclover.soulbound.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class SoulForgeRecipe implements Recipe<SimpleInventory> {

    private final ItemStack output;
    private final List<Ingredient> recipeItems;
    public final int heatTime;
    public final int hammerHits;
    private final Identifier id;

    public SoulForgeRecipe(List<Ingredient> ingredients, ItemStack itemStack, int heatTime, int hammerHits, Identifier id) {
        this.output = itemStack;
        this.recipeItems = ingredients;
        this.heatTime = heatTime;
        this.hammerHits = hammerHits;
        this.id = id;
    }


    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) {
            return false;
        }

        boolean match = true;

        for (int i = 0; i < recipeItems.size(); i++) {
            boolean found = false;
            for (int j = 0; j < recipeItems.size(); j++) {
                if (recipeItems.get(i).test(inventory.getStack(j)) && inventory.getStack(j) != ItemStack.EMPTY) { found = true; }
            }
            if (!found) {
                match = false;
            }
        }

        return match;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(this.recipeItems.size());
        list.addAll(recipeItems);
        return list;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    public static class Type implements RecipeType<SoulForgeRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "soul_forging";
    }

    public static class Serializer implements RecipeSerializer<SoulForgeRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "soul_forging";

        @Override
        public SoulForgeRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(3, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            int hammerHits = JsonHelper.getInt(json, "hammer_hits");
            int heatTime = JsonHelper.getInt(json, "heat_time");

            return new SoulForgeRecipe(inputs, output, heatTime, hammerHits, id);
        }

        @Override
        public SoulForgeRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            int heatTime = buf.readInt();
            int hammerHits = buf.readInt();

            ItemStack output = buf.readItemStack();
            return new SoulForgeRecipe(inputs, output, heatTime, hammerHits, id);
        }

        @Override
        public void write(PacketByteBuf buf, SoulForgeRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buf);
            }

            buf.writeInt(recipe.heatTime);
            buf.writeInt(recipe.hammerHits);

            buf.writeItemStack(recipe.getOutput(null));
        }
    }

}
