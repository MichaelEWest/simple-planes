package xyz.roqadaq.simpleplanes;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import xyz.roqadaq.simpleplanes.compat.ironchest.IronChestsCompat;
import xyz.roqadaq.simpleplanes.compat.quark.QuarkCompat;
import xyz.roqadaq.simpleplanes.entities.PlaneEntity;
import xyz.roqadaq.simpleplanes.network.SimplePlanesNetworking;
import xyz.roqadaq.simpleplanes.setup.*;

@Mod(SimplePlanesMod.MODID)
public class SimplePlanesMod {

    public static final String MODID = "simpleplanes";

    public SimplePlanesMod(IEventBus bus, ModContainer modContainer) {
        SimplePlanesConfig.init(modContainer);
        SimplePlanesEntities.init(bus);
        SimplePlanesBlocks.init(bus);
        SimplePlanesContainers.init(bus);
        SimplePlanesUpgrades.init(bus);
        SimplePlanesSounds.init(bus);
        SimplePlanesItems.init(bus);
        SimplePlanesRecipes.init(bus);
        SimplePlanesDatapack.init();
        SimplePlanesComponents.init(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::registerCapabilities);
        bus.addListener(SimplePlanesNetworking::register);
//        ModList.get().getModContainerById("cgm").ifPresent(cgm -> MinecraftForge.EVENT_BUS.register(MrCrayfishGunCompat.class));
    }
    public static Identifier texture(String filename) {
        return Identifier.fromNamespaceAndPath(MODID, "textures/plane_upgrades/" + filename);
    }
    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.FLOATY_BEDDING.get(), SimplePlanesUpgrades.FLOATY_BEDDING.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.BOOSTER.get(), SimplePlanesUpgrades.BOOSTER.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.HEALING.get(), SimplePlanesUpgrades.HEALING.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.ARMOR.get(), SimplePlanesUpgrades.ARMOR.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.SOLAR_PANEL.get(), SimplePlanesUpgrades.SOLAR_PANEL.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.FOLDING.get(), SimplePlanesUpgrades.FOLDING.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.SEATS.get(), SimplePlanesUpgrades.SEATS.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.SHOOTER.get(), SimplePlanesUpgrades.SHOOTER.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.FURNACE_ENGINE.get(), SimplePlanesUpgrades.FURNACE_ENGINE.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.ELECTRIC_ENGINE.get(), SimplePlanesUpgrades.ELECTRIC_ENGINE.get());
            SimplePlanesUpgrades.registerUpgradeItem(SimplePlanesItems.LIQUID_ENGINE.get(), SimplePlanesUpgrades.LIQUID_ENGINE.get());

            SimplePlanesUpgrades.registerUpgradeItem(Items.WHITE_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.ORANGE_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.MAGENTA_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.LIGHT_BLUE_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.YELLOW_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.LIME_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.PINK_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.GRAY_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.LIGHT_GRAY_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.CYAN_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.PURPLE_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.BLUE_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.BROWN_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.GREEN_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.RED_BANNER, SimplePlanesUpgrades.BANNER.get());
            SimplePlanesUpgrades.registerUpgradeItem(Items.BLACK_BANNER, SimplePlanesUpgrades.BANNER.get());

            SimplePlanesUpgrades.registerLargeUpgradeItem(Items.CHEST, SimplePlanesUpgrades.CHEST.get());
            SimplePlanesUpgrades.registerLargeUpgradeItem(SimplePlanesItems.SUPPLY_CRATE.get(), SimplePlanesUpgrades.SUPPLY_CRATE.get());
            SimplePlanesUpgrades.registerLargeUpgradeItem(Items.JUKEBOX, SimplePlanesUpgrades.JUKEBOX.get());

            IronChestsCompat.registerUpgradeItems();
            QuarkCompat.registerUpgradeItems();
});
    }
    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        // TODO: block entity capability types changed in NF 26.x - needs migration to new resource handler API
        // event.registerBlockEntity(Capabilities.Energy.BLOCK, ...);
        // event.registerBlockEntity(Capabilities.Item.BLOCK, ...);

        registerPlaneEntityCaps(event, SimplePlanesEntities.PLANE.get());
        registerPlaneEntityCaps(event, SimplePlanesEntities.LARGE_PLANE.get());
        registerPlaneEntityCaps(event, SimplePlanesEntities.CARGO_PLANE.get());
        registerPlaneEntityCaps(event, SimplePlanesEntities.HELICOPTER.get());
    }
    private static <T extends PlaneEntity> void registerPlaneEntityCaps(
            RegisterCapabilitiesEvent event,
            EntityType<T> type
    ) {
        event.registerEntity(Capabilities.Item.ENTITY, type,
                (entity, ctx) -> entity.getCap(Capabilities.Item.ENTITY)
        );
        event.registerEntity(Capabilities.Fluid.ENTITY, type,
                (entity, ctx) -> entity.getCap(Capabilities.Fluid.ENTITY)
        );
        event.registerEntity(Capabilities.Energy.ENTITY, type,
                (entity, ctx) -> entity.getCap(Capabilities.Energy.ENTITY)
        );
}
}