package xyz.roqadaq.simpleplanes.setup;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.roqadaq.simpleplanes.SimplePlanesMod;
import xyz.roqadaq.simpleplanes.container.PlaneWorkbenchContainer;
import xyz.roqadaq.simpleplanes.items.DescriptionItem;
import xyz.roqadaq.simpleplanes.items.ParachuteItem;
import xyz.roqadaq.simpleplanes.items.PlaneArmorItem;
import xyz.roqadaq.simpleplanes.items.PlaneItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SimplePlanesItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SimplePlanesMod.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SimplePlanesMod.MODID);

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
    }

    public static List<PlaneItem> getPlaneItems() {
        ArrayList<PlaneItem> planeItems = new ArrayList<>(4);
        planeItems.add(PLANE_ITEM.get());
        planeItems.add(LARGE_PLANE_ITEM.get());
        planeItems.add(CARGO_PLANE_ITEM.get());
        planeItems.add(HELICOPTER_ITEM.get());
        return planeItems;
    }

    public static final DeferredItem<Item> PROPELLER = ITEMS.registerSimpleItem("propeller");

    public static final DeferredItem<Item> FLOATY_BEDDING = ITEMS.registerSimpleItem("floaty_bedding");
    public static final DeferredItem<Item> BOOSTER = ITEMS.registerSimpleItem("booster");
    public static final DeferredItem<Item> HEALING = ITEMS.registerSimpleItem("healing");
    public static final DeferredItem<Item> ARMOR = ITEMS.registerItem("armor", props -> new PlaneArmorItem(props.stacksTo(1)));
    public static final DeferredItem<Item> SOLAR_PANEL = ITEMS.registerItem("solar_panel", props -> new Item(props.stacksTo(1)));
    public static final DeferredItem<Item> FOLDING = ITEMS.registerSimpleItem("folding");
    public static final DeferredItem<Item> SUPPLY_CRATE = ITEMS.registerSimpleItem("supply_crate");
    public static final DeferredItem<Item> SEATS = ITEMS.registerSimpleItem("seats");
    public static final DeferredItem<Item> SHOOTER = ITEMS.registerItem("shooter",
            props -> new DescriptionItem(props, Component.translatable(SimplePlanesMod.MODID + ".shooter_desc",
                    Component.keybind("key.plane_inventory_open.desc"), Component.keybind("key.attack"))));

    public static final DeferredItem<Item> ELECTRIC_ENGINE = ITEMS.registerItem("electric_engine",
            props -> new DescriptionItem(props, Component.translatable(SimplePlanesMod.MODID + ".press_key",
                    Component.keybind("key.plane_inventory_open.desc"))));
    public static final DeferredItem<Item> FURNACE_ENGINE = ITEMS.registerItem("furnace_engine",
            props -> new DescriptionItem(props, Component.translatable(SimplePlanesMod.MODID + ".press_key",
                    Component.keybind("key.plane_inventory_open.desc"))));
    public static final DeferredItem<Item> LIQUID_ENGINE = ITEMS.registerItem("liquid_engine",
            props -> new DescriptionItem(props, Component.translatable(SimplePlanesMod.MODID + ".press_key",
                    Component.keybind("key.plane_inventory_open.desc"))));

    public static final DeferredItem<Item> WRENCH = ITEMS.registerSimpleItem("wrench");
    public static final DeferredItem<BlockItem> PLANE_WORKBENCH = ITEMS.registerItem("plane_workbench",
            props -> new BlockItem(SimplePlanesBlocks.PLANE_WORKBENCH_BLOCK.get(), props));
    public static final DeferredItem<BlockItem> CHARGING_STATION = ITEMS.registerItem("charging_station",
            props -> new BlockItem(SimplePlanesBlocks.CHARGING_STATION_BLOCK.get(), props));

    public static final DeferredItem<PlaneItem> PLANE_ITEM = ITEMS.registerItem("plane",
            props -> new PlaneItem(props, SimplePlanesEntities.PLANE));
    public static final DeferredItem<PlaneItem> LARGE_PLANE_ITEM = ITEMS.registerItem("large_plane",
            props -> new PlaneItem(props, SimplePlanesEntities.LARGE_PLANE));
    public static final DeferredItem<PlaneItem> CARGO_PLANE_ITEM = ITEMS.registerItem("cargo_plane",
            props -> new PlaneItem(props, SimplePlanesEntities.CARGO_PLANE));
    public static final DeferredItem<PlaneItem> HELICOPTER_ITEM = ITEMS.registerItem("helicopter",
            props -> new PlaneItem(props, SimplePlanesEntities.HELICOPTER));

    public static final DeferredItem<ParachuteItem> PARACHUTE_ITEM = ITEMS.registerItem("parachute",
            ParachuteItem::new);

    public static final Supplier<CreativeModeTab> PLANES_TAB = CREATIVE_MODE_TABS.register("planes_tab", () -> CreativeModeTab.builder()
            .icon(() -> PLANE_ITEM.get().getDefaultInstance())
            .title(Component.translatable(SimplePlanesMod.MODID + ".planes_tab"))
            .displayItems((parameters, output) -> {
                output.accept(PROPELLER.get());
                output.accept(FLOATY_BEDDING.get());
                output.accept(BOOSTER.get());
                output.accept(HEALING.get());
                output.accept(ARMOR.get());
                output.accept(SOLAR_PANEL.get());
                output.accept(FOLDING.get());
                output.accept(SUPPLY_CRATE.get());
                output.accept(SEATS.get());
                output.accept(SHOOTER.get());
                output.accept(ELECTRIC_ENGINE.get());
                output.accept(FURNACE_ENGINE.get());
                output.accept(LIQUID_ENGINE.get());
                output.accept(WRENCH.get());
                output.accept(PLANE_WORKBENCH.get());
                output.accept(CHARGING_STATION.get());
                output.accept(PARACHUTE_ITEM.get());

                BuiltInRegistries.BLOCK.getTagOrEmpty(PlaneWorkbenchContainer.PLANE_MATERIALS_TAG).forEach(block -> {
                    ItemStack planeStack = new ItemStack(PLANE_ITEM.get());
                    ItemStack largePlaneStack = new ItemStack(LARGE_PLANE_ITEM.get());
                    ItemStack cargoPlaneStack = new ItemStack(CARGO_PLANE_ITEM.get());
                    ItemStack heliStack = new ItemStack(HELICOPTER_ITEM.get());

                    CompoundTag entityTag = new CompoundTag();
                    entityTag.putString("material", BuiltInRegistries.BLOCK.getKey(block.value()).toString());

                    planeStack.set(SimplePlanesComponents.ENTITY_TAG, entityTag);
                    largePlaneStack.set(SimplePlanesComponents.ENTITY_TAG, entityTag);
                    cargoPlaneStack.set(SimplePlanesComponents.ENTITY_TAG, entityTag);
                    heliStack.set(SimplePlanesComponents.ENTITY_TAG, entityTag);

                    output.accept(planeStack);
                    output.accept(largePlaneStack);
                    output.accept(cargoPlaneStack);
                    output.accept(heliStack);
                });
            }).build());
}
