package xyz.przemyk.simpleplanes.setup;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.przemyk.simpleplanes.SimplePlanesMod;
import xyz.przemyk.simpleplanes.blocks.ChargingStationBlock;
import xyz.przemyk.simpleplanes.blocks.ChargingStationBlockEntity;
import xyz.przemyk.simpleplanes.blocks.PlaneWorkbenchBlock;
import xyz.przemyk.simpleplanes.blocks.PlaneWorkbenchBlockEntity;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SimplePlanesBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SimplePlanesMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE, SimplePlanesMod.MODID);

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        TILES.register(bus);
    }

    public static final DeferredBlock<PlaneWorkbenchBlock> PLANE_WORKBENCH_BLOCK = BLOCKS.registerBlock("plane_workbench",
            props -> new PlaneWorkbenchBlock(props.strength(2.5f).sound(net.minecraft.world.level.block.SoundType.WOOD)));
    public static final DeferredBlock<ChargingStationBlock> CHARGING_STATION_BLOCK = BLOCKS.registerBlock("charging_station",
            props -> new ChargingStationBlock(props.strength(2.0f, 6.0f).sound(net.minecraft.world.level.block.SoundType.STONE)));

    public static final Supplier<BlockEntityType<PlaneWorkbenchBlockEntity>> PLANE_WORKBENCH_TILE = TILES.register("plane_workbench",
            () -> new BlockEntityType<>(PlaneWorkbenchBlockEntity::new, ImmutableSet.of(PLANE_WORKBENCH_BLOCK.get())));
    public static final Supplier<BlockEntityType<ChargingStationBlockEntity>> CHARGING_STATION_TILE = TILES.register("charging_station",
            () -> new BlockEntityType<>(ChargingStationBlockEntity::new, ImmutableSet.of(CHARGING_STATION_BLOCK.get())));
}
