package xyz.przemyk.simpleplanes.compat.quark;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import xyz.przemyk.simpleplanes.setup.SimplePlanesUpgrades;

public class QuarkCompat {

    private static void registerChest(Item chestItem) {
        if (chestItem != Items.AIR) {
            SimplePlanesUpgrades.registerLargeUpgradeItem(chestItem, SimplePlanesUpgrades.CHEST.get());
}
    }
    public static void registerUpgradeItems() {
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "azalea_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "oak_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "spruce_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "birch_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "jungle_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "acacia_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "dark_oak_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "crimson_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "warped_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "mangrove_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "nether_brick_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "purpur_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "prismarine_chest")).orElse(null));
        registerChest(BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("quark", "blossom_chest")).orElse(null));
}
}