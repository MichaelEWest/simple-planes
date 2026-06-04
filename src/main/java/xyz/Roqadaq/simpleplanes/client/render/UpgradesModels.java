package xyz.roqadaq.simpleplanes.client.render;
import xyz.roqadaq.simpleplanes.client.render.PlaneRenderState;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.monster.shulker.ShulkerModel;
import net.minecraft.resources.Identifier;
import xyz.roqadaq.simpleplanes.upgrades.UpgradeType;
import xyz.roqadaq.simpleplanes.upgrades.armor.ArmorWindowModel;
import xyz.roqadaq.simpleplanes.upgrades.floating.WoodenCargoFloatingModel;
import xyz.roqadaq.simpleplanes.upgrades.seats.*;

import java.util.HashMap;

public class UpgradesModels {
    public static ShulkerModel SHULKER_FOLDING;
    public static SeatsModel SEATS;
    public static LargeSeatsModel LARGE_SEATS;
    public static CargoSeatsModel CARGO_SEATS;
    public static HeliSeatsModel HELI_SEATS;
    public static WoodenSeatsModel WOODEN_SEATS;
    public static WoodenHeliSeatsModel WOODEN_HELI_SEATS;
    public static WoodenCargoSeatsModel WOODEN_CARGO_SEATS; // rendered when seats are NOT installed
    public static WoodenCargoFloatingModel WOODEN_CARGO_FLOATING;
    public static ArmorWindowModel ARMOR_WINDOW;

    public static final HashMap<UpgradeType, ModelEntry> MODEL_ENTRIES = new HashMap<>();

    public record ModelEntry(EntityModel<PlaneRenderState> normal, Identifier normalTexture,
                             EntityModel<PlaneRenderState> large, Identifier largeTexture,
                             EntityModel<PlaneRenderState> heli, Identifier heliTexture,
                             EntityModel<PlaneRenderState> cargo, Identifier cargoTexture) {
}
}