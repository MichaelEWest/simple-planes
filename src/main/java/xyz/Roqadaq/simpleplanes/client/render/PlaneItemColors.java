package xyz.roqadaq.simpleplanes.client.render;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesComponents;

import java.util.HashMap;

public class PlaneItemColors {
    public static final HashMap<Block, Integer> cachedColors = new HashMap<>();
    public static final int DEFAULT_COLOR = 0xFFB28F55;

    public static void clearCache() {
        cachedColors.clear();
    }

    public static int getColor(ItemStack itemStack, int tintIndex) {
        CompoundTag entityTag = itemStack.get(SimplePlanesComponents.ENTITY_TAG);
        if (tintIndex != 0) {
            return -1;
        }
        if (entityTag != null && entityTag.contains("material")) {
            String materialStr = entityTag.getString("material").orElse(null);
            if (materialStr != null) {
                Block block = BuiltInRegistries.BLOCK.getOptional(Identifier.parse(materialStr)).orElse(null);
                if (block != null) {
                    if (cachedColors.containsKey(block)) {
                        return cachedColors.get(block);
                    }
                    // Block texture color lookup API changed in 26.1
                    cachedColors.put(block, DEFAULT_COLOR);
                    return DEFAULT_COLOR;
                }
            }
        }
        return DEFAULT_COLOR;
    }
}
