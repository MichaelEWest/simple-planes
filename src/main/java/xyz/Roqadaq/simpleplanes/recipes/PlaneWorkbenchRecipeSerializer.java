package xyz.przemyk.simpleplanes.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class PlaneWorkbenchRecipeSerializer {

    public static final MapCodec<PlaneWorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec(
        kind -> kind.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(PlaneWorkbenchRecipe::ingredient),
                Codec.INT.fieldOf("ingredient_amount").forGetter(PlaneWorkbenchRecipe::ingredientAmount),
                Codec.INT.fieldOf("material_amount").forGetter(PlaneWorkbenchRecipe::materialAmount),
                Identifier.CODEC.fieldOf("result").forGetter(PlaneWorkbenchRecipe::resultId)
            ).apply(kind, PlaneWorkbenchRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlaneWorkbenchRecipe> STREAM_CODEC = StreamCodec.of(
        PlaneWorkbenchRecipeSerializer::toNetwork, PlaneWorkbenchRecipeSerializer::fromNetwork
    );

    public static final RecipeSerializer<PlaneWorkbenchRecipe> INSTANCE = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private static PlaneWorkbenchRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        int ingredientAmount = buffer.readVarInt();
        int materialAmount = buffer.readVarInt();
        Identifier resultId = Identifier.parse(buffer.readUtf());
        return new PlaneWorkbenchRecipe(ingredient, ingredientAmount, materialAmount, resultId);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, PlaneWorkbenchRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient());
        buffer.writeVarInt(recipe.ingredientAmount());
        buffer.writeVarInt(recipe.materialAmount());
        buffer.writeUtf(recipe.resultId().toString());
    }
}
