package xyz.przemyk.simpleplanes.setup;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.przemyk.simpleplanes.SimplePlanesMod;
import xyz.przemyk.simpleplanes.entities.*;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public class SimplePlanesEntities {
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(SimplePlanesMod.MODID);

    public static void init(IEventBus bus) {
        ENTITIES.register(bus);
    }
    public static final DeferredHolder<EntityType<?>, EntityType<PlaneEntity>> PLANE = ENTITIES.registerEntityType("plane", PlaneEntity::new, MobCategory.MISC,
        b -> b.sized(2.5F, 1.8F).clientTrackingRange(5).updateInterval(3));
    public static final DeferredHolder<EntityType<?>, EntityType<LargePlaneEntity>> LARGE_PLANE = ENTITIES.registerEntityType("large_plane", LargePlaneEntity::new, MobCategory.MISC,
        b -> b.sized(3F, 2.3F).clientTrackingRange(5).updateInterval(3));
    public static final DeferredHolder<EntityType<?>, EntityType<CargoPlaneEntity>> CARGO_PLANE = ENTITIES.registerEntityType("cargo_plane", CargoPlaneEntity::new, MobCategory.MISC,
        b -> b.sized(3F, 2.3F).clientTrackingRange(5).updateInterval(3));
    public static final DeferredHolder<EntityType<?>, EntityType<HelicopterEntity>> HELICOPTER = ENTITIES.registerEntityType("helicopter", HelicopterEntity::new, MobCategory.MISC,
        b -> b.sized(2.5F, 2.2F).clientTrackingRange(5).updateInterval(3));

    public static final DeferredHolder<EntityType<?>, EntityType<ParachuteEntity>> PARACHUTE = ENTITIES.registerEntityType("parachute", ParachuteEntity::new, MobCategory.MISC,
        b -> b.sized(1.0F, 1.0F).clientTrackingRange(5).updateInterval(3));
}
