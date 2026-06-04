package xyz.roqadaq.simpleplanes.setup;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.roqadaq.simpleplanes.SimplePlanesMod;
import xyz.roqadaq.simpleplanes.upgrades.UpgradeType;
import xyz.roqadaq.simpleplanes.upgrades.armor.ArmorUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.banner.BannerUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.booster.BoosterUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.engines.electric.ElectricEngineUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.engines.liquid.LiquidEngineUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.floating.FloatingUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.engines.furnace.FurnaceEngineUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.folding.FoldingUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.heal.HealingUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.jukebox.JukeboxUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.payload.PayloadUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.seats.SeatsUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.shooter.ShooterUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.solarpanel.SolarPanelUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.storage.ChestUpgrade;
import xyz.roqadaq.simpleplanes.upgrades.supplycrate.SupplyCrateUpgrade;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class SimplePlanesUpgrades {
    private static final DeferredRegister<UpgradeType> UPGRADE_TYPES = DeferredRegister.create(SimplePlanesRegistries.UPGRADE_TYPE, SimplePlanesMod.MODID);
    public static final Map<Item, UpgradeType> ITEM_UPGRADE_MAP = new HashMap<>();
    public static final Map<Item, UpgradeType> LARGE_ITEM_UPGRADE_MAP = new HashMap<>();

    public static void init(IEventBus bus) {
        UPGRADE_TYPES.register(bus);
    }
    public static void registerUpgradeItem(Item item, UpgradeType upgradeType) {
        ITEM_UPGRADE_MAP.put(item, upgradeType);
    }
    public static void registerLargeUpgradeItem(Item item, UpgradeType upgradeType) {
        LARGE_ITEM_UPGRADE_MAP.put(item, upgradeType);
    }
    public static Optional<UpgradeType> getUpgradeFromItem(Item item) {
        return Optional.ofNullable(ITEM_UPGRADE_MAP.get(item));
    }
    public static Optional<UpgradeType> getLargeUpgradeFromItem(Item item) {
        return Optional.ofNullable(LARGE_ITEM_UPGRADE_MAP.get(item));
    }
    public static final Supplier<UpgradeType> FLOATY_BEDDING = UPGRADE_TYPES.register("floaty_bedding", () -> new UpgradeType(FloatingUpgrade::new));
    public static final Supplier<UpgradeType> BOOSTER = UPGRADE_TYPES.register("booster", () -> new UpgradeType(BoosterUpgrade::new));
    public static final Supplier<UpgradeType> SHOOTER = UPGRADE_TYPES.register("shooter", () -> new UpgradeType(ShooterUpgrade::new));
    public static final Supplier<UpgradeType> HEALING = UPGRADE_TYPES.register("healing", () -> new UpgradeType(HealingUpgrade::new));
    public static final Supplier<UpgradeType> ARMOR = UPGRADE_TYPES.register("armor", () -> new UpgradeType(ArmorUpgrade::new));
    public static final Supplier<UpgradeType> SOLAR_PANEL = UPGRADE_TYPES.register("solar_panel", () -> new UpgradeType(SolarPanelUpgrade::new));
    public static final Supplier<UpgradeType> FOLDING = UPGRADE_TYPES.register("folding", () -> new UpgradeType(FoldingUpgrade::new));
    public static final Supplier<UpgradeType> SEATS = UPGRADE_TYPES.register("seats", () -> new UpgradeType(SeatsUpgrade::new));

    public static final Supplier<UpgradeType> FURNACE_ENGINE = UPGRADE_TYPES.register("furnace_engine", () -> new UpgradeType(FurnaceEngineUpgrade::new, true));
    public static final Supplier<UpgradeType> ELECTRIC_ENGINE = UPGRADE_TYPES.register("electric_engine", () -> new UpgradeType(ElectricEngineUpgrade::new, true));
    public static final Supplier<UpgradeType> LIQUID_ENGINE = UPGRADE_TYPES.register("liquid_engine", () -> new UpgradeType(LiquidEngineUpgrade::new, true));

    public static final Supplier<UpgradeType> BANNER = UPGRADE_TYPES.register("banner", () -> new UpgradeType(BannerUpgrade::new));
    public static final Supplier<UpgradeType> PAYLOAD = UPGRADE_TYPES.register("payload", () -> new UpgradeType(PayloadUpgrade::new));
    public static final Supplier<UpgradeType> CHEST = UPGRADE_TYPES.register("chest", () -> new UpgradeType(ChestUpgrade::new));
    public static final Supplier<UpgradeType> SUPPLY_CRATE = UPGRADE_TYPES.register("supply_crate", () -> new UpgradeType(SupplyCrateUpgrade::new));
    public static final Supplier<UpgradeType> JUKEBOX = UPGRADE_TYPES.register("jukebox", () -> new UpgradeType(JukeboxUpgrade::new));
}