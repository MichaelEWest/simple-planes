package xyz.przemyk.simpleplanes.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import xyz.przemyk.simpleplanes.misc.EnergyStorageWithSet;
import xyz.przemyk.simpleplanes.setup.SimplePlanesBlocks;

public class ChargingStationBlockEntity extends BlockEntity {

    public final EnergyStorageWithSet energyStorage = new EnergyStorageWithSet(1000);

    public ChargingStationBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SimplePlanesBlocks.CHARGING_STATION_TILE.get(), blockPos, blockState);
    }
    public static void tick(ChargingStationBlockEntity blockEntity) {
        for (Entity entity : blockEntity.level.getEntities(null, new AABB(blockEntity.worldPosition.above()))) {
            // TODO: energy transfer API changed in NF26.x - needs migration to new EnergyHandler system
}
}
    @Override
    protected void saveAdditional(ValueOutput out) {
        super.saveAdditional(out);
        out.putInt("energy", energyStorage.getEnergyStored());
    }
    @Override
    protected void loadAdditional(ValueInput in) {
        super.loadAdditional(in);
        energyStorage.setEnergy(in.getIntOr("energy", 0));
    }
}